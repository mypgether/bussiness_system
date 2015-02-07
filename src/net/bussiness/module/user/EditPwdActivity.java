package net.bussiness.module.user;

import net.bussiness.activities.R;
import net.bussiness.dto.UserDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbToastUtil;
import com.ab.view.titlebar.AbTitleBar;

public class EditPwdActivity extends AbActivity {
	private IApplication iApp;
	private EditText et3;
	private EditText et4;
	private EditText et5;
	private boolean isSubmitting = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.user_edit_pwd);
		initTitleBar();
		iApp = (IApplication) getApplication();
		et3 = (EditText) findViewById(R.id.user_old_pwd);
		et4 = (EditText) findViewById(R.id.user_new_pwd);
		et5 = (EditText) findViewById(R.id.user_confirm_new_pwd);
	}

	private void initTitleBar() {
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("修改密码");
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
				if (isSubmitting) {
					AbToastUtil
							.showToast(EditPwdActivity.this, "已经在提交了，请稍后...");
					return;
				}
				final UserDto newUser = obtainUserDto();
				if (newUser != null) {
					AbToastUtil.showToast(EditPwdActivity.this, "正在提交，请稍后...");
					isSubmitting = true;
					AbRequestParams params1 = new AbRequestParams();
					params1.put("userDto", JacksonUtils.bean2Json(newUser));
					NetworkUtils net = NetworkUtils
							.newInstance(EditPwdActivity.this);
					net.post(ConstServer.USER_UPDATE(), params1,
							new AbHttpListener() {
								@Override
								public void onSuccess(String content) {
									iApp.mUser.setPassword(newUser
											.getPassword());
									AbToastUtil.showToast(EditPwdActivity.this,
											"保存成功！");
									iApp.save2SharedPreference(iApp.mUser,
											iApp.mUserIdentity);
									isSubmitting = false;
									finish();
								}

								@Override
								public void onFailure(String content) {
									AbToastUtil.showToast(EditPwdActivity.this,
											content);
									isSubmitting = false;
								}
							});
				} else {
					isSubmitting = false;
				}
			}
		});
	}

	private UserDto obtainUserDto() {
		UserDto newUser = iApp.mUser;
		String oldPwd = et3.getText().toString().trim();
		String newPwd = et4.getText().toString().trim();
		String confirmNewPwd = et5.getText().toString().trim();
		if (iApp.mUser.getPassword().equals(oldPwd)) {
			if (confirmNewPwd.equals(newPwd)) {
				newUser.setPassword(newPwd);
				return newUser;
			} else {
				AbToastUtil.showToast(EditPwdActivity.this, "两次输入的密码不一致！");
			}
		} else {
			AbToastUtil.showToast(EditPwdActivity.this, "输入密码与原密码不一致！");
		}
		return null;
	}
}
