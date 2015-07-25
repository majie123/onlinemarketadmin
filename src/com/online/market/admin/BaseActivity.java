package com.online.market.admin;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.online.market.admin.bean.MyUser;
import com.online.market.admin.util.ActivityControl;
import com.online.market.admin.util.BitmapHelp;
import com.online.market.admin.util.FileUtils;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends Activity {
	
	protected String dir;
	protected MyUser user;
	protected BitmapUtils bitmapUtils;
	protected BitmapDisplayConfig config;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityControl.getInstance().addActivity(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		user=BmobUser.getCurrentUser(this, MyUser.class);
		bitmapUtils=BitmapHelp.getBitmapUtils(this);
		config=BitmapHelp.getDisplayConfig(this, 50, 50);

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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityControl.getInstance().removeActivity(this);
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}	
}
