package com.pinche.statistics.passenger.dao;

import java.util.List;
import java.util.Map;

public interface PassengerMapper {
	
	public List<Passenger> search(Map<String, Object> map);

}
