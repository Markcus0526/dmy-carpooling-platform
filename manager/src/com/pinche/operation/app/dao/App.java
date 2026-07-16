package com.pinche.operation.app.dao;

public class App {
    //table app and table app_version
	private String app_code;
	private String app_name;
	private String version;
	private String size;
	private  String qrcode_path;
	private String pack_name;
	private String url;
	private short deleted;
	private Long id;
	private String bundle_id;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getApp_code() {
		return app_code;
	}
	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getPack_name() {
		return pack_name;
	}
	public void setPack_name(String pack_name) {
		this.pack_name = pack_name;
	}
	public short getDeleted() {
		return deleted;
	}
	public void setDeleted(short deleted) {
		this.deleted = deleted;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getQrcode_path() {
		return qrcode_path;
	}
	public void setQrcode_path(String qrcode_path) {
		this.qrcode_path = qrcode_path;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBundle_id() {
		return bundle_id;
	}
	public void setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
	}

}
