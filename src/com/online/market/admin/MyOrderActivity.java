package com.online.market.admin;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.online.market.admin.adapter.MyOrderAdapter;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.util.ProgressUtil;
import com.online.market.admin.view.xlist.XListView;
import com.online.market.admin.view.xlist.XListView.IXListViewListener;

public class MyOrderActivity extends BaseActiviity {
	
	private XListView xlv;
	private List<OrderBean> orders=new ArrayList<OrderBean>();
	private TextView tvNoOrder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderlist);
		
		initViews();
		setListeners();
		initData();
	}


	public void initData() {

		queryOrders();
	}

	
	private void queryOrders(){
		ProgressUtil.showProgress(this, "");
		BmobQuery<OrderBean> query	 = new BmobQuery<OrderBean>();
		query.addWhereNotEqualTo("status", OrderBean.STATUS_DELIVED);
//		query.setLimit(10);
		query.findObjects(this, new FindListener<OrderBean>() {

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
		MyOrderAdapter adapter=new MyOrderAdapter(this, orders);
		xlv.setAdapter(adapter);
	}

	@Override
	public void initViews() {
		xlv=(XListView) findViewById(R.id.xlv);
//		xlv.setPullRefreshEnable(false);
		tvNoOrder=(TextView) findViewById(R.id.no_order);
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

}
