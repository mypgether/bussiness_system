package net.bussiness.module.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bussiness.activities.R;
import net.bussiness.dialog.lib.Effectstype;
import net.bussiness.dialog.lib.NiftyDialogBuilder;
import net.bussiness.module.base.NavMenuFragment.SLMenuOnItemClickListener;
import net.bussiness.module.individualcenter.IndividualCenter;
import net.bussiness.module.ywsq.SlidingTabFragment;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.util.AbToastUtil;
import com.ab.view.slidingmenu.SlidingMenu;
import com.ab.view.slidingmenu.SlidingMenu.CanvasTransformer;
import com.ab.view.slidingmenu.SlidingMenu.OnOpenedListener;
import com.ab.view.titlebar.AbTitleBar;

@SuppressLint("UseSparseArrays")
public class NavActivity extends FragmentActivity implements
		SLMenuOnItemClickListener, OnOpenedListener {
	private int nowIndex = 0;
	private SlidingMenu menu;
	private AbTitleBar mAbTitleBar;
	private List<Fragment> list;
	private Map<Integer, Fragment> index2Fragment;

	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 使用系统标题栏位置，为了放入自定义的标题栏
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.sliding_menu_content);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		// 自定义的标题栏
		mAbTitleBar = new AbTitleBar(this);
		mAbTitleBar.setTitleText(R.string.individual_center_title);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(
				R.drawable.button_selector_menu);

		// 加到系统标题栏位置上
		LinearLayout titleBarLinearLayout = (LinearLayout) this
				.findViewById(R.id.titleBar);
		LinearLayout.LayoutParams layoutParamsFF = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleBarLinearLayout.addView(mAbTitleBar, layoutParamsFF);
		mAbTitleBar.getLogoView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				menu.toggle();
			}
		});

		// 主视图的Fragment添加
		getFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new IndividualCenter()).commit();

		// SlidingMenu的配置
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		// slidingmenu的事件模式，如果里面有可以滑动的请用TOUCHMODE_MARGIN
		// 可解决事件冲突问题
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// menu视图的Fragment添加
		menu.setMenu(R.layout.sliding_menu_menu);
		menu.setOnOpenedListener(this);
		getFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new NavMenuFragment()).commit();
		// 动画配置
		menu.setBehindCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				// 将画布默认的黑背景替换掉
				canvas.drawColor(NavActivity.this.getResources().getColor(
						R.color.gray_white));
				canvas.translate(
						0,
						canvas.getHeight()
								* (1 - interp.getInterpolation(percentOpen)));
			}
		});

		// init data
		list = new ArrayList<Fragment>();
		list.add(new IndividualCenter());
		list.add(new IndividualCenter());
		list.add(new SlidingTabFragment());
		list.add(new IndividualCenter());
		list.add(new IndividualCenter());
		list.add(new IndividualCenter());

		index2Fragment = new HashMap<Integer, Fragment>();
		index2Fragment.put(0, new IndividualCenter());
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			exitApp();
		}
	}

	private void exitApp() {
		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
				.getInstance(this);
		dialogBuilder.withTitle("退出系统")
				// .withTitle(null) no title
				.withTitleColor("#FFFFFF")
				// def
				.withDividerColor("#11000000")
				// def
				.withMessage("确定退出出差管理系统?")
				// .withMessage(null) no Msg
				.withMessageColor("#FFFFFFFF")
				// def | withMessageColor(int resid)
				.withDialogColor("#FFE74C3C")
				// def | withDialogColor(int resid)
				.withIcon(getResources().getDrawable(R.drawable.ic_launcher))
				.withDuration(300) // def
				.withEffect(Effectstype.Newspager) // def Effectstype.Slidetop
				.withButton1Text("确定") // def gone
				.withButton2Text("取消") // def gone
				.isCancelableOnTouchOutside(false) // def | isCancelable(true)
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}

	@Override
	public void selectItem(int position, String title) {
		if (nowIndex == position) {
			menu.toggle();
		} else {
			switchContent(position, title);
		}
	}

	/**
	 * 左侧菜单点击切换首页的内容
	 */
	public void switchContent(int position, String title) {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.hide(index2Fragment.get(nowIndex));
		nowIndex = position;
		if (index2Fragment.containsKey(nowIndex)) {
			transaction.show(index2Fragment.get(nowIndex));
		} else {
			index2Fragment.put(nowIndex, list.get(nowIndex));
			transaction.add(R.id.content_frame, list.get(nowIndex));
		}
		mAbTitleBar.setTitleText(title);
		transaction.commit();
		menu.toggle();
	}

	@Override
	public void selectLogo() {
		AbToastUtil.showToast(NavActivity.this, "LogoClick");
	}

	@Override
	public void onOpened() {
		View view = getFragmentManager().findFragmentById(R.id.menu_frame)
				.getView();
		// shake logo
		// view.findViewById(R.id.menu_user_logo).startAnimation(
		// AnimationUtils.loadAnimation(NavActivity.this, R.anim.shake));

		// shake counter
		ListView lv = (ListView) view.findViewById(R.id.menu_lv);
		for (int i = 0; i < lv.getChildCount(); i++) {
			RelativeLayout layout = (RelativeLayout) lv.getChildAt(i);
			TextView cntTv = (TextView) layout.findViewById(R.id.counter);
			if (!"".equals(cntTv.getText())) {
				cntTv.startAnimation(AnimationUtils.loadAnimation(
						NavActivity.this, R.anim.shake));
			}
		}
	}
}