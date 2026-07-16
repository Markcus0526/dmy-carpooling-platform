package com.pinche.statistics.finance.dao;

import java.util.List;
import java.util.Map;

public interface FinanceMapper {
	
	public List<Finance> search(Map<String, Object> map);

}
