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
		btDelive.setText("标记为");
		btDelive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogUtil.dialog(mContext, "标记为", new String []{"未打包","已打包","已出发","已送达"}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						update(bean, arg1);
						dialog.dismiss();

					}
				});
			}
		});
	}

}
