package com.online.market.admin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.bmob.v3.Bmob;

import com.online.market.admin.fragment.CompletedOrderFragment;
import com.online.market.admin.fragment.DepartedOrderFragment;
import com.online.market.admin.fragment.PackedOrderFragment;
import com.online.market.admin.fragment.UntreatedOrderFragment;
import com.online.market.admin.fragment.base.BaseOrderFragment;
import com.online.market.admin.servie.CountService;

public class MainActivity extends BaseActivity {
	public static String APPID = "bb9c8700c4d1821c09bfebaf1ba006b1";

	private BaseOrderFragment utFragment;
	private BaseOrderFragment cmFragment;
	private BaseOrderFragment paFragment;
	private BaseOrderFragment dpFragment;

	private Button btPacked,btUntreated,btCompleted,btDeparted;
	private Button btSet;
	private Button lastBt;
	
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

		btPacked=(Button) findViewById(R.id.bt_packedorder);
		btUntreated=(Button) findViewById(R.id.bt_untreatedorder);
		btCompleted=(Button) findViewById(R.id.bt_completedorder);
		btDeparted=(Button) findViewById(R.id.bt_departedorder);

		btSet=(Button) findViewById(R.id.bt_set);

	}

	@Override
	public void initData() {
		Bmob.initialize(getApplicationContext(),APPID);
		if(user==null){
			startActivity(LoginActivity.class);
			finish();
			return;
		}

		initLastBt(btUntreated);
		utFragment=new UntreatedOrderFragment();
		replaceFragment(utFragment);
		
		startService(new Intent(this, CountService.class));
	}

	@Override
	public void setListeners() {

		btCompleted.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(cmFragment==null){
					cmFragment=new CompletedOrderFragment();
//				}
				initLastBt(btCompleted);
				replaceFragment(cmFragment);
			}
		});
		
        btPacked.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(cmFragment==null){
					paFragment=new PackedOrderFragment();
//				}
				initLastBt(btPacked);
				replaceFragment(paFragment);
			}
		});
		
        btUntreated.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(utFragment==null){
					utFragment=new UntreatedOrderFragment();
//				}
				initLastBt(btUntreated);
				replaceFragment(utFragment);
			}
		});
        
        btDeparted.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(utFragment==null){
					dpFragment=new DepartedOrderFragment();
//				}
				initLastBt(btDeparted);
				replaceFragment(dpFragment);
			}
		});
        
        btSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(SettingActivity.class);
			}
		});
	}
	
	private void initLastBt(Button bt){
		if(lastBt!=null){
			lastBt.setTextColor(0xffffffff);
		}
		bt.setTextColor(0xffFF6100);
		lastBt=bt;
	}
	
	private void replaceFragment(Fragment fragment)  
    {  
        FragmentManager fm = getFragmentManager();  
        FragmentTransaction transaction = fm.beginTransaction();  
        transaction.replace(R.id.id_content, fragment);
        transaction.commit();  
    } 
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if(user==null){
			startActivity(LoginActivity.class);
			finish();
		}
	}


}
