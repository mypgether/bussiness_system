package net.bussiness.module.base;

import net.bussiness.activities.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ab.fragment.AbFragment;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;

public abstract class BaseExpandableListViewLoad extends AbFragment {

	protected AbPullToRefreshView mAbPullToRefreshView = null;
	protected ExpandableListView mExpandableListView = null;
	protected Activity mActivity = null;

	protected int nowPage = 1;
	protected int rows = 1;

	@Override
	public View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		mActivity = getActivity();
		View view = inflater.inflate(R.layout.pull_to_refresh_expandlist, null);
		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) view
				.findViewById(R.id.mPullRefreshView);
		mExpandableListView = (ExpandableListView) view
				.findViewById(R.id.mExpandableListView);
		initViewAdapter();
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
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

		// 加载数据必须
		this.setAbFragmentOnLoadListener(new AbFragmentOnLoadListener() {
			@Override
			public void onLoad() {
				refreshTask();
			}
		});
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