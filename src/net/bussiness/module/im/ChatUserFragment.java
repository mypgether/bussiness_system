package net.bussiness.module.im;

import java.util.ArrayList;
import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.adapter.IMChatUserdapter;
import net.bussiness.dto.ChatUserDto;
import net.bussiness.module.base.BasePull2RefreshLvFragment;
import net.bussiness.module.base.NavActivity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.db.storage.AbSqliteStorageListener.AbDataDeleteListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataSelectListener;
import com.ab.db.storage.AbStorageQuery;
import com.ab.util.AbToastUtil;

public class ChatUserFragment extends BasePull2RefreshLvFragment {
	private List<ChatUserDto> mChatUser = null;
	private IMChatUserdapter mUserdapter = null;

	@Override
	public void onResume() {
		super.onResume();
		refreshTask();
	}

	@Override
	public void initViewAdapter() {
		mChatUser = new ArrayList<ChatUserDto>();
		mUserdapter = new IMChatUserdapter(getActivity(), mChatUser);
		mListView.setAdapter(mUserdapter);
		mAbPullToRefreshView.setLoadMoreEnable(false);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (null != mChatUser.get(0)) {
					Intent intent = new Intent(getActivity(),
							ChatDetailActivity.class);
					intent.putExtra("mChatUser", mChatUser.get(position));
					startActivity(intent);
				}
			}
		});
		registerForContextMenu(mListView);
	}

	@Override
	public void refreshTask() {
		queryChatUserList();
	}

	/**
	 * 查询聊天用户数据
	 */
	public void queryChatUserList() {
		AbStorageQuery query = new AbStorageQuery();
		query.setOrderBy(" lastChatTime desc ");
		iApp.mAbSqliteStorage.findData(query, iApp.mUserDao,
				new AbDataSelectListener() {
					@Override
					public void onFailure(int errorCode, String errorMessage) {
						AbToastUtil.showToast(getActivity(), errorMessage);
						mAbPullToRefreshView.onHeaderRefreshFinish();
						showContentView();
					}

					@Override
					public void onSuccess(List<?> paramList) {
						mChatUser.clear();
						if (null != paramList && paramList.size() != 0) {
							mChatUser.addAll((List<ChatUserDto>) paramList);
						} else {
							mChatUser.add(null);
						}
						mUserdapter.notifyDataSetChanged();
						mAbPullToRefreshView.onHeaderRefreshFinish();
						showContentView();
					}
				});
	}

	/**
	 * 删除聊天用户数据
	 */
	public void deleteChatUser(final int position) {
		AbStorageQuery query = new AbStorageQuery();
		query.equals("senderId", mChatUser.get(position).getSenderId());
		iApp.mAbSqliteStorage.deleteData(query, iApp.mUserDao,
				new AbDataDeleteListener() {
					@Override
					public void onSuccess(int rows) {
						mChatUser.remove(position);
						if (mChatUser.size() == 0)
							mChatUser.add(null);
						mUserdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(int errorCode, String errorMessage) {
						AbToastUtil.showToast(getActivity(), errorMessage);
					}
				});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(((NavActivity) getActivity()).mAbTitleBar
				.getTitleTextButton().getText());
		new MenuInflater(getActivity()).inflate(R.menu.im_chatuser, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete:
			if (null != mChatUser.get(0)) {
				deleteChatUser(menuInfo.position);
			}
			break;
		}
		return super.onContextItemSelected(item);
	}
}
