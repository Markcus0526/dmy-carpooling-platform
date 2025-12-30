package com.pinche.statistics.finance.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.statistics.finance.dao.Finance;
import com.pinche.statistics.finance.dao.FinanceMapper;

public class FinanceServiceImpl implements FinanceService {

	SqlSession session = null;
	FinanceMapper mapper = null;
	
	public FinanceServiceImpl() {
		session = SqlSessionHelper.getSession();
		mapper = session.getMapper(FinanceMapper.class);
	}

	@Override
	public List<Finance> search(Map<String, Object> map) {
		return mapper.search(map);
	}

}
