package com.nmdy.webService.service;

import java.util.List;

import javax.jws.WebService;

import com.nmdy.webService.dao.Insurance;
import com.nmdy.webService.dao.InsuranceDTD;

@WebService
public interface InsuranceService {

	public abstract Insurance fetcheInsuData(int insuID);

	public abstract List<InsuranceDTD> fetcheInsuDTD(int start, int end);
	
	public abstract boolean feedback(int id ,String appl_no ,String errorMsg);

}