package net.bussiness.module.ywnr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.adapter.YwsqLeaderExpandableLvdapter;
import net.bussiness.adapter.YwsqNormalExpandableLvAdapter;
import net.bussiness.dto.ChatUserDto;
import net.bussiness.dto.UserDto;
import net.bussiness.dto.YwsqDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.module.base.BasePull2RefreshExpandableLvFragment;
import net.bussiness.module.base.NavActivity;
import net.bussiness.module.im.ChatDetailActivity;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbToastUtil;

public class YwnrFragment extends BasePull2RefreshExpandableLvFragment {

	private BaseExpandableListAdapter mAdapter = null;
	private NetworkUtils networkWeb;
	private String url;

	// ExpandableListView的数据源
	private List<YwsqDto> group;
	private List<List<YwsqDto>> child;
	private IApplication iApp = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		iApp = (IApplication) getActivity().getApplication();
		if (iApp.mUserIdentity == 1) {
			url = ConstServer.YWSQ_FINDAPPROVE_WITHPC(iApp.mUser.getUserId());
		} else {
			url = ConstServer.YWSQ_FINDPROPOSE_WITHPC(iApp.mUser.getUserId());
		}
		networkWeb = NetworkUtils.newInstance(getActivity());
	}

	@Override
	public void initViewAdapter() {
		group = new ArrayList<YwsqDto>();
		child = new ArrayList<List<YwsqDto>>();
		if (iApp.mUserIdentity == 1) {
			mAdapter = new YwsqLeaderExpandableLvdapter(getActivity(), group,
					child, false);
		} else {
			mAdapter = new YwsqNormalExpandableLvAdapter(getActivity(), group,
					child, false);
		}
		mExpandableListView.setAdapter(mAdapter);
		registerForContextMenu(mExpandableListView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(((NavActivity) getActivity()).mAbTitleBar
				.getTitleTextButton().getText());
		new MenuInflater(getActivity()).inflate(R.menu.ywnrdetail, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo menuInfo = (ExpandableListContextMenuInfo) item
				.getMenuInfo();
		int groupPos = ExpandableListView
				.getPackedPositionGroup(menuInfo.packedPosition);
		if (group.get(groupPos) == null) {
			return super.onContextItemSelected(item);
		}
		switch (item.getItemId()) {
		case R.id.chat:
			if (iApp.mUserIdentity == 1) {
				goChat(group.get(groupPos).getUserByProposerId());
			} else {
				goChat(group.get(groupPos).getUserByApproverId());
			}
			break;
		case R.id.detail:
			Intent intent = new Intent(getActivity(), YwnrDetailActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("ywsqDto", group.get(groupPos));
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void goChat(UserDto user) {
		if (user.getUserId().equals(iApp.mUser.getUserId())) {
			AbToastUtil.showToast(getActivity(), "无法创建与自己的聊天!");
			return;
		}
		Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
		ChatUserDto chatUserDto = new ChatUserDto();
		chatUserDto.setSenderId(user.getUserId());
		chatUserDto.setSenderLogoPath(user.getPhotoPath());
		chatUserDto.setSenderName(user.getUserName());
		intent.putExtra("mChatUser", chatUserDto);
		startActivity(intent);
	}

	public void refreshTask() {
		super.refreshTask();
		networkWeb.post(url, getParams(), new AbHttpListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				List<YwsqDto> dao = (List<YwsqDto>) JacksonUtils
						.jsonPageResult2Bean("rows", content, YwsqDto.class);
				group.clear();
				child.clear();
				if (null == dao || dao.size() == 0) {
					group.add(null);
					dao.add(null);
					child.add(dao);
				} else {
					for (YwsqDto tmp : dao) {
						group.add(tmp);
					}
					sortResult(group);
					for (YwsqDto tmp : group) {
						List<YwsqDto> temp = new ArrayList<YwsqDto>();
						temp.add(tmp);
						child.add(temp);
					}
				}
				mAdapter.notifyDataSetChanged();
				mAbPullToRefreshView.onHeaderRefreshFinish();
				showContentView();
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(getActivity(), content);
				mAbPullToRefreshView.onHeaderRefreshFinish();
				showContentView();
			}
		});
	}

	public void loadMoreTask() {
		super.loadMoreTask();
		networkWeb.post(url, getParams(), new AbHttpListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				List<YwsqDto> dao = (List<YwsqDto>) JacksonUtils
						.jsonPageResult2Bean("rows", content, YwsqDto.class);
				if (null == dao || dao.size() == 0) {
					AbToastUtil.showToast(getActivity(), "没有更多内容...");
				} else {
					group.addAll(dao);
					sortResult(group);
					child.clear();
					for (YwsqDto tmp : group) {
						List<YwsqDto> temp = new ArrayList<YwsqDto>();
						temp.add(tmp);
						child.add(temp);
					}
					mAdapter.notifyDataSetChanged();
				}
				mAbPullToRefreshView.onFooterLoadFinish();
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(getActivity(), content);
				mAbPullToRefreshView.onFooterLoadFinish();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void sortResult(List<YwsqDto> ywsqs) {
		Collections.sort(ywsqs, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				YwsqDto ywsq0 = (YwsqDto) arg0;
				YwsqDto ywsq1 = (YwsqDto) arg1;
				// 业务开始时间从近到远
				// 升序
				int flag = ywsq1.getTimestamp().compareTo(ywsq0.getTimestamp());
				return flag;
			}
		});
	}

	private AbRequestParams getParams() {
		AbRequestParams params = new AbRequestParams();
		params.put("approveState", "1");
		params.put("page", nowPage + "");
		params.put("rows", iApp.mConfig.getmCMsgLoadingNumberDto()
				.getYwspNumber() + "");
		return params;
	}
}