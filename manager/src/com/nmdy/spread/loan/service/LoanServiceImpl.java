package com.nmdy.spread.loan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.nmdy.spread.loan.dao.Activities;
import com.nmdy.spread.loan.dao.ActivitiesMapper;
import com.nmdy.spread.loan.dao.City;
import com.nmdy.spread.loan.dao.CityMapper;
import com.nmdy.spread.loan.dao.CouponSendLog;
import com.nmdy.spread.loan.dao.CouponSendLogMapper;
import com.nmdy.spread.loan.dao.GlobalParams;
import com.nmdy.spread.loan.dao.GlobalParamsMapper;
import com.nmdy.spread.loan.dao.SingleCoupon;
import com.nmdy.spread.loan.dao.SingleCouponMapper;
import com.nmdy.spread.loan.dao.Syscoupon;
import com.nmdy.spread.loan.dao.Syscoupon1;
import com.nmdy.spread.loan.dao.SyscouponMapper;

public class LoanServiceImpl implements LoanService {

	private static final int SYS_COUPONCODE_LEN = 6;
	private static final String SYS_COUPONCODE_PREFIX = "C";
	private static final int SINGLE_COUPONCODE_LEN = 8;
	private static final String SINGLE_COUPONCODE_PREFIX = "SIC";
	
	private SyscouponMapper syscouponMapper;
	private CityMapper cityMapper;
	private CouponSendLogMapper couponSendLogMapper;
	private SingleCouponMapper singleCouponMapper;
	private GlobalParamsMapper globalParamsMapper;
	private ActivitiesMapper activitiesMapper;
	@Override
	public List<Syscoupon> search(String releaseChannel, String isNoConditionCash, String isConditionCash, String isGood) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			return syscouponMapper.search(releaseChannel, isNoConditionCash, isConditionCash, isGood);
		} finally {
//			session.close();
		}
	}

	@Override
	public List<Map<String, Object>> findPagination(Map<String, Object> map) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
		
			return syscouponMapper.findPagination(map);
		} finally {
//			session.close();
		}
	}
	
	public List<Syscoupon> findByConditionForGame(Map<String, Object> map) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
		
			return syscouponMapper.findByConditionForGame(map);
		} finally {
//			session.close();
		}
	}
	
	@Override
	public List<Map<String, Object>> getUserBasicInfoList(
			Map<String, Object> searchParam) {
		try {
		
			return syscouponMapper.getUserBasicInfoList(searchParam);
		} finally {
//			session.close();
		}
	}
	
	public int getUserCountByCondition(Map<String, Object> params) {
		try {
		
			return syscouponMapper.getUserCountByCondition(params);
		} finally {
//			session.close();
		}
	}
	
	@Override
	public int getSys_couponCountbyCondition(Map<String, Object> params) {
		
			return  syscouponMapper.getSys_couponCountbyCondition(params);
	}

	@Override
	public String genNewCouponCode() {
		
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
		
			String ret = SYS_COUPONCODE_PREFIX;
			int maxId = syscouponMapper.getMaxId();
			String strMaxId = String.valueOf(maxId);
			for(int i=0; i<SYS_COUPONCODE_LEN-1-strMaxId.length(); i++)
				ret += "0";
			ret += String.valueOf(maxId + 1);
			return ret;
		} finally {
//			session.close();
		}
	}

	@Override
	public  void insert(Syscoupon newCoupon) {
		try {
			syscouponMapper.insert(newCoupon);
		} finally {
		}
		
	}

	@Override
	public void insertCouponSendLog(CouponSendLog newSendLog) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			couponSendLogMapper.insert(newSendLog);
		} finally {
//			session.close();
		}
		
	}

	@Override
	public Syscoupon findOneSyscouponById(long syscouponId) {
		try {
		
			return syscouponMapper.findOneById(syscouponId);
		} finally {
//			session.close();
		}
	}

	@Override
	public String genNewSingleCouponCode() {
		try {
			String ret = SINGLE_COUPONCODE_PREFIX;
			int maxId = syscouponMapper.getMaxId();
			String strMaxId = String.valueOf(maxId);
			for(int i=0; i<SINGLE_COUPONCODE_LEN-1-strMaxId.length(); i++)
				ret += "0";
			ret += String.valueOf(maxId + 1);
			return ret;
		} finally {
//			session.close();
		}
	}

	@Override
	public void insertSingleCoupon(SingleCoupon singleCoupon) {
		try {
			singleCouponMapper.insert(singleCoupon);
		} finally {
//			session.close();
		}
	}

	@Override
	public void updateSyscoupon(Syscoupon syscoupon) {
		try {
		
			syscouponMapper.update(syscoupon);
		} finally {
//			session.close();
		}
	}

	@Override
	public List<CouponSendLog> searchCouponSendLogs(String syscouponCode) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("searchall", 1);
			map.put("syscouponCode", syscouponCode);
			return couponSendLogMapper.search(map);
		} finally {
//			session.close();
		}
	}

	@Override
	public List<CouponSendLog> searchCouponSendLogs(Map<String, Object> map) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			map.put("searchall", 0);
			return couponSendLogMapper.search(map);
		} finally {
//			session.close();
		}
	}

	@Override
	public List<Syscoupon> search4autoSend(String couponCode, Integer price, String validPeriod,int limit) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("searchall", 1);
			map.put("couponCode", "%"+couponCode+"%");
			map.put("point", price);
			if(validPeriod!=null && !"".equals(validPeriod)){
				map.put("validPeriod", validPeriod);
				}else{
					map.put("validPeriod", "");
				}
	          int valid_period_unit=0;
				if(limit==0){
					valid_period_unit=1;
				}if(limit==1){
					valid_period_unit=2;
				}if(limit==2){
					valid_period_unit=3;
				}if(limit==3){
					valid_period_unit=4;
				}
			map.put("valid_period_unit", valid_period_unit);
			return syscouponMapper.search4autoSend(map);
	}

	@Override
	public List<Syscoupon> findPagination4autoSend(Map<String, Object> map) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			map.put("searchall", 0);
		
			return syscouponMapper.search4autoSend(map);
		} finally {
//			session.close();
		}
	}

	@Override
	public GlobalParams getGlobalParam() {
		try {
			return globalParamsMapper.findOneById(1);
		} finally {
		}
	}

	@Override
	public City findOneCityByName(String name) {
		try {
			return cityMapper.findOneByName(name);
		} finally {
		}
	}

	@Override
	public void updateGlobalParam(Map<String, Object> params) {
		try {
			globalParamsMapper.update(params);
		} finally {
		}
	}

	@Override
	public void updateCity(Map<String, Object> params) {
		try {
			cityMapper.update(params);
		} finally {
		}
	}

	@Override
	public List<Activities> search4activity(String activityCode, int price) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("searchall", 1);
			map.put("activityCode", '%'+activityCode+'%');
			map.put("price", price);
			return activitiesMapper.search(map);
		} finally {
		}
	}

	@Override
	public List<Activities> findPagination4activity(Map<String, Object> map) {
		try {
			map.put("searchall", 0);
			return activitiesMapper.search(map);
		} finally {
		}
	}

	@Override
	public void insertActivity(Activities activity) {
		try {
						
			activitiesMapper.insert(activity);
		} finally {
		}
	}

	@Override
	public Activities findOneActivityByCode(String activityCode) {
		try {
			return activitiesMapper.findOneByCode(activityCode);
		} finally {
		}
	}

	@Override
	public int updateActivity(String stopActivityCode) {
			return activitiesMapper.update(stopActivityCode);
	}

	@Override
	public Syscoupon1 findSyscouponById(long id) {
		try {
		
			return syscouponMapper.findSyscouponById(id);
		} finally {
		}
	}

	@Override
	public int insertNotifyPersion(Map<String, Object> mm) {
	return syscouponMapper.insertNotifyPersion(mm);
			}
		

	@Override
	public int insertCouponSendLog(Map<String, Object> mm) {
	return syscouponMapper.insertCouponSendLog(mm);
	}

	@Override
	public City findCityParam(String name) {
		return cityMapper.findCityParam(name);
	}

	@Override
	public City findglobal() {
		return cityMapper.findglobal();
	}

	public SyscouponMapper getSyscouponMapper() {
		return syscouponMapper;
	}

	public void setSyscouponMapper(SyscouponMapper syscouponMapper) {
		this.syscouponMapper = syscouponMapper;
	}

	public CityMapper getCityMapper() {
		return cityMapper;
	}

	public void setCityMapper(CityMapper cityMapper) {
		this.cityMapper = cityMapper;
	}

	public CouponSendLogMapper getCouponSendLogMapper() {
		return couponSendLogMapper;
	}

	public void setCouponSendLogMapper(CouponSendLogMapper couponSendLogMapper) {
		this.couponSendLogMapper = couponSendLogMapper;
	}

	public SingleCouponMapper getSingleCouponMapper() {
		return singleCouponMapper;
	}

	public void setSingleCouponMapper(SingleCouponMapper singleCouponMapper) {
		this.singleCouponMapper = singleCouponMapper;
	}

	public GlobalParamsMapper getGlobalParamsMapper() {
		return globalParamsMapper;
	}

	public void setGlobalParamsMapper(GlobalParamsMapper globalParamsMapper) {
		this.globalParamsMapper = globalParamsMapper;
	}

	public ActivitiesMapper getActivitiesMapper() {
		return activitiesMapper;
	}

	public void setActivitiesMapper(ActivitiesMapper activitiesMapper) {
		this.activitiesMapper = activitiesMapper;
	}
	
}
