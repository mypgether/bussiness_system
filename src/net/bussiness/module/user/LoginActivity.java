package net.bussiness.module.user;

import net.bussiness.activities.R;
import net.bussiness.dao.UserDao;
import net.bussiness.module.base.NavActivity;
import net.bussiness.tools.StringUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ab.activity.AbActivity;
import com.ab.util.AbToastUtil;

public class LoginActivity extends AbActivity implements OnClickListener {
	private EditText userIdEt;
	private EditText userPwdEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
	public void onBackPressed() {
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
			UserDao user = new UserDao();
			String userId = userIdEt.getText().toString();
			String pwd = userPwdEt.getText().toString();
			if (StringUtils.isBlank(userId) || StringUtils.isBlank(pwd)) {
				AbToastUtil.showToast(getApplicationContext(), "员工编号或密码不能为空！");
				return;
			}
			try {
				user.setUserId(Integer.parseInt(userId));
			} catch (Exception e) {
				AbToastUtil.showToast(getApplicationContext(), "请输入正确的员工编号！");
				return;
			}
			user.setPassword(pwd);
			startActivity(new Intent(LoginActivity.this, NavActivity.class));
			finish();
			break;
		case R.id.cancelBtn:
			System.exit(0);
			break;
		}
	}
}
