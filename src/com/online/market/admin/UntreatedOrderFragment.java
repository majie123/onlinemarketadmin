package com.online.market.admin;

import java.util.ArrayList;
import java.util.List;

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
		MyOrderAdapter adapter=new MyOrderAdapter(getActivity(), orders,MyOrderAdapter.ORDER_UNTREATED);
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

}
