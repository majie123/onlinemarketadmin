package com.online.market.admin.servie;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CountService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		new CountThread().start();
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

}
