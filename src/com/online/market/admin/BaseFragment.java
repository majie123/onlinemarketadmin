package com.online.market.admin;

import cn.bmob.v3.BmobUser;

import com.online.market.admin.bean.MyUser;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {
	protected MyUser user;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user=BmobUser.getCurrentUser(getActivity(), MyUser.class);
	}
	
	public abstract void initViews();
	public abstract void initData();
	public abstract void setListeners();
	
	protected void toastMsg(int msgId) {
		toastMsg(getString(msgId));
	}
	
	/** toast **/
	public void toastMsg(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}

}
