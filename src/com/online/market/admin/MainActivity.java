package com.online.market.admin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.bmob.v3.Bmob;

public class MainActivity extends BaseActivity {
	public static String APPID = "bb9c8700c4d1821c09bfebaf1ba006b1";

	private UntreatedOrderFragment utFragment;
	private CompletedFragment cmFragment;
	private Button btNewOrder,btUntreated,btCompleted;
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

		btNewOrder=(Button) findViewById(R.id.bt_neworder);
		btUntreated=(Button) findViewById(R.id.bt_untreatedorder);
		btCompleted=(Button) findViewById(R.id.bt_completedorder);
		
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
	}

	@Override
	public void setListeners() {

		btCompleted.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(cmFragment==null){
					cmFragment=new CompletedFragment();
//				}
				initLastBt(btCompleted);
				replaceFragment(cmFragment);
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
