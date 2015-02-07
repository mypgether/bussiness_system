package net.bussiness.module.im;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.adapter.IMChatEmotionViewAdapter;
import net.bussiness.adapter.IMGroupMsgViewAdapter;
import net.bussiness.dto.DeptDto;
import net.bussiness.dto.GroupmsgDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.module.base.CropImageActivity;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import net.bussiness.tools.NotificationUtils;
import net.bussiness.tools.StringUtils;
import net.bussiness.tools.VibrateUtils;
import net.bussiness.view.SubGridview;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ab.fragment.AbFragment;
import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;

public class ChatGroupDetailFragment extends AbFragment {
	private Activity mActivity;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private IApplication iApp = null;

	private IMGroupMsgViewAdapter mChatMsgViewAdapter = null;
	private List<GroupmsgDto> mChatMsgList = null;
	private ListView mMsgListView = null;
	private EditText mContentEdit = null;
	private Button mSendBtn = null;
	private ImageButton mAddBtn = null;
	private ImageButton mVoiceBtn = null;
	private Button mVoiceSendBtn = null;

	// 发送选项面板
	private RelativeLayout chatAppPanel = null;
	private SubGridview mGridView = null;
	private EmotionParser mEmotionParser = null;
	// 推送内容
	private String mContentStr = null;

	// 是否可以发送下一条
	private boolean isSendEnable = true;

	// 和哪个群聊天
	protected DeptDto mDept;

	// 我的录音类
	private IMRecorder mIMRecorder = null;

	// 当前页数
	private int nowPage = 1;
	private NetworkUtils networkWeb = null;

	private View mAvatarView = null;

	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;

	public static final int CLEAR_IGROUPCHATMSG = 3024;

	/* 拍照的照片存储位置 */
	private File PHOTO_DIR = null;
	// 照相机拍照得到的图片
	private File mCurrentPhotoFile;
	private String mFileName;
	private String photoPath = null;

	private SoundPool sound = null;

	public void onReceiveGroupMessage(GroupmsgDto msg, int notificationId) {
		mChatMsgList.add(msg);
		mChatMsgViewAdapter.notifyDataSetChanged();
		mMsgListView.setSelection(mChatMsgList.size() - 1);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		NotificationUtils.clearNotification(mActivity, notificationId);
	}

	@SuppressLint("CutPasteId")
	@Override
	public View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		mActivity = getActivity();
		View view = inflater.inflate(R.layout.imchat, null);
		mAbPullToRefreshView = (AbPullToRefreshView) view
				.findViewById(R.id.mPullRefreshView);
		// 设置监听器
		mAbPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						refreshTask();
					}
				});
		mAbPullToRefreshView.setLoadMoreEnable(false);
		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		// 加载数据必须
		this.setAbFragmentOnLoadListener(new AbFragmentOnLoadListener() {
			@Override
			public void onLoad() {
				refreshTask();
			}
		});

		iApp = (IApplication) mActivity.getApplication();
		mDept = iApp.mUser.getDept();
		mContentEdit = (EditText) view.findViewById(R.id.content);
		mSendBtn = (Button) view.findViewById(R.id.sendBtn);
		mAddBtn = (ImageButton) view.findViewById(R.id.addBtn);
		mVoiceBtn = (ImageButton) view.findViewById(R.id.voiceBtn);
		mVoiceSendBtn = (Button) view.findViewById(R.id.voiceSendBtn);

		// 面板选项
		chatAppPanel = (RelativeLayout) view.findViewById(R.id.chatAppPanel);

		mGridView = (SubGridview) view.findViewById(R.id.myGrid);
		mEmotionParser = new EmotionParser(getActivity());
		IMChatEmotionViewAdapter mIEmotionAdapter = new IMChatEmotionViewAdapter(
				mActivity, mEmotionParser.getmEmotionList());
		mGridView.setAdapter(mIEmotionAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mContentEdit.append(mEmotionParser.encode(position));
			}
		});

		mContentEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chatAppPanel.setVisibility(View.GONE);
			}
		});
		mContentEdit.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String str = mContentEdit.getText().toString().trim();
				int length = str.length();
				if (length > 0) {
					mSendBtn.setVisibility(View.VISIBLE);
					mAddBtn.setVisibility(View.GONE);
				} else {
					mSendBtn.setVisibility(View.GONE);
					mAddBtn.setVisibility(View.VISIBLE);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				int nSelStart = 0;
				int nSelEnd = 0;
				nSelStart = mContentEdit.getSelectionStart();
				nSelEnd = mContentEdit.getSelectionEnd();
				String str = mContentEdit.getText().toString().trim();
				if (str.getBytes().length > 4 * 1024) {
					AbToastUtil.showToast(getActivity(), "文字超出4K！");
					s.delete(nSelStart - 1, nSelEnd);
					mContentEdit.setTextKeepState(s);
				}
			}
		});

		mVoiceBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chatAppPanel.setVisibility(View.GONE);
				if (mVoiceSendBtn.getVisibility() == View.VISIBLE) {
					mVoiceSendBtn.setVisibility(View.GONE);
					mContentEdit.setVisibility(View.VISIBLE);
					mVoiceBtn
							.setBackgroundResource(R.drawable.button_selector_chat_voice);
				} else {
					mVoiceSendBtn.setVisibility(View.VISIBLE);
					mContentEdit.setVisibility(View.GONE);
					mVoiceBtn
							.setBackgroundResource(R.drawable.button_selector_chat_key);
				}
			}
		});

		// 按住录音
		mVoiceSendBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 开始录音
					startRecording();
					VibrateUtils.Vibrate(getActivity(), VibrateUtils.LONG_TIME);
					sound.play(1, 1, 1, 0, 0, 1);
					mVoiceSendBtn.setText("松开 结束");
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					// 结束录音
					stopRecording(false);
					VibrateUtils.Vibrate(getActivity(), VibrateUtils.LONG_TIME);
					sound.play(2, 1, 1, 0, 0, 1);
					mVoiceSendBtn.setText("按住 说话");
					break;
				}
				return false;
			}
		});

		mSendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mContentStr = mContentEdit.getText().toString().trim();
				if (!StringUtils.isEmpty(mContentStr)) {
					if (!isSendEnable) {
						AbToastUtil.showToast(getActivity(), "上一条正在发送，请稍等");
						return;
					}
					// 发送通知
					isSendEnable = false;
					// 清空文本框
					mContentEdit.setText("");

					GroupmsgDto mIMMsg = new GroupmsgDto();
					mIMMsg.setMsgTime(new Date());
					mIMMsg.setGroup(mDept);
					mIMMsg.setMsgType(1);
					mIMMsg.setMsgContent(mContentStr.getBytes());
					mIMMsg.setUserBySenderId(iApp.mUser);

					mChatMsgList.add(mIMMsg);
					mChatMsgViewAdapter.notifyDataSetChanged();
					mMsgListView.setSelection(mChatMsgList.size() - 1);

					sendMessage(mIMMsg, null);
				}
			}
		});

		mMsgListView = (ListView) view.findViewById(R.id.mListView);
		mChatMsgList = new ArrayList<GroupmsgDto>();
		mChatMsgViewAdapter = new IMGroupMsgViewAdapter(mActivity,
				mChatMsgList, mEmotionParser);
		mMsgListView.setAdapter(mChatMsgViewAdapter);
		registerForContextMenu(mMsgListView);

		ImageButton addBtn = (ImageButton) view.findViewById(R.id.addBtn);
		final LinearLayout chatAppPanel_ll = (LinearLayout) view
				.findViewById(R.id.chatAppPanel_ll);
		addBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (chatAppPanel.getVisibility() == View.GONE
						|| chatAppPanel_ll.getVisibility() == View.GONE) {
					chatAppPanel.setVisibility(View.VISIBLE);
					chatAppPanel_ll.setVisibility(View.VISIBLE);
					mGridView.setVisibility(View.GONE);
				} else {
					chatAppPanel.setVisibility(View.GONE);
				}
			}
		});

		ImageButton addEmotionBtn = (ImageButton) view
				.findViewById(R.id.addEmotionBtn);
		addEmotionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (chatAppPanel.getVisibility() == View.GONE
						|| mGridView.getVisibility() == View.GONE) {
					chatAppPanel.setVisibility(View.VISIBLE);
					chatAppPanel_ll.setVisibility(View.GONE);
					mGridView.setVisibility(View.VISIBLE);

					mVoiceSendBtn.setVisibility(View.GONE);
					mContentEdit.setVisibility(View.VISIBLE);
					mVoiceBtn
							.setBackgroundResource(R.drawable.button_selector_chat_voice);
				} else {
					chatAppPanel.setVisibility(View.GONE);
				}
			}
		});
		ImageButton addPhotoBtn = (ImageButton) view
				.findViewById(R.id.chatAppPanel_addPhoto);
		addPhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater mInflater = LayoutInflater.from(mActivity);
				mAvatarView = mInflater.inflate(R.layout.choose_avatar, null);
				Button albumButton = (Button) mAvatarView
						.findViewById(R.id.choose_album);
				Button camButton = (Button) mAvatarView
						.findViewById(R.id.choose_cam);
				Button cancelButton = (Button) mAvatarView
						.findViewById(R.id.choose_cancel);
				albumButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AbDialogUtil.removeDialog(getActivity());
						// 从相册中去获取
						try {
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT, null);
							intent.setType("image/*");
							mActivity.startActivityForResult(intent,
									PHOTO_PICKED_WITH_DATA);
						} catch (ActivityNotFoundException e) {
							AbToastUtil.showToast(getActivity(), "没有找到照片");
						}
					}
				});

				camButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AbDialogUtil.removeDialog(getActivity());
						doPickPhotoAction();
					}
				});

				cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AbDialogUtil.removeDialog(getActivity());
					}
				});
				AbDialogUtil.showDialog(mAvatarView, Gravity.BOTTOM);
			}
		});

		networkWeb = NetworkUtils.newInstance(mActivity);
		sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
		sound.load(mActivity, R.raw.tone_im_start_recording, 1);
		sound.load(mActivity, R.raw.tone_im_end_recording, 1);
		return view;
	}

	@Override
	public void setResource() {
		// 设置加载的资源
		this.setLoadDrawable(R.drawable.ic_load);
		this.setLoadMessage("正在查询,请稍候");

		this.setRefreshDrawable(R.drawable.ic_refresh);
		this.setRefreshMessage("请求出错，请重试");
	}

	/**
	 * 
	 * 描述：发消息
	 * 
	 * @param mIMMsg
	 * @throws
	 */
	protected void sendMessage(final GroupmsgDto mIMMsg, final File file) {
		AbRequestParams params = new AbRequestParams();
		params.put("groupmsgDto", JacksonUtils.bean2Json(mIMMsg));
		networkWeb.post(ConstServer.IM_GROUP_ADD(iApp.mUser.getUserId()),
				params, new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						if (file != null) {
							uploadFile(mIMMsg, file, Integer.parseInt(content));
						} else {
							AbToastUtil.showToast(mActivity, "消息发送成功！");
						}
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(mActivity, "消息发送失败！");
					}
				});
		isSendEnable = true;
	}

	private void uploadFile(final GroupmsgDto mIMMsg, final File file, int msgId) {
		AbRequestParams params = new AbRequestParams();
		params.put("file", file);
		params.put("deptId", mDept.getId() + "");
		String url = "";
		if (mIMMsg.getMsgType() == 2)
			url = ConstServer.IM_GROUP_UPLOADPHOTO(iApp.mUser.getUserId(),
					msgId);
		else if (mIMMsg.getMsgType() == 3) {
			url = ConstServer.IM_GROUP_UPLOADRECORD(iApp.mUser.getUserId(),
					msgId);
		}
		networkWeb.post(url, params, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				AbToastUtil.showToast(mActivity, "消息发送成功！");
				mIMMsg.setMsgContent(content.getBytes());
				mChatMsgList.add(mIMMsg);
				mChatMsgViewAdapter.notifyDataSetChanged();
				mMsgListView.setSelection(mChatMsgList.size() - 1);
				AbFileUtil.deleteFile(file);
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(mActivity, "消息发送失败！");
				AbFileUtil.deleteFile(file);
			}
		});
	}

	public void startRecording() {
		if (mIMRecorder == null) {
			mIMRecorder = new IMRecorder(mActivity, new IMRecordListener() {
				@Override
				public void onRecording() {
					AbToastUtil.showToast(mActivity, "正在录音");
				}

				@Override
				public void onPreRecording() {
				}

				@Override
				public void onFinish(File file, long time) {
					if (time < 1000) {
						AbFileUtil.deleteFile(file);
						AbToastUtil.showToast(mActivity, "语音时间太短！");
						return;
					}
					isSendEnable = true;
					GroupmsgDto mIMMsg = new GroupmsgDto();
					mIMMsg.setMsgTime(new Date());
					mIMMsg.setMsgType(3);
					mIMMsg.setGroup(mDept);
					mIMMsg.setMsgContent("".getBytes());
					mIMMsg.setUserBySenderId(iApp.mUser);

					sendMessage(mIMMsg, file);
				}

				@Override
				public void onError(int errorCode, String errorMessage) {
					AbToastUtil.showToast(mActivity, "提示：" + errorMessage);
				}

				@Override
				public void onCancel() {
				}
			});
		}
		// 开始录音
		mIMRecorder.startRecording();
	}

	public void stopRecording(boolean isCancel) {
		mIMRecorder.stopRecording(isCancel);
	}

	public void refreshTask() {
		networkWeb.post(ConstServer.IM_GROUP_FINDIMSWITHPC(
				iApp.mUser.getUserId(), iApp.mUser.getDept().getId()),
				getParams(), new AbHttpListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(String content) {
						List<GroupmsgDto> dao = (List<GroupmsgDto>) JacksonUtils
								.jsonPageResult2Bean("rows", content,
										GroupmsgDto.class);
						if (null == dao || dao.size() == 0) {
							AbToastUtil.showToast(mActivity, "没有更多内容...");
							mAbPullToRefreshView.onHeaderRefreshFinish();
							showContentView();
							if (nowPage == 1) {
								mChatMsgList.clear();
								mChatMsgViewAdapter.notifyDataSetChanged();
							}
							return;
						}
						if (nowPage == 1)
							mChatMsgList.clear();
						nowPage++;
						mChatMsgList.addAll(dao);
						Collections.sort(mChatMsgList, new Comparator() {
							@Override
							public int compare(Object lhs, Object rhs) {
								GroupmsgDto msg1 = (GroupmsgDto) lhs;
								GroupmsgDto msg2 = (GroupmsgDto) rhs;
								return msg1.getMsgTime().compareTo(
										msg2.getMsgTime());
							}
						});
						mAbPullToRefreshView.onHeaderRefreshFinish();
						mChatMsgViewAdapter.notifyDataSetChanged();
						mMsgListView.setSelection(mChatMsgList.size() - 1);
						showContentView();
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(mActivity, content);
						mAbPullToRefreshView.onHeaderRefreshFinish();
						showContentView();
					}
				});
	}

	private AbRequestParams getParams() {
		AbRequestParams params = new AbRequestParams();
		params.put("page", nowPage + "");
		params.put("rows", iApp.mConfig.getmCMsgLoadingNumberDto()
				.getImChatMsgNumber() + "");
		return params;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("操作");
		new MenuInflater(mActivity).inflate(R.menu.im_chatuser, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete:
			deleteChatMsg(menuInfo.position);
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void deleteChatMsg(final int position) {
		GroupmsgDto msgDto = mChatMsgList.get(position);
		AbRequestParams params = new AbRequestParams();
		params.put("groupmsgDto", JacksonUtils.bean2Json(msgDto));
		networkWeb.post(ConstServer.IM_GROUP_DELETE(iApp.mUser.getUserId()),
				params, new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						mChatMsgList.remove(position);
						mChatMsgViewAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(mActivity, content);
					}
				});
	}

	/**
	 * 从照相机获取
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		// 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			AbToastUtil.showToast(mActivity, "没有可用的存储卡");
		}
	}

	/**
	 * 拍照获取图片
	 */
	private void doTakePhoto() {
		try {
			mFileName = System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			mActivity.startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			AbToastUtil.showToast(mActivity, "未找到系统相机程序");
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		switch (requestCode) {
		case CLEAR_IGROUPCHATMSG:
			if (mIntent.getBooleanExtra("isClear", false)) {
				nowPage = 1;
				refreshTask();
			}
			break;
		case PHOTO_PICKED_WITH_DATA:
			Uri uri = mIntent.getData();
			String currentFilePath = getPath(uri);
			if (!AbStrUtil.isEmpty(currentFilePath)) {
				Intent intent1 = new Intent(mActivity, CropImageActivity.class);
				intent1.putExtra("PATH", currentFilePath);
				mActivity.startActivityForResult(intent1, CAMERA_CROP_DATA);
			} else {
				AbToastUtil.showToast(mActivity, "未在存储卡中找到这个文件");
			}
			break;
		case CAMERA_WITH_DATA:
			AbLogUtil.d(mActivity,
					"将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getPath());
			String currentFilePath2 = mCurrentPhotoFile.getPath();
			Intent intent2 = new Intent(mActivity, CropImageActivity.class);
			intent2.putExtra("PATH", currentFilePath2);
			mActivity.startActivityForResult(intent2, CAMERA_CROP_DATA);
			break;
		case CAMERA_CROP_DATA:
			photoPath = mIntent.getStringExtra("PATH");
			AbLogUtil.d(mActivity, "裁剪后得到的图片的路径是 = " + photoPath);

			isSendEnable = false;
			GroupmsgDto mIMMsg = new GroupmsgDto();
			mIMMsg.setGroup(mDept);
			mIMMsg.setMsgTime(new Date());
			mIMMsg.setMsgType(2);
			mIMMsg.setUserBySenderId(iApp.mUser);
			mIMMsg.setMsgContent("".getBytes());

			sendMessage(mIMMsg, new File(photoPath));
			break;
		}
	}

	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if (AbStrUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = mActivity.managedQuery(uri, projection, null, null,
				null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}
}
