package net.bussiness.module.yw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.adapter.YwsqLeaderExpandableLvdapter;
import net.bussiness.adapter.YwsqNormalExpandableLvAdapter;
import net.bussiness.dialog.lib.Effectstype;
import net.bussiness.dialog.lib.NiftyDialogBuilder;
import net.bussiness.dto.ChatUserDto;
import net.bussiness.dto.UserDto;
import net.bussiness.dto.YwsqDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.module.base.BasePull2RefreshExpandableLvFragment;
import net.bussiness.module.im.ChatDetailActivity;
import net.bussiness.module.ywnr.YwnrDetailActivity;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import net.bussiness.tools.StringUtils;
import net.bussiness.tools.VibrateUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbToastUtil;
import com.ab.view.app.AbPopoverView;

public class BaseYwsqFragment extends BasePull2RefreshExpandableLvFragment {

	private BaseExpandableListAdapter mAdapter = null;
	private int approveState = 0;
	private NetworkUtils networkWeb;
	private String url;

	// ExpandableListView的数据源
	private List<YwsqDto> group;
	private List<List<YwsqDto>> child;

	// 长按ExpandableListView之后，显示的弹出框
	private AbPopoverView popoverView = null;
	private LayoutInflater mInflater = null;
	private RelativeLayout rootView = null;
	private IApplication iApp = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		iApp = (IApplication) getActivity().getApplication();
		approveState = getArguments().getInt("approveState");
		if (iApp.mUserIdentity == 1) {
			if (approveState == 0)
				url = ConstServer.YWSQ_FINDAPPROVEING_WITHPC(
						iApp.mUser.getUserId(), iApp.mUser.getDept().getId());
			else
				url = ConstServer.YWSQ_FINDAPPROVE_WITHPC(iApp.mUser
						.getUserId());
		} else {
			url = ConstServer.YWSQ_FINDPROPOSE_WITHPC(iApp.mUser.getUserId());
		}
		networkWeb = NetworkUtils.newInstance(getActivity());
		initPopView();
		mInflater = LayoutInflater.from(getActivity());
		rootView = (RelativeLayout) getActivity().findViewById(
				R.id.ywsq_main_rl);
	}

	private void initPopView() {
		popoverView = new AbPopoverView(getActivity());
		popoverView.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.btn_yellow_n));
		popoverView.setArrowDownDrawable(getActivity().getResources()
				.getDrawable(R.drawable.popover_arrow_down));
	}

	@Override
	public void initViewAdapter() {
		group = new ArrayList<YwsqDto>();
		child = new ArrayList<List<YwsqDto>>();
		if (iApp.mUserIdentity == 1) {
			mAdapter = new YwsqLeaderExpandableLvdapter(getActivity(), group,
					child, approveState == 0);
		} else {
			mAdapter = new YwsqNormalExpandableLvAdapter(getActivity(), group,
					child, approveState == 0);
		}
		mExpandableListView.setAdapter(mAdapter);
		mExpandableListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						if (iApp.mUserIdentity == 1 && null != group.get(0)) {
							VibrateUtils.Vibrate(getActivity(),
									VibrateUtils.LONG_TIME);
							showLeaderPopView(view, position);
						} else if (null != group.get(0)) {
							VibrateUtils.Vibrate(getActivity(),
									VibrateUtils.LONG_TIME);
							showNormalPopView(view, position);
						}
						return true;
					}
				});
	}

	/**
	 * <pre>
	 * Purpose:显示领导的提示框
	 * @author Myp
	 * Create Time: 2015-1-14 上午10:47:34
	 * @param position
	 * @param view
	 * Version: 1.0
	 * </pre>
	 */
	private void showLeaderPopView(View view, int position) {
		popoverView.removeAllViews();
		final int realPos = getRealPos(position);
		if (approveState == 0) {
			popoverView.setContentSizeForViewInPopover(new Point(390, 90));
			popoverView.showPopoverFromRectInViewGroup(rootView,
					AbPopoverView.getFrameForView(view),
					AbPopoverView.PopoverArrowDirectionDown, true);
			popoverView.setPopoverContentView(mInflater.inflate(
					R.layout.ywsq_leader_approving_popview, null));
			getActivity().findViewById(R.id.agree).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							showConfirmDialog(realPos, 1);
							popoverView.dissmissPopover(true);
						}
					});
			getActivity().findViewById(R.id.reject).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							showConfirmDialog(realPos, 2);
							popoverView.dissmissPopover(true);
						}
					});
			getActivity().findViewById(R.id.chat).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							goChat(group.get(realPos).getUserByProposerId());
							popoverView.dissmissPopover(true);
						}
					});
		} else if (approveState == 1) {
			popoverView.setContentSizeForViewInPopover(new Point(280, 90));
			popoverView.showPopoverFromRectInViewGroup(rootView,
					AbPopoverView.getFrameForView(view),
					AbPopoverView.PopoverArrowDirectionDown, true);
			popoverView.setPopoverContentView(mInflater.inflate(
					R.layout.ywsq_leader_agree_popview, null));
			bindChatAndMore(realPos, 1);
		} else if (approveState == 2) {
			popoverView.setContentSizeForViewInPopover(new Point(130, 90));
			popoverView.showPopoverFromRectInViewGroup(rootView,
					AbPopoverView.getFrameForView(view),
					AbPopoverView.PopoverArrowDirectionDown, true);
			popoverView.setPopoverContentView(mInflater.inflate(
					R.layout.ywsq_leader_reject_popview, null));
			getActivity().findViewById(R.id.chat).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							goChat(group.get(realPos).getUserByProposerId());
							popoverView.dissmissPopover(true);
						}
					});
		}
	}

	/**
	 * <pre>
	 * Purpose:显示普通员工的提示框
	 * @author Myp
	 * Create Time: 2015-1-14 上午10:47:50
	 * @param position 
	 * @param view
	 * Version: 1.0
	 * </pre>
	 */
	private void showNormalPopView(View view, int position) {
		popoverView.removeAllViews();
		final int realPos = getRealPos(position);
		if (approveState == 0) {
			popoverView.setContentSizeForViewInPopover(new Point(130, 90));
			popoverView.showPopoverFromRectInViewGroup(rootView,
					AbPopoverView.getFrameForView(view),
					AbPopoverView.PopoverArrowDirectionDown, true);
			popoverView.setPopoverContentView(mInflater.inflate(
					R.layout.ywsq_normal_approving_popview, null));
			getActivity().findViewById(R.id.delete).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							deleteYwsq(realPos);
							popoverView.dissmissPopover(true);
						}
					});
		} else if (approveState == 1) {
			popoverView.setContentSizeForViewInPopover(new Point(270, 90));
			popoverView.showPopoverFromRectInViewGroup(rootView,
					AbPopoverView.getFrameForView(view),
					AbPopoverView.PopoverArrowDirectionDown, true);
			popoverView.setPopoverContentView(mInflater.inflate(
					R.layout.ywsq_normal_agree_popview, null));
			bindChatAndMore(realPos, 2);
		} else if (approveState == 2) {
			popoverView.setContentSizeForViewInPopover(new Point(130, 90));
			popoverView.showPopoverFromRectInViewGroup(rootView,
					AbPopoverView.getFrameForView(view),
					AbPopoverView.PopoverArrowDirectionDown, true);
			popoverView.setPopoverContentView(mInflater.inflate(
					R.layout.ywsq_normal_reject_popview, null));
			getActivity().findViewById(R.id.chat).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							goChat(group.get(realPos).getUserByApproverId());
							popoverView.dissmissPopover(true);
						}
					});
		}
	}

	/**
	 * <pre>
	 * Purpose:
	 * @author Myp
	 * Create Time: 2015-1-22 下午5:41:03
	 * @param position
	 * @param fromType 1表示领导，2表示普通员工
	 * Version: 1.0
	 * </pre>
	 */
	private void bindChatAndMore(final int position, final int fromType) {
		getActivity().findViewById(R.id.chat).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (fromType == 1) {
							goChat(group.get(position).getUserByProposerId());
						} else {
							goChat(group.get(position).getUserByApproverId());
						}
						popoverView.dissmissPopover(true);
					}
				});
		getActivity().findViewById(R.id.detail).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						goDetail(group.get(position));
						popoverView.dissmissPopover(true);
					}
				});
	}

	/**
	 * <pre>
	 * Purpose:弹出审批的确认框
	 * @author Myp
	 * Create Time: 2015-1-21 下午4:01:36
	 * @param position 数据的位置
	 * @param state 审批的状态(1通过/2拒绝)
	 * Version: 1.0
	 * </pre>
	 */
	private void showConfirmDialog(final int position, final int state) {
		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
				.getInstance(getActivity());
		dialogBuilder
				.withTitle("说明原因")
				.withTitleColor("#FFFFFF")
				.withDividerColor("#11000000")
				.withMessage(null)
				.withDialogColor("#FFE74C3C")
				.withIcon(getResources().getDrawable(R.drawable.ic_home_pages))
				.withDuration(300)
				.withEffect(Effectstype.Shake)
				.withButton1Text("确定")
				.withButton2Text("取消")
				.isCancelableOnTouchOutside(false)
				.setCustomView(R.layout.ywsq_popview_confirmdialog,
						getActivity())
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						EditText et = (EditText) dialogBuilder
								.getCurrentFocus().findViewById(R.id.reason);
						if (state == 1) {
							et.setHint("通过！");
						} else if (state == 2) {
							et.setHint("拒绝！");
						}
						String reason = "";
						if (StringUtils.isBlank(et.getText().toString())) {
							reason = et.getHint().toString();
						} else {
							reason = et.getText().toString().trim();
						}
						updateYwsq(position, state, reason);
						dialogBuilder.dismiss();
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();
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

	private void goDetail(YwsqDto ywsqDto) {
		Intent intent = new Intent(getActivity(), YwnrDetailActivity.class);
		Bundle b = new Bundle();
		b.putSerializable("ywsqDto", ywsqDto);
		intent.putExtras(b);
		startActivity(intent);
	}

	private void updateYwsq(int position, int state, String reason) {
		NetworkUtils web = NetworkUtils.newInstance(getActivity());
		AbRequestParams params = new AbRequestParams();
		YwsqDto ywsqDto = group.get(position);
		ywsqDto.setApproveState(state);
		ywsqDto.setApproveReason(reason);
		ywsqDto.setUserByApproverId(iApp.mUser);
		ywsqDto.setApproveTime(new Date());
		System.err.println(ywsqDto.toString());
		params.put("ywsqDto", JacksonUtils.bean2Json(ywsqDto));
		web.post(ConstServer.YWSQ_UPDATE(iApp.mUser.getUserId()), params,
				new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						AbToastUtil.showToast(getActivity(), "审批成功！");
						mAbPullToRefreshView.headerRefreshing();
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(getActivity(), "审批失败，请重新再试!");
					}
				});
	}

	private void deleteYwsq(int position) {
		NetworkUtils web = NetworkUtils.newInstance(getActivity());
		AbRequestParams params = new AbRequestParams();
		YwsqDto ywsqDto = group.get(position);
		System.err.println(ywsqDto.toString());
		params.put("ywsqDto", JacksonUtils.bean2Json(ywsqDto));
		web.post(ConstServer.YWSQ_DELETE(iApp.mUser.getUserId()), params,
				new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						mAbPullToRefreshView.headerRefreshing();
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(getActivity(), content);
					}
				});
	}

	/**
	 * 下载数据
	 */
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
				if (approveState == 0) {
					// 申请时间从远到近
					// 降序
					int flag1 = ywsq0.getApplyTime().compareTo(
							ywsq1.getApplyTime());
					return flag1;
				} else {
					// 审批时间从近到远
					// 升序
					int flag = ywsq1.getApproveTime().compareTo(
							ywsq0.getApproveTime());
					return flag;
				}
			}
		});
	}

	private AbRequestParams getParams() {
		AbRequestParams params = new AbRequestParams();
		params.put("approveState", approveState + "");
		params.put("page", nowPage + "");
		params.put("rows", iApp.mConfig.getmCMsgLoadingNumberDto()
				.getYwspNumber() + "");
		return params;
	}
}