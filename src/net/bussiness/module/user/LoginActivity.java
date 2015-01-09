package net.bussiness.module.user;

import net.bussiness.activities.R;
import net.bussiness.dto.UserDto;
import net.bussiness.module.base.NavActivity;
import net.bussiness.tools.ConstServer;
import net.bussiness.tools.IApplication;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkWeb;
import net.bussiness.tools.StringUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbToastUtil;

public class LoginActivity extends AbActivity implements OnClickListener {
	private EditText userIdEt;
	private EditText userPwdEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		UserDto currentUser = getUserfromSharedPreference();
		if (currentUser != null) {
			IApplication iApplication = (IApplication) getApplication();
			iApplication.setCurrentUser(currentUser);
			login(currentUser.getUserId() + "", currentUser.getPassword(), true);
			startActivity(new Intent(LoginActivity.this, NavActivity.class));
			finish();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		this.setFinishOnTouchOutside(false);
		userIdEt = (EditText) findViewById(R.id.userId);
		userPwdEt = (EditText) findViewById(R.id.userPwd);
		findViewById(R.id.clearId).setOnClickListener(this);
		findViewById(R.id.clearPwd).setOnClickListener(this);
		findViewById(R.id.pwdBtn).setOnClickListener(this);
		findViewById(R.id.loginBtn).setOnClickListener(this);
		findViewById(R.id.cancelBtn).setOnClickListener(this);
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
				AbToastUtil.showToast(LoginActivity.this, "请输入正确的员工编号！");
				return;
			}
			login(userId, pwd, true);
			break;
		case R.id.cancelBtn:
			System.exit(0);
			break;
		}
	}

	private void login(String userId, String pwd, final boolean isFirst) {
		NetworkWeb web = NetworkWeb.newInstance(LoginActivity.this);
		AbRequestParams params = new AbRequestParams();
		params.put("userId", userId);
		params.put("password", pwd);
		web.post(ConstServer.LOGIN(), params, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if (isFirst) {
					UserDto currentUser = (UserDto) JacksonUtils.json2Bean(
							content, UserDto.class);
					if (currentUser == null) {
						AbToastUtil
								.showToast(LoginActivity.this, "员工编号和密码不匹配！");
					} else {
						IApplication iApplication = (IApplication) getApplication();
						iApplication.setCurrentUser(currentUser);
						if (((CheckBox) findViewById(R.id.login_check))
								.isChecked())
							save2SharedPreference(currentUser);
						startActivity(new Intent(LoginActivity.this,
								NavActivity.class));
						finish();
					}
				}
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(LoginActivity.this, content);
			}
		});
	}

	private UserDto getUserfromSharedPreference() {
		UserDto currentUser = null;
		int userId = AbSharedUtil.getInt(LoginActivity.this, "userId");
		if (userId != 0) {
			currentUser = new UserDto();
			currentUser.setUserId(userId);
			currentUser.setPassword(AbSharedUtil.getString(LoginActivity.this,
					"pwd"));
		}
		return currentUser;
	}

	private void save2SharedPreference(UserDto currentUser) {
		AbSharedUtil.putInt(LoginActivity.this, "userId",
				currentUser.getUserId());
		AbSharedUtil.putString(LoginActivity.this, "pwd",
				currentUser.getPassword());
	}
}