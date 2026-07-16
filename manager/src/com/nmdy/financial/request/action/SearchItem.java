package com.nmdy.financial.request.action;

public class SearchItem {
	private String userid;
	private String username;
	private String phone;
	private int chkcharge;
	private int chkdischarge;
	private int chkuser;
	private int chkgroup;
	private int chkunit;
	private int status;
	private String start_time;
	private String end_time;
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public int getChkcharge() {
		return chkcharge;
	}
	
	public void setChkcharge(int chkcharge) {
		this.chkcharge = chkcharge;
	}
	
	public int getChkdischarge() {
		return chkdischarge;
	}
	
	public void setChkdischarge(int chkdischarge) {
		this.chkdischarge = chkdischarge;
	}
	
	public int getChkuser() {
		return chkuser;
	}
	
	public void setChkuser(int chkuser) {
		this.chkuser = chkuser;
	}
	
	public int getChkunit() {
		return chkunit;
	}
	
	public void setChkunit(int chkunit) {
		this.chkunit = chkunit;
	}
	
	public int getChkgroup() {
		return chkgroup;
	}
	
	public void setChkgroup(int chkgroup) {
		this.chkgroup = chkgroup;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	
	public String getStart_time() {
		return this.start_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
	public String getEnd_time() {
		return this.end_time;
	}

}
