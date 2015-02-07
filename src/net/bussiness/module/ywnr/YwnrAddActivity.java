package net.bussiness.module.ywnr;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.bussiness.activities.R;
import net.bussiness.adapter.ImageShowAdapter;
import net.bussiness.dto.YwnrDto;
import net.bussiness.dto.YwsqDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.module.base.CropImageActivity;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import net.bussiness.tools.TimeUtils;
import net.bussiness.view.SubGridview;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.calendar.CalendarCell;
import com.ab.view.calendar.CalendarView;
import com.ab.view.progress.AbHorizontalProgressBar;
import com.ab.view.titlebar.AbTitleBar;
import com.ab.view.wheel.AbWheelUtil;
import com.ab.view.wheel.AbWheelView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class YwnrAddActivity extends AbActivity {
	private YwsqDto mYwsqDto;
	private NetworkUtils web;
	private AbHttpUtil mAbHttpUtil = null;

	private CalendarView mCalendarView = null;
	private EditText costEt = null;
	private EditText descEt = null;
	private TextView timeTv = null;

	private List<String> monthList = null;
	private int currentMonthIndex = 0;
	private TextView monthText = null;
	private String currentMonth = null;

	// 上传图片
	private ImageShowAdapter mImagePathAdapter = null;
	private SubGridview mGridView = null;
	private ArrayList<String> mPhotoList = null;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;

	/* 拍照的照片存储位置 */
	private File PHOTO_DIR = null;
	// 照相机拍照得到的图片
	private File mCurrentPhotoFile;
	private String mFileName;

	private int selectIndex = 0;
	private int camIndex = 0;
	private View mAvatarView = null;

	// 百度地图定位
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	private String mAddr = "";

	private AbHorizontalProgressBar mAbProgressBar;
	private TextView numberText, maxText;
	private DialogFragment mAlertDialog = null;
	private int max;
	private int successCnt;
	private int failCnt;

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			mAddr = location.getAddrStr();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.yw_detail_add);
		mYwsqDto = (YwsqDto) getIntent().getSerializableExtra("ywsqDto");
		web = NetworkUtils.newInstance(YwnrAddActivity.this);
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mPhotoList = new ArrayList<String>();
		mPhotoList.add(String.valueOf(R.drawable.cam_photo));
		mGridView = (SubGridview) findViewById(R.id.myGrid);
		mImagePathAdapter = new ImageShowAdapter(this, mPhotoList, 116, 116);
		mGridView.setAdapter(mImagePathAdapter);

		InitBDLoc();
		// 初始化图片保存路径
		String photo_dir = AbFileUtil.getImageDownloadDir(this);
		if (AbStrUtil.isEmpty(photo_dir)) {
			AbToastUtil.showToast(this, "存储卡不存在");
		} else {
			PHOTO_DIR = new File(photo_dir);
		}
		initTitleBar();
		initCalendar();
		initLable();
	}

	private void InitBDLoc() {
		// 声明LocationClient类
		mLocationClient = new LocationClient(getApplicationContext());
		// 注册监听函数
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		// 设置定位为高精度
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("gcj02");
		// 定位时间间隔为1秒
		option.setScanSpan(3000);
		// 设置反地理编码
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}

	private void initTitleBar() {
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("添加业务详细");
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
						YwnrDto ywnrDto = obtainYwnrDto();
						if (ywnrDto != null) {
							submitYwnrDto(ywnrDto);
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
		costEt = (EditText) findViewById(R.id.cost);
		descEt = (EditText) findViewById(R.id.desc);
		timeTv = (TextView) findViewById(R.id.time);
		timeTv.setText(new Date().getHours() + ":" + new Date().getMinutes());
		timeTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View mTimeView3 = mInflater.inflate(R.layout.choose_two, null);
				initWheelTime(mTimeView3, timeTv);
				AbDialogUtil.showDialog(mTimeView3, Gravity.BOTTOM);
			}
		});

		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectIndex = position;
				if (selectIndex == camIndex) {
					mAvatarView = mInflater.inflate(R.layout.choose_avatar,
							null);
					Button albumButton = (Button) mAvatarView
							.findViewById(R.id.choose_album);
					Button camButton = (Button) mAvatarView
							.findViewById(R.id.choose_cam);
					Button cancelButton = (Button) mAvatarView
							.findViewById(R.id.choose_cancel);
					albumButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							AbDialogUtil.removeDialog(YwnrAddActivity.this);
							// 从相册中去获取
							try {
								Intent intent = new Intent(
										Intent.ACTION_GET_CONTENT, null);
								intent.setType("image/*");
								startActivityForResult(intent,
										PHOTO_PICKED_WITH_DATA);
							} catch (ActivityNotFoundException e) {
								AbToastUtil.showToast(YwnrAddActivity.this,
										"没有找到照片");
							}
						}
					});

					camButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							AbDialogUtil.removeDialog(YwnrAddActivity.this);
							doPickPhotoAction();
						}
					});

					cancelButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							AbDialogUtil.removeDialog(YwnrAddActivity.this);
						}
					});
					AbDialogUtil.showDialog(mAvatarView, Gravity.BOTTOM);
				} else {
					for (int i = 0; i < mImagePathAdapter.getCount(); i++) {
						ImageShowAdapter.ViewHolder mViewHolder = (ImageShowAdapter.ViewHolder) mGridView
								.getChildAt(i).getTag();
						if (mViewHolder != null) {
							mViewHolder.mImageView2.setBackgroundDrawable(null);
						}
					}
					ImageShowAdapter.ViewHolder mViewHolder = (ImageShowAdapter.ViewHolder) view
							.getTag();
					mViewHolder.mImageView2
							.setBackgroundResource(R.drawable.photo_select);
				}
			}
		});
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

	/**
	 * 从照相机获取
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		// 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			AbToastUtil.showToast(YwnrAddActivity.this, "没有可用的存储卡");
		}
	}

	/**
	 * 拍照获取图片
	 */
	private void doTakePhoto() {
		try {
			mFileName = System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			AbToastUtil.showToast(YwnrAddActivity.this, "未找到系统相机程序");
		}
	}

	/**
	 * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			Uri uri = mIntent.getData();
			String currentFilePath = getPath(uri);
			if (!AbStrUtil.isEmpty(currentFilePath)) {
				Intent intent1 = new Intent(this, CropImageActivity.class);
				intent1.putExtra("PATH", currentFilePath);
				startActivityForResult(intent1, CAMERA_CROP_DATA);
			} else {
				AbToastUtil.showToast(YwnrAddActivity.this, "未在存储卡中找到这个文件");
			}
			break;
		case CAMERA_WITH_DATA:
			AbLogUtil.d(YwnrAddActivity.class, "将要进行裁剪的图片的路径是 = "
					+ mCurrentPhotoFile.getPath());
			String currentFilePath2 = mCurrentPhotoFile.getPath();
			Intent intent2 = new Intent(this, CropImageActivity.class);
			intent2.putExtra("PATH", currentFilePath2);
			startActivityForResult(intent2, CAMERA_CROP_DATA);
			break;
		case CAMERA_CROP_DATA:
			String path = mIntent.getStringExtra("PATH");
			AbLogUtil.d(YwnrAddActivity.class, "裁剪后得到的图片的路径是 = " + path);
			mImagePathAdapter.addItem(mImagePathAdapter.getCount() - 1, path);
			camIndex++;
			break;
		}
	}

	@Override
	public void finish() {
		for (int i = 0; i < mPhotoList.size() - 1; i++)
			AbFileUtil.deleteFile(new File(mPhotoList.get(i)));
		super.finish();
	}

	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if (AbStrUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	private void submitYwnrDto(YwnrDto ywnrDto) {
		AbRequestParams params = new AbRequestParams();
		params.put("ywnrDto", JacksonUtils.bean2Json(ywnrDto));
		web.post(ConstServer.YWNR_ADD(
				((IApplication) abApplication).mUser.getUserId(),
				mYwsqDto.getYwId()), params, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				if (!content.equals("0")) {
					if (mPhotoList.size() - 1 == 0) {
						dismissAlertDialog();
						AbToastUtil.showToast(YwnrAddActivity.this, "提交成功!");
						finish();
					} else {
						uploadPic(Integer.parseInt(content));
					}
				} else {
					AbToastUtil.showToast(YwnrAddActivity.this, "提交业务详细失败!");
					dismissAlertDialog();
				}
			}

			@Override
			public void onStart() {
				View v = LayoutInflater.from(YwnrAddActivity.this).inflate(
						R.layout.progress_bar_horizontal, null, false);
				mAbProgressBar = (AbHorizontalProgressBar) v
						.findViewById(R.id.horizontalProgressBar);
				numberText = (TextView) v.findViewById(R.id.numberText);
				maxText = (TextView) v.findViewById(R.id.maxText);
				max = mPhotoList.size() - 1;
				successCnt = 0;
				failCnt = 0;
				maxText.setText(0 + "/" + String.valueOf(max));
				mAbProgressBar.setMax(100);
				mAbProgressBar.setProgress(0);

				mAlertDialog = AbDialogUtil.showAlertDialog("正在上传", v);
				mAlertDialog.setCancelable(false);
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(YwnrAddActivity.this, "提交业务详细失败!");
				dismissAlertDialog();
			}
		});
	}

	private void dismissAlertDialog() {
		if (mAlertDialog != null) {
			mAlertDialog.dismiss();
			mAlertDialog = null;
		}
	}

	private YwnrDto obtainYwnrDto() {
		if ("".equals(mAddr)) {
			AbToastUtil.showToast(YwnrAddActivity.this, "定位失败，请返回再试！");
			return null;
		}
		YwnrDto ywnrDto = new YwnrDto();
		ywnrDto.setNrLocation(mAddr);
		ywnrDto.setYwsq(mYwsqDto);
		ywnrDto.setDescription(descEt.getText().toString().trim());
		try {
			ywnrDto.setFee(Integer.parseInt(costEt.getText().toString().trim()));
		} catch (Exception e) {
			AbToastUtil.showToast(YwnrAddActivity.this, "请输入正确的花费格式！");
			return null;
		}
		try {
			ywnrDto.setNrTime(TimeUtils.parseStringInDate(
					mCalendarView.getCalSelected() + " " + timeTv.getText(),
					TimeUtils.DATE_TIME_PATTERN));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ywnrDto;
	}

	private void uploadPic(int ywnrId) {
		try {
			for (int i = 0; i < mPhotoList.size() - 1; i++) {
				AbRequestParams params = new AbRequestParams();
				params.put("file", new File(mPhotoList.get(i)));
				System.err.println(mYwsqDto.getUserByProposerId().getUserId()
						+ " " + mYwsqDto.getYwId() + " " + ywnrId);
				mAbHttpUtil.post(ConstServer.YWNR_PHOTO_UPLOADPHOTO(mYwsqDto
						.getUserByProposerId().getUserId(), mYwsqDto.getYwId(),
						ywnrId), params, new AbStringHttpResponseListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
						if (successCnt + failCnt == max) {
							dismissAlertDialog();
							AbToastUtil
									.showToast(YwnrAddActivity.this, "提交成功!");
							finish();
						}
					}

					@Override
					public void onProgress(int bytesWritten, int totalSize) {
						mAbProgressBar.setProgress(bytesWritten
								/ (totalSize / 100));
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						successCnt++;
						maxText.setText(successCnt + "/" + max);
						mAbProgressBar.setProgress(0);
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						failCnt++;
						AbToastUtil.showToast(YwnrAddActivity.this, content);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initCalendar() {
		LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.layout01_cal);
		mCalendarView = new CalendarView(YwnrAddActivity.this);
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
						AbToastUtil.showToast(YwnrAddActivity.this, "你选择了"
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
}
