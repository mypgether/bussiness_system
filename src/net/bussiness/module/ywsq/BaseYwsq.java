package net.bussiness.module.ywsq;

import java.util.ArrayList;
import java.util.List;

import net.bussiness.module.base.BaseFragmentLoad;
import net.bussiness.tools.NetworkWeb;
import android.app.Activity;
import android.widget.ArrayAdapter;

import com.ab.http.AbHttpListener;
import com.ab.util.AbToastUtil;

public class BaseYwsq extends BaseFragmentLoad {

	private ArrayAdapter<String> mAdapter = null;
	private List<String> mContent = null;
	private String url = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		url = getArguments().getString("url");
	}

	@Override
	public void initViewAdapter() {
		mContent = new ArrayList<String>();
		mAdapter = new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_list_item_1, mContent);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * ÏÂÔØÊý¾Ý
	 */
	public void refreshTask() {
		AbToastUtil.showToast(mActivity, "url=" + url);
		NetworkWeb networkWeb = NetworkWeb.newInstance(mActivity);
		networkWeb.get(url, null, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				AbToastUtil.showToast(mActivity, content);
				mAbPullToRefreshView.onHeaderRefreshFinish();
				showContentView();
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(mActivity, content);
			}
		});
	}

	public void loadMoreTask() {
	}
}