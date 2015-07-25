package com.online.market.admin;

import java.util.List;

import com.online.market.admin.adapter.NewOrderAdapter;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.util.SoundUtil;
import com.online.market.admin.util.Speecher;
import com.online.market.admin.util.VibratorUtil;
import com.online.market.admin.view.xlist.XListView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NewOrderActivity extends BaseActivity {
	
	private Button btIgnore;
	private XListView xlv;
	private List<OrderBean> orders;
	private Speecher speecher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neworder);
		
		initViews();
		setListeners();
		initData();
	}

	@Override
	public void initViews() {

		btIgnore=(Button) findViewById(R.id.bt_ignore);
		xlv=(XListView) findViewById(R.id.xlv);
		xlv.setPullRefreshEnable(false);
	}

	@Override
	public void initData() {

		speecher=Speecher.getSpeecher(this);
		orders=(List<OrderBean>) getIntent().getSerializableExtra("data");
		if(orders==null||orders.size()==0){
			finish();
			return;
		}
		NewOrderAdapter adapter=new NewOrderAdapter(this, orders);
		xlv.setAdapter(adapter);
		
		VibratorUtil.vibra(this);
//		SoundUtil.soundRing(this);
		
		speecher.speech("有新订单了，送货地址："+orders.get(0).getAddress());
	}

	@Override
	public void setListeners() {

		btIgnore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		speecher.destroy();
	}

}
