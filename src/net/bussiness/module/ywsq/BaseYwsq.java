package net.bussiness.module.ywsq;

import java.util.ArrayList;
import java.util.List;

import net.bussiness.dao.YwsqDao;
import net.bussiness.module.base.BaseFragmentLoad;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkWeb;
import android.app.Activity;
import android.widget.ArrayAdapter;

import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbToastUtil;

public class BaseYwsq extends BaseFragmentLoad {

	private ArrayAdapter<String> mAdapter = null;
	private List<String> mContent = null;
	private String url = null;
	private int approveState = 0;
	private NetworkWeb networkWeb;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		url = getArguments().getString("url");
		approveState = getArguments().getInt("approveState");
		networkWeb = NetworkWeb.newInstance(mActivity);
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
		super.refreshTask();
		System.out.println("url=" + url);
		networkWeb.post(url, getParams(), new AbHttpListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				List<YwsqDao> dao = (List<YwsqDao>) JacksonUtils
						.jsonPageResult2Bean("rows", content, YwsqDao.class);
				mContent.clear();
				for (YwsqDao tmp : dao) {
					mContent.add(tmp.toString());
				}
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
		super.loadMoreTask();
		System.out.println("url=" + url);
		networkWeb.post(url, getParams(), new AbHttpListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				List<YwsqDao> dao = (List<YwsqDao>) JacksonUtils
						.jsonPageResult2Bean("rows", content, YwsqDao.class);
				for (YwsqDao tmp : dao) {
					mContent.add(tmp.toString());
				}
				mAbPullToRefreshView.onFooterLoadFinish();
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(mActivity, content);
			}
		});
	}

	private AbRequestParams getParams() {
		AbRequestParams params = new AbRequestParams();
		params.put("approveState", approveState + "");
		params.put("page", nowPage + "");
		params.put("rows", rows + "");
		return params;
	}
}