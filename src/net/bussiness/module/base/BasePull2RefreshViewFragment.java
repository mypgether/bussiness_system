package net.bussiness.module.base;

import net.bussiness.activities.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ab.fragment.AbFragment;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;

/**
 * <pre>
 * Project Name:bussiness_system
 * Package:net.bussiness.module.base
 * FileName:BasePull2RefreshViewFragment.java
 * Purpose:下拉刷新带有普通View的Fragment，需要自己填充View视图
 * Create Time: 2015-1-27 下午12:27:51
 * Create Specification:
 * Modified Time:
 * Modified by:
 * Modified Specification:
 * Version: 1.0
 * </pre>
 * 
 * @author Myp
 */
public abstract class BasePull2RefreshViewFragment extends AbFragment {

	protected AbPullToRefreshView mAbPullToRefreshView = null;
	protected LinearLayout mLL = null;

	@Override
	public View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pull_to_refresh_view, null);
		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) view
				.findViewById(R.id.mPullRefreshView);
		mLL = (LinearLayout) view.findViewById(R.id.mLL);
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
						mAbPullToRefreshView.onFooterLoadFinish();
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

		initView(inflater);
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
	 * Purpose:初始化视图,填充到mView当中
	 * @author Myp
	 * Create Time: 2014-10-26 下午2:17:31
	 * Version: 1.0
	 * </pre>
	 */
	public abstract void initView(LayoutInflater inflater);

	/**
	 * <pre>
	 * Purpose:刷新数据
	 * @author Myp
	 * Create Time: 2014-10-26 下午1:33:22
	 * Version: 1.0
	 * </pre>
	 */
	public abstract void refreshTask();
}