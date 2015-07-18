package com.online.market.admin;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.bmob.v3.Bmob;

public class MainActivity extends BaseActiviity {
	public static String APPID = "bb9c8700c4d1821c09bfebaf1ba006b1";

	private Button btPublish,btOrder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initViews();
		setListeners();
		initData();
	}

	@Override
	public void initViews() {

		btPublish=(Button) findViewById(R.id.publish_commodity);
		btOrder=(Button) findViewById(R.id.order);
	}

	@Override
	public void initData() {
		Bmob.initialize(getApplicationContext(),APPID);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

//		startService(new Intent(this, NewOrderService.class));
        
	}

	@Override
	public void setListeners() {

		btPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, PublishCommodityActivity.class));
			}
		});
		
		btOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, OrderActivity.class));
			}
		});
	}


}
