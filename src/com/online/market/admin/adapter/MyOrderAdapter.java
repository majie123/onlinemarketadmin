package com.online.market.admin.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.bmob.v3.listener.UpdateListener;

import com.online.market.admin.R;
import com.online.market.admin.adapter.base.MyBaseAdapter;
import com.online.market.admin.adapter.base.ViewHolder;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.bean.ShopCartaBean;
import com.online.market.admin.util.DialogUtil;
import com.online.market.admin.util.ProgressUtil;

public class MyOrderAdapter extends MyBaseAdapter {
	private List<OrderBean > orderBeans;

	public MyOrderAdapter(Context context,List<OrderBean > orderBeans) {
		super(context);
		this.orderBeans=orderBeans;
	}

	@Override
	public int getCount() {
		return orderBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.myorder_item, null);
		}
		TextView tvOrderDetail=ViewHolder.get(convertView, R.id.orderdetail);
		TextView tvOrderName=ViewHolder.get(convertView, R.id.ordername);
		TextView tvOrderAddress=ViewHolder.get(convertView, R.id.orderaddress);
		TextView tvOrderPhonenum=ViewHolder.get(convertView, R.id.orderphonenum);
		TextView tvOrderTime=ViewHolder.get(convertView, R.id.ordertime);
		Button btDelive=ViewHolder.get(convertView, R.id.delive);
		
		final OrderBean bean=orderBeans.get(arg0);
        tvOrderName.setText("收货人： "+bean.getReceiver());
        tvOrderAddress.setText("收货地址： "+bean.getAddress());
        tvOrderPhonenum.setText(bean.getPhonenum());
        tvOrderTime.setText("下单时间：  "+bean.getCreatedAt());
        String detail="";
        for(ShopCartaBean p:bean.getShopcarts()){
        	detail+=p.getName()+" X "+p.getNumber()+"\n";
        }
    	tvOrderDetail.setText("商品： "+detail);
    	
    	btDelive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogUtil.dialog(mContext, "你确认已经送达了吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						update(bean);
						dialog.dismiss();
					}
				}, "取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();

					}
				});
				
			}
		});

		return convertView;
	}
	
	private void update(final OrderBean bean){
		ProgressUtil.showProgress(mContext, "");
		OrderBean p=new OrderBean();
		p.setStatus(OrderBean.STAUTS_DELIVED);
		p.setObjectId(bean.getObjectId());
		p.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				orderBeans.remove(bean);
				notifyDataSetChanged();
				ShowToast("success");
				ProgressUtil.closeProgress();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast("fail "+arg1);
				ProgressUtil.closeProgress();

			}
		});
		
	}

}
