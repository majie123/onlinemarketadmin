package com.online.market.admin;

import java.io.File;

import com.online.market.admin.util.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public abstract class BaseActiviity extends Activity {
	
	protected String dir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

}
