package net.bussiness.module.setting;

import net.bussiness.activities.R;
import net.bussiness.global.IApplication;
import android.os.Bundle;
import android.view.View;

import com.ab.activity.AbActivity;
import com.ab.view.sliding.AbSlidingButton;
import com.ab.view.titlebar.AbTitleBar;

public class MsgNotificationActivity extends AbActivity {

	private AbSlidingButton slb1;
	private AbSlidingButton slb2;
	private AbSlidingButton slb3;
	private AbSlidingButton slb4;
	private AbSlidingButton slb5;
	private AbSlidingButton slb6;

	private IApplication iApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.setting_msgnotify);
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("设置消息提醒");
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		iApp = (IApplication) getApplication();
		initView();
	}

	public void initView() {
		slb1 = (AbSlidingButton) findViewById(R.id.mSliderBtn1);
		slb1.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
				R.drawable.btn_mask, R.drawable.btn_unpressed,
				R.drawable.btn_pressed);
		slb1.setChecked(iApp.mConfig.getmCMsgNotificationDto().isImPersonOpen());

		slb2 = (AbSlidingButton) findViewById(R.id.mSliderBtn2);
		slb2.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
				R.drawable.btn_mask, R.drawable.btn_unpressed,
				R.drawable.btn_pressed);
		slb2.setChecked(iApp.mConfig.getmCMsgNotificationDto().isImGroupOpen());

		if (iApp.mUserIdentity == 1) {
			findViewById(R.id.setting_msgnotify_ywsq).setVisibility(
					View.VISIBLE);
			findViewById(R.id.setting_msgnotify_ywsp).setVisibility(View.GONE);
			slb3 = (AbSlidingButton) findViewById(R.id.mSliderBtn3);
			slb3.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
					R.drawable.btn_mask, R.drawable.btn_unpressed,
					R.drawable.btn_pressed);
			slb3.setChecked(iApp.mConfig.getmCMsgNotificationDto().isYwsqOpen());
		} else {
			findViewById(R.id.setting_msgnotify_ywsq).setVisibility(View.GONE);
			findViewById(R.id.setting_msgnotify_ywsp).setVisibility(
					View.VISIBLE);
			slb4 = (AbSlidingButton) findViewById(R.id.mSliderBtn4);
			slb4.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
					R.drawable.btn_mask, R.drawable.btn_unpressed,
					R.drawable.btn_pressed);
			slb4.setChecked(iApp.mConfig.getmCMsgNotificationDto().isYwspOpen());
		}

		slb5 = (AbSlidingButton) findViewById(R.id.mSliderBtn5);
		slb5.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
				R.drawable.btn_mask, R.drawable.btn_unpressed,
				R.drawable.btn_pressed);
		slb5.setChecked(iApp.mConfig.getmCMsgNotificationDto().isYwpjOpen());

		slb6 = (AbSlidingButton) findViewById(R.id.mSliderBtn6);
		slb6.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
				R.drawable.btn_mask, R.drawable.btn_unpressed,
				R.drawable.btn_pressed);
		slb6.setChecked(iApp.mConfig.getmCMsgNotificationDto().isYwnrOpen());
	}

	@Override
	public void finish() {
		iApp.mConfig.getmCMsgNotificationDto()
				.setImPersonOpen(slb1.isChecked());
		iApp.mConfig.getmCMsgNotificationDto().setImGroupOpen(slb2.isChecked());
		if (iApp.mUserIdentity == 1) {
			iApp.mConfig.getmCMsgNotificationDto()
					.setYwsqOpen(slb3.isChecked());
		} else {
			iApp.mConfig.getmCMsgNotificationDto()
					.setYwspOpen(slb4.isChecked());
		}
		iApp.mConfig.getmCMsgNotificationDto().setYwpjOpen(slb5.isChecked());
		iApp.mConfig.getmCMsgNotificationDto().setYwnrOpen(slb6.isChecked());
		super.finish();
	}
}
