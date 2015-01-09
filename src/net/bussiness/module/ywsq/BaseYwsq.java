package net.bussiness.module.ywsq;

import java.util.ArrayList;
import java.util.List;

import net.bussiness.adapter.YwsqExpandableListViewAdapter;
import net.bussiness.dto.YwsqDto;
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
	private List<YwsqDto> group;
	private List<List<YwsqDto>> child;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		url = getArguments().getString("url");
		approveState = getArguments().getInt("approveState");
		networkWeb = NetworkWeb.newInstance(mActivity);
	}

	@Override
	public void initViewAdapter() {
		group = new ArrayList<YwsqDto>();
		child = new ArrayList<List<YwsqDto>>();
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
				List<YwsqDto> dao = (List<YwsqDto>) JacksonUtils
						.jsonPageResult2Bean("rows", content, YwsqDto.class);
				group.clear();
				child.clear();
				for (YwsqDto tmp : dao) {
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
				List<YwsqDto> dao = (List<YwsqDto>) JacksonUtils
						.jsonPageResult2Bean("rows", content, YwsqDto.class);
				for (YwsqDto tmp : dao) {
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