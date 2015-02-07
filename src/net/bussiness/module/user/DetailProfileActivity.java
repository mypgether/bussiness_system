package net.bussiness.module.user;

import net.bussiness.activities.R;
import net.bussiness.dto.ChatUserDto;
import net.bussiness.dto.UserDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.module.im.ChatDetailActivity;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpListener;
import com.ab.util.AbToastUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.titlebar.AbTitleBar;

public class DetailProfileActivity extends AbActivity {
	/*
	 * 1.用户名 2.头像 3.用户编号ID
	 * 
	 * 4.个人简介
	 * 
	 * 5.联系电话 6.电子邮箱
	 * 
	 * 7.所属部门 8.所属职位 9.加入时间
	 */
	private ImageView iv1;
	private TextView tv1;
	private TextView tv2;

	private TextView tv3;

	private TextView tv4;
	private TextView tv5;

	private TextView tv6;
	private TextView tv7;
	private TextView tv8;
	private UserDto mUserDto;
	private NetworkUtils net;

	protected AbPullToRefreshView mAbPullToRefreshView = null;
	protected LinearLayout mLL = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.pull_to_refresh_view);
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("详细资料");
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

		mLL = (LinearLayout) findViewById(R.id.mLL);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);

		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(
						R.drawable.listview_pull_refresh02));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(
						R.drawable.listview_pull_refresh02));
		// 设置监听器
		mAbPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						refreshTask();
					}
				});
		mAbPullToRefreshView
				.setOnFooterLoadListener(new OnFooterLoadListener() {
					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						mAbPullToRefreshView.onFooterLoadFinish();
					}
				});

		initView(LayoutInflater.from(this));

		refreshTask();
	}

	public void initView(LayoutInflater inflater) {
		net = NetworkUtils.newInstance(this);
		mUserDto = (UserDto) getIntent().getSerializableExtra("mUserDto");
		View tempView = inflater.inflate(R.layout.user_individualcenter, null);
		mLL.addView(tempView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		iv1 = (ImageView) tempView.findViewById(R.id.user_logo);
		tv1 = (TextView) tempView.findViewById(R.id.user_name);
		tv2 = (TextView) tempView.findViewById(R.id.user_id);

		tv3 = (TextView) tempView.findViewById(R.id.user_desc);

		tv4 = (TextView) tempView.findViewById(R.id.user_tel);
		tv5 = (TextView) tempView.findViewById(R.id.user_email);

		tv6 = (TextView) tempView.findViewById(R.id.user_dept);
		tv7 = (TextView) tempView.findViewById(R.id.user_position);
		tv8 = (TextView) tempView.findViewById(R.id.user_joinTime);
		if (((IApplication) getApplication()).mUser.getUserId().equals(
				mUserDto.getUserId())) {
			tempView.findViewById(R.id.user_send_msg).setVisibility(View.GONE);
		} else {
			tempView.findViewById(R.id.user_send_msg).setVisibility(
					View.VISIBLE);
			tempView.findViewById(R.id.user_send_msg).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									DetailProfileActivity.this,
									ChatDetailActivity.class);
							ChatUserDto chatUserDto = new ChatUserDto();
							chatUserDto.setSenderId(mUserDto.getUserId());
							chatUserDto.setSenderLogoPath(mUserDto
									.getPhotoPath());
							chatUserDto.setSenderName(mUserDto.getUserName());
							intent.putExtra("mChatUser", chatUserDto);
							startActivity(intent);
							finish();
						}
					});
		}
	}

	public void refreshTask() {
		net.post(ConstServer.USER_LOAD(mUserDto.getUserId()), null,
				new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						mUserDto = (UserDto) JacksonUtils.json2Bean(content,
								UserDto.class);
						refreshView();
						System.out.println(mUserDto.toString());
						mAbPullToRefreshView.onHeaderRefreshFinish();
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(DetailProfileActivity.this,
								content);
						mAbPullToRefreshView.onHeaderRefreshFinish();
					}
				});
	}

	private void refreshView() {
		((IApplication) getApplication()).mAbImageLoader.display(
				iv1,
				ConstServer.USER_DOWNLOADLOGO(mUserDto.getUserId(),
						mUserDto.getPhotoPath()));
		tv1.setText(mUserDto.getUserName());
		tv2.setText("员工编号:" + mUserDto.getUserId());

		tv3.setText(mUserDto.getDescription());

		tv4.setText(mUserDto.getTel());
		tv5.setText(mUserDto.getEmail());

		tv6.setText(mUserDto.getDept().getDeptName());
		tv7.setText(mUserDto.getPosition().getDescription());
		tv8.setText(mUserDto.getJoinTime().toLocaleString());
	}
}
