package com.online.market.admin.fragment;

import cn.bmob.v3.BmobQuery;

import com.online.market.admin.adapter.PackedOrderAdapter;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.fragment.base.BaseOrderFragment;

public class PackedOrderFragment extends BaseOrderFragment {

	@Override
	protected void setBmobQueryCondition(BmobQuery<OrderBean> query) {
		query.addWhereEqualTo("state", OrderBean.STATE_PACKED);
	}

	@Override
	protected void initAdapter() {
		adapter=new PackedOrderAdapter(getActivity(), orders);
	}

}
