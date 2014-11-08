package net.bussiness.module.ywsq;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.bussiness.adapter.YwsqExpandableListViewAdapter;
import net.bussiness.dao.YwsqDao;
import net.bussiness.module.base.BaseExpandableListViewLoad;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkWeb;
import android.app.Activity;

import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbToastUtil;

public class BaseYwsq extends BaseExpandableListViewLoad {

	private YwsqExpandableListViewAdapter mAdapter = null;
	private String url = null;
	private int approveState = 0;
	private NetworkWeb networkWeb;

	// ExpandableListView的数据源
	private List<YwsqDao> group;
	private List<List<YwsqDao>> child;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		url = getArguments().getString("url");
		approveState = getArguments().getInt("approveState");
		networkWeb = NetworkWeb.newInstance(mActivity);
	}

	@Override
	public void initViewAdapter() {
		group = new ArrayList<YwsqDao>();
		child = new ArrayList<List<YwsqDao>>();
		mAdapter = new YwsqExpandableListViewAdapter(mActivity, group, child,
				approveState == 0);
		mExpandableListView.setAdapter(mAdapter);
	}

	/**
	 * 下载数据
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
				group.clear();
				child.clear();
				for (YwsqDao tmp : dao) {
					group.add(tmp);
				}
				child.add(dao);
				mAdapter.notifyDataSetChanged();
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
					group.add(tmp);
				}
				child.add(dao);
				mAdapter.notifyDataSetChanged();
				mAbPullToRefreshView.onFooterLoadFinish();
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