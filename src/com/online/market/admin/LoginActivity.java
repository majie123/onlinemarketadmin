package com.online.market.admin;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.online.market.admin.bean.MyUser;
import com.online.market.admin.util.ProgressUtil;

public class LoginActivity extends BaseActivity {
	private EditText username, userpsw;
	private Button signin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initViews();
		setListeners();
		
		initData();
	}

    public void setListeners(){
    	signin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = username.getText().toString();
				String psw = userpsw.getText().toString();
				
				login(name, psw);
			}
		});
    }

	/**
	 * 登陆用户
	 */
	private void login(String name,String psw) {
		ProgressUtil.showProgress(this, "");
		final MyUser bu = new MyUser();
		bu.setUsername(name);
		bu.setPassword(psw);
		bu.login(this, new SaveListener() {

			@Override
			public void onSuccess() {
				ProgressUtil.closeProgress();
				MyUser user=BmobUser.getCurrentUser(getApplicationContext(), MyUser.class);
				if(user.getGroup()==MyUser.GROUP_USER){
					toastMsg("普通用户不能登录系统");
					BmobUser.logOut(getApplicationContext());
				}else{
					toastMsg(bu.getUsername() + "登陆成功");
					startActivity(MainActivity.class);
					finish();
				}
			}

			@Override
			public void onFailure(int code, String msg) {
				ProgressUtil.closeProgress();
				toastMsg("登陆失败:" + msg);
				
			}
		});
	}

	@Override
	public void initViews() {
		signin = (Button) findViewById(R.id.signin);
		username = (EditText) findViewById(R.id.username);
		userpsw = (EditText) findViewById(R.id.userpsw);
	}

	@Override
	public void initData() {

	}
	
}
