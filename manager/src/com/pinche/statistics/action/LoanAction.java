package com.pinche.statistics.action;

import java.util.*;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pinche.common.WebUtil;
import com.pinche.statistics.coupon.dao.Coupon;
import com.pinche.statistics.coupon.dao.SCoupon;
import com.pinche.statistics.coupon.dao.SearchItem;
import com.pinche.statistics.coupon.service.CouponService;
import com.pinche.statistics.coupon.service.SCouponService;

public class LoanAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CouponService couponService;
	private List<Coupon> couponList;
	private JSONObject resultObj;
	private SearchItem searchItem;
	private Integer rowLen;
	private int page;
	private Integer rows;
	
	private Integer selectedID;
	private SCouponService scouponService;
	private List<SCoupon> scouponList;
	private JSONObject sresultObj;
	private SearchItem ssearchItem;
	private Integer srowLen;
	private Integer spage;
	private Integer srows;
	
	public SCouponService getScouponService() {
		return scouponService;
	}

	public void setScouponService(SCouponService scouponService) {
		this.scouponService = scouponService;
	}

	public List<SCoupon> getScouponList() {
		return scouponList;
	}

	public void setScouponList(List<SCoupon> scouponList) {
		this.scouponList = scouponList;
	}

	public JSONObject getSresultObj() {
		return sresultObj;
	}

	public void setSresultObj(JSONObject sresultObj) {
		this.sresultObj = sresultObj;
	}

	public SearchItem getSsearchItem() {
		return ssearchItem;
	}

	public void setSsearchItem(SearchItem ssearchItem) {
		this.ssearchItem = ssearchItem;
	}

	public Integer getSrowLen() {
		return srowLen;
	}

	public void setSrowLen(Integer srowLen) {
		this.srowLen = srowLen;
	}

	public int getSpage() {
		return spage;
	}

	public void setSpage(int spage) {
		this.spage = spage;
	}

	public Integer getSrows() {
		return srows;
	}

	public void setSrows(Integer srows) {
		this.srows = srows;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	public Integer getRowLen() {
		return rowLen;
	}

	public void setRowLen(Integer rowLen) {
		this.rowLen = rowLen;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}


	public SearchItem getSearchItem() {
		return searchItem;
	}

	public void setSearchItem(SearchItem searchItem) {
		this.searchItem = searchItem;
	}

	public String index() {
		return SUCCESS;
	}
	
	public String search() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {		
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			map.put("isTotal", 0);
			map.put("beginDate", searchItem.getBeginDate());
			map.put("endDate", searchItem.getEndDate());
			map.put("beginTime", searchItem.getBeginTime());
			map.put("endTime", searchItem.getEndTime());
			map.put("releaseChannel", searchItem.getRChannel());
			map.put("City", searchItem.getStaticsCity());
			map.put("noCondCash", searchItem.getNocondcash());
			map.put("condCash", searchItem.getCondcash());
			map.put("good", searchItem.getGood());
			map.put("singlePinche", searchItem.getSinglePinche());
			map.put("dimPinche", searchItem.getDimPinche());
			map.put("divPinche", searchItem.getDivPinche());
			map.put("longPinche", searchItem.getLongPinhe());
			
			couponList = couponService.search(map);
			rowLen = couponList.size();
			map.put("isTotal", 1);
			couponList = couponService.search(map);
		} catch (Exception e) {
			e.printStackTrace();
			resultObj = null;
			return ERROR;
		}
		
		WebUtil._setResponseContentType("application/json");
		makeJSONObjFromList();

		return SUCCESS;
	}
	
	public String viewDetail() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {		
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			map.put("isTotal", 0);
			map.put("selectedId", searchItem.getSelectedId());
			map.put("beginDate", searchItem.getBeginDate());
			map.put("endDate", searchItem.getEndDate());
			map.put("beginTime", searchItem.getBeginTime());
			map.put("endTime", searchItem.getEndTime());
			map.put("releaseChannel", searchItem.getRChannel());
			map.put("City", searchItem.getStaticsCity());
			map.put("noCondCash", searchItem.getNocondcash());
			map.put("condCash", searchItem.getCondcash());
			map.put("good", searchItem.getGood());
			map.put("singlePinche", searchItem.getSinglePinche());
			map.put("dimPinche", searchItem.getDimPinche());
			map.put("divPinche", searchItem.getDivPinche());
			map.put("longPinche", searchItem.getLongPinhe());
			map.put("couponId",selectedID);
			map.put("date_type", searchItem.getDateType());
			scouponList = scouponService.viewDetail(map);
			rowLen = scouponList.size();
			map.put("isTotal", 1);
			scouponList = scouponService.viewDetail(map);
		} catch (Exception e) {
			e.printStackTrace();
			resultObj = null;
			return ERROR;
		}
		
		WebUtil._setResponseContentType("application/json");
		makeJSONObjFromsList();

		return SUCCESS;
	}
	
	private void makeJSONObjFromList() {

		if (couponList == null) {
			resultObj = null;
			return;
		}

		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Coupon info : couponList) {

			Map<String, Object> m = new HashMap<String, Object>();
			
			String url = String.format("<a href=\"javascript:view(%d)\">选择</a>",info.getId());
			m.put("view", url);
			m.put("couponid", info.getSyscoupon_code());
			m.put("releasechannel", info.getRelease_channel());
			//m.put("content", info.);
			if(info.getGoods_or_cash() == 2)
			{
				m.put("content",info.getGoods());
			}
			else if(info.getGoods_or_cash() ==1)
			{
				m.put("content", info.getSum());				
			}
			m.put("goodsorcash", info.getGoods_or_cash());
			m.put("coupontype", info.getCoupon_type());
			m.put("validperiod", info.getValid_period());
			m.put("deploycount", info.getDeployCount());
			m.put("usecount", info.getUseCount());
			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		
		json.put("total", rowLen);
		json.put("rows", al);
		resultObj = JSONObject.fromObject(json);
	}

	private void makeJSONObjFromsList() {

		if (scouponList == null) {
			sresultObj = null;
			return;
		}

		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (SCoupon info : scouponList) {
			Map<String, Object> m = new HashMap<String, Object>();
/*			String url = String.format("<a href=\"javascript:view(%d)\">选择</a>",info.getId());
			m.put("view", url);
			m.put("couponid", info.getSyscoupon_code());
			m.put("releasechannel", info.getRelease_channel());
			//m.put("content", info.);
			if(info.getGoods_or_cash() == 2)
			{
				m.put("content",info.getGoods());
			}
			else if(info.getGoods_or_cash() ==1)
			{
				m.put("content", info.getSum());				
			}
			m.put("goodsorcash", info.getGoods_or_cash());
			m.put("coupontype", info.getCoupon_type());
			m.put("validperiod", info.getValid_period());
			m.put("deploycount", info.getDeployCount());
			m.put("usecount", info.getUseCount()); info.getSc_date()- info.getUsed_date()*/
			m.put("view", info.getValid_period_unit());
			m.put("issuedCount", info.getIsused());
			m.put("usedCount", info.getUseCount());
			m.put("deltatime",info.getValid_period() / info.getUseCount());
			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		
		json.put("total", rowLen);
		json.put("rows", al);
		sresultObj = JSONObject.fromObject(json);
	}

	public CouponService getCouponService() {
		return couponService;
	}

	public void setCouponService(CouponService couponService) {
		this.couponService = couponService;
	}

	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	public List<Coupon> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<Coupon> couponList) {
		this.couponList = couponList;
	}

	public Integer getSelectedID() {
		return selectedID;
	}

	public void setSelectedID(Integer selectedID) {
		this.selectedID = selectedID;
	}
}
