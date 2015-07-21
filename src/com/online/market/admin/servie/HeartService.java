package com.online.market.admin.servie;

import java.io.Serializable;
import java.util.List;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.online.market.admin.MainActivity;
import com.online.market.admin.NewOrderActivity;
import com.online.market.admin.R;
import com.online.market.admin.bean.MyUser;
import com.online.market.admin.bean.OrderBean;

public class HeartService extends Service {
	private MyUser user;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Notification notification = new Notification(R.drawable.ic_launcher,  
		getString(R.string.app_name), System.currentTimeMillis());  
		 PendingIntent pendingintent = PendingIntent.getActivity(this, 0,  
				 new Intent(this, MainActivity.class), 0);  
		 notification.setLatestEventInfo(this, "天天在线管理端", "守护进程正在后台运行......",  
				 pendingintent); 		 		
		startForeground(0x111, notification);
		user=BmobUser.getCurrentUser(getApplicationContext(), MyUser.class);
		new CountThread().start();
		registerReceiver();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		stopForeground(true);
		unregisterReceiver(receiver);
	}
	
	class CountThread extends Thread{
		@Override
		public void run() {
			super.run();
			int i=0;
			while(true){
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i+=10;
				Intent intent=new Intent("intent_count");
				intent.putExtra("count", i);
				sendBroadcast(intent);
			}
		}
	}
	
    private BroadcastReceiver receiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(intent.getAction().equals("intent_count")){
				int count=intent.getIntExtra("count", -1);
				//每120秒刷新一次
				if(count%120==0){
					queryOrders();
				}
			}
		}
	};
	
	private void registerReceiver(){
		IntentFilter filter=new IntentFilter("intent_count");
		registerReceiver(receiver, filter);
	}
	
	private void queryOrders(){
		BmobQuery<OrderBean> query	 = new BmobQuery<OrderBean>();
		if(user.getGroup()==MyUser.GROUP_PACKER){
			query.addWhereEqualTo("packer", "untreated");
		}else{
			query.addWhereEqualTo("dispatcher", "untreated");
		}
		query.addWhereNotEqualTo("state", OrderBean.STATE_DELIVED);
		query.setLimit(10);
		query.findObjects(this, new FindListener<OrderBean>() {

			@Override
			public void onSuccess(List<OrderBean> object) {
//				Log.e("majie", "size = "+object.size());
				if(object.size()!=0){
					Intent intent=new Intent(getApplicationContext(),NewOrderActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("data", (Serializable)object);
					startActivity(intent);
				}
			}

			@Override
			public void onError(int code, String msg) {
			}
		});	
	}

}
