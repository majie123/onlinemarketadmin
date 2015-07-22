package com.online.market.admin;

import java.util.List;

import com.online.market.admin.bean.MyUser;
import com.online.market.admin.servie.HeartService;
import com.online.market.admin.util.ActivityControl;
import com.online.market.admin.util.DialogUtil;
import com.online.market.admin.util.SharedPrefUtil;

import cn.bmob.v3.BmobUser;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends BaseActivity {
	public static final String STATE="state";
	public static final String STATE_ONLINE="online";
	public static final String STATE_OFFLINE="offline";

	private Button btPublish,btLogout,btSetOffline;
	private SharedPrefUtil su;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		initViews();
		setListeners();
		initData();
	}

	@Override
	public void initViews() {

		btPublish=(Button) findViewById(R.id.bt_publish_commodity);
		btLogout=(Button) findViewById(R.id.bt_logout);
		btSetOffline=(Button) findViewById(R.id.bt_setoffline);

	}

	@Override
	public void initData() {
		su=new SharedPrefUtil(this, "tiantianadmin");
		setOnlineState();

		if(user.getGroup()!=MyUser.GROUP_ROOT){
			btPublish.setVisibility(View.GONE);
		}else{
			btSetOffline.setVisibility(View.GONE);
		}
	}
	
	private void setOnlineState(){
		if(su.getValueByKey(STATE,STATE_ONLINE).equals(STATE_ONLINE)){
			btSetOffline.setText("设置为离线");
		}else{
			btSetOffline.setText("设置为在线");
		}
	}

	@Override
	public void setListeners() {
		
		btSetOffline.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(SettingActivity.this, HeartService.class);
				if(su.getValueByKey(STATE,STATE_ONLINE).equals(STATE_ONLINE)){
					su.putValueByKey(STATE, STATE_OFFLINE);
					stopService(intent);
				}else{
					su.putValueByKey(STATE, STATE_ONLINE);
					startService(intent);
				}
				setOnlineState();
			}
		});

		btPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(SettingActivity.this, PublishCommodityActivity.class));
			}
		});
		
		btLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogUtil.dialog(SettingActivity.this, "您确认退出吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						BmobUser.logOut(SettingActivity.this);
						//终止掉轮询进程
						stopService(new Intent(SettingActivity.this, HeartService.class));
						arg0.dismiss();
						List<Activity> activities=ActivityControl.getInstance().getListActivitys();
						for(Activity activity:activities){
							activity.finish();
						}
					}

				}, "取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});
				
			}
		});
	}
	
}
