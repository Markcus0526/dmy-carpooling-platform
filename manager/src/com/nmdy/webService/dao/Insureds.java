package com.nmdy.webService.dao;

import java.util.List;

public class Insureds {

	private  String isd_count;
	private List<Insured_data>  Insured_List;

	
	
	public String getIsd_count() {
		return isd_count;
	}
	public void setIsd_count(String isd_count) {
		this.isd_count = isd_count;
	}
	public List<Insured_data> getInsured_List() {
		return Insured_List;
	}
	public void setInsured_List(List<Insured_data> insured_List) {
		Insured_List = insured_List;
	}


	
	
}
