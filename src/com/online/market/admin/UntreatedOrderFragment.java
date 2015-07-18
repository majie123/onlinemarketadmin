package com.online.market.admin;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.online.market.admin.adapter.MyOrderAdapter;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.util.ProgressUtil;
import com.online.market.admin.view.xlist.XListView;
import com.online.market.admin.view.xlist.XListView.IXListViewListener;
/***
 * 未处理订单
 * @author majie
 *
 */
public class UntreatedOrderFragment extends BaseFragment {
	private View view;
	private XListView xlv;
	private List<OrderBean> orders=new ArrayList<OrderBean>();
	private TextView tvNoOrder;
	private MyOrderAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.frag_untreatedorder, null);
		initViews();
		setListeners();
		initData();
		return view;
	}


	public void initData() {

		queryOrders();
		
	}
	
	private void registerReceiver(){
		IntentFilter filter=new IntentFilter("intent_count");
		getActivity().registerReceiver(receiver, filter);
	}

	
	private void queryOrders(){
		ProgressUtil.showProgress(getActivity(), "");
		BmobQuery<OrderBean> query	 = new BmobQuery<OrderBean>();
		query.addWhereNotEqualTo("status", OrderBean.STATUS_DELIVED);
//		query.setLimit(10);
		query.findObjects(getActivity(), new FindListener<OrderBean>() {

			@Override
			public void onSuccess(List<OrderBean> object) {
				ProgressUtil.closeProgress();
				orders.addAll(object);
				xlv.stopRefresh();
				setData();
				
			}

			@Override
			public void onError(int code, String msg) {
				ProgressUtil.closeProgress();
				toastMsg("net fail");
				xlv.stopRefresh();
			}
		});	
		
	}
	
	private void setData(){
		if(orders.size()==0){
			tvNoOrder.setVisibility(View.VISIBLE);
		}
		adapter=new MyOrderAdapter(getActivity(), orders,MyOrderAdapter.ORDER_UNTREATED);
		xlv.setAdapter(adapter);
	}

	@Override
	public void initViews() {
		xlv=(XListView) view.findViewById(R.id.xlv);
		tvNoOrder=(TextView) view.findViewById(R.id.no_order);
	}


	@Override
	public void setListeners() {
		xlv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				orders.clear();
				queryOrders();
			}
			
			@Override
			public void onLoadMore() {
				
			}
		});
	}
	
	private BroadcastReceiver receiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(intent.getAction().equals("intent_count")){
				int count=intent.getIntExtra("count", -1);
				//每30秒刷新一次
				if(count%120==0){
					orders.clear();
					queryOrders();
				}
				if(count%10==0){
					adapter.notifyDataSetChanged();
				}
			}
		}
	};
	
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	};

}
