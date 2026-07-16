package com.pinche.statistics.passenger.service;

import java.util.List;
import java.util.Map;

import com.pinche.statistics.passenger.dao.Passenger;

public interface PassengerService {
	public List<Passenger> search(Map<String, Object> map);
}
