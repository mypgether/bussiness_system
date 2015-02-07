package net.bussiness.module.user;

import net.bussiness.activities.R;
import net.bussiness.dto.UserDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.module.base.NavActivity;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import net.bussiness.tools.StringUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbDialogFragment.AbDialogOnLoadListener;
import com.ab.fragment.AbProgressDialogFragment;
import com.ab.fragment.AbRefreshDialogFragment;
import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;

public class LoginActivity extends AbActivity implements OnClickListener {
	private EditText userIdEt;
	private EditText userPwdEt;
	private IApplication iApp = null;
	private AbRefreshDialogFragment mRefreshFragment = null;
	private AbProgressDialogFragment mProgressFragment = null;
	private UserDto mUserDto = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		iApp = (IApplication) getApplication();
		mUserDto = iApp.mUser;
		if (mUserDto != null) {
			mProgressFragment = AbDialogUtil.showProgressDialog(this, 0,
					"登录中，请稍后...");
			mProgressFragment.setCancelable(false);
			login(mUserDto.getUserId() + "", mUserDto.getPassword(), true);
		} else
			mUserDto = new UserDto();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		this.setFinishOnTouchOutside(false);
		userIdEt = (EditText) findViewById(R.id.userId);
		userPwdEt = (EditText) findViewById(R.id.userPwd);
		findViewById(R.id.clearId).setOnClickListener(this);
		findViewById(R.id.clearPwd).setOnClickListener(this);
		findViewById(R.id.pwdBtn).setOnClickListener(this);
		findViewById(R.id.loginBtn).setOnClickListener(this);
		findViewById(R.id.registerBtn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clearId:
			userIdEt.setText("");
			break;
		case R.id.clearPwd:
			userPwdEt.setText("");
			break;
		case R.id.pwdBtn:
			startActivity(new Intent(LoginActivity.this, FindPwdActivity.class));
			break;
		case R.id.loginBtn:
			String userId = userIdEt.getText().toString().trim();
			String pwd = userPwdEt.getText().toString().trim();
			if (StringUtils.isBlank(userId) || StringUtils.isBlank(pwd)) {
				AbToastUtil.showToast(LoginActivity.this, "员工编号或密码不能为空！");
				return;
			}
			try {
				Integer.parseInt(userId);
			} catch (Exception e) {
				AbToastUtil.showToast(LoginActivity.this, "请输入正确的员工编号格式！");
				return;
			}
			mUserDto.setUserId(Integer.parseInt(userId));
			mUserDto.setPassword(pwd);
			login(userId, pwd, false);
			break;
		case R.id.registerBtn:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		}
	}

	private void login(String userId, String pwd, final boolean fromSP) {
		mProgressFragment = AbDialogUtil.showProgressDialog(this, 0,
				"登录中，请稍后...");
		mProgressFragment.setCancelable(false);
		NetworkUtils web = NetworkUtils.newInstance(LoginActivity.this);
		AbRequestParams params = new AbRequestParams();
		params.put("userId", userId);
		params.put("password", pwd);
		web.post(ConstServer.LOGIN(), params, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				if (mRefreshFragment != null)
					mRefreshFragment.loadFinish();
				if (null != mProgressFragment)
					mProgressFragment.dismiss();
				UserDto currentUser = (UserDto) JacksonUtils.json2Bean(content,
						UserDto.class);
				if (currentUser == null) {
					AbToastUtil.showToast(LoginActivity.this, "员工编号和密码不匹配！");
					if (fromSP) {
						iApp.clearSharedPreference();
						System.exit(0);
					}
				} else {
					mUserDto = currentUser;
					iApp.mUserIdentity = (currentUser.getPosition().getId() == 1) ? 1
							: 2;
					iApp.mUser = currentUser;
					if (fromSP
							|| ((CheckBox) findViewById(R.id.login_check))
									.isChecked()) {
						iApp.save2SharedPreference(currentUser,
								iApp.mUserIdentity);
					}
					startActivity(new Intent(LoginActivity.this,
							NavActivity.class));
					finish();
				}
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(LoginActivity.this, content);
				if (null != mProgressFragment)
					mProgressFragment.dismiss();
				mRefreshFragment = AbDialogUtil.showRefreshDialog(
						LoginActivity.this, R.drawable.ic_refresh,
						"登录失败，请重试...", new AbDialogOnLoadListener() {
							@Override
							public void onLoad() {
								login(mUserDto.getUserId() + "",
										mUserDto.getPassword(), true);
							}
						});
				mRefreshFragment.setCancelable(false);
			}
		});
	}
}