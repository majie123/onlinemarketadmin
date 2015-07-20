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

import com.online.market.admin.bean.MyUser;
import com.online.market.admin.fragment.CompletedOrderFragment;
import com.online.market.admin.fragment.DepartedOrderFragment;
import com.online.market.admin.fragment.PackedOrderFragment;
import com.online.market.admin.fragment.UnpackedOrderFragment;
import com.online.market.admin.fragment.base.BaseOrderFragment;
import com.online.market.admin.servie.HeartService;
import com.testin.agent.TestinAgent;

public class MainActivity extends BaseActivity {
	public static String APPID = "bb9c8700c4d1821c09bfebaf1ba006b1";

	private BaseOrderFragment utFragment;
	private BaseOrderFragment cmFragment;
	private BaseOrderFragment paFragment;
	private BaseOrderFragment dpFragment;

	private Button btUnPacked,btPacked,btCompleted,btDeparted;
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
		btUnPacked=(Button) findViewById(R.id.bt_unpackedorder);
		btCompleted=(Button) findViewById(R.id.bt_completedorder);
		btDeparted=(Button) findViewById(R.id.bt_departedorder);
		
		btSet=(Button) findViewById(R.id.bt_set);

	}

	@Override
	public void initData() {
		Bmob.initialize(getApplicationContext(),APPID);
		TestinAgent.init(this, "215008c07a999b080bfa94bc57607040", "android");

		if(user==null){
			startActivity(LoginActivity.class);
			finish();
			return;
		}

		if(user.getGroup()==MyUser.GROUP_PACKER){
			btPacked.setVisibility(View.GONE);
			btDeparted.setVisibility(View.GONE);
			
			initLastBt(btUnPacked);
			utFragment=new UnpackedOrderFragment();
			replaceFragment(utFragment);
			
		}else if(user.getGroup()==MyUser.GROUP_DISPATCHER){
			btUnPacked.setVisibility(View.GONE);
			
			initLastBt(btPacked);
			paFragment=new PackedOrderFragment();
			replaceFragment(paFragment);
			
		}
		
		if(user.getGroup()!=MyUser.GROUP_ROOT){
			startService(new Intent(this, HeartService.class));
		}
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
		
        btUnPacked.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(utFragment==null){
					utFragment=new UnpackedOrderFragment();
//				}
				initLastBt(btUnPacked);
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
