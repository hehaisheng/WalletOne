package com.zhiyu.wallet.bean;


import com.zhiyu.wallet.util.Println;

/**
 * 2018/7/12
 *
 * @author zhiyu
 */

public class User {
	private String id;
	private String phone;// 手机号
	private String idcar;// 身份证
	private String username;// 姓名
	private String imageurl;// 头像地址
	private boolean iscredit;// 是否公安部认证
	private String session;
	private boolean isLogin =false;
	private boolean isRelainfo; //亲友信息
	private String fristcontact ;
	private String firstcontactphone ;
	private String fristcontactrelation ;
	private String emergencycontact ;
	private String emergencycontactphone ;
	private String emergencycontactrelation ;
	private String copy1 ;
	private String copy2 ;

	private String contacts;
	private String idcarbackup;// 身份证备份

	private String wechatid ;
	private String kith;
	public static User user;

	public static User getInstance(){

		if(user==null){
			synchronized(User.class){
				if (user==null){
					user=new User();
				}
			}
		}

		return user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User user1) {
		user = user1;
	}

	public String getWechatid() {
		return wechatid;
	}

	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}

	public boolean isIscredit() {
		return iscredit;
	}

	public void setIscredit(boolean iscredit) {
		this.iscredit = iscredit;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean login) {
		isLogin = login;
	}

	public boolean isRelainfo() {
		return isRelainfo;
	}

	public void setRelainfo(boolean relainfo) {
		isRelainfo = relainfo;
	}

	public String getFirstcontact() {
		return fristcontact;
	}

	public void setFirstcontact(String firstcontact) {
		this.fristcontact = firstcontact;
	}

	public String getFirstcontactphone() {
		return firstcontactphone;
	}

	public void setFirstcontactphone(String firstcontactphone) {
		this.firstcontactphone = firstcontactphone;
	}

	public String getFirstcontactrelation() {
		return fristcontactrelation;
	}

	public void setFirstcontactrelation(String firstcontactrelation) {
		this.fristcontactrelation = firstcontactrelation;
	}

	public String getEmergencycontact() {
		return emergencycontact;
	}

	public void setEmergencycontact(String emergencycontact) {
		this.emergencycontact = emergencycontact;
	}

	public String getEmergencycontactphone() {
		return emergencycontactphone;
	}

	public void setEmergencycontactphone(String emergencycontactphone) {
		this.emergencycontactphone = emergencycontactphone;
	}

	public String getEmergencycontactrelation() {
		return emergencycontactrelation;
	}

	public void setEmergencycontactrelation(String emergencycontactrelation) {
		this.emergencycontactrelation = emergencycontactrelation;
	}

	public String getKith() {
		return kith;
	}

	public void setKith(String kith) {
		this.kith = kith;
	}

	public String getIdcar() {
		return idcar;
	}

	public void setIdcar(String idcar) {
		this.idcar = idcar;
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

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public boolean isCredit() {
		return iscredit;
	}

	public void setCredit(boolean iscredit) {
		this.iscredit = iscredit;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getIdcarbackup() {
		return idcarbackup;
	}

	public void setIdcarbackup(String idcarbackup) {
		this.idcarbackup = idcarbackup;
	}

	public String getCopy1() {
		return copy1;
	}

	public void setCopy1(String copy1) {
		this.copy1 = copy1;
	}

	public String getCopy2() {
		return copy2;
	}

	public void setCopy2(String copy2) {
		this.copy2 = copy2;
	}
}
