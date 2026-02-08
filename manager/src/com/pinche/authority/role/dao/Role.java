package com.pinche.authority.role.dao;

public class Role {

	private Integer id = null;
	private String name = "";
	private String remark = "";
	private int type;
	private int deleted;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public int getDeleted() {
		return this.deleted;
	}
	
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
}
