package net.bussiness.module.individualcenter;

import java.util.ArrayList;
import java.util.List;

import net.bussiness.module.base.BaseListViewLoad;
import android.widget.ArrayAdapter;

public class IndividualCenter extends BaseListViewLoad {

	private ArrayAdapter<String> mAdapter = null;
	private List<String> mContent = null;

	@Override
	public void initViewAdapter() {
		mContent = new ArrayList<String>();
		mAdapter = new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_list_item_1, mContent);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * ÏÂÔØÊı¾İ
	 */
	public void refreshTask() {
	}

	public void loadMoreTask() {
	}
}