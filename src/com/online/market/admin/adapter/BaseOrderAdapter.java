package com.online.market.admin.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.bmob.v3.listener.UpdateListener;

import com.online.market.admin.R;
import com.online.market.admin.adapter.base.MyBaseAdapter;
import com.online.market.admin.adapter.base.ViewHolder;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.bean.ShopCartaBean;
import com.online.market.admin.util.DateUtil;
import com.online.market.admin.util.ProgressUtil;

public abstract class BaseOrderAdapter extends MyBaseAdapter {

	private List<OrderBean > orderBeans;
	protected TextView tvOrderTime;
	protected Button btDelive;

	public BaseOrderAdapter(Context context,List<OrderBean > orderBeans) {
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
		tvOrderTime=ViewHolder.get(convertView, R.id.ordertime);
		btDelive=ViewHolder.get(convertView, R.id.delive);
		
//		if(orderType==ORDER_COMPLETED){
//			btDelive.setVisibility(View.GONE);
//			tvOrderTime.setVisibility(View.GONE);
//		}
		
		final OrderBean bean=orderBeans.get(arg0);
        tvOrderName.setText("收货人： "+bean.getReceiver());
        tvOrderAddress.setText("收货地址： "+bean.getAddress());
        tvOrderPhonenum.setText(bean.getPhonenum());
        String time=DateUtil.getDate(bean.getCreatedAt());
//        if(time!=null){
        	tvOrderTime.setText(time);
//        }else{
//        	tvOrderTime.setText("订单已超时");
//        }
        String detail="";
        for(ShopCartaBean p:bean.getShopcarts()){
        	detail+=p.getName()+" X "+p.getNumber()+"\n";
        }
    	tvOrderDetail.setText("商品： "+detail);
    	
    	action(bean);
    	
		return convertView;
	}
	
	/**getview中的抽象方法*/
	protected abstract void action(final OrderBean bean);
	
	/**更新order的状态*/
	protected void update(final OrderBean bean,int state){
		ProgressUtil.showProgress(mContext, "");
		OrderBean p=new OrderBean();
		p.setState(state);
		p.setObjectId(bean.getObjectId());
		p.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				orderBeans.remove(bean);
				notifyDataSetChanged();
				ShowToast("成功");
				ProgressUtil.closeProgress();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast("失败 "+arg1);
				ProgressUtil.closeProgress();

			}
		});
		
	}

}
