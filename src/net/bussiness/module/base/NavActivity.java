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
import net.bussiness.module.ywsq.YwsqSlidingTabFragment;
import net.bussiness.receiver.PushMessageReceiver;
import net.bussiness.tools.BDPushUtils;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.res.Resources;
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
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

@SuppressLint("UseSparseArrays")
public class NavActivity extends FragmentActivity implements
		SLMenuOnItemClickListener, OnOpenedListener {
	private static boolean isFristOpen = true;
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
		if (isFristOpen) {
			loadBdPushConfig();
			isFristOpen = false;
		}
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.sliding_menu_content);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		mAbTitleBar = new AbTitleBar(this);
		mAbTitleBar.setTitleText(R.string.individual_center_title);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(
				R.drawable.button_selector_menu);

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

		// 添加Fragment
		getFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new IndividualCenter()).commit();

		// SlidingMenu
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		// slidingmenu设置为TOUCHMODE_MARGIN
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// menu添加到Fragment当中
		menu.setMenu(R.layout.sliding_menu_menu);
		menu.setOnOpenedListener(this);
		getFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new NavMenuFragment()).commit();
		menu.setBehindCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
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
		list.add(new YwsqSlidingTabFragment());
		list.add(new IndividualCenter());
		list.add(new IndividualCenter());
		list.add(new IndividualCenter());

		index2Fragment = new HashMap<Integer, Fragment>();
		index2Fragment.put(0, new IndividualCenter());
	}

	private void loadBdPushConfig() {
		PushMessageReceiver.mActivity = NavActivity.this;
		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
		// 这里把apikey存放于manifest文件中，只是一种存放方式，
		// 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
		// "api_key")
		/*
		 * ！！ 请将AndroidManifest.xml 104行处 api_key 字段值修改为自己的 api_key 方可使用 ！！ ！！
		 * ATTENTION：You need to modify the value of api_key to your own at row
		 * 104 in AndroidManifest.xml to use this Demo !!
		 */
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				BDPushUtils.getMetaValue(NavActivity.this, "api_key"));
		// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
		// PushManager.enableLbs(getApplicationContext());

		Resources resource = this.getResources();
		String pkgName = this.getPackageName();
		// Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
		// 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
		// 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
		CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
				getApplicationContext(), resource.getIdentifier(
						"notification_custom_builder", "layout", pkgName),
				resource.getIdentifier("notification_icon", "id", pkgName),
				resource.getIdentifier("notification_title", "id", pkgName),
				resource.getIdentifier("notification_text", "id", pkgName));
		cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
				| Notification.DEFAULT_VIBRATE);
		cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
		cBuilder.setLayoutDrawable(resource.getIdentifier(
				"simple_notification_icon", "drawable", pkgName));
		PushManager.setNotificationBuilder(this, 1, cBuilder);
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
	 * 切换主屏幕的内容
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