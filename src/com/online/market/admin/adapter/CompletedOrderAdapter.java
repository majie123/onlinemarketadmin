package com.online.market.admin.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

import com.online.market.admin.adapter.BaseOrderAdapter;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.util.DialogUtil;

public class CompletedOrderAdapter extends BaseOrderAdapter {

	public CompletedOrderAdapter(Context context, List<OrderBean> orderBeans) {
		super(context, orderBeans);
	}

	@Override
	protected void action(final OrderBean bean) {
		tvOrderTime.setVisibility(View.GONE);
		btDelive.setText("标记为未处理");
		btDelive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogUtil.dialog(mContext, "你确认要标记为未处理吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						update(bean,OrderBean.STATE_UNTREATED);
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
	}

}
