package com.nmdy.financial.request.action;

import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import com.nmdy.financial.manage.service.MyFormService;
import com.nmdy.financial.request.service.FreezeService;
import com.pinche.authority.manager.dao.Administrator;
import com.pinche.authority.manager.service.AdministratorService;
import com.pinche.common.Common;
import com.pinche.common.Constants;
import com.pinche.common.Errors;
import com.pinche.common.WebUtil;
import com.pinche.common.action.SecureAction;
import com.pinche.customer.group.dao.Group;
import com.pinche.customer.group.service.GroupService;
import com.pinche.customer.joinunit.dao.Joinunit;
import com.pinche.customer.joinunit.service.JoinunitService;
import com.pinche.customer.user.dao.Customer;
import com.pinche.customer.user.service.CustomerService;

public class RequestAction extends SecureAction {

	private MyFormService myService;
	private String reqidstr;
	private int cherge;
	private int deposit;
	private int chkuser;
	private int chkgroup;
	private int chkunit;
	private int notclose;
	private int all;
	private long userid;
	private String username;
	private String begintime;
	private String endtime;
	private String phone;
	private static final long serialVersionUID = 1L;
	private FreezeService freezeService;
	private int page;
	private int rows;
	private JSONObject jsonObject;// 返回的json
	// dealsingle
	private int reqid;
	// colse
	private int cloaccounttype;
	private long clouserid;
	private int utsid;
	private BigDecimal sum;
	private BigDecimal tsbalance;
	private BigDecimal tsbalance1;
	private String sumstr;
	private String clouseridstr;
	private String utsidstr;
	private String accountType;

	public String index() {
		return SUCCESS;
	}

	/**
	 * 页面的数据表格的查询方法
	 * 
	 * @return
	 */
	public String findby() {
		Map<String, Object> json = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		if (0 != userid) {
			params.put("userid", "%" + userid + "%");

		}
		if (null != username && !"0".equals(username)) {
			params.put("username", "%" + username + "%");
		}
		if (null != phone && !"0".equals(phone)) {
			params.put("phone", "%" + phone + "%");
		}
		if (cherge == 0 && deposit == 1) {
			params.put("oper_type2", 1);
			params.put("oper_type1", "");

		}
		if (deposit == 0 && cherge == 1) {
			params.put("oper_type2", "");
			params.put("oper_type1", 2);

		}
		if (deposit == 1 && cherge == 1) {
			params.put("oper_type2", 1);
			params.put("oper_type1", 2);

		}
		if (chkuser == 1) {
			params.put("account_type1", 1);
		}
		if (chkuser == 0) {
			params.put("account_type1", 0);
		}
		if (chkgroup == 1) {
			params.put("account_type2", 2);
		}
		if (chkgroup == 0) {
			params.put("account_type2", 0);
		}
		if (chkunit == 1) {
			params.put("account_type3", 3);
		}
		if (chkunit == 0) {
			params.put("account_type3", 0);
		}

		if (null != begintime && !"0".equals(begintime)) {
			System.out.println("begintime" + begintime);
			params.put("begintime", begintime+" 00:00:00");
		}
		if (null != endtime && !"0".equals(endtime)) {

			params.put("endtime", endtime+" 23:59:59");
		}

		if (1 == notclose) {
			params.put("status", notclose);
		}
		if (2 == notclose) {
			params.put("status", notclose);
		}

		params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		if(StringUtils.isNotEmpty(accountType)){
			String[] array = accountType.split(",");
			params.put("array", array);
		}
		List<Map<String, Object>> chergeinfo = freezeService.findby(params);
		json.put("total", freezeService.findcount(params));
		json.put("rows", chergeinfo);
		jsonObject = JSONObject.fromObject(json);
		return SUCCESS;
	}

	/**
	 * 批量处理
	 * 
	 * @return
	 */
	public String deal() {
		if (null != reqidstr) {
			// System.out.println("receiver1" + receiver);
			// System.out.println("title1"+title);

			String receivers[] = reqidstr.split(",");
			for (int i = 0; i < receivers.length; i++) {

				int singleid = Integer.parseInt(receivers[i]);
				freezeService.updatedealsingle(singleid);
			}

		}
		return SUCCESS;
	}

	/**
	 * 处理单个
	 * 
	 * @return
	 */
	public String dealsingle() {
		if (reqid > 0) {
			freezeService.updatedealsingle(reqid);
		}

		return SUCCESS;
	}

	/**
	 * 批量完成
     * 1.在ts里插入一条记录，将金额先加回去，返回此时的tsid作为解冻记录id
	 * 2.在freeze表里插入一条解冻记录，并将返回的解冻记录id一起插入
	 * 3.在对应客户的记录里更新最新的balance_ts 用于查询最新的余额信息
	 * 4.在ts再插入一条记录，将金额变回去，然后更新解冻记录状态为“用掉了”
	 * @return
	 */
	public String finish() {
		// 余额信息
		Map<String, Object> params1 = new HashMap<String, Object>();
		Map<String, Object> balance = new HashMap<String, Object>();
		// 1,查询出fp表中的信息
		Map<String, Object> params = new HashMap<String, Object>();

		Map<String, Object> params2 = new HashMap<String, Object>();

		Map<String, Object> params6 = new HashMap<String, Object>();
		Map<String, Object> params4 = new HashMap<String, Object>();
		// tsinfo
		Map<String, Object> params3 = new HashMap<String, Object>();
		// 存储fp信息
		Map<String, Object> fpinfos = new HashMap<String, Object>();
		if (null != reqidstr) {
			// System.out.println("receiver1" + receiver);
			// System.out.println("title1"+title);

			String receivers[] = reqidstr.split(",");
			String sums[] = sumstr.split(",");
			String userids[] = clouseridstr.split(",");
			String tsids[] = utsidstr.split(",");
			for (int i = 0; i < receivers.length; i++) {
				int userid = Integer.parseInt(userids[i]);
				int tsid = Integer.parseInt(tsids[i]);
				sum = new BigDecimal(sums[i]);
				params1.put("uid", userid);
				balance = freezeService.finduserbalance(params1);
				// 1,获得最新余额
				if (null != balance.get("ubalance")) {
					tsbalance = (BigDecimal) balance.get("ubalance");
					System.out.println("*********" + tsbalance);
					System.out.println("*********" + sum);
					tsbalance1 = tsbalance.add(sum);
					params3.put("balance", tsbalance1);
					params3.put("sum", sum);
				}
				// 2,向ts表插入一条新数据
				Map<String, Object> tsinfo = new HashMap<String, Object>();
				tsinfo = myService.findtsinfo(tsid);
				// 取出里面的信息
				long order_cs_id = (long) tsinfo.get("order_cs_id");
				long order_id = (long) tsinfo.get("order_id");
				int order_type = (int) tsinfo.get("order_type");
				String ts_type = "tx_code_011";
				int oper = 2;
				int ts_way = (int) tsinfo.get("ts_way");

				String remark = (String) tsinfo.get("remark");
				String account = (String) tsinfo.get("account");
				int account_type = (int) tsinfo.get("account_type");
				String application = "管理后台";
				// ___________________________________________________________________________________________
				if (order_cs_id > 0) {
					params3.put("order_cs_id", order_cs_id);
				} else {
					params3.put("order_cs_id", 0);
				}
				if (order_id > 0) {
					params3.put("order_id", order_id);
				} else {
					params3.put("order_id", 0);
				}
				if (order_type > 0) {
					params3.put("order_type", order_type);
				} else {
					params3.put("order_type", 0);
				}

				params3.put("oper", oper);

				if (ts_way > 0) {
					params3.put("ts_way", ts_way);
				} else {
					params3.put("ts_way", 1);// 默认
				}
				params3.put("groupid", 0);
				params3.put("unitid", 0);
				if (userid > 0) {
					params3.put("userid", userid);
				} else {
					params3.put("userid", 0);
				}
				if (null != remark) {
					params3.put("remark", remark);
				} else {
					params3.put("remark", "");
				}
				if (null != account) {
					params3.put("account", account);
				} else {
					params3.put("account", "");
				}
				if (account_type > 0) {
					params3.put("account_type", account_type);
				} else {
					params3.put("account_type", 1);
				}
				if (null != application) {
					params3.put("application", application);
				} else {
					params3.put("application", "");
				}

				params3.put("ts_type", ts_type);
				// 解冻
				myService.addmodifytsbal(params3);
				long mytsid = (long) params3.get("id");
				System.out.println("$$$$$$$$$$$$$$$$$" + mytsid);

				// 解东
				params.put("uid", userid);
				params.put("tsid", tsid);
				fpinfos = freezeService.findfpinfo(params);
				long fuserid = userid;
				int source = (int) fpinfos.get("source");
				long adminid = (long) fpinfos.get("adminid");
				BigDecimal fbalance = sum;
				int state = 1;
				long freeze_ts_id = (long) fpinfos.get("freeze_ts_id");
				long release_ts_id = mytsid;
				params2.put("userid", fuserid);
				params2.put("source", source);
				params2.put("adminid", adminid);
				params2.put("balance", fbalance);
				params2.put("state", state);
				params2.put("freeze_ts_id", freeze_ts_id);
				params2.put("release_ts_id", release_ts_id);
				// 插入一个新的解冻记录
				freezeService.addupfp(params2);
				// 用掉了
				params6.put("uid", userid);
				params6.put("utsid", mytsid);
				params3.put("balance", tsbalance);
				params3.put("oper", 1);
				myService.addmodifytsbal(params3);
				long mytsid1 = (long) params3.get("id");
				freezeService.updateupfpstate(params6);
				int singleid = Integer.parseInt(receivers[i]);
				freezeService.updatefinishsingle(singleid);
				params4.put("uid", userid);
				params4.put("tsid", mytsid1);
				myService.updateupuser(params4);
				if (reqid > 0) {
					freezeService.updatefinishsingle(reqid);
				}
			}

		}
		return SUCCESS;
	}

	/**
	 * 完成一个
	 * 1.在ts里插入一条记录，将金额先加回去，返回此时的tsid作为解冻记录id
	 * 2.在freeze表里插入一条解冻记录，并将返回的解冻记录id一起插入
	 * 3.在对应客户的记录里更新最新的balance_ts 用于查询最新的余额信息
	 * 4.在ts再插入一条记录，将金额变回去，然后更新解冻记录状态为“用掉了”
	 * @return
	 */
	public String finishsingle() {
		// 1,查询出fp表中的信息
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> params1 = new HashMap<String, Object>();
		Map<String, Object> params2 = new HashMap<String, Object>();
		Map<String, Object> params3 = new HashMap<String, Object>();
		Map<String, Object> params6 = new HashMap<String, Object>();
		Map<String, Object> params4 = new HashMap<String, Object>();
		// 存储fp信息
		Map<String, Object> fpinfos = new HashMap<String, Object>();
		// 余额信息
		Map<String, Object> balance = new HashMap<String, Object>();
		// 2,向ts表里插入一条信息并返回tsid
		// 2.1 先查找到现在最新的余额
		params1.put("uid", clouserid);
		balance = freezeService.finduserbalance(params1);

		// 1,获得最新余额
		if (null != balance.get("ubalance")) {
			tsbalance = (BigDecimal) balance.get("ubalance");
			System.out.println("*********" + tsbalance);
			tsbalance1 = tsbalance.add(sum);
			params3.put("balance", tsbalance1);
			params3.put("sum", sum);
		}
		// 2,向ts表插入一条新数据
		Map<String, Object> tsinfo = new HashMap<String, Object>();
		tsinfo = myService.findtsinfo(utsid);
		long order_cs_id = 0;
		// 取出里面的信息
		if(tsinfo.containsValue("order_cs_id")){
			 order_cs_id = Long.parseLong(String.valueOf(tsinfo.get("order_cs_id")));
		}
		long order_id = (long) tsinfo.get("order_id");
		int order_type = (int) tsinfo.get("order_type");

		String ts_type = "tx_code_011";
		int oper = 2;
		int ts_way = (int) tsinfo.get("ts_way");

		String remark = (String) tsinfo.get("remark");
		String account = (String) tsinfo.get("account");
		int account_type = (int) tsinfo.get("account_type");
		String application = "管理后台";
		// ___________________________________________________________________________________________
		if (order_cs_id > 0) {
			params3.put("order_cs_id", order_cs_id);
		} else {
			params3.put("order_cs_id", 0);
		}
		if (order_id > 0) {
			params3.put("order_id", order_id);
		} else {
			params3.put("order_id", 0);
		}
		if (order_type > 0) {
			params3.put("order_type", order_type);
		} else {
			params3.put("order_type", 0);
		}

		params3.put("oper", oper);

		if (ts_way > 0) {
			params3.put("ts_way", ts_way);
		} else {
			params3.put("ts_way", 1);// 默认
		}
		params3.put("groupid", 0);
		params3.put("unitid", 0);
		if (userid > 0) {
			params3.put("userid", userid);
		} else {
			params3.put("userid", 0);
		}
		if (null != remark) {
			params3.put("remark", remark);
		} else {
			params3.put("remark", "");
		}
		if (null != account) {
			params3.put("account", account);
		} else {
			params3.put("account", "");
		}
		if (account_type > 0) {
			params3.put("account_type", account_type);
		} else {
			params3.put("account_type", 1);
		}
		if (null != application) {
			params3.put("application", application);
		} else {
			params3.put("application", "");
		}

		params3.put("ts_type", ts_type);

		myService.addmodifytsbal(params3);
		long mytsid = (long) params3.get("id");

		System.out.println("$$$$$$$$$$$$$$$$$" + mytsid);

		params.put("uid", clouserid);
		params.put("tsid", utsid);
		fpinfos = freezeService.findfpinfo(params);
		long fuserid = clouserid;
		int source = (int) fpinfos.get("source");
		long adminid = (long) fpinfos.get("adminid");
		BigDecimal fbalance = sum;
		int state = 1;
		long freeze_ts_id = (long) fpinfos.get("freeze_ts_id");
		long release_ts_id = mytsid;
		params2.put("userid", fuserid);
		params2.put("source", source);
		params2.put("adminid", adminid);
		params2.put("balance", fbalance);
		params2.put("state", state);
		params2.put("freeze_ts_id", freeze_ts_id);
		params2.put("release_ts_id", release_ts_id);
		// 插入一个新的解冻记录
		freezeService.addupfp(params2);
		// 用掉了
		params6.put("uid", clouserid);
		params6.put("utsid", mytsid);
		params3.put("balance", tsbalance);
		params3.put("oper", 1);
		myService.addmodifytsbal(params3);
		long mytsid1 = (long) params3.get("id");
		freezeService.updateupfpstate(params6);
		params4.put("uid", clouserid);
		params4.put("tsid", mytsid1);
		myService.updateupuser(params4);
		if (reqid > 0) {
			freezeService.updatefinishsingle(reqid);
		}

		return SUCCESS;
	}

	/**
	 * 关闭一个
	 * 1.在ts表里插入一条将金额加回去，并返回tsid
	 * 2.在对应客户的记录里更新最新的balance_ts 用于查询最新的余额信息
	 * 3.在freeze表里插入一条解冻记录，并插入解冻记录id
	 * @return
	 */
	public String closesingle() {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> params1 = new HashMap<String, Object>();
		Map<String, Object> params3 = new HashMap<String, Object>();
		Map<String, Object> params4 = new HashMap<String, Object>();
		Map<String, Object> params5 = new HashMap<String, Object>();
		Map<String, Object> balance = new HashMap<String, Object>();
		Map<String, Object> fpinfos = new HashMap<String, Object>();
		long userid = 0;

		BigDecimal tsbalance;
		BigDecimal tsbalance1;
		// freezeService.insertts(params);

		params.put("uid", clouserid);
		params1.put("uid", clouserid);
		params1.put("tsid", utsid);
		balance = freezeService.finduserbalance(params);
		userid = clouserid;

		// 1,获得最新余额
		if (null != balance.get("ubalance")) {
			tsbalance = (BigDecimal) balance.get("ubalance");
			System.out.println("*********" + tsbalance);
			tsbalance1 = tsbalance.add(sum);
			params3.put("balance", tsbalance1);
			params3.put("sum", sum);
		}
		// 2,向ts表插入一条新数据
		Map<String, Object> tsinfo = new HashMap<String, Object>();
		tsinfo = myService.findtsinfo(utsid);
		// 取出里面的信息
		long order_cs_id = (long) tsinfo.get("order_cs_id");
		long order_id = (long) tsinfo.get("order_id");
		int order_type = (int) tsinfo.get("order_type");
		String ts_type = "tx_code_007";
		int oper = 1;
		int ts_way = (int) tsinfo.get("ts_way");

		String remark = (String) tsinfo.get("remark");
		String account = (String) tsinfo.get("account");
		int account_type = (int) tsinfo.get("account_type");
		String application = "管理后台";
		// ___________________________________________________________________________________________
		if (order_cs_id > 0) {
			params3.put("order_cs_id", order_cs_id);
		} else {
			params3.put("order_cs_id", 0);
		}
		if (order_id > 0) {
			params3.put("order_id", order_id);
		} else {
			params3.put("order_id", 0);
		}
		if (order_type > 0) {
			params3.put("order_type", order_type);
		} else {
			params3.put("order_type", 0);
		}

		params3.put("oper", oper);

		if (ts_way > 0) {
			params3.put("ts_way", ts_way);
		} else {
			params3.put("ts_way", 1);// 默认
		}
		params3.put("groupid", 0);
		params3.put("unitid", 0);
		if (userid > 0) {
			params3.put("userid", userid);
		} else {
			params3.put("userid", 0);
		}
		if (null != remark) {
			params3.put("remark", remark);
		} else {
			params3.put("remark", "");
		}
		if (null != account) {
			params3.put("account", account);
		} else {
			params3.put("account", "");
		}
		if (account_type > 0) {
			params3.put("account_type", account_type);
		} else {
			params3.put("account_type", 1);
		}
		if (null != application) {
			params3.put("application", application);
		} else {
			params3.put("application", "");
		}

		params3.put("ts_type", ts_type);
		myService.addmodifytsbal(params3);
		long mytsid = (long) params3.get("id");
		System.out.println("$$$$$$$$$$$$$$$$$" + mytsid);
		// 3.向user，group，unit表插入新tsid
		params4.put("uid", clouserid);
		params4.put("tsid", mytsid);

		myService.updateupuser(params4);
		// 解冻，插入解冻记录id
		if (null != fpinfos) {
			fpinfos = freezeService.findfpinfo(params1);
			long fuserid = clouserid;
			int source = (int) fpinfos.get("source");
			long adminid = (long) fpinfos.get("adminid");
			BigDecimal fbalance = sum;
			int state = 1;
			long freeze_ts_id = (long) fpinfos.get("freeze_ts_id");
			long release_ts_id = mytsid;
			params5.put("userid", fuserid);
			params5.put("source", source);
			params5.put("adminid", adminid);
			params5.put("balance", fbalance);
			params5.put("state", state);
			params5.put("freeze_ts_id", freeze_ts_id);
			params5.put("release_ts_id", release_ts_id);
			freezeService.addupfp(params5);

		}

		//
		if (reqid > 0) {
			int r = freezeService.updateclosesingle(reqid);
		}
		return SUCCESS;
	}

	// ---------------------get ---------------------set----------------

	public FreezeService getFreezeService() {
		return freezeService;
	}

	public void setFreezeService(FreezeService freezeService) {
		this.freezeService = freezeService;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getCherge() {
		return cherge;
	}

	public void setCherge(int cherge) {
		this.cherge = cherge;
	}

	public int getDeposit() {
		return deposit;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	public int getChkuser() {
		return chkuser;
	}

	public void setChkuser(int chkuser) {
		this.chkuser = chkuser;
	}

	public int getChkgroup() {
		return chkgroup;
	}

	public void setChkgroup(int chkgroup) {
		this.chkgroup = chkgroup;
	}

	public int getChkunit() {
		return chkunit;
	}

	public void setChkunit(int chkunit) {
		this.chkunit = chkunit;
	}

	public int getNotclose() {
		return notclose;
	}

	public void setNotclose(int notclose) {
		this.notclose = notclose;
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getReqid() {
		return reqid;
	}

	public void setReqid(int reqid) {
		this.reqid = reqid;
	}

	public String getReqidstr() {
		return reqidstr;
	}

	public void setReqidstr(String reqidstr) {
		this.reqidstr = reqidstr;
	}

	public int getCloaccounttype() {
		return cloaccounttype;
	}

	public void setCloaccounttype(int cloaccounttype) {
		this.cloaccounttype = cloaccounttype;
	}

	public long getClouserid() {
		return clouserid;
	}

	public void setClouserid(long clouserid) {
		this.clouserid = clouserid;
	}

	public MyFormService getMyService() {
		return myService;
	}

	public void setMyService(MyFormService myService) {
		this.myService = myService;
	}

	public int getUtsid() {
		return utsid;
	}

	public void setUtsid(int utsid) {
		this.utsid = utsid;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public String getSumstr() {
		return sumstr;
	}

	public void setSumstr(String sumstr) {
		this.sumstr = sumstr;
	}

	public String getClouseridstr() {
		return clouseridstr;
	}

	public void setClouseridstr(String clouseridstr) {
		this.clouseridstr = clouseridstr;
	}

	public String getUtsidstr() {
		return utsidstr;
	}

	public void setUtsidstr(String utsidstr) {
		this.utsidstr = utsidstr;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
}
