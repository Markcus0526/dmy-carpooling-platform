package com.pinche.statistics.finance.service;

import java.util.List;
import java.util.Map;

import com.pinche.statistics.finance.dao.Finance;

public interface FinanceService {
	public List<Finance> search(Map<String, Object> map);
}
