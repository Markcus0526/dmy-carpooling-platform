package com.webapi.crypto.service;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.crypto.config.CryptoConfig;
import com.webapi.crypto.util.EthRpcClient;
import com.webapi.crypto.util.EthRpcClient.Log;
import com.webapi.crypto.util.EthRpcClient.Receipt;
import com.webapi.crypto.util.HexUtil;
import com.webapi.structure.SVCResult;


/**
 * Verifies an on-chain escrow deposit and records it against a carpool order.
 *
 * We deliberately do NOT reach into SVCOrderService.payNormalOrder /
 * payReserveOrder: those methods freeze/decrement app-side balance points,
 * which don't apply when the passenger paid from a wallet. Instead we record
 * the payment in the parallel `order_crypto_tx` table. A follow-up
 * `finalizeCryptoPayment(orderId)` action (or a hook in the existing
 * end*Order flows) is responsible for driving the actual on-chain release().
 */
public class SVCCryptoPayService
{
	/** keccak256("Deposited(uint256,address,address,uint256)") — printed by
	 *  hardhat at deploy time. Set this before running against real funds;
	 *  when null we fall back to "match by contract address + topic count",
	 *  which is fine for a testnet POC but not for production. */
	private static final String TOPIC_DEPOSITED = null;


	public SVCResult payOrderCrypto(String source, long userid, long orderid, int orderType,
	                                double expectedPriceCny, String txHash,
	                                String walletAddress, String devtoken)
	{
		SVCResult result = new SVCResult();
		JSONObject retdata = new JSONObject();
		result.retdata = retdata;

		ApiGlobal.logMessage("payOrderCrypto: " + userid + "," + orderid + "," + orderType
				+ "," + expectedPriceCny + "," + txHash + "," + walletAddress);

		if (source == null || source.equals("") || userid < 0 || orderid < 0
				|| orderType < 0 || expectedPriceCny < 0
				|| txHash == null || txHash.equals("")
				|| walletAddress == null || walletAddress.equals("")
				|| devtoken == null || devtoken.equals(""))
		{
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg  = ConstMgr.ErrMsg_Param;
			return result;
		}
		if (!ApiGlobal.IsValidSource(source))
		{
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg  = ConstMgr.ErrMsg_InvalidSource;
			return result;
		}

		Connection dbConn = null;
		try
		{
			// --- 1. Reject duplicate submissions of the same tx --------------
			dbConn = DBManager.getDBConnection();

			if (txAlreadyRecorded(dbConn, txHash))
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg  = "该交易已被记录";
				return result;
			}

			// --- 2. Fetch + validate the on-chain receipt --------------------
			EthRpcClient rpc = new EthRpcClient(CryptoConfig.rpcUrl());
			Receipt r = rpc.getReceipt(txHash);
			if (r == null)
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg  = "链上交易尚未打包，请稍后重试";
				return result;
			}
			if (!r.success)
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg  = "链上交易执行失败";
				return result;
			}
			if (!CryptoConfig.escrowAddress().equalsIgnoreCase(r.to))
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg  = "交易目标合约不匹配";
				return result;
			}

			BigInteger head = rpc.blockNumber();
			int confs = head.subtract(r.blockNumber).intValue() + 1;
			if (confs < CryptoConfig.minConfirmations())
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg  = "链上确认数不足 (" + confs + "/" + CryptoConfig.minConfirmations() + ")";
				return result;
			}

			// --- 3. Decode the Deposited event -------------------------------
			DepositEvent dep = findDepositEvent(r.logs, orderid);
			if (dep == null)
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg  = "未在交易中找到订单 " + orderid + " 的存款事件";
				return result;
			}

			if (!dep.payer.equalsIgnoreCase(walletAddress))
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg  = "存款钱包与提交钱包不一致";
				return result;
			}

			// --- 4. Amount check: on-chain amount must >= expected -----------
			BigInteger expectedWei = CryptoConfig.weiPerCny()
					.multiply(BigInteger.valueOf((long)(expectedPriceCny * 100)))
					.divide(BigInteger.valueOf(100));

			if (dep.amountWei.compareTo(expectedWei) < 0)
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg  = "支付金额不足";
				return result;
			}

			// --- 5. Record it -------------------------------------------------
			insertRecord(dbConn, orderid, orderType, userid, txHash, walletAddress,
					dep.driver, dep.amountWei.toString(), r.blockNumber.longValue(),
					CryptoConfig.chainId(), expectedPriceCny);

			retdata.put("orderid", orderid);
			retdata.put("tx_hash", txHash);
			retdata.put("amount_wei", dep.amountWei.toString());
			retdata.put("confirmations", confs);
			retdata.put("status", "confirmed");
			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg  = ConstMgr.ErrMsg_None;
			return result;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			result.retcode = ConstMgr.ErrCode_Exception;
			result.retmsg  = ConstMgr.ErrMsg_Exception;
			return result;
		}
		finally
		{
			try { if (dbConn != null) dbConn.close(); } catch (Exception ignore) {}
		}
	}


	public SVCResult getCryptoPayment(long orderid)
	{
		SVCResult result = new SVCResult();
		JSONObject retdata = new JSONObject();
		result.retdata = retdata;

		if (orderid < 0)
		{
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg  = ConstMgr.ErrMsg_Param;
			return result;
		}

		Connection dbConn = null;
		try
		{
			dbConn = DBManager.getDBConnection();
			String sql = "SELECT tx_hash, wallet_addr, driver_addr, amount_wei, "
					+ "chain_id, block_number, released, refunded, created_at "
					+ "FROM order_crypto_tx WHERE order_id=" + orderid + " LIMIT 1";
			Statement st = dbConn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next())
			{
				retdata.put("tx_hash",      rs.getString("tx_hash"));
				retdata.put("wallet_addr",  rs.getString("wallet_addr"));
				retdata.put("driver_addr",  rs.getString("driver_addr"));
				retdata.put("amount_wei",   rs.getString("amount_wei"));
				retdata.put("chain_id",     rs.getLong("chain_id"));
				retdata.put("block_number", rs.getLong("block_number"));
				retdata.put("released",     rs.getInt("released"));
				retdata.put("refunded",     rs.getInt("refunded"));
				retdata.put("created_at",   rs.getString("created_at"));
			}
			rs.close();
			st.close();

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg  = ConstMgr.ErrMsg_None;
			return result;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			result.retcode = ConstMgr.ErrCode_Exception;
			result.retmsg  = ConstMgr.ErrMsg_Exception;
			return result;
		}
		finally
		{
			try { if (dbConn != null) dbConn.close(); } catch (Exception ignore) {}
		}
	}


	// ----------------------------------------------------------------------

	private static boolean txAlreadyRecorded(Connection c, String txHash) throws Exception
	{
		PreparedStatement ps = c.prepareStatement(
				"SELECT 1 FROM order_crypto_tx WHERE tx_hash=? LIMIT 1");
		ps.setString(1, txHash);
		ResultSet rs = ps.executeQuery();
		boolean exists = rs.next();
		rs.close();
		ps.close();
		return exists;
	}


	private static void insertRecord(Connection c, long orderId, int orderType, long userid,
	                                 String txHash, String wallet, String driver,
	                                 String amountWei, long blockNumber, long chainId,
	                                 double expectedCny) throws Exception
	{
		PreparedStatement ps = c.prepareStatement(
				"INSERT INTO order_crypto_tx "
				+ "(order_id, order_type, user_id, tx_hash, wallet_addr, driver_addr, "
				+ " amount_wei, chain_id, block_number, expected_cny, released, refunded, created_at) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,0,0,NOW())");
		ps.setLong  (1, orderId);
		ps.setInt   (2, orderType);
		ps.setLong  (3, userid);
		ps.setString(4, txHash);
		ps.setString(5, wallet.toLowerCase());
		ps.setString(6, driver.toLowerCase());
		ps.setString(7, amountWei);
		ps.setLong  (8, chainId);
		ps.setLong  (9, blockNumber);
		ps.setDouble(10, expectedCny);
		ps.executeUpdate();
		ps.close();
	}


	private static DepositEvent findDepositEvent(List<Log> logs, long expectedOrderId)
	{
		String escrow = CryptoConfig.escrowAddress();
		for (Log l : logs)
		{
			if (!l.address.equalsIgnoreCase(escrow)) continue;
			if (l.topics.size() < 4) continue;
			// topic0=event sig, topic1=orderId, topic2=payer, topic3=driver
			if (TOPIC_DEPOSITED != null
					&& !l.topics.get(0).equalsIgnoreCase(TOPIC_DEPOSITED))
				continue;

			BigInteger orderId = HexUtil.topicToUint(l.topics.get(1));
			if (orderId.longValue() != expectedOrderId) continue;

			DepositEvent ev = new DepositEvent();
			ev.orderId   = orderId.longValue();
			ev.payer     = HexUtil.topicToAddress(l.topics.get(2));
			ev.driver    = HexUtil.topicToAddress(l.topics.get(3));
			// data is a single uint256 (amount)
			ev.amountWei = HexUtil.parseQuantity(l.data);
			return ev;
		}
		return null;
	}


	private static final class DepositEvent
	{
		long       orderId;
		String     payer;
		String     driver;
		BigInteger amountWei;
	}
}
