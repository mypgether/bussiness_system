package net.bussiness.module.im;

import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.dialog.lib.Effectstype;
import net.bussiness.dialog.lib.NiftyDialogBuilder;
import net.bussiness.dto.IMMsgIgnoreUserDto;
import net.bussiness.dto.UserDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.module.user.DetailProfileActivity;
import net.bussiness.tools.NetworkUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.db.storage.AbSqliteStorageListener.AbDataSelectListener;
import com.ab.db.storage.AbStorageQuery;
import com.ab.http.AbHttpListener;
import com.ab.util.AbToastUtil;
import com.ab.view.sliding.AbSlidingButton;
import com.ab.view.titlebar.AbTitleBar;

public class ChatInfoActivity extends AbActivity {
	private ImageView iv1;
	private TextView tv1;
	private TextView tv2;

	private Button btn1;
	private AbSlidingButton slb;

	private UserDto mUserDto;
	private IApplication iApp;
	private NetworkUtils net;

	private boolean firstIsChecked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.im_chatinfo);
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("聊天详细");
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
		net = NetworkUtils.newInstance(this);
		initView();
	}

	public void initView() {
		mUserDto = (UserDto) getIntent().getSerializableExtra("mUserDto");
		iv1 = (ImageView) findViewById(R.id.user_logo);
		tv1 = (TextView) findViewById(R.id.user_name);
		tv2 = (TextView) findViewById(R.id.user_id);

		btn1 = (Button) findViewById(R.id.clearChatingMsg);
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
						.getInstance(ChatInfoActivity.this);
				dialogBuilder
						.withTitleColor("#FFFFFF")
						.withDividerColor("#11000000")
						.withMessage(
								"确认删除和" + mUserDto.getUserName() + "的聊天记录吗?")
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
		findViewById(R.id.im_chatinfo_ll).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ChatInfoActivity.this,
								DetailProfileActivity.class);
						intent.putExtra("mUserDto", mUserDto);
						startActivity(intent);
					}
				});
		slb = (AbSlidingButton) findViewById(R.id.mSliderBtn1);
		slb.setImageResource(R.drawable.btn_bottom, R.drawable.btn_frame,
				R.drawable.btn_mask, R.drawable.btn_unpressed,
				R.drawable.btn_pressed);

		iApp.mAbImageLoader.display(
				iv1,
				ConstServer.USER_DOWNLOADLOGO(mUserDto.getUserId(),
						mUserDto.getPhotoPath()));
		tv1.setText(mUserDto.getUserName());
		tv2.setText("员工编号:" + mUserDto.getUserId());
		AbStorageQuery mAbStorageQuery = new AbStorageQuery();
		mAbStorageQuery.equals("userId", mUserDto.getUserId());
		iApp.mAbSqliteStorage.findData(mAbStorageQuery,
				iApp.mIMMsgIgnoreUserDao, new AbDataSelectListener() {
					@Override
					public void onSuccess(List<?> paramList) {
						if (paramList.size() > 0) {
							slb.setChecked(true);
							firstIsChecked = true;
						}
					}

					@Override
					public void onFailure(int errorCode, String errorMessage) {
					}
				});
	}

	private void clearIMs() {
		net.post(
				ConstServer.IM_PERSON_CLEAR(iApp.mUser.getUserId(),
						mUserDto.getUserId()), null, new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						AbToastUtil.showToast(ChatInfoActivity.this,
								"清除聊天记录成功!");
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(ChatInfoActivity.this, content);
					}
				});
		AbStorageQuery mAbStorageQuery = new AbStorageQuery();
		mAbStorageQuery.equals("senderId", mUserDto.getUserId());
		iApp.mAbSqliteStorage.deleteData(mAbStorageQuery, iApp.mUserDao, null);
	}

	@Override
	public void finish() {
		if (slb.isChecked() && !firstIsChecked) {
			IMMsgIgnoreUserDto dto = new IMMsgIgnoreUserDto();
			dto.setUserId(mUserDto.getUserId());
			iApp.mAbSqliteStorage.insertData(dto, iApp.mIMMsgIgnoreUserDao,
					null);
		} else if (firstIsChecked) {
			AbStorageQuery mAbStorageQuery = new AbStorageQuery();
			mAbStorageQuery.equals("userId", mUserDto.getUserId());
			iApp.mAbSqliteStorage.deleteData(mAbStorageQuery,
					iApp.mIMMsgIgnoreUserDao, null);
		}
		super.finish();
	}
}
