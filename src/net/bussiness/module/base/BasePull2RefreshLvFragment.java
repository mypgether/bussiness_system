package net.bussiness.module.base;

import net.bussiness.activities.R;
import net.bussiness.global.IApplication;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ab.fragment.AbFragment;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;

/**
 * <pre>
 * Project Name:bussiness_system
 * Package:net.bussiness.module.base
 * FileName:BasePull2RefreshFragment.java
 * Purpose:下拉刷新带有ListView的Fragment
 * Create Time: 2015-1-16 下午4:34:32
 * Create Specification:
 * Modified Time:
 * Modified by:
 * Modified Specification:
 * Version: 1.0
 * </pre>
 * 
 * @author Myp
 */
public abstract class BasePull2RefreshLvFragment extends AbFragment {

	protected AbPullToRefreshView mAbPullToRefreshView = null;
	protected ListView mListView = null;
	protected Activity mActivity = null;

	protected int nowPage = 1;
	protected int rows = 1;

	protected IApplication iApp = null;

	@Override
	public View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		mActivity = getActivity();
		iApp = (IApplication) mActivity.getApplication();
		View view = inflater.inflate(R.layout.pull_to_refresh_list, null);
		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) view
				.findViewById(R.id.mPullRefreshView);
		mListView = (ListView) view.findViewById(R.id.mListView);
		// 设置监听器
		mAbPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						refreshTask();
					}
				});
		mAbPullToRefreshView
				.setOnFooterLoadListener(new OnFooterLoadListener() {
					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						loadMoreTask();
					}
				});

		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(
						R.drawable.listview_pull_refresh02));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(
						R.drawable.listview_pull_refresh02));

		// 加载数据必须
		this.setAbFragmentOnLoadListener(new AbFragmentOnLoadListener() {
			@Override
			public void onLoad() {
				refreshTask();
			}
		});
		initViewAdapter();
		return view;
	}

	@Override
	public void setResource() {
		// 设置加载的资源
		this.setLoadDrawable(R.drawable.ic_load);
		this.setLoadMessage("正在查询,请稍候");

		this.setRefreshDrawable(R.drawable.ic_refresh);
		this.setRefreshMessage("请求出错，请重试");
	}

	/**
	 * <pre>
	 * Purpose:初始化Adapter,填充到mListView当中
	 * @author Myp
	 * Create Time: 2014-10-26 下午2:17:31
	 * Version: 1.0
	 * </pre>
	 */
	public abstract void initViewAdapter();

	/**
	 * <pre>
	 * Purpose:刷新数据
	 * @author Myp
	 * Create Time: 2014-10-26 下午1:33:22
	 * Version: 1.0
	 * </pre>
	 */
	public void refreshTask() {
		nowPage = 1;
	}

	/**
	 * <pre>
	 * Purpose:加载更多数据
	 * @author Myp
	 * Create Time: 2014-10-26 下午1:33:27
	 * Version: 1.0
	 * </pre>
	 */
	public void loadMoreTask() {
		nowPage++;
	}
}