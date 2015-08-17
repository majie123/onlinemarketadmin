package com.online.market.admin.bean;

import cn.bmob.v3.BmobObject;

public class CouponBean extends BmobObject{
	/**通用券*/
	public static  int COUPON_TYPE_COMMON=0;
	/**折扣券*/
	public static int COUPON_TYPE_ONSALE=1;
	/**不适用优惠券*/
	public static int COUPON_TYPE_DONTUSE=2;
	
	/**未消费*/
	public static  int COUPON_STATUS_UNCONSUMED=0;
	/**已消费*/
	public static int COUPON_STATUS_CONSUMED=1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private float amount;
	private String username;
	private float limit; 
	private int type;
	private int status;
	
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public float getLimit() {
		return limit;
	}
	public void setLimit(float limit) {
		this.limit = limit;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
