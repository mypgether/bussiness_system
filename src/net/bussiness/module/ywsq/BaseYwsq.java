package net.bussiness.module.ywsq;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
import net.bussiness.dao.YwsqDao;
import net.bussiness.module.base.BaseFragmentLoad;
import net.bussiness.tools.JacksonUtils;
=======
import net.bussiness.module.base.BaseFragmentLoad;
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
import net.bussiness.tools.NetworkWeb;
import android.app.Activity;
import android.widget.ArrayAdapter;

import com.ab.http.AbHttpListener;
<<<<<<< HEAD
import com.ab.http.AbRequestParams;
=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
import com.ab.util.AbToastUtil;

public class BaseYwsq extends BaseFragmentLoad {

	private ArrayAdapter<String> mAdapter = null;
	private List<String> mContent = null;
	private String url = null;
<<<<<<< HEAD
	private int approveState = 0;
	private NetworkWeb networkWeb;
=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		url = getArguments().getString("url");
<<<<<<< HEAD
		approveState = getArguments().getInt("approveState");
		networkWeb = NetworkWeb.newInstance(mActivity);
=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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
<<<<<<< HEAD
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
=======
		AbToastUtil.showToast(mActivity, "url=" + url);
		NetworkWeb networkWeb = NetworkWeb.newInstance(mActivity);
		networkWeb.get(url, null, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				AbToastUtil.showToast(mActivity, content);
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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
<<<<<<< HEAD
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
=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
	}
}