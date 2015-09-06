package com.online.market.admin.adapter;

import java.text.DecimalFormat;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.UpdateListener;

import com.online.market.admin.R;
import com.online.market.admin.adapter.base.MyBaseAdapter;
import com.online.market.admin.adapter.base.ViewHolder;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.bean.ShopCartaBean;
import com.online.market.admin.util.DateUtil;
import com.online.market.admin.util.ProgressUtil;

public abstract class BaseOrderAdapter extends MyBaseAdapter {
//	public static final int FIELD_STATE=0;
	public static int FIELD_PACKER=1;
	public static int FIELD_DISPATCHER=2;

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
		TextView tvOrderPaymethod=ViewHolder.get(convertView, R.id.orderpaymethod);
		tvOrderTime=ViewHolder.get(convertView, R.id.ordertime);
		TextView tvResponer=ViewHolder.get(convertView, R.id.order_responer);
		btDelive=ViewHolder.get(convertView, R.id.delive);
		ImageView ivCall=ViewHolder.get(convertView, R.id.iv_call);
		
		final OrderBean bean=orderBeans.get(arg0);
        tvOrderName.setText("收货人： "+bean.getReceiver());
        tvOrderAddress.setText("收货地址： "+bean.getAddress());
        tvOrderPhonenum.setText(bean.getPhonenum());
        String packer=bean.getPacker();
        String dispatcher=bean.getDispatcher();
        if(packer.equals("untreater")){
        	packer="暂无";
        }
        if(dispatcher.equals("untreated")){
        	dispatcher="暂无";
        }
        tvResponer.setText(String.format(tvResponer.getText().toString(), packer,dispatcher));
        if(bean.getPayMethod()==OrderBean.PAYMETHOD_PAYFAILED){
        	tvOrderPaymethod.setText("付款失败");
        }else if(bean.getPayMethod()==OrderBean.PAYMETHOD_CASHONDELIVEY){
        	DecimalFormat   df=new DecimalFormat("#.##");   
    		String totalPriceStr=df.format(bean.getPrice());
        	tvOrderPaymethod.setText("货到付款：需支付 "+totalPriceStr+" 元");
        }else {
        	tvOrderPaymethod.setText("在线已支付");
        }        
        String time=DateUtil.getDate(bean.getCreatedAt());
        tvOrderTime.setText(time);
        String detail="";
        for(ShopCartaBean p:bean.getShopcarts()){
        	if(p.getNumber()>0){
        		detail+=p.getName()+" X "+p.getNumber()+"\n";
        	}
        }
    	tvOrderDetail.setText("商品： "+detail);
    	ivCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+bean.getPhonenum()));  
                mContext.startActivity(intent); 
			}
		});
    	
    	action(bean);
    	
		return convertView;
	}
	
	/**getview中的抽象方法*/
	protected abstract void action(final OrderBean bean);
	
	protected void update(final OrderBean bean,int field,String value){
		ProgressUtil.showProgress(mContext, "");
		OrderBean p=new OrderBean();
		if(field==FIELD_PACKER){
			p.setPacker(value);
			p.setDispatcher(bean.getDispatcher());
		}else if(field==FIELD_DISPATCHER){
			p.setDispatcher(value);
			p.setPacker(bean.getPacker());
		}
		p.setPayMethod(bean.getPayMethod());
		p.setState(bean.getState());
		p.setPrice(bean.getPrice());
		p.setObjectId(bean.getObjectId());
		p.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				orderBeans.remove(bean);
				
				notifyDataSetChanged();
				ShowToast("成功");
				ProgressUtil.closeProgress();
                if(orderBeans.size()==0){
					((Activity)mContext).finish();
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast("接单失败 \n"+arg1);
				ProgressUtil.closeProgress();

			}
		});
	}
	
	/**更新order的状态*/
	protected void update(final OrderBean bean,int state){
		ProgressUtil.showProgress(mContext, "");
		OrderBean p=new OrderBean();
		p.setDispatcher(bean.getDispatcher());
		p.setPacker(bean.getPacker());
		p.setPayMethod(bean.getPayMethod());
		p.setState(state);
		p.setPrice(bean.getPrice());
		p.setObjectId(bean.getObjectId());
		p.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				orderBeans.remove(bean);
				notifyDataSetChanged();
				ShowToast("成功");
				ProgressUtil.closeProgress();
				if(bean.getState()==OrderBean.STATE_DEPART){
					AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
					JSONObject object=new JSONObject();
					try {
						object.put("username", bean.getUsername());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					//第一个参数是上下文对象，第二个参数是云端代码的方法名称，第三个参数是上传到云端代码的参数列表（JSONObject cloudCodeParams），第四个参数是回调类
					ace.callEndpoint(mContext, "reward", object, 
					    new CloudCodeListener() {
					            @Override
					            public void onSuccess(Object object) {
					                ShowToast( "奖励成功");
					            }
					            @Override
					            public void onFailure(int code, String msg) {
					            	ShowToast("奖励失败" );
					            }
					        });
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowToast("失败 "+arg1);
				ProgressUtil.closeProgress();

			}
		});
		
	}
	

}
