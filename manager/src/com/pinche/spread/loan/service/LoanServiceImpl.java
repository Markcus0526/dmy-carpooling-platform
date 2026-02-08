package com.pinche.spread.loan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.tomcat.util.http.mapper.Mapper;

import com.pinche.common.Constants;
import com.pinche.common.SqlSessionHelper;
import com.pinche.spread.loan.dao.Activities;
import com.pinche.spread.loan.dao.ActivitiesMapper;
import com.pinche.spread.loan.dao.City;
import com.pinche.spread.loan.dao.CityMapper;
import com.pinche.spread.loan.dao.CouponSendLog;
import com.pinche.spread.loan.dao.CouponSendLogMapper;
import com.pinche.spread.loan.dao.GlobalParams;
import com.pinche.spread.loan.dao.GlobalParamsMapper;
import com.pinche.spread.loan.dao.SingleCoupon;
import com.pinche.spread.loan.dao.SingleCouponMapper;
import com.pinche.spread.loan.dao.Syscoupon;
import com.pinche.spread.loan.dao.Syscoupon1;
import com.pinche.spread.loan.dao.SyscouponMapper;

public class LoanServiceImpl implements LoanService {

	private static final int SYS_COUPONCODE_LEN = 6;
	private static final String SYS_COUPONCODE_PREFIX = "C";
	private static final int SINGLE_COUPONCODE_LEN = 8;
	private static final String SINGLE_COUPONCODE_PREFIX = "SIC";
	
	SqlSession session = null;

	public LoanServiceImpl() {
		session = SqlSessionHelper.getSession();
	}
	
	@Override
	public List<Syscoupon> search(String releaseChannel, String isNoConditionCash, String isConditionCash, String isGood) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			return mapper.search(releaseChannel, isNoConditionCash, isConditionCash, isGood);
		} finally {
//			session.close();
		}
	}

	@Override
	public List<Map<String, Object>> findPagination(Map<String, Object> map) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			return mapper.findPagination(map);
		} finally {
//			session.close();
		}
	}
	
	public List<Syscoupon> findByConditionForGame(Map<String, Object> map) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			return mapper.findByConditionForGame(map);
		} finally {
//			session.close();
		}
	}
	
	@Override
	public List<Map<String, Object>> getUserBasicInfoList(
			Map<String, Object> searchParam) {
		try {
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			return mapper.getUserBasicInfoList(searchParam);
		} finally {
//			session.close();
		}
	}
	
	public int getUserCountByCondition(Map<String, Object> params) {
		try {
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			return mapper.getUserCountByCondition(params);
		} finally {
//			session.close();
		}
	}
	
	@Override
	public int getSys_couponCountbyCondition(Map<String, Object> params) {
		try {
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			return  mapper.getSys_couponCountbyCondition(params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public String genNewCouponCode() {
		
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			String ret = SYS_COUPONCODE_PREFIX;
			int maxId = mapper.getMaxId();
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
	public void insert(Syscoupon newCoupon) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			/*	if(newCoupon.getGoods_or_cash() == Constants.SyscouponTable.GoodsOrCash.GOODS) {
				newCoupon.setGoods(newCoupon.getSumOrGoodsname());
			}else {
				int t = 0;
				try {
					t = Integer.parseInt( newCoupon.getSumOrGoodsname());
				}catch(Exception e) {
					e.printStackTrace();
				}
				newCoupon.setSum(t);
			}*/
			mapper.insert(newCoupon);
			session.commit();
		} finally {
//			session.close();
		}
		
	}

	@Override
	public void insertCouponSendLog(CouponSendLog newSendLog) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			CouponSendLogMapper mapper = session.getMapper(CouponSendLogMapper.class);
			mapper.insert(newSendLog);
			session.commit();
		} finally {
//			session.close();
		}
		
	}

	@Override
	public Syscoupon findOneSyscouponById(long syscouponId) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			return mapper.findOneById(syscouponId);
		} finally {
//			session.close();
		}
	}

	@Override
	public String genNewSingleCouponCode() {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			SingleCouponMapper mapper = session.getMapper(SingleCouponMapper.class);
			String ret = SINGLE_COUPONCODE_PREFIX;
			int maxId = mapper.getMaxId();
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
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			SingleCouponMapper mapper = session.getMapper(SingleCouponMapper.class);
			mapper.insert(singleCoupon);
			session.commit();
		} finally {
//			session.close();
		}
	}

	@Override
	public void updateSyscoupon(Syscoupon syscoupon) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			mapper.update(syscoupon);
			session.commit();
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
			CouponSendLogMapper mapper = session.getMapper(CouponSendLogMapper.class);
			return mapper.search(map);
		} finally {
//			session.close();
		}
	}

	@Override
	public List<CouponSendLog> searchCouponSendLogs(Map<String, Object> map) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			map.put("searchall", 0);
			CouponSendLogMapper mapper = session.getMapper(CouponSendLogMapper.class);
			return mapper.search(map);
		} finally {
//			session.close();
		}
	}

	@Override
	public List<Syscoupon> search4autoSend(String couponCode, Integer price,
			String validPeriod) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("searchall", 1);
			map.put("couponCode", couponCode);
			map.put("point", price);
			if(validPeriod!=null && !"".equals(validPeriod)){
			int validPeriod1 = Integer.parseInt(validPeriod.substring(0,validPeriod.length()-1));
				String class1=validPeriod.substring(validPeriod.length()-1);
			map.put("validPeriod", validPeriod1);
			int valid_period_unit=0;
			if(class1.equals("日")){
				valid_period_unit=1;
			}if(class1.equals("周")){
				valid_period_unit=2;
			}if(class1.equals("月")){
				valid_period_unit=3;
			}if(class1.equals("年")){
				valid_period_unit=4;
			}	
			map.put("valid_period_unit", valid_period_unit);
			}else{
				map.put("validPeriod", validPeriod);
				map.put("valid_period_unit", 0);
			}
		
			
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			return mapper.search4autoSend(map);
		} finally {
//			session.close();
		}
	}

	@Override
	public List<Syscoupon> findPagination4autoSend(Map<String, Object> map) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			map.put("searchall", 0);
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			return mapper.search4autoSend(map);
		} finally {
//			session.close();
		}
	}

	@Override
	public GlobalParams getGlobalParam() {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			GlobalParamsMapper mapper = session.getMapper(GlobalParamsMapper.class);
			return mapper.findOneById(1);
		} finally {
//			session.close();
		}
	}

	@Override
	public City findOneCityByName(String name) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			CityMapper mapper = session.getMapper(CityMapper.class);
			return mapper.findOneByName(name);
		} finally {
//			session.close();
		}
	}

	@Override
	public void updateGlobalParam(Map<String, Object> params) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			GlobalParamsMapper mapper = session.getMapper(GlobalParamsMapper.class);
			mapper.update(params);
			session.commit();
		} finally {
//			session.close();
		}
	}

	@Override
	public void updateCity(Map<String, Object> params) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			CityMapper mapper = session.getMapper(CityMapper.class);
			mapper.update(params);
			session.commit();
		} finally {
//			session.close();
		}
	}

	@Override
	public List<Activities> search4activity(String activityCode, int price) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("searchall", 1);
			map.put("activityCode", activityCode);
			map.put("price", price);

			ActivitiesMapper mapper = session.getMapper(ActivitiesMapper.class);
			return mapper.search(map);
		} finally {
//			session.close();
		}
	}

	@Override
	public List<Activities> findPagination4activity(Map<String, Object> map) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			map.put("searchall", 0);
			ActivitiesMapper mapper = session.getMapper(ActivitiesMapper.class);
			return mapper.search(map);
		} finally {
//			session.close();
		}
	}

	@Override
	public void insertActivity(Activities activity) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			ActivitiesMapper mapper = session.getMapper(ActivitiesMapper.class);
						
			mapper.insert(activity);
			session.commit();
		} finally {
//			session.close();
		}
	}

	@Override
	public Activities findOneActivityByCode(String activityCode) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			ActivitiesMapper mapper = session.getMapper(ActivitiesMapper.class);
			return mapper.findOneByCode(activityCode);
		} finally {
//			session.close();
		}
	}

	@Override
	public void updateActivity(Activities activity) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			ActivitiesMapper mapper = session.getMapper(ActivitiesMapper.class);
			mapper.update(activity);
			session.commit();
		} finally {
//			session.close();
		}
	}

	@Override
	public Syscoupon1 findSyscouponById(long id) {
//		SqlSession session = com.pinche.common.SqlSessionHelper.getSession();
		try {
			SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
			return mapper.findSyscouponById(id);
		} finally {
//			session.close();
		}
	}

	@Override
	public int insertNotifyPersion(Map<String, Object> mm) {
		SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
		int row=mapper.insertNotifyPersion(mm);
		if(row>0){
			session.commit();
		}
		return row;
	}

	@Override
	public int insertCouponSendLog(Map<String, Object> mm) {
		SyscouponMapper mapper = session.getMapper(SyscouponMapper.class);
		int row=mapper.insertCouponSendLog(mm);
		if(row>0){
			session.commit();
		}
		return row;
	}

	@Override
	public City findCityParam(String name) {
		CityMapper mapper = session.getMapper(CityMapper.class);
		return mapper.findCityParam(name);
	}

	@Override
	public City findglobal() {
		CityMapper mapper = session.getMapper(CityMapper.class);
		return mapper.findglobal();
	}
}
