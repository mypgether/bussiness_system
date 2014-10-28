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

public abstract class BaseSlidingTabFragment extends Fragment {
	public List<Fragment> mFragments;
	public List<String> tabTexts;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_top, null);
		AbSlidingTabView mAbSlidingTabView = (AbSlidingTabView) view
				.findViewById(R.id.mAbSlidingTabView);

		// ��������ҳ���б�������ԭ��
		// Fragment�����õ�AbTaskQueue,�����ж��tab��˳���������ӳ٣���û���غþͱ������ˡ��ĳ���AbTaskPool����ok�ˡ�
		// ����setOffscreenPageLimit(0)

		// ��������
		mAbSlidingTabView.getViewPager().setOffscreenPageLimit(5);
		mFragments = new ArrayList<Fragment>();
		tabTexts = new ArrayList<String>();
		initSlidingTab();
		// ������ʽ
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
	 * Purpose:mFragments��tabTexts���������
	 * @author Myp
	 * Create Time: 2014-10-27 ����2:17:13
	 * Version: 1.0
	 * </pre>
	 */
	public abstract void initSlidingTab();
}