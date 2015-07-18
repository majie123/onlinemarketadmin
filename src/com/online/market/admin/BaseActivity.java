package com.online.market.admin;

import java.io.File;

import cn.bmob.v3.BmobUser;

import com.online.market.admin.bean.MyUser;
import com.online.market.admin.util.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	
	protected String dir;
	protected MyUser user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		user=BmobUser.getCurrentUser(this, MyUser.class);

		dir=FileUtils._PATH;
		File dirFile=new File(dir);
		
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
	}
	
	public abstract void initViews();
	public abstract void initData();
	public abstract void setListeners();
	
	protected void toastMsg(int msgId) {
		toastMsg(getString(msgId));
	}
	
	/** toast **/
	public void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 跳转
	 * @param c
	 */
	protected void startActivity(Class c) {
		startActivity(c, null);
	}
	
	/**
	 * 跳转
	 * @param c
	 * @param extras
	 */
	protected void startActivity(Class c, Bundle extras) {
		Intent intent = new Intent();
		intent.setClass(this, c);
		if (null != extras) {
			intent.putExtras(extras);
		}
		startActivity(intent);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		user=BmobUser.getCurrentUser(this, MyUser.class);
	}

}
