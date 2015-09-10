package com.online.market.admin.fragment;

import cn.bmob.v3.BmobQuery;

import com.online.market.admin.adapter.CompletedOrderAdapter;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.fragment.base.BaseOrderFragment;

public class CompletedOrderFragment extends BaseOrderFragment {

	@Override
	protected void setBmobQueryCondition(BmobQuery<OrderBean> query) {
		query.addWhereEqualTo("state", OrderBean.STATE_DELIVED);
		query.order("-updatedAt");
	}

	@Override
	protected void initAdapter() {
		adapter=new CompletedOrderAdapter(getActivity(), orders);
	}


}
