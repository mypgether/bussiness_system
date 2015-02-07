package net.bussiness.module.base;

import net.bussiness.activities.R;
import net.bussiness.module.user.DetailProfileActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ab.activity.AbActivity;
import com.ab.image.AbImageLoader;
import com.ab.util.AbToastUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;

/**
 * <pre>
 * Project Name:bussiness_system
 * Package:net.bussiness.module.base
 * FileName:BasePull2RefreshViewActivity.java
 * Purpose:下拉刷新带有普通View的Activity，需要自己填充View视图
 * Create Time: 2015-1-29 下午12:58:00
 * Create Specification:
 * Modified Time:
 * Modified by:
 * Modified Specification:
 * Version: 1.0
 * </pre>
 * 
 * @author Myp
 */
public abstract class BasePull2RefreshViewActivity extends AbActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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