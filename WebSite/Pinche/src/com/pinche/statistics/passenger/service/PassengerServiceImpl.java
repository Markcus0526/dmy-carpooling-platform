package com.pinche.statistics.passenger.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.statistics.passenger.dao.Passenger;
import com.pinche.statistics.passenger.dao.PassengerMapper;

public class PassengerServiceImpl implements PassengerService {

	SqlSession session = null;
	PassengerMapper mapper = null;
	
	public PassengerServiceImpl() {
		session = SqlSessionHelper.getSession();
		mapper = session.getMapper(PassengerMapper.class);
	}

	@Override
	public List<Passenger> search(Map<String, Object> map) {
		return mapper.search(map);
	}

}
