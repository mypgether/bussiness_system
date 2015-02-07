package net.bussiness.module.user;

import net.bussiness.activities.R;
import net.bussiness.global.ConstServer;
import net.bussiness.tools.NetworkUtils;
import net.bussiness.tools.StringUtils;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpListener;
import com.ab.util.AbToastUtil;

public class FindPwdActivity extends AbActivity implements OnClickListener {
	private EditText userIdEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_pwd);
		this.setFinishOnTouchOutside(false);
		userIdEt = (EditText) findViewById(R.id.userId);
		findViewById(R.id.clearId).setOnClickListener(this);
		findViewById(R.id.findPwdBtn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clearId:
			userIdEt.setText("");
			break;
		case R.id.findPwdBtn:
			String userId = userIdEt.getText().toString().trim();
			if (StringUtils.isBlank(userId)) {
				AbToastUtil.showToast(getApplicationContext(), "员工编号不能为空！");
				return;
			}
			int mUserId = 0;
			try {
				mUserId = Integer.parseInt(userId);
			} catch (Exception e) {
				AbToastUtil.showToast(getApplicationContext(), "请输入正确的员工编号！");
				return;
			}
			NetworkUtils net = NetworkUtils.newInstance(FindPwdActivity.this);
			net.post(ConstServer.USER_FINDPWD(mUserId), null,
					new AbHttpListener() {
						@Override
						public void onSuccess(String content) {
							System.out.println("USER_FINDPWD" + content);
							if (content.equals("0")) {
								AbToastUtil.showToast(FindPwdActivity.this,
										"该账号没有填写注册邮箱！");
								finish();
							} else if (content.equals("1")) {
								AbToastUtil.showToast(FindPwdActivity.this,
										"密码找回邮件已经发送至注册邮箱，请查收！");
								finish();
							} else {
								AbToastUtil.showToast(FindPwdActivity.this,
										"绑定出错，请重新再试！");
							}
						}

						@Override
						public void onFailure(String content) {
							AbToastUtil
									.showToast(FindPwdActivity.this, content);
						}
					});
			break;
		}
	}
}
