package net.bussiness.module.base;

import java.util.ArrayList;
import java.util.List;

import net.bussiness.activities.R;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.view.sliding.AbSlidingTabView;

/**
 * <pre>
 * Project Name:bussiness_system
 * Package:net.bussiness.module.base
 * FileName:BaseSlidingTabFragment.java
 * Purpose:滑动Tab的Fragment
 * Create Time: 2015-1-16 下午4:37:34
 * Create Specification:
 * Modified Time:
 * Modified by:
 * Modified Specification:
 * Version: 1.0
 * </pre>
 * 
 * @author Myp
 */
public abstract class BaseSlidingTabFragment extends Fragment {
	public List<Fragment> mFragments;
	public List<String> tabTexts;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_top, null);
		AbSlidingTabView mAbSlidingTabView = (AbSlidingTabView) view
				.findViewById(R.id.mAbSlidingTabView);

		// 如果里面的页面列表不能下载原因：
		// Fragment里面用的AbTaskQueue,由于有多个tab，顺序下载有延迟，还没下载好就被缓存了。改成用AbTaskPool，就ok了。
		// 或者setOffscreenPageLimit(0)

		// 缓存数量
		mAbSlidingTabView.getViewPager().setOffscreenPageLimit(5);
		mFragments = new ArrayList<Fragment>();
		tabTexts = new ArrayList<String>();
		initSlidingTab();
		// 设置样式
		mAbSlidingTabView.setTabTextColor(Color.BLACK);
		mAbSlidingTabView.setTabSelectColor(Color.rgb(30, 168, 131));
		mAbSlidingTabView.setTabBackgroundResource(R.drawable.tab_bg);
		mAbSlidingTabView.setTabLayoutBackgroundResource(R.drawable.slide_top);
		mAbSlidingTabView.setTabPadding(20, 8, 20, 8);
		mAbSlidingTabView.addItemViews(tabTexts, mFragments);
		return view;
	}

	/**
	 * <pre>
	 * Purpose:mFragments、tabTexts中添加数据
	 * @author Myp
	 * Create Time: 2014-10-27 下午2:17:13
	 * Version: 1.0
	 * </pre>
	 */
	public abstract void initSlidingTab();
}