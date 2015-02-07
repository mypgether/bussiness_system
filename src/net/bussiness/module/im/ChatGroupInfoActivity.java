package net.bussiness.module.im;

import net.bussiness.activities.R;
import net.bussiness.dialog.lib.Effectstype;
import net.bussiness.dialog.lib.NiftyDialogBuilder;
import net.bussiness.dto.ConfigDto;
import net.bussiness.dto.DeptDto;
import net.bussiness.dto.UserDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbToastUtil;
import com.ab.view.sliding.AbSlidingButton;
import com.ab.view.titlebar.AbTitleBar;

public class ChatGroupInfoActivity extends AbActivity {
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;

	private Button btn1;
	private AbSlidingButton slb;

	private DeptDto mDept;
	private IApplication iApp;
	private ConfigDto config;
	private NetworkUtils net;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.im_chat_group_info);
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("部门详细");
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
		iApp = (IApplication) getApplication();
		intent = new Intent();
		intent.putExtra("isClear", false);
		String json = iApp.getConfigfromSharedPreference();
		if (json == null || "".equals(json)) {
			config = new ConfigDto();
		} else {
			config = (ConfigDto) JacksonUtils.json2Bean(json, ConfigDto.class);
		}
		net = NetworkUtils.newInstance(this);
		initView();
	}

	public void initView() {
		mDept = (DeptDto) getIntent().getSerializableExtra("mDept");
		tv1 = (TextView) findViewById(R.id.dept_name);
		tv2 = (TextView) findViewById(R.id.creater_name);
		tv3 = (TextView) findViewById(R.id.create_time);
		tv4 = (TextView) findViewById(R.id.dept_desc);

		btn1 = (Button) findViewById(R.id.clearChatingMsg);
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
						.getInstance(ChatGroupInfoActivity.this);
				dialogBuilder
						.withTitleColor("#FFFFFF")
						.withDividerColor("#11000000")
						.withMessage("确认删除在" + mDept.getDeptName() + "中的聊天记录吗?")
						.withMessageColor("#FFFFFFFF")
						.withDialogColor("#FFE74C3C")
						.withIcon(
								getResources().getDrawable(
										R.drawable.ic_launcher))
						.withDuration(100) // def
						.withEffect(Effectstype.Slidetop) // def
						.withButton1Text("确定") // def gone
						.withButton2Text("取消") // def gone
						.setButton1Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								clearIMs();
								dialogBuilder.dismiss();
							}
						}).setButton2Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialogBuilder.dismiss();
							}
						}).show();
			}
		});
		slb = (AbSlidingButton) findViewById(R.id.mSliderBtn1);
		slb.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
				R.drawable.btn_mask, R.drawable.btn_unpressed,
				R.drawable.btn_pressed);

		tv1.setText(mDept.getDeptName());
		tv3.setText("创建时间:" + mDept.getCreateTime().toLocaleString());
		tv4.setText(mDept.getDescription());

		slb.setChecked(config.isBlockGroupMsg());
		net.post(ConstServer.DEPT_FINDDEPTCREATER(mDept.getCreaterId()), null,
				new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						UserDto user = (UserDto) JacksonUtils.json2Bean(
								content, UserDto.class);
						tv2.setText("创建人:" + user.getUserName());
					}

					@Override
					public void onFailure(String content) {
						tv2.setText("");
					}
				});
	}

	private void clearIMs() {
		AbRequestParams params = new AbRequestParams();
		params.put("groupId", mDept.getId() + "");
		net.post(ConstServer.IM_GROUP_CLEAR(iApp.mUser.getUserId()), params,
				new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						AbToastUtil.showToast(ChatGroupInfoActivity.this,
								"清除聊天记录成功!");
						intent.putExtra("isClear", true);
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(ChatGroupInfoActivity.this,
								content);
					}
				});
	}

	@Override
	public void finish() {
		config.setBlockGroupMsg(slb.isChecked());
		iApp.saveConfig2SharedPreference(JacksonUtils.bean2Json(config));
		setResult(0, intent);
		super.finish();
	}
}
