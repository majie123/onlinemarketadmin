package com.online.market.admin.fragment;

import cn.bmob.v3.BmobQuery;

import com.online.market.admin.adapter.DepartedOrderAdapter;
import com.online.market.admin.bean.OrderBean;
import com.online.market.admin.fragment.base.BaseOrderFragment;

public class DepartedOrderFragment extends BaseOrderFragment {

	@Override
	protected void setBmobQueryCondition(BmobQuery<OrderBean> query) {
		query.addWhereEqualTo("state", OrderBean.STATE_DEPART);
	}

	@Override
	protected void initAdapter() {
		adapter=new DepartedOrderAdapter(getActivity(), orders);
	}

}
