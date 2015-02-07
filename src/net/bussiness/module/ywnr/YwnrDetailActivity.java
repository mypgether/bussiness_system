package net.bussiness.module.ywnr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.adapter.YwnrAllExpandableLvAdapter;
import net.bussiness.dto.YwnrDto;
import net.bussiness.dto.YwnrPhotosDto;
import net.bussiness.dto.YwpjDto;
import net.bussiness.dto.YwsqDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbProgressDialogFragment;
import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.titlebar.AbTitleBar;

public class YwnrDetailActivity extends AbActivity {
	private YwsqDto mYwsqDto = null;
	public IApplication iApp = null;

	// 当前页数
	private int nowPage = 1;
	private NetworkUtils networkWeb = null;

	private AbPullToRefreshView mAbPullToRefreshView = null;
	// ExpandableListView的数据源
	private List<YwnrDto> group;
	private List<List<List<YwnrPhotosDto>>> childPhotos;
	private List<List<YwpjDto>> childPjs;
	private BaseExpandableListAdapter mAdapter = null;
	private ExpandableListView mExpandableListView = null;
	private int[] expandGroupPosition;

	private AbProgressDialogFragment mProgressFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.yw_detail);
		mProgressFragment = AbDialogUtil.showProgressDialog(this, 0,
				"正在加载,请稍后...");
		mProgressFragment.setCancelable(false);

		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
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
						loadMoreTask();
					}
				});
		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(
						R.drawable.listview_pull_refresh02));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(
						R.drawable.listview_pull_refresh02));
		iApp = (IApplication) abApplication;
		networkWeb = NetworkUtils.newInstance(this);
		expandGroupPosition = new int[1000];

		initTitleBar();

		mYwsqDto = (YwsqDto) getIntent().getExtras().getSerializable("ywsqDto");

		group = new ArrayList<YwnrDto>();
		childPhotos = new ArrayList<List<List<YwnrPhotosDto>>>();
		childPjs = new ArrayList<List<YwpjDto>>();
		mAdapter = new YwnrAllExpandableLvAdapter(this, group, childPhotos,
				childPjs);
		mExpandableListView = (ExpandableListView) findViewById(R.id.mExpandableListView);
		mExpandableListView.setAdapter(mAdapter);
		mExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {
					@Override
					public void onGroupExpand(int groupPosition) {
						collapseOtherGroup();
						expandGroupPosition[groupPosition] = 1;
						if (group.get(groupPosition) != null) {
							loadYwnrPhotos(group.get(groupPosition),
									groupPosition);
							loadYwnrPjs(group.get(groupPosition), groupPosition);
						}
					}
				});
		mExpandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {
					@Override
					public void onGroupCollapse(int groupPosition) {
						expandGroupPosition[groupPosition] = 0;
					}
				});
		registerForContextMenu(mExpandableListView);
		refreshTask();
	}

	private void collapseOtherGroup() {
		for (int i = 0; i < group.size(); i++)
			if (expandGroupPosition[i] == 1) {
				mExpandableListView.collapseGroup(i);
				expandGroupPosition[i] = 0;
			}
	}

	private void initTitleBar() {
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText("签到详细");
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
		if (iApp.mUserIdentity != 1) {
			View rightViewMore = LayoutInflater.from(this).inflate(
					R.layout.add_btn, null);
			mAbTitleBar.addRightView(rightViewMore);
			Button addBtn = (Button) rightViewMore.findViewById(R.id.addBtn);
			addBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(YwnrDetailActivity.this,
							YwnrAddActivity.class);
					intent.putExtra("ywsqDto", mYwsqDto);
					startActivity(intent);
				}
			});
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(this.getTitleBar().getTitleTextButton().getText());
		new MenuInflater(this).inflate(R.menu.im_chatuser, menu);
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
		case R.id.delete:
			deleteYwnrDetail(group.get(groupPos));
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void deleteYwnrDetail(YwnrDto ywnrDto) {
		AbRequestParams params = new AbRequestParams();
		params.put("ywnrDto", JacksonUtils.bean2Json(ywnrDto));
		networkWeb.post(ConstServer.YWNR_DELETE(iApp.mUser.getUserId(), ywnrDto
				.getYwsq().getYwId()), params, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				mAbPullToRefreshView.headerRefreshing();
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(YwnrDetailActivity.this, content);
			}
		});
	}

	public void refreshTask() {
		nowPage = 1;
		networkWeb.post(ConstServer.YWNR_FINDWITHPC(mYwsqDto
				.getUserByProposerId().getUserId(), mYwsqDto.getYwId()),
				getParams(), new AbHttpListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(String content) {
						System.out.println(content);
						List<YwnrDto> dao = (List<YwnrDto>) JacksonUtils
								.jsonPageResult2Bean("rows", content,
										YwnrDto.class);
						group.clear();
						if (null == dao || dao.size() == 0) {
							group.add(null);
							dao.add(null);
						} else {
							for (YwnrDto tmp : dao) {
								group.add(tmp);
							}
							sortResult(group);
						}
						mAdapter.notifyDataSetChanged();
						mAbPullToRefreshView.onHeaderRefreshFinish();
						if (null != mProgressFragment)
							mProgressFragment.dismiss();
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(YwnrDetailActivity.this, content);
						if (null != mProgressFragment)
							mProgressFragment.dismiss();
					}
				});
	}

	public void loadMoreTask() {
		nowPage++;
		networkWeb.post(ConstServer.YWNR_FINDWITHPC(mYwsqDto
				.getUserByProposerId().getUserId(), mYwsqDto.getYwId()),
				getParams(), new AbHttpListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						List<YwnrDto> dao = (List<YwnrDto>) JacksonUtils
								.jsonPageResult2Bean("rows", content,
										YwnrDto.class);
						if (null == dao || dao.size() == 0) {
							AbToastUtil.showToast(YwnrDetailActivity.this,
									"没有更多内容...");
							mAbPullToRefreshView.onFooterLoadFinish();
							return;
						}
						group.addAll(dao);
						sortResult(group);
						mAdapter.notifyDataSetChanged();
						mAbPullToRefreshView.onFooterLoadFinish();
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(YwnrDetailActivity.this, content);
					}
				});
	}

	private AbRequestParams getParams() {
		AbRequestParams params = new AbRequestParams();
		params.put("page", nowPage + "");
		params.put("rows", iApp.mConfig.getmCMsgLoadingNumberDto()
				.getYwnrNumber() + "");
		return params;
	}

	@SuppressWarnings("unchecked")
	private void sortResult(List<YwnrDto> ywnrs) {
		Collections.sort(ywnrs, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				YwnrDto ywnr0 = (YwnrDto) arg0;
				YwnrDto ywnr1 = (YwnrDto) arg1;
				// 审批时间从近到远
				// 升序
				return ywnr0.getNrTime().compareTo(ywnr1.getNrTime());
			}
		});
	}

	public void loadYwnrPhotos(YwnrDto ywnrDto, final int groupPosition) {
		AbRequestParams params = new AbRequestParams();
		params.put("page", "1");
		params.put("rows", "500");
		networkWeb.post(ConstServer.YWNR_PHOTO_FINDWITHPC(mYwsqDto
				.getUserByProposerId().getUserId(), mYwsqDto.getYwId(), ywnrDto
				.getId()), params, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				@SuppressWarnings("unchecked")
				List<YwnrPhotosDto> dao = (List<YwnrPhotosDto>) JacksonUtils
						.jsonPageResult2Bean("rows", content,
								YwnrPhotosDto.class);
				if (dao.size() != 0) {
					if (childPhotos.size() <= groupPosition) {
						for (int i = childPhotos.size(); i <= groupPosition; i++)
							childPhotos.add(null);
					}
					List<List<YwnrPhotosDto>> temp = new ArrayList<List<YwnrPhotosDto>>();
					temp.add(dao);
					childPhotos.set(groupPosition, temp);
					mAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(YwnrDetailActivity.this, content);
			}
		});
	}

	public void loadYwnrPjs(YwnrDto ywnrDto, final int groupPosition) {
		AbRequestParams params = new AbRequestParams();
		params.put("page", "1");
		params.put("rows", "500");
		networkWeb.post(ConstServer.YWNR_PJ_FINDWITHPC(mYwsqDto
				.getUserByProposerId().getUserId(), mYwsqDto.getYwId(), ywnrDto
				.getId()), params, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				@SuppressWarnings("unchecked")
				List<YwpjDto> dao = (List<YwpjDto>) JacksonUtils
						.jsonPageResult2Bean("rows", content, YwpjDto.class);
				if (dao.size() != 0) {
					if (childPjs.size() <= groupPosition) {
						for (int i = childPjs.size(); i <= groupPosition; i++)
							childPjs.add(null);
					}
					childPjs.set(groupPosition, dao);
					mAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(YwnrDetailActivity.this, content);
			}
		});
	}

	public void addYwnrPjs(YwnrDto ywnrDto, final int groupPosition,
			final String remark) {
		AbRequestParams params = new AbRequestParams();
		final YwpjDto pj = new YwpjDto();
		pj.setRemark(remark.getBytes());
		pj.setUserByRemarkerId(iApp.mUser);
		pj.setYwnr(ywnrDto);
		params.put("ywpjDto", JacksonUtils.bean2Json(pj));
		networkWeb.post(
				ConstServer.YWNR_PJ_ADD(iApp.mUser.getUserId(),
						mYwsqDto.getYwId(), ywnrDto.getId()), params,
				new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						if (childPjs.size() > groupPosition) {
							childPjs.get(groupPosition).add(pj);
							mAdapter.notifyDataSetChanged();
							AbToastUtil.showToast(YwnrDetailActivity.this,
									"发表成功！");
						}
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(YwnrDetailActivity.this, content);
					}
				});
	}
}
