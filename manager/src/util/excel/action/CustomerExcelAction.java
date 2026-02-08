package util.excel.action;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import util.excel.pojo.ExcelUser;
import util.excel.service.ExcelUserService;

import com.nmdy.customerManage.group.service.GroupNewService;
import com.nmdy.customerManage.user.service.UserNewService;
import com.nmdy.financial.manage.dao.OrderEntity;
import com.nmdy.financial.manage.dao.UsersEntity;
import com.nmdy.financial.manage.service.MyFormService;
import com.opensymphony.xwork2.ActionSupport;
import com.pinche.authority.manager.service.AdministratorService;
import com.pinche.common.Constants;

public class CustomerExcelAction extends ActionSupport {
	private InputStream excelStream; // 输入流变量
	private String downloadFileName;
	private String downloadFileAdminName;
	private String downloadFileMyFormName;
	private String downloadFileGroupName;
	private String downloadFileGroupMoneyName;
	private String downloadFileGroupShowMoneyName;
	private String conditionSelect;//下拉框选项参数
	private String conditionInput;//输入查询条件
	private String verifiedStaus;//认证状态
	private UserNewService userNewService;
	private ExcelUserService excelUserService;
	private AdministratorService administratorService;
	private List<ExcelUser> list;
	private String searchType;
	private String searchValue;
	private int chkusertype;
	private int chkusertype1;
	private int usersid;
	// 修改重提用
		BigDecimal ubalance ;
		BigDecimal ubalance1 ;
		// 关闭功能
		// 查看详细
		private MyFormService myService;
		private int page;
		private int rows;
		private int ordertype1;
		private int ordertype2;
		private int ordertype3;
		private int orderstatus1;
		private int orderstatus2;
		private String begintime = "0000-00-00";
		private String endtime = "9999-12-30";
		private String ordernum;
		private JSONObject jsonObject;
		private int chkuser;
		private int chkgroup;
		private int chkunit;
		private int chkwait;
		private int chkfinish;
		private int chkrefuse;
		private int chkcancel;
		private int userselect;
		private String input;
		private String gid;
		private GroupNewService groupNewService;
		private String id;
		
	// 处理下载时 文件名是中文的方法：
	public String getDownloadFileName() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd ");
		String downloadFileName = (sf.format(new Date()).toString())+"個人客戶集團管理.xls";
		try {
			downloadFileName = new String(downloadFileName.getBytes(),"ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return downloadFileName;
	}

	public String listBillDetailsToExcel() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		if ("usercode".equals(conditionSelect)&& conditionInput.length() > 0) {
			params.put("conditionSelect", "usercode");
			params.put("conditionInput", conditionInput);
		}
		if ("username".equals(conditionSelect)&& conditionInput.length() > 0) {
			params.put("conditionSelect", "username");
			params.put("conditionInput", conditionInput);
		}

		if ("id".equals(conditionSelect)&& conditionInput.length() > 0) {
			params.put("conditionSelect", "id");
			params.put("conditionInput", conditionInput);
		}
		if ("phone".equals(conditionSelect)&& conditionInput.length() > 0) {
			params.put("conditionSelect", "phone");
			params.put("conditionInput", conditionInput);
		}
		if (verifiedStaus!=null) {
			params.put("verifiedStaus", verifiedStaus);				
		}
		try {
			list = userNewService.findUsersByConditionExcel(params);
			excelStream = excelUserService.exportPointDetails(list);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	// 处理下载时 文件名是中文的方法：
		public String getDownloadFileAdminName() {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd ");
			String downloadFileName = (sf.format(new Date()).toString())+"用户管理.xls";
			try {
				downloadFileName = new String(downloadFileName.getBytes(),"ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return downloadFileName;
		}
		
	public String listAdminToExcel(){
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("searchType:"+searchType+" searchValue:"+searchValue);
		try {
			if(searchType==null||searchType.trim().equals("")){
				searchType="0";
			}
			switch (Integer.parseInt(searchType)) {
			case Constants.SEARCH_BY_USERCODE:
				map.put("usercode",  searchValue );
				break;
			case Constants.SEARCH_BY_USERNAME:
				map.put("username", searchValue);
				break;
			case Constants.SEARCH_BY_PHONENUM:
				map.put("phone",searchValue);
				break;
			case Constants.SEARCH_BY_ID:
				map.put("id", searchValue);
				break;
			case Constants.SEARCH_BY_UNIT:
				map.put("unit", searchValue);
				break;
			}
			list = administratorService.searchForExcel(map);
			excelStream = excelUserService.exportPointUserDetails(list);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
	}
	
	
	public String getDownloadFileMyFormName() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd ");
		String downloadFileName = (sf.format(new Date()).toString())+"我的表单.xls";
		try {
			downloadFileName = new String(downloadFileName.getBytes(),"ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return downloadFileName;
	}

	public void setDownloadFileMyFormName(String downloadFileMyFormName) {
		this.downloadFileMyFormName = downloadFileMyFormName;
	}

	/**
	 * 选择单个订单
	 * @return
	 */
	public String listMyFormToExcel() {
		List<UsersEntity> users;// 返回的实体类存放处
		Map<String, Object> params = new HashMap<String, Object>();
		if (chkuser == 1) {
			params.put("gr", "个人客户");

		}
		if (chkgroup == 1) {
			params.put("jt", "集团客户");

		}
		if (chkunit == 1) {
			params.put("hz", "合作单位");

		}
		if (chkwait == 1) {
			params.put("dsh", "待审核");
		}
		if (chkfinish == 1) {
			params.put("ysh", "已审核");
		}
		if (chkrefuse == 1) {
			params.put("ybh", "已驳回");
		}
		if (chkcancel == 1) {
			params.put("ycx", "已撤销");
		}
		if (userselect == 0) {
			params.put("userid", input);

		}
		if (userselect == 1) {
			params.put("username", input);

		}
		if (userselect == 2) {
			params.put("phone", input);
		}
		if (userselect == 3) {
			params.put("sum", input);
		}
		if (userselect == 4) {
			params.put("order_cs_id", input);

		}
		users = myService.find(params);
		excelStream = excelUserService.exportPointMyFormDetails(users);
		return SUCCESS;
	}
	
	public String getDownloadFileGroupName() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd ");
		String downloadFileName = (sf.format(new Date()).toString())+"集团客户管理.xls";
		try {
			downloadFileName = new String(downloadFileName.getBytes(),"ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return downloadFileName;
	}
	
	public void setDownloadFileGroupName(String downloadFileMyFormName) {
		this.downloadFileMyFormName = downloadFileMyFormName;
	}
	
	public String listGroupToExcel() {
		/*		HttpServletRequest request = ServletActionContext.getRequest();
				String gid=request.getParameter("gid");
				System.out.println("gid is :"+gid);*/
				//System.out.println("conditionSelect:"+conditionSelect+"       dadd  gid:"+gid);
				try {
					List<Map<String, Object>> list = null;
					Map<String, Object> params = new HashMap<String, Object>();
					
					if ("usercode".equals(conditionSelect)&& conditionInput.length() > 0) {
						params.put("conditionSelect", "usercode");
						params.put("conditionInput", conditionInput);
					}
					if ("username".equals(conditionSelect)&& conditionInput.length() > 0) {
						params.put("conditionSelect", "username");
						params.put("conditionInput", conditionInput);
					}

					if ("id".equals(conditionSelect)&& conditionInput.length() > 0) {
						params.put("conditionSelect", "id");
						params.put("conditionInput", conditionInput);
					}
					if ("phone".equals(conditionSelect)&& conditionInput.length() > 0) {
						params.put("conditionSelect", "phone");
						params.put("conditionInput", conditionInput);
					}
					if (gid!=null) {
						params.put("gid",Long.parseLong(gid));
					}
					// 查询结果从第0条开始，查询10条记录

					list = groupNewService.findGroupDriversByCondition(params);
					excelStream = excelUserService.exportPointGroupDetails(list);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return SUCCESS;
			}
	
	public String getDownloadFileGroupMoneyName() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String downloadFileName = (sf.format(new Date()).toString())+"返利信息.xls";
		try {
			downloadFileName = new String(downloadFileName.getBytes(),"ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return downloadFileName;
	}
	
	public void setDownloadFileGroupMoneyName(String downloadFileMyFormName) {
		this.downloadFileMyFormName = downloadFileMyFormName;
	}
	
	/**
	 * 获得所有返利列表
	 * @return
	 */
	public  String listGroupMoneyToExcel(){
		List<Map<String, Object>> list = null;
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("id", Long.parseLong(id));
		params.put("id", id);
		list = groupNewService.findGroupAllRebateListInfo(params);
		excelStream = excelUserService.exportPointGroupGetMoneyDetails(list);
		return SUCCESS;
	}
	
	public String getDownloadFileGroupShowMoneyName() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String downloadFileName = (sf.format(new Date()).toString())+"查看返利详情.xls";
		try {
			downloadFileName = new String(downloadFileName.getBytes(),"ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return downloadFileName;
	}
	
	public void setDownloadFileGroupShowMoneyName(String downloadFileGroupShowMoneyName) {
		this.downloadFileGroupShowMoneyName = downloadFileGroupShowMoneyName;
	}
	
	/**
	 * 获得指定用户的返利列表信息
	 * @return
	 */
	public String listGroupShowMoneyToExcel(){
		List<Map<String, Object>> list = groupNewService.findGroupRebateListInfo(Long.parseLong(id));
		excelStream = excelUserService.exportPointGroupShowMoneyDetails(list);
		return SUCCESS;
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getConditionSelect() {
		return conditionSelect;
	}

	public void setConditionSelect(String conditionSelect) {
		this.conditionSelect = conditionSelect;
	}

	public String getConditionInput() {
		return conditionInput;
	}

	public void setConditionInput(String conditionInput) {
		this.conditionInput = conditionInput;
	}

	public String getVerifiedStaus() {
		return verifiedStaus;
	}

	public void setVerifiedStaus(String verifiedStaus) {
		this.verifiedStaus = verifiedStaus;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public UserNewService getUserNewService() {
		return userNewService;
	}

	public void setUserNewService(UserNewService userNewService) {
		this.userNewService = userNewService;
	}

	public ExcelUserService getExcelUserService() {
		return excelUserService;
	}

	public void setExcelUserService(ExcelUserService excelUserService) {
		this.excelUserService = excelUserService;
	}

	public List<ExcelUser> getList() {
		return list;
	}

	public void setList(List<ExcelUser> list) {
		this.list = list;
	}

	public AdministratorService getAdministratorService() {
		return administratorService;
	}

	public void setAdministratorService(AdministratorService administratorService) {
		this.administratorService = administratorService;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public void setDownloadFileAdminName(String downloadFileAdminName) {
		this.downloadFileAdminName = downloadFileAdminName;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public int getChkusertype() {
		return chkusertype;
	}

	public void setChkusertype(int chkusertype) {
		this.chkusertype = chkusertype;
	}

	public int getChkusertype1() {
		return chkusertype1;
	}

	public void setChkusertype1(int chkusertype1) {
		this.chkusertype1 = chkusertype1;
	}

	public int getUsersid() {
		return usersid;
	}

	public void setUsersid(int usersid) {
		this.usersid = usersid;
	}

	public BigDecimal getUbalance() {
		return ubalance;
	}

	public void setUbalance(BigDecimal ubalance) {
		this.ubalance = ubalance;
	}

	public BigDecimal getUbalance1() {
		return ubalance1;
	}

	public void setUbalance1(BigDecimal ubalance1) {
		this.ubalance1 = ubalance1;
	}

	public MyFormService getMyService() {
		return myService;
	}

	public void setMyService(MyFormService myService) {
		this.myService = myService;
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

	public int getOrdertype1() {
		return ordertype1;
	}

	public void setOrdertype1(int ordertype1) {
		this.ordertype1 = ordertype1;
	}

	public int getOrdertype2() {
		return ordertype2;
	}

	public void setOrdertype2(int ordertype2) {
		this.ordertype2 = ordertype2;
	}

	public int getOrdertype3() {
		return ordertype3;
	}

	public void setOrdertype3(int ordertype3) {
		this.ordertype3 = ordertype3;
	}

	public int getOrderstatus1() {
		return orderstatus1;
	}

	public void setOrderstatus1(int orderstatus1) {
		this.orderstatus1 = orderstatus1;
	}

	public int getOrderstatus2() {
		return orderstatus2;
	}

	public void setOrderstatus2(int orderstatus2) {
		this.orderstatus2 = orderstatus2;
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

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
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

	public int getChkwait() {
		return chkwait;
	}

	public void setChkwait(int chkwait) {
		this.chkwait = chkwait;
	}

	public int getChkfinish() {
		return chkfinish;
	}

	public void setChkfinish(int chkfinish) {
		this.chkfinish = chkfinish;
	}

	public int getChkrefuse() {
		return chkrefuse;
	}

	public void setChkrefuse(int chkrefuse) {
		this.chkrefuse = chkrefuse;
	}

	public int getChkcancel() {
		return chkcancel;
	}

	public void setChkcancel(int chkcancel) {
		this.chkcancel = chkcancel;
	}

	public int getUserselect() {
		return userselect;
	}

	public void setUserselect(int userselect) {
		this.userselect = userselect;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public GroupNewService getGroupNewService() {
		return groupNewService;
	}

	public void setGroupNewService(GroupNewService groupNewService) {
		this.groupNewService = groupNewService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
