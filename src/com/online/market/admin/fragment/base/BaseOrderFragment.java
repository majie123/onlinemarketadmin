package com.online.market.admin.fragment.base;

import java.util.ArrayList;
import java.util.List;

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
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.util.ProgressUtil;
import com.online.market.admin.view.xlist.XListView;
import com.online.market.admin.view.xlist.XListView.IXListViewListener;

public abstract class BaseOrderFragment extends BaseFragment {

	private View view;
	private XListView xlv;
	protected List<OrderBean> orders=new ArrayList<OrderBean>();
	private TextView tvNoOrder;
	protected BaseOrderAdapter adapter;
	
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
		BmobQuery<OrderBean> query	 = new BmobQuery<OrderBean>();
//		query.addWhereEqualTo("state", OrderBean.STATE_DELIVED);
		setBmobQueryCondition(query);
		query.setLimit(10);
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
				toastMsg(msg);
				xlv.stopRefresh();
			}
		});	
	}
	
	protected abstract void setBmobQueryCondition(BmobQuery<OrderBean> query);
	
	private void setData(){
		if(orders.size()==0){
			tvNoOrder.setVisibility(View.VISIBLE);
		}
//		adapter=new CompletedOrderAdapter(getActivity(), orders);
		initAdapter();
		xlv.setAdapter(adapter);
	}
	
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
				queryOrders();
			}
			
			@Override
			public void onLoadMore() {
				
			}
		});
	}


}
