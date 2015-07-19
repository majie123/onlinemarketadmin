package com.online.market.admin.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import cn.bmob.v3.BmobQuery;

import com.online.market.admin.adapter.UntreatedOrderAdapter;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.fragment.base.BaseOrderFragment;
/***
 * 未处理订单
 * @author majie
 *
 */
public class UntreatedOrderFragment extends BaseOrderFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver();
	}
	
	
	private void registerReceiver(){
		IntentFilter filter=new IntentFilter("intent_count");
		getActivity().registerReceiver(receiver, filter);
	}

	private BroadcastReceiver receiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(intent.getAction().equals("intent_count")){
				int count=intent.getIntExtra("count", -1);
				//每30秒刷新一次
				if(count%10==0){
					adapter.notifyDataSetChanged();
				}
			}
		}
	};
	
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}


	@Override
	protected void setBmobQueryCondition(BmobQuery<OrderBean> query) {
		query.addWhereEqualTo("state", OrderBean.STATE_UNTREATED);
	}


	@Override
	protected void initAdapter() {
		adapter=new UntreatedOrderAdapter(getActivity(), orders);
	};

}
