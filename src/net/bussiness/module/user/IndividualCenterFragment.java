package net.bussiness.module.user;

import net.bussiness.activities.R;
import net.bussiness.dto.UserDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.module.base.BasePull2RefreshViewFragment;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.http.AbHttpListener;
import com.ab.util.AbToastUtil;

public class IndividualCenterFragment extends BasePull2RefreshViewFragment {
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
	private IApplication iApp = null;

	@SuppressWarnings("deprecation")
	@Override
	public void initView(LayoutInflater inflater) {
		iApp = (IApplication) getActivity().getApplication();
		mUserDto = iApp.mUser;
		mAbPullToRefreshView.setLoadMoreEnable(false);

		net = NetworkUtils.newInstance(getActivity());
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
		tempView.findViewById(R.id.user_send_msg).setVisibility(View.GONE);
	}

	@Override
	public void refreshTask() {
		net.post(ConstServer.USER_LOAD(mUserDto.getUserId()), null,
				new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						UserDto userDto = (UserDto) JacksonUtils.json2Bean(
								content, UserDto.class);
						mUserDto = userDto;
						((IApplication) getActivity().getApplication()).mUser = userDto;
						mAbPullToRefreshView.onHeaderRefreshFinish();
						showContentView();
						refreshView();
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(getActivity(), content);
						mAbPullToRefreshView.onHeaderRefreshFinish();
						showContentView();
					}
				});
	}

	private void refreshView() {
		iApp.mAbImageLoader.display(
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