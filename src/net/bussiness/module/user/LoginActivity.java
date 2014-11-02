package net.bussiness.module.user;

import net.bussiness.activities.R;
import net.bussiness.dao.UserDao;
import net.bussiness.module.base.NavActivity;
<<<<<<< HEAD
import net.bussiness.tools.ConstServer;
import net.bussiness.tools.IApplication;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkWeb;
=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
import net.bussiness.tools.StringUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
<<<<<<< HEAD
import android.widget.CheckBox;
import android.widget.EditText;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbSharedUtil;
=======
import android.widget.EditText;

import com.ab.activity.AbActivity;
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
import com.ab.util.AbToastUtil;

public class LoginActivity extends AbActivity implements OnClickListener {
	private EditText userIdEt;
	private EditText userPwdEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
<<<<<<< HEAD
		UserDao currentUser = getUserfromSharedPreference();
		if (currentUser != null) {
			IApplication iApplication = (IApplication) getApplication();
			iApplication.setCurrentUser(currentUser);
			startActivity(new Intent(LoginActivity.this, NavActivity.class));
			finish();
		}
=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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
<<<<<<< HEAD
=======
	public void onBackPressed() {
	}

	@Override
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
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
<<<<<<< HEAD
			String userId = userIdEt.getText().toString().trim();
			String pwd = userPwdEt.getText().toString().trim();
			if (StringUtils.isBlank(userId) || StringUtils.isBlank(pwd)) {
				AbToastUtil.showToast(LoginActivity.this, "Ա����Ż����벻��Ϊ�գ�");
				return;
			}
			try {
				Integer.parseInt(userId);
			} catch (Exception e) {
				AbToastUtil.showToast(LoginActivity.this, "��������ȷ��Ա����ţ�");
				return;
			}
			login(userId, pwd);
=======
			UserDao user = new UserDao();
			String userId = userIdEt.getText().toString();
			String pwd = userPwdEt.getText().toString();
			if (StringUtils.isBlank(userId) || StringUtils.isBlank(pwd)) {
				AbToastUtil.showToast(getApplicationContext(), "Ա����Ż����벻��Ϊ�գ�");
				return;
			}
			try {
				user.setUserId(Integer.parseInt(userId));
			} catch (Exception e) {
				AbToastUtil.showToast(getApplicationContext(), "��������ȷ��Ա����ţ�");
				return;
			}
			user.setPassword(pwd);
			startActivity(new Intent(LoginActivity.this, NavActivity.class));
			finish();
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
			break;
		case R.id.cancelBtn:
			System.exit(0);
			break;
		}
	}
<<<<<<< HEAD

	private void login(String userId, String pwd) {
		NetworkWeb web = NetworkWeb.newInstance(LoginActivity.this);
		AbRequestParams params = new AbRequestParams();
		params.put("userId", userId);
		params.put("password", pwd);
		web.post(ConstServer.LOGIN(), params, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				UserDao currentUser = (UserDao) JacksonUtils.json2Bean(content,
						UserDao.class);
				if (currentUser == null) {
					AbToastUtil.showToast(LoginActivity.this, "Ա����ź����벻ƥ�䣡");
				} else {
					IApplication iApplication = (IApplication) getApplication();
					iApplication.setCurrentUser(currentUser);
					if (((CheckBox) findViewById(R.id.login_check)).isChecked())
						save2SharedPreference(currentUser);
					startActivity(new Intent(LoginActivity.this,
							NavActivity.class));
					finish();
				}
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(LoginActivity.this, content);
			}
		});
	}

	private UserDao getUserfromSharedPreference() {
		UserDao currentUser = null;
		int userId = AbSharedUtil.getInt(LoginActivity.this, "userId");
		if (userId != 0) {
			currentUser = new UserDao();
			currentUser.setUserId(userId);
			currentUser.setPassword(AbSharedUtil.getString(LoginActivity.this,
					"pwd"));
		}
		return currentUser;
	}

	private void save2SharedPreference(UserDao currentUser) {
		AbSharedUtil.putInt(LoginActivity.this, "userId",
				currentUser.getUserId());
		AbSharedUtil.putString(LoginActivity.this, "pwd",
				currentUser.getPassword());
	}
=======
>>>>>>> 3a533ba27428b86a95b76152af51d97058d0c69f
}