package com.webapi.crypto.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;


/**
 * Zero-dependency Ethereum JSON-RPC client. We only need three calls:
 * eth_blockNumber, eth_getTransactionReceipt, eth_getTransactionByHash.
 *
 * We deliberately avoid web3j to keep the war's dependency footprint small
 * — this is a POC. If you later need signed transactions from the backend
 * (e.g. to call escrow.release()), swap this out for web3j.
 */
public final class EthRpcClient
{
	private final String rpcUrl;

	public EthRpcClient(String rpcUrl)
	{
		this.rpcUrl = rpcUrl;
	}


	public BigInteger blockNumber() throws IOException
	{
		JSONObject r = call("eth_blockNumber", new JSONArray());
		return HexUtil.parseQuantity(r.getString("result"));
	}


	public Receipt getReceipt(String txHash) throws IOException
	{
		JSONArray params = new JSONArray();
		params.add(txHash);
		JSONObject r = call("eth_getTransactionReceipt", params);
		Object res = r.get("result");
		// JSON-RPC returns null for `result` when the tx isn't mined yet.
		if (res == null || res instanceof JSONNull)
			return null;
		return Receipt.from(r.getJSONObject("result"));
	}


	private JSONObject call(String method, JSONArray params) throws IOException
	{
		JSONObject req = new JSONObject();
		req.put("jsonrpc", "2.0");
		req.put("id", 1);
		req.put("method", method);
		req.put("params", params);

		byte[] body = req.toString().getBytes(StandardCharsets.UTF_8);

		HttpURLConnection conn = (HttpURLConnection) new URL(rpcUrl).openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(15000);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Content-Length", Integer.toString(body.length));

		try (DataOutputStream out = new DataOutputStream(conn.getOutputStream()))
		{
			out.write(body);
		}

		int code = conn.getResponseCode();
		if (code / 100 != 2)
			throw new IOException("rpc http " + code);

		StringBuilder sb = new StringBuilder();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)))
		{
			String line;
			while ((line = in.readLine()) != null) sb.append(line);
		}

		JSONObject resp = JSONObject.fromObject(sb.toString());
		Object err = resp.opt("error");
		if (err != null && !(err instanceof JSONNull))
			throw new IOException("rpc error: " + err);
		return resp;
	}


	// ----------------------------------------------------------------------

	public static final class Receipt
	{
		public String  txHash;
		public String  from;
		public String  to;
		public BigInteger blockNumber;
		public boolean success;
		public List<Log> logs;

		public static Receipt from(JSONObject j)
		{
			Receipt r = new Receipt();
			r.txHash      = j.getString("transactionHash");
			r.from        = j.getString("from").toLowerCase();
			r.to          = j.optString("to", "").toLowerCase();
			r.blockNumber = HexUtil.parseQuantity(j.getString("blockNumber"));
			// post-Byzantium: status "0x1" = success, "0x0" = fail
			r.success     = j.has("status") && "0x1".equals(j.getString("status"));

			JSONArray arr = j.getJSONArray("logs");
			r.logs = new java.util.ArrayList<Log>(arr.size());
			for (int i = 0; i < arr.size(); i++)
				r.logs.add(Log.from(arr.getJSONObject(i)));
			return r;
		}
	}

	public static final class Log
	{
		public String    address;
		public List<String> topics;
		public String    data;

		public static Log from(JSONObject j)
		{
			Log l = new Log();
			l.address = j.getString("address").toLowerCase();
			JSONArray t = j.getJSONArray("topics");
			l.topics = new java.util.ArrayList<String>(t.size());
			for (int i = 0; i < t.size(); i++) l.topics.add(t.getString(i));
			l.data = j.optString("data", "0x");
			return l;
		}
	}
}
