package net.bussiness.module.user;

import java.io.File;

import net.bussiness.activities.R;
import net.bussiness.dto.UserDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.module.base.CropImageActivity;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.titlebar.AbTitleBar;

public class EditProfileActivity extends AbActivity {
	private IApplication iApp;
	private UserDto mUserDto;
	private ImageView iv1;
	private EditText et2;
	private EditText et3;
	private EditText et4;
	private EditText et5;
	private View mAvatarView = null;

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
	private String photoPath = null;
	private boolean isError = true;
	private boolean isSubmitting = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.user_edit_profile);
		initTitleBar();
		iApp = ((IApplication) getApplication());
		mUserDto = iApp.mUser;

		iv1 = (ImageView) findViewById(R.id.user_logo);
		et2 = (EditText) findViewById(R.id.user_name);
		et3 = (EditText) findViewById(R.id.user_desc);
		et4 = (EditText) findViewById(R.id.user_tel);
		et5 = (EditText) findViewById(R.id.user_email);
		initIoc();
		refreshView();
	}

	private void initIoc() {
		findViewById(R.id.modifyPwdBtn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(EditProfileActivity.this,
								EditPwdActivity.class));
					}
				});
		et5.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!(et5.getText().toString()
						.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") && s.length() > 0)) {
					et5.setError("请输入正确的邮箱格式");
					isError = true;
				} else {
					isError = false;
				}
			}
		});
		iv1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAvatarView = mInflater.inflate(R.layout.choose_avatar, null);
				Button albumButton = (Button) mAvatarView
						.findViewById(R.id.choose_album);
				Button camButton = (Button) mAvatarView
						.findViewById(R.id.choose_cam);
				Button cancelButton = (Button) mAvatarView
						.findViewById(R.id.choose_cancel);
				albumButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AbDialogUtil.removeDialog(EditProfileActivity.this);
						// 从相册中去获取
						try {
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT, null);
							intent.setType("image/*");
							startActivityForResult(intent,
									PHOTO_PICKED_WITH_DATA);
						} catch (ActivityNotFoundException e) {
							AbToastUtil.showToast(EditProfileActivity.this,
									"没有找到照片");
						}
					}
				});

				camButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AbDialogUtil.removeDialog(EditProfileActivity.this);
						doPickPhotoAction();
					}
				});

				cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AbDialogUtil.removeDialog(EditProfileActivity.this);
					}
				});
				AbDialogUtil.showDialog(mAvatarView, Gravity.BOTTOM);
			}
		});
	}

	private void refreshView() {
		System.out.println(mUserDto.toString());
		((IApplication) getApplication()).mAbImageLoader.display(
				iv1,
				ConstServer.USER_DOWNLOADLOGO(mUserDto.getUserId(),
						mUserDto.getPhotoPath()));
		if (mUserDto.getUserName() == null) {
			et2.setText("请点击添加姓名");
		} else {
			et2.setText(mUserDto.getUserName());
		}
		if (mUserDto.getDescription() == null) {
			et3.setText("请点击添加简介");
		} else {
			et3.setText(mUserDto.getDescription());
		}
		if (mUserDto.getTel() == null) {
			et4.setText("请点击添加联系方式");
		} else {
			et4.setText(mUserDto.getTel());
		}
		if (mUserDto.getEmail() == null) {
			et5.setText("请点击添加邮箱");
		} else {
			et5.setText(mUserDto.getEmail());
		}
	}

	private void initTitleBar() {
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("我的资料");
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

		View rightViewMore = LayoutInflater.from(this).inflate(
				R.layout.save_btn, null);
		mAbTitleBar.addRightView(rightViewMore);
		Button saveBtn = (Button) rightViewMore.findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isError) {
					AbToastUtil.showToast(EditProfileActivity.this,
							"请输入正确的邮箱格式");
					return;
				}
				if (isSubmitting) {
					AbToastUtil.showToast(EditProfileActivity.this,
							"已经在保存了，请稍后...");
					return;
				}
				AbToastUtil.showToast(EditProfileActivity.this, "正在保存，请稍后...");
				uploadUser();
			}
		});
	}

	private File mFile = null;

	private void uploadPhoto() {
		isSubmitting = true;
		try {
			mFile = new File(photoPath);
		} catch (Exception e) {
			e.printStackTrace();
			isSubmitting = false;
			AbToastUtil.showToast(EditProfileActivity.this, "头像无法找到，请重新再试！");
			return;
		}
		AbRequestParams params2 = new AbRequestParams();
		params2.put("file", mFile);
		NetworkUtils net = NetworkUtils.newInstance(EditProfileActivity.this);
		net.post(ConstServer.USER_UPLOADLOGO(mUserDto.getUserId()), params2,
				new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						isSubmitting = false;
						if (content != null) {
							iApp.mUser.setPhotoPath(content);
							AbToastUtil.showToast(EditProfileActivity.this,
									"保存成功！");
							finish();
						} else {
							AbToastUtil.showToast(EditProfileActivity.this,
									"保存头像出错...");
						}
						AbFileUtil.deleteFile(mFile);
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(EditProfileActivity.this,
								"保存头像出错...");
						isSubmitting = false;
						AbFileUtil.deleteFile(mFile);
					}
				});
	}

	private void uploadUser() {
		isSubmitting = true;
		UserDto user = obtainUserDto();
		if (user != null) {
			AbRequestParams params1 = new AbRequestParams();
			params1.put("userDto", JacksonUtils.bean2Json(user));
			NetworkUtils net = NetworkUtils
					.newInstance(EditProfileActivity.this);
			net.post(ConstServer.USER_UPDATE(), params1, new AbHttpListener() {
				@Override
				public void onSuccess(String content) {
					iApp.mUser.setDescription(et3.getText().toString().trim());
					iApp.mUser.setTel(et4.getText().toString().trim());
					iApp.mUser.setEmail(et5.getText().toString().trim());
					if (null == photoPath) {
						AbToastUtil
								.showToast(EditProfileActivity.this, "保存成功！");
						isSubmitting = false;
						finish();
					} else {
						uploadPhoto();
					}
				}

				@Override
				public void onFailure(String content) {
					AbToastUtil.showToast(EditProfileActivity.this, content);
					isSubmitting = false;
					if (photoPath != null) {
						uploadPhoto();
					}
				}
			});
		} else if (photoPath != null) {
			uploadPhoto();
		} else {
			AbToastUtil.showToast(EditProfileActivity.this, "没有任何改动...");
			isSubmitting = false;
		}
	}

	private UserDto obtainUserDto() {
		UserDto user = new UserDto();
		user.setId(mUserDto.getId());
		user.setUserId(mUserDto.getUserId());
		if (et3.getText().toString().trim().equals(mUserDto.getDescription())
				&& et4.getText().toString().trim().equals(mUserDto.getTel())
				&& et5.getText().toString().trim().equals(mUserDto.getEmail())) {
			return null;
		}
		user.setDescription(et3.getText().toString().trim());
		user.setTel(et4.getText().toString().trim());
		user.setEmail(et5.getText().toString().trim());
		return user;
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
			AbToastUtil.showToast(EditProfileActivity.this, "没有可用的存储卡");
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
			AbToastUtil.showToast(EditProfileActivity.this, "未找到系统相机程序");
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
				AbToastUtil.showToast(EditProfileActivity.this, "未在存储卡中找到这个文件");
			}
			break;
		case CAMERA_WITH_DATA:
			AbLogUtil.d(EditProfileActivity.class, "将要进行裁剪的图片的路径是 = "
					+ mCurrentPhotoFile.getPath());
			String currentFilePath2 = mCurrentPhotoFile.getPath();
			Intent intent2 = new Intent(this, CropImageActivity.class);
			intent2.putExtra("PATH", currentFilePath2);
			startActivityForResult(intent2, CAMERA_CROP_DATA);
			break;
		case CAMERA_CROP_DATA:
			photoPath = mIntent.getStringExtra("PATH");
			AbLogUtil.d(EditProfileActivity.class, "裁剪后得到的图片的路径是 = "
					+ photoPath);
			Bitmap mBitmap = AbFileUtil.getBitmapFromSD(new File(photoPath));
			if (mBitmap != null) {
				iv1.setImageBitmap(mBitmap);
			} else {
				iv1.setImageResource(R.drawable.image_empty);
			}
			break;
		}
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
}
