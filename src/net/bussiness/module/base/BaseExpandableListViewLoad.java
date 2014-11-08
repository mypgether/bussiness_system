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
		// ��ȡListView����
		mAbPullToRefreshView = (AbPullToRefreshView) view
				.findViewById(R.id.mPullRefreshView);
		mExpandableListView = (ExpandableListView) view
				.findViewById(R.id.mExpandableListView);
		initViewAdapter();
		// ���ü�����
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

		// ���ý���������ʽ
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

		// �������ݱ���
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
		// ���ü��ص���Դ
		this.setLoadDrawable(R.drawable.ic_load);
		this.setLoadMessage("���ڲ�ѯ,���Ժ�");

		this.setRefreshDrawable(R.drawable.ic_refresh);
		this.setRefreshMessage("�������������");
	}

	/**
	 * <pre>
	 * Purpose:��ʼ��Adapter,��䵽mListView����
	 * @author Myp
	 * Create Time: 2014-10-26 ����2:17:31
	 * Version: 1.0
	 * </pre>
	 */
	public abstract void initViewAdapter();

	/**
	 * <pre>
	 * Purpose:ˢ������
	 * @author Myp
	 * Create Time: 2014-10-26 ����1:33:22
	 * Version: 1.0
	 * </pre>
	 */
	public void refreshTask() {
		nowPage = 1;
	}

	/**
	 * <pre>
	 * Purpose:���ظ�������
	 * @author Myp
	 * Create Time: 2014-10-26 ����1:33:27
	 * Version: 1.0
	 * </pre>
	 */
	public void loadMoreTask() {
		nowPage++;
	}
}