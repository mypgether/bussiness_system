package net.bussiness.module.setting;

import net.bussiness.activities.R;
import net.bussiness.dialog.lib.Effectstype;
import net.bussiness.dialog.lib.NiftyDialogBuilder;
import net.bussiness.dto.CNoBotherModeDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.module.user.EditProfileActivity;
import net.bussiness.tools.DataManagerUtils;
import net.bussiness.tools.JacksonUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.sliding.AbSlidingButton;
import com.ab.view.titlebar.AbTitleBar;
import com.ab.view.wheel.AbWheelUtil;
import com.ab.view.wheel.AbWheelView;

public class SettingActivity extends AbActivity {
	private ImageView iv1;

	private RelativeLayout rl1;
	private RelativeLayout rl2;
	private RelativeLayout rl3;
	private RelativeLayout rl4;

	private TextView tv1;
	private TextView tv2;

	private AbSlidingButton slb;

	private IApplication iApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.setting);
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("设置");
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
		iv1 = (ImageView) findViewById(R.id.user_logo);
		rl1 = (RelativeLayout) findViewById(R.id.setting_user_rl);
		rl1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this,
						EditProfileActivity.class));
			}
		});
		rl2 = (RelativeLayout) findViewById(R.id.setting_msg_notification);
		rl2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this,
						MsgNotificationActivity.class));
			}
		});
		rl3 = (RelativeLayout) findViewById(R.id.setting_pageloading);
		rl3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this,
						MsgLoadingNumberActivity.class));
			}
		});
		rl4 = (RelativeLayout) findViewById(R.id.setting_clearCache);
		rl4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
						.getInstance(SettingActivity.this);
				dialogBuilder
						.withTitleColor("#FFFFFF")
						.withDividerColor("#11000000")
						.withMessage("确认清空所有缓存?")
						.withMessageColor("#FFFFFFFF")
						.withDialogColor("#FFE74C3C")
						.withIcon(
								getResources().getDrawable(
										R.drawable.ic_launcher))
						.withDuration(100) // def
						.withEffect(Effectstype.Slidetop) // def
						.withButton1Text("确定") // def gone
						.withButton2Text("取消") // def gone
						.setButton1Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								DataManagerUtils
										.cleanInternalCache(SettingActivity.this);
								DataManagerUtils
										.cleanExternalCache(SettingActivity.this);
								AbToastUtil.showToast(SettingActivity.this,
										"清除成功!");
								dialogBuilder.dismiss();
							}
						}).setButton2Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialogBuilder.dismiss();
							}
						}).show();
			}
		});

		slb = (AbSlidingButton) findViewById(R.id.mSliderBtn1);
		slb.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
				R.drawable.btn_mask, R.drawable.btn_unpressed,
				R.drawable.btn_pressed);

		tv1 = (TextView) findViewById(R.id.startTime);
		tv2 = (TextView) findViewById(R.id.endTime);
		tv1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View mTimeView3 = mInflater.inflate(R.layout.choose_two, null);
				initWheelTime(mTimeView3, tv1);
				AbDialogUtil.showDialog(mTimeView3, Gravity.BOTTOM);
			}
		});
		tv2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View mTimeView3 = mInflater.inflate(R.layout.choose_two, null);
				initWheelTime(mTimeView3, tv2);
				AbDialogUtil.showDialog(mTimeView3, Gravity.BOTTOM);
			}
		});

		if (iApp.mConfig.getmCNoBotherModeDto().isOpen()) {
			slb.setChecked(true);
			findViewById(R.id.setting_nobothermode_ll).setVisibility(
					View.VISIBLE);
			tv1.setText(iApp.mConfig.getmCNoBotherModeDto().getStartTime());
			tv2.setText(iApp.mConfig.getmCNoBotherModeDto().getEndTime());
		} else {
			slb.setChecked(false);
		}

		slb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					findViewById(R.id.setting_nobothermode_ll).setVisibility(
							View.VISIBLE);
					tv1.setText("9:00");
					tv2.setText("17:00");
				} else {
					findViewById(R.id.setting_nobothermode_ll).setVisibility(
							View.GONE);
				}
			}
		});

		iApp.mAbImageLoader.display(iv1, ConstServer.USER_DOWNLOADLOGO(
				iApp.mUser.getUserId(), iApp.mUser.getPhotoPath()));
	}

	public void initWheelTime(View mTimeView, TextView mText) {
		final AbWheelView mWheelViewMM = (AbWheelView) mTimeView
				.findViewById(R.id.wheelView1);
		final AbWheelView mWheelViewHH = (AbWheelView) mTimeView
				.findViewById(R.id.wheelView2);
		Button okBtn = (Button) mTimeView.findViewById(R.id.okBtn);
		Button cancelBtn = (Button) mTimeView.findViewById(R.id.cancelBtn);
		mWheelViewMM.setCenterSelectDrawable(this.getResources().getDrawable(
				R.drawable.wheel_select));
		mWheelViewHH.setCenterSelectDrawable(this.getResources().getDrawable(
				R.drawable.wheel_select));
		AbWheelUtil.initWheelTimePicker2(this, mText, mWheelViewMM,
				mWheelViewHH, okBtn, cancelBtn, 1, 1, true);
	}

	@Override
	public void finish() {
		CNoBotherModeDto mode = new CNoBotherModeDto();
		if (slb.isChecked() == true) {
			mode = new CNoBotherModeDto(true, tv1.getText().toString().trim(),
					tv2.getText().toString().trim());
		}
		iApp.mConfig.setmCNoBotherModeDto(mode);
		iApp.saveConfig2SharedPreference(JacksonUtils.bean2Json(iApp.mConfig));
		super.finish();
	}
}
