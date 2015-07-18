package com.online.market.admin.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MyUser extends BmobUser {
	/**超级用户*/
	public static int GROUP_ROOT=2;
	/**管理用户*/
	public static int GROUP_ADMIN=1;
	/**普通用户*/
	public static int GROUP_USER=0;
	
	private static final long serialVersionUID = 1L;
	private String nickname;
	private Boolean gender;
	private BmobFile avatar;
	private int age;
	private int group;
	
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}
	public Boolean getGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String realname) {
		this.nickname = realname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	
}
