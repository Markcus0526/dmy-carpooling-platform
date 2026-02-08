package com.pinche.authority.menu.dao;

public class Menu {

	private int id;
	private String name = "";
	private String url = "";
	private int menucode = 0;
	private int submenu = 0;
	private int parent = 0;

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
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getMenucode() {
		return menucode;
	}
	
	public void setMenucode(int menucode) {
		this.menucode = menucode;
	}
	
	public int getSubmenu() {
		return submenu;
	}
	
	public void setSubmenu(int submenu) {
		this.submenu = submenu;
	}
	
	public int getParent() {
		return parent;
	}
	
	public void setParent(int parent) {
		this.parent = parent;
	}

}
