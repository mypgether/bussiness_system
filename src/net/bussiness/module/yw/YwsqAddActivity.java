package net.bussiness.module.yw;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.bussiness.activities.R;
import net.bussiness.dto.YwsqDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import net.bussiness.tools.TimeUtils;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbProgressDialogFragment;
import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.calendar.CalendarCell;
import com.ab.view.calendar.CalendarView;
import com.ab.view.sliding.AbSlidingButton;
import com.ab.view.titlebar.AbTitleBar;

public class YwsqAddActivity extends AbActivity implements
		OnCheckedChangeListener {
	private CalendarView mCalendarView = null;
	private AbSlidingButton sb1 = null, sb2 = null, sb3 = null, sb4 = null;
	private EditText locationEt = null;
	private EditText budgetEt = null;
	private List<String> monthList = null;
	private int currentMonthIndex = 0;
	private TextView monthText = null;
	private String currentMonth = null;

	private AbProgressDialogFragment mProgressFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.ywsq_add);
		initTitleBar();
		initCalendar();
		initSlidingButton();
		initLable();
	}

	private void initTitleBar() {
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("申请业务");
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
	}

	private void initLable() {
		((Button) findViewById(R.id.submitBtn))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						YwsqDto ywsqDto = obtainYwsqDto();
						if (ywsqDto != null) {
							mProgressFragment = AbDialogUtil
									.showProgressDialog(YwsqAddActivity.this,
											0, "提交申请中，请稍后...");
							mProgressFragment.setCancelable(false);
							NetworkUtils web = NetworkUtils
									.newInstance(YwsqAddActivity.this);
							AbRequestParams params = new AbRequestParams();
							params.put("ywsqDto",
									JacksonUtils.bean2Json(ywsqDto));
							web.post(
									ConstServer
											.YWSQ_ADD(((IApplication) abApplication).mUser
													.getUserId()), params,
									new AbHttpListener() {
										@Override
										public void onSuccess(String content) {
											if (null != mProgressFragment)
												mProgressFragment.dismiss();
											super.onSuccess(content);
											AbToastUtil.showToast(
													YwsqAddActivity.this,
													"提交业务申请成功！");
											finish();
										}

										@Override
										public void onFailure(String content) {
											if (null != mProgressFragment)
												mProgressFragment.dismiss();
											AbToastUtil.showToast(
													YwsqAddActivity.this,
													"提交业务申请失败!");
											finish();
										}
									});
						}
					}
				});
		((Button) findViewById(R.id.cancelBtn))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		locationEt = (EditText) findViewById(R.id.location);
		budgetEt = (EditText) findViewById(R.id.budget);
	}

	private YwsqDto obtainYwsqDto() {
		YwsqDto ywsqDto = new YwsqDto();
		ywsqDto.setApplyTime(new Date());
		ywsqDto.setApproveState(0);
		ywsqDto.setLocation(locationEt.getText().toString().trim());
		try {
			ywsqDto.setBudget(Integer.parseInt(budgetEt.getText().toString()
					.trim()));
		} catch (Exception e) {
			AbToastUtil.showToast(YwsqAddActivity.this, "请输入正确的预算格式！");
			return null;
		}
		String reason = "";
		// 如果沒有選擇任何原因
		if (!sb1.isChecked() && !sb2.isChecked() && !sb3.isChecked()
				&& !sb4.isChecked()) {
			AbToastUtil.showToast(YwsqAddActivity.this, "请选择申请原因！");
			return null;
		}
		if (sb1.isChecked()) {
			reason += "出差、";
		}
		if (sb2.isChecked()) {
			reason += "访客、";
		}
		if (sb3.isChecked()) {
			reason += "客户关怀、";
		}
		if (sb4.isChecked()) {
			reason += "其他、";
		}
		ywsqDto.setReason(reason.substring(0, reason.length() - 1) + "。");
		try {
			ywsqDto.setTimestamp(TimeUtils.parseStringInDate(
					mCalendarView.getCalSelected(), TimeUtils.DATE_PATTERN));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ywsqDto.setUserByProposerId(((IApplication) abApplication).mUser);
		return ywsqDto;
	}

	private void initSlidingButton() {
		sb1 = (AbSlidingButton) findViewById(R.id.mSliderBtn1);
		sb2 = (AbSlidingButton) findViewById(R.id.mSliderBtn2);
		sb3 = (AbSlidingButton) findViewById(R.id.mSliderBtn3);
		sb4 = (AbSlidingButton) findViewById(R.id.mSliderBtn4);
		sb1.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
				R.drawable.btn_mask, R.drawable.btn_unpressed,
				R.drawable.btn_pressed);
		sb2.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
				R.drawable.btn_mask, R.drawable.btn_unpressed,
				R.drawable.btn_pressed);
		sb3.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
				R.drawable.btn_mask, R.drawable.btn_unpressed,
				R.drawable.btn_pressed);
		sb4.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
				R.drawable.btn_mask, R.drawable.btn_unpressed,
				R.drawable.btn_pressed);
		sb1.setOnCheckedChangeListener(this);
		sb2.setOnCheckedChangeListener(this);
		sb3.setOnCheckedChangeListener(this);
		sb4.setOnCheckedChangeListener(this);
	}

	private void initCalendar() {
		LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.layout01_cal);
		mCalendarView = new CalendarView(YwsqAddActivity.this);
		mLinearLayout.addView(mCalendarView);

		mCalendarView.setHeaderHeight(45);
		mCalendarView.setHeaderTextSize(20);
		mCalendarView.setBackgroundResource(R.drawable.calendar_bg);
		mCalendarView.setHeaderBackgroundResource(R.drawable.week_bg);
		mCalendarView
				.setOnItemClickListener(new CalendarView.AbOnItemClickListener() {
					@Override
					public void onClick(int position) {
						String date = mCalendarView
								.getStrDateAtPosition(position);
						AbToastUtil.showToast(YwsqAddActivity.this, "你选择了"
								+ date);
					}
				});
		Calendar calendar = Calendar.getInstance();
		monthList = new ArrayList<String>();
		int curYear = calendar.get(Calendar.YEAR); // 得到系统年份
		int curMonth = calendar.get(Calendar.MONTH) + 1; // 得到系统月份
		for (int i = curMonth; i <= 12; i++) {
			monthList.add(curYear + "-"
					+ AbStrUtil.strFormat2(String.valueOf(i)));
		}
		int postYear = curYear + 1;
		for (int i = 1; i <= 12; i++) {
			monthList.add(postYear + "-"
					+ AbStrUtil.strFormat2(String.valueOf(i)));
		}
		currentMonthIndex = 0;
		currentMonth = monthList.get(currentMonthIndex);

		monthText = (TextView) findViewById(R.id.monthText);
		monthText.setText(currentMonth);

		Button leftBtn = (Button) findViewById(R.id.leftBtn);
		Button rightBtn = (Button) findViewById(R.id.rightBtn);

		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentMonthIndex--;
				if (currentMonthIndex >= 0) {
					currentMonth = monthList.get(currentMonthIndex);
					monthText.setText(currentMonth);
					String[] yearmonth = currentMonth.split("-");
					Calendar cal_select = Calendar.getInstance();
					cal_select.set(Calendar.YEAR,
							Integer.parseInt(yearmonth[0]));
					cal_select.set(Calendar.MONTH,
							Integer.parseInt(yearmonth[1]) - 1);
					cal_select.set(Calendar.DAY_OF_MONTH, 1);
					mCalendarView.rebuildCalendar(cal_select);
					ArrayList<CalendarCell> mCalendarCell = mCalendarView
							.getCalendarCells();
					for (int i = 0; i < 5; i++) {
						CalendarCell cc = mCalendarCell.get(new Random()
								.nextInt(mCalendarCell.size()));
						// 有数据
						cc.setHasRecord(true);
					}
				} else {
					currentMonthIndex++;
				}
			}
		});

		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentMonthIndex++;
				if (currentMonthIndex < monthList.size()) {
					currentMonth = monthList.get(currentMonthIndex);
					monthText.setText(currentMonth);
					String[] yearmonth = currentMonth.split("-");
					Calendar cal_select = Calendar.getInstance();
					cal_select.set(Calendar.YEAR,
							Integer.parseInt(yearmonth[0]));
					cal_select.set(Calendar.MONTH,
							Integer.parseInt(yearmonth[1]) - 1);
					cal_select.set(Calendar.DAY_OF_MONTH, 1);
					mCalendarView.rebuildCalendar(cal_select);
					ArrayList<CalendarCell> mCalendarCell = mCalendarView
							.getCalendarCells();
					for (int i = 0; i < 5; i++) {
						CalendarCell cc = mCalendarCell.get(new Random()
								.nextInt(mCalendarCell.size()));
						// 有数据
						cc.setHasRecord(true);
					}
				} else {
					currentMonthIndex--;
				}
			}
		});

		ArrayList<CalendarCell> mCalendarCell = mCalendarView
				.getCalendarCells();
		for (int i = 0; i < 5; i++) {
			CalendarCell cc = mCalendarCell.get(new Random()
					.nextInt(mCalendarCell.size()));
			// 有数据
			cc.setHasRecord(true);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.mSliderBtn1:
			if (isChecked)
				AbToastUtil.showToast(YwsqAddActivity.this, "你已选择出差！");
			break;
		case R.id.mSliderBtn2:
			if (isChecked)
				AbToastUtil.showToast(YwsqAddActivity.this, "你已选择访客！");
			break;
		case R.id.mSliderBtn3:
			if (isChecked)
				AbToastUtil.showToast(YwsqAddActivity.this, "你已选择客户关怀！");
			break;
		case R.id.mSliderBtn4:
			if (isChecked)
				AbToastUtil.showToast(YwsqAddActivity.this, "你已选择其他！");
			break;
		}
	}
}
