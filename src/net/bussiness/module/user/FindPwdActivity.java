package net.bussiness.module.user;

import net.bussiness.activities.R;
import net.bussiness.dto.UserDto;
import net.bussiness.tools.StringUtils;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ab.activity.AbActivity;
import com.ab.util.AbToastUtil;

public class FindPwdActivity extends AbActivity implements OnClickListener {
	private EditText userIdEt;
	private EditText emailEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_pwd);
		this.setFinishOnTouchOutside(false);
		userIdEt = (EditText) findViewById(R.id.userId);
		emailEt = (EditText) findViewById(R.id.email);
		findViewById(R.id.clearId).setOnClickListener(this);
		findViewById(R.id.clearEmail).setOnClickListener(this);
		findViewById(R.id.findPwdBtn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clearId:
			userIdEt.setText("");
			break;
		case R.id.clearEmail:
			emailEt.setText("");
			break;
		case R.id.findPwdBtn:
			UserDto user = new UserDto();
			String userId = userIdEt.getText().toString();
			String email = emailEt.getText().toString();
			if (StringUtils.isBlank(userId) || StringUtils.isBlank(email)) {
				AbToastUtil.showToast(getApplicationContext(), "员工编号或邮箱不能为空！");
				return;
			}
			try {
				user.setUserId(Integer.parseInt(userId));
			} catch (Exception e) {
				AbToastUtil.showToast(getApplicationContext(), "请输入正确的员工编号！");
				return;
			}
			user.setEmail(email);
			finish();
			break;
		}
	}
}
