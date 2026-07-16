package util.excel.pojo;
public class ExcelUser {
	private String 	   id          ;//numeric(8,0) comment '主键',
	private String 	   usercode  ;// text comment '用户名',
	private String     username  ;// text comment '昵称',
	private String     group_name  ;// text comment '姓名',
	private String     phone     ;// text comment '电话，忘记密码时用',
	private String invitecode_self;
	private String invitecode_regist;
	
	//----------身份证相关信息-----------
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getInvitecode_self() {
		return invitecode_self;
	}
	public void setInvitecode_self(String invitecode_self) {
		this.invitecode_self = invitecode_self;
	}
	public String getInvitecode_regist() {
		return invitecode_regist;
	}
	public void setInvitecode_regist(String invitecode_regist) {
		this.invitecode_regist = invitecode_regist;
	}
}
