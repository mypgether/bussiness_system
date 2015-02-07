package net.bussiness.module.base;

import net.bussiness.activities.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.ab.fragment.AbFragment;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;

/**
 * <pre>
 * Project Name:bussiness_system
 * Package:net.bussiness.module.base
 * FileName:BasePull2RefreshExpandableFragment.java
 * Purpose:下拉刷新带有ExpandableListview的Fragment
 * Create Time: 2015-1-16 下午4:33:42
 * Create Specification:
 * Modified Time:
 * Modified by:
 * Modified Specification:
 * Version: 1.0
 * </pre>
 * 
 * @author Myp
 */
public abstract class BasePull2RefreshExpandableLvFragment extends AbFragment {

	protected AbPullToRefreshView mAbPullToRefreshView = null;
	protected ExpandableListView mExpandableListView = null;
	protected Activity mActivity = null;

	protected int nowPage = 1;

	private int[] expandGroupPosition;

	@Override
	public View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		mActivity = getActivity();
		View view = inflater.inflate(R.layout.pull_to_refresh_expandlist, null);
		mAbPullToRefreshView = (AbPullToRefreshView) view
				.findViewById(R.id.mPullRefreshView);
		mExpandableListView = (ExpandableListView) view
				.findViewById(R.id.mExpandableListView);
		expandGroupPosition = new int[1000];
		mExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {
					@Override
					public void onGroupExpand(int groupPosition) {
						expandGroupPosition[groupPosition] = 1;
					}
				});
		mExpandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {
					@Override
					public void onGroupCollapse(int groupPosition) {
						expandGroupPosition[groupPosition] = 0;
					}
				});
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

		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(
						R.drawable.listview_pull_refresh02));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(
						R.drawable.listview_pull_refresh02));
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
		this.setLoadDrawable(R.drawable.ic_load);
		this.setLoadMessage("正在加载,请稍后...");

		this.setRefreshDrawable(R.drawable.ic_refresh);
		this.setRefreshMessage("正在更新数据,请稍后...");
	}

	/**
	 * <pre>
	 * Purpose:获取ExpandListView的真实双击位置
	 * @author Myp
	 * Create Time: 2015-1-24 上午11:35:03
	 * @param position
	 * @return groupPosition
	 * Version: 1.0
	 * </pre>
	 */
	protected int getRealPos(int position) {
		int i = 0;
		int end = position;
		for (; i <= end; i++) {
			if (expandGroupPosition[i] == 1) {
				position -= 2;
			} else
				position--;
			if (position < 0)
				break;
		}
		return i;
	}

	/**
	 * <pre>
	 * Purpose:初始化Adapter,绑定ListView
	 * @author Myp
	 * Create Time: 2014-10-26 下午2:17:31
	 * Version: 1.0
	 * </pre>
	 */
	public abstract void initViewAdapter();

	/**
	 * <p)re>
	 * Purpose:刷新数据
	 * 
	 * @author Myp Create Time: 2014-10-26 下午1:33:22 Version: 1.0 </pre>
	 */
	public void refreshTask() {
		nowPage = 1;
	}

	/**
	 * <pre>
	 * Purpose:加载更多数据
	 * @author Myp
	 * Create Time: 2014-10-26 下午1:33:27
	 * Version: 1.0
	 * </pre>
	 */
	public void loadMoreTask() {
		nowPage++;
	}
}