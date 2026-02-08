package com.pinche.statistics.passenger.dao;

public class Passenger {
	private int id;
	private String date;
	private int cnt_app;
	private int cnt_register;
	private int cnt_invitecode;
	private int cnt_person;
	private int cnt_bankcard;
	private int cnt_driver;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCnt_app() {
		return cnt_app;
	}

	public void setCnt_app(int cnt_app) {
		this.cnt_app = cnt_app;
	}

	public int getCnt_register() {
		return cnt_register;
	}
	
	public void setCnt_register(int cnt_register) {
		this.cnt_register = cnt_register;
	}
	
	public int getCnt_invitecode() {
		return cnt_invitecode;
	}
	
	public void setCnt_invitecode(int cnt_invitecode) {
		this.cnt_invitecode = cnt_invitecode;
	}
	
	public int getCnt_person() {
		return cnt_person;
	}
	
	public void getCnt_person(int cnt_person) {
		this.cnt_person = cnt_person;
	}
	
	public int getCnt_bankcard() {
		return cnt_bankcard;
	}
	
	public void setCnt_bankcard(int cnt_bankcard) {
		this.cnt_bankcard = cnt_bankcard;
	}
	
	public int getCnt_driver() {
		return cnt_driver;
	}
	
	public void setCnt_driver(int cnt_driver) {
		this.cnt_driver = cnt_driver;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
