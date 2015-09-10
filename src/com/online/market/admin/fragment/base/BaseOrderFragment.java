package com.online.market.admin.fragment.base;

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

import com.online.market.admin.BaseFragment;
import com.online.market.admin.R;
import com.online.market.admin.adapter.BaseOrderAdapter;
import com.online.market.admin.bean.MyUser;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.fragment.CompletedOrderFragment;
import com.online.market.admin.util.ProgressUtil;
import com.online.market.admin.util.Speecher;
import com.online.market.admin.view.xlist.XListView;
import com.online.market.admin.view.xlist.XListView.IXListViewListener;

public abstract class BaseOrderFragment extends BaseFragment {

	private View view;
	private XListView xlv;
	protected List<OrderBean> orders=new ArrayList<OrderBean>();
	private TextView tvNoOrder;
	protected BaseOrderAdapter adapter;
	private Speecher speecher;
	private int skip=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		speecher=Speecher.getSpeecher(getActivity());
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

	
	private void queryOrders(){
		ProgressUtil.showProgress(getActivity(), "");
		BmobQuery<OrderBean> query	= new BmobQuery<OrderBean>();
		if(user.getGroup()==MyUser.GROUP_PACKER){
			query.addWhereEqualTo("packer", user.getUsername());
		}else if(user.getGroup()==MyUser.GROUP_DISPATCHER){
			query.addWhereEqualTo("dispatcher", user.getUsername());
		}
		setBmobQueryCondition(query);
		query.setLimit(10);
		query.setSkip(skip);
		query.findObjects(getActivity(), new FindListener<OrderBean>() {

			@Override
			public void onSuccess(List<OrderBean> object) {
				ProgressUtil.closeProgress();
				xlv.stopRefresh();
				xlv.stopLoadMore();
				
				orders.addAll(object);
				 if(skip==0){
					 setData();
				 }else{
					 adapter.notifyDataSetChanged();
				 }
				 
				 xlv.setSelection(skip);
				if(object.size()>=10){
					xlv.setPullLoadEnable(true);
				}
//				else{
//					xlv.setPullLoadEnable(false);
//				}
				skip+=object.size();
			}

			@Override
			public void onError(int code, String msg) {
				ProgressUtil.closeProgress();
				toastMsg(msg);
				xlv.stopRefresh();
				xlv.stopLoadMore();
			}
		});	
	}
	
	protected abstract void setBmobQueryCondition(BmobQuery<OrderBean> query);
	
	private void setData(){
		if(orders.size()==0){
			tvNoOrder.setVisibility(View.VISIBLE);
		}
		initAdapter();
		xlv.setAdapter(adapter);
	}
	
	/***
	 * 初始化adapter,抽象方法
	 */
	protected abstract void initAdapter();

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
				skip=0;
				queryOrders();
			}
			
			@Override
			public void onLoadMore() {
				queryOrders();
			}
		});
	}
    
	private BroadcastReceiver receiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(intent.getAction().equals("intent_count")){
				int count=intent.getIntExtra("count", -1);
				//每30秒刷新一次
				if(count%10==0&&adapter!=null){
					adapter.notifyDataSetChanged();
				}
				if(count%50==0){
					for(OrderBean order:orders){
						if(!(BaseOrderFragment.this instanceof CompletedOrderFragment )){
							if(order.isOutOfTime()){
								speecher.speech("有订单已经超时，请尽快处理");
								break;
							}else if(order.isHurryUp()){
								speecher.speech("有订单即将超时，请尽快处理");
								break;
							}
						}
					}
				}
			}
		}
	};
	
	private void registerReceiver(){
		IntentFilter filter=new IntentFilter("intent_count");
		getActivity().registerReceiver(receiver, filter);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(receiver!=null){
			getActivity().unregisterReceiver(receiver);
		}
	}

}
