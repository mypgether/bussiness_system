package net.bussiness.module.im;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.adapter.IMChatEmotionViewAdapter;
import net.bussiness.adapter.IMChatMsgViewAdapter;
import net.bussiness.db.ChatUserDao;
import net.bussiness.dto.ChatUserDto;
import net.bussiness.dto.ChatmsgDto;
import net.bussiness.dto.UserDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.Constants;
import net.bussiness.global.IApplication;
import net.bussiness.module.base.CropImageActivity;
import net.bussiness.tools.JacksonUtils;
import net.bussiness.tools.NetworkUtils;
import net.bussiness.tools.NotificationUtils;
import net.bussiness.tools.StringUtils;
import net.bussiness.tools.VibrateUtils;
import net.bussiness.view.SubGridview;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ab.activity.AbActivity;
import com.ab.db.storage.AbSqliteStorage;
import com.ab.db.storage.AbSqliteStorageListener.AbDataSelectListener;
import com.ab.db.storage.AbStorageQuery;
import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.titlebar.AbTitleBar;

public class ChatDetailActivity extends AbActivity {

	private AbPullToRefreshView mAbPullToRefreshView = null;
	private IApplication iApp = null;
	private AbTitleBar mAbTitleBar = null;

	private IMChatMsgViewAdapter mChatMsgViewAdapter = null;
	private List<ChatmsgDto> mChatMsgList = null;
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

	// 登录用户
	protected String userName;
	// 和谁聊天
	protected UserDto toUser;

	protected ChatUserDto iChatUserDto;
	// 我的录音类
	private IMRecorder mIMRecorder = null;

	// 当前页数
	private int nowPage = 1;
	private NetworkUtils networkWeb = null;

	// 数据库操作对象
	public AbSqliteStorage mAbSqliteStorage = null;
	public ChatUserDao mUserDao = null;
	private ChatmsgDto lastChatMsg = null;
	private int msgType = 0;

	private View mAvatarView = null;

	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;

	/* 拍照的照片存储位置 */
	private File PHOTO_DIR = null;
	// 照相机拍照得到的图片
	private File mCurrentPhotoFile;
	private String mFileName;
	private String photoPath = null;

	private SoundPool sound = null;
	// 创建BroadcastReceiver
	private BroadcastReceiver mDataReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ChatmsgDto mIMMsg = (ChatmsgDto) intent
					.getSerializableExtra("MESSAGE");
			if (mIMMsg != null) {
				if (iApp.mUser != null && toUser.getUserName() != null) {
					if (mIMMsg.getUserByReceiverId().getUserName()
							.equals(iApp.mUser.getUserName())
							&& mIMMsg.getUserBySenderId().getUserName()
									.equals(toUser.getUserName())) {
						mChatMsgList.add(mIMMsg);
						mChatMsgViewAdapter.notifyDataSetChanged();
						mMsgListView.setSelection(mChatMsgList.size() - 1);
						lastChatMsg = mIMMsg;
						msgType = 0;
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						NotificationUtils.clearNotification(context,
								intent.getIntExtra("notificationId", 0));
					}
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.imchat);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
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
		iApp = (IApplication) abApplication;
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.setTitleTextBold(false);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER, Gravity.CENTER);

		iChatUserDto = (ChatUserDto) getIntent().getSerializableExtra(
				"mChatUser");
		// 聊天对象
		toUser = new UserDto();
		toUser.setPhotoPath(iChatUserDto.getSenderLogoPath());
		toUser.setUserId(iChatUserDto.getSenderId());
		toUser.setUserName(iChatUserDto.getSenderName());

		if (iApp.mUser != null) {
			userName = iApp.mUser.getUserName();
		}
		mAbTitleBar.setTitleText("与" + toUser.getUserName() + "的会话");
		View rightViewMore = LayoutInflater.from(this).inflate(
				R.layout.more_btn, null);
		mAbTitleBar.addRightView(rightViewMore);
		Button moreBtn = (Button) rightViewMore.findViewById(R.id.moreBtn);
		moreBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChatDetailActivity.this,
						ChatInfoActivity.class);
				intent.putExtra("mUserDto", toUser);
				startActivity(intent);
			}
		});

		mContentEdit = (EditText) findViewById(R.id.content);
		mSendBtn = (Button) findViewById(R.id.sendBtn);
		mAddBtn = (ImageButton) findViewById(R.id.addBtn);
		mVoiceBtn = (ImageButton) findViewById(R.id.voiceBtn);
		mVoiceSendBtn = (Button) findViewById(R.id.voiceSendBtn);

		// 面板选项
		chatAppPanel = (RelativeLayout) this.findViewById(R.id.chatAppPanel);

		mGridView = (SubGridview) findViewById(R.id.myGrid);
		mEmotionParser = new EmotionParser(ChatDetailActivity.this);
		IMChatEmotionViewAdapter mIEmotionAdapter = new IMChatEmotionViewAdapter(
				this, mEmotionParser.getmEmotionList());
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
					AbToastUtil.showToast(ChatDetailActivity.this, "文字超出4K！");
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
					VibrateUtils.Vibrate(ChatDetailActivity.this,
							VibrateUtils.LONG_TIME);
					sound.play(1, 1, 1, 0, 0, 1);
					mVoiceSendBtn.setText("松开 结束");
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					// 结束录音
					stopRecording(false);
					VibrateUtils.Vibrate(ChatDetailActivity.this,
							VibrateUtils.LONG_TIME);
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
						AbToastUtil.showToast(ChatDetailActivity.this,
								"上一条正在发送，请稍等");
						return;
					}
					// 发送通知
					isSendEnable = false;
					// 清空文本框
					mContentEdit.setText("");
					ChatmsgDto mIMMsg = new ChatmsgDto();
					mIMMsg.setMsgTime(new Date());
					mIMMsg.setMsgType(1);
					mIMMsg.setMsgContent(mContentStr.getBytes());
					mIMMsg.setUserBySenderId(iApp.mUser);
					mIMMsg.setUserByReceiverId(toUser);

					mChatMsgList.add(mIMMsg);
					mChatMsgViewAdapter.notifyDataSetChanged();
					mMsgListView.setSelection(mChatMsgList.size() - 1);

					sendMessage(mIMMsg, null);
				}
			}
		});

		mMsgListView = (ListView) this.findViewById(R.id.mListView);
		mChatMsgList = new ArrayList<ChatmsgDto>();
		mChatMsgViewAdapter = new IMChatMsgViewAdapter(this, mChatMsgList,
				mEmotionParser);
		mMsgListView.setAdapter(mChatMsgViewAdapter);
		registerForContextMenu(mMsgListView);

		ImageButton addBtn = (ImageButton) this.findViewById(R.id.addBtn);
		final LinearLayout chatAppPanel_ll = (LinearLayout) findViewById(R.id.chatAppPanel_ll);
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

		ImageButton addEmotionBtn = (ImageButton) this
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
		ImageButton addPhotoBtn = (ImageButton) this
				.findViewById(R.id.chatAppPanel_addPhoto);
		addPhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
						AbDialogUtil.removeDialog(ChatDetailActivity.this);
						// 从相册中去获取
						try {
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT, null);
							intent.setType("image/*");
							startActivityForResult(intent,
									PHOTO_PICKED_WITH_DATA);
						} catch (ActivityNotFoundException e) {
							AbToastUtil.showToast(ChatDetailActivity.this,
									"没有找到照片");
						}
					}
				});

				camButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AbDialogUtil.removeDialog(ChatDetailActivity.this);
						doPickPhotoAction();
					}
				});

				cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AbDialogUtil.removeDialog(ChatDetailActivity.this);
					}
				});
				AbDialogUtil.showDialog(mAvatarView, Gravity.BOTTOM);
			}
		});

		networkWeb = NetworkUtils.newInstance(this);
		mAbSqliteStorage = AbSqliteStorage.getInstance(this);
		// 初始化数据库操作实现类
		mUserDao = new ChatUserDao(this);
		sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
		sound.load(this, R.raw.tone_im_start_recording, 1);
		sound.load(this, R.raw.tone_im_end_recording, 1);
		refreshTask();
	}

	@Override
	protected void onStart() {
		// 注册广播接收器
		IntentFilter mIntentFilter = new IntentFilter(
				Constants.IM_PERSON_MESSAGE_ACTION);
		registerReceiver(mDataReceiver, mIntentFilter);
		super.onStart();
	}

	private boolean isChatUserExist = false;
	private int idColumn = 0;

	@Override
	public void finish() {
		if (lastChatMsg != null) {
			saveIMMessage(lastChatMsg, msgType);
		}
		// 取消注册的广播接收器
		unregisterReceiver(mDataReceiver);
		super.finish();
	}

	/**
	 * <pre>
	 * Purpose:
	 * @author Myp
	 * Create Time: 2015-1-22 下午9:21:48
	 * @param msg
	 * @param msgType 0表示来的消息，1表示发送的消息
	 * Version: 1.0
	 * </pre>
	 */
	private void saveIMMessage(ChatmsgDto msg, int msgType) {
		UserDto user = msg.getUserBySenderId();
		if (msgType == 1) {
			user = msg.getUserByReceiverId();
		}
		// 组装ChatUserDto
		final ChatUserDto mChatUserDto = new ChatUserDto();
		mChatUserDto.setSenderId(user.getUserId());
		mChatUserDto.setSenderName(user.getUserName());
		mChatUserDto.setSenderLogoPath(user.getPhotoPath());
		mChatUserDto.setLastChatTime(msg.getMsgTime().toLocaleString());
		if (msg.getMsgType() == 1) {
			mChatUserDto.setLastChatContent(new String(msg.getMsgContent()));
		} else if (msg.getMsgType() == 2) {
			mChatUserDto.setLastChatContent("[图片]");
		} else {
			mChatUserDto.setLastChatContent("[语音]");
		}
		isChatUserExist = false;
		// 查询数据
		AbStorageQuery mAbStorageQuery = new AbStorageQuery();
		mAbStorageQuery.equals("senderId", user.getUserId());
		mAbSqliteStorage.findData(mAbStorageQuery, mUserDao,
				new AbDataSelectListener() {
					@Override
					public void onSuccess(List<?> paramList) {
						if (paramList.size() != 0) {
							isChatUserExist = true;
							idColumn = ((ChatUserDto) paramList.get(0)).getId();
						}
						if (!isChatUserExist) {
							mAbSqliteStorage.insertData(mChatUserDto, mUserDao,
									null);
						} else {
							mChatUserDto.setId(idColumn);
							mAbSqliteStorage.updateData(mChatUserDto, mUserDao,
									null);
						}
					}

					@Override
					public void onFailure(int errorCode, String errorMessage) {
						AbToastUtil.showToast(ChatDetailActivity.this,
								errorMessage);
						mAbSqliteStorage.insertData(mChatUserDto, mUserDao,
								null);
					}
				});
	}

	public void updateChatUser(String photoPath) {
		iChatUserDto.setSenderLogoPath(photoPath);
		mAbSqliteStorage.updateData(iChatUserDto, mUserDao, null);
	}

	/**
	 * 
	 * 描述：发消息
	 * 
	 * @param mIMMsg
	 * @throws
	 */
	protected void sendMessage(final ChatmsgDto mIMMsg, final File file) {
		AbRequestParams params = new AbRequestParams();
		params.put("chatmsgDto", JacksonUtils.bean2Json(mIMMsg));
		networkWeb.post(ConstServer.IM_PERSON_ADD(iApp.mUser.getUserId()),
				params, new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						lastChatMsg = mIMMsg;
						msgType = 1;
						if (file != null) {
							uploadFile(mIMMsg, file, Integer.parseInt(content));
						} else {
							AbToastUtil.showToast(ChatDetailActivity.this,
									"消息发送成功！");
						}
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(ChatDetailActivity.this,
								"消息发送失败！");
					}
				});
		isSendEnable = true;
	}

	private void uploadFile(final ChatmsgDto mIMMsg, final File file, int msgId) {
		AbRequestParams params = new AbRequestParams();
		params.put("file", file);
		String url = "";
		if (mIMMsg.getMsgType() == 2)
			url = ConstServer.IM_PERSON_UPLOADPHOTO(iApp.mUser.getUserId(),
					toUser.getUserId(), msgId);
		else if (mIMMsg.getMsgType() == 3) {
			url = ConstServer.IM_PERSON_UPLOADRECORD(iApp.mUser.getUserId(),
					toUser.getUserId(), msgId);
		}
		networkWeb.post(url, params, new AbHttpListener() {
			@Override
			public void onSuccess(String content) {
				AbToastUtil.showToast(ChatDetailActivity.this, "消息发送成功！");
				mIMMsg.setMsgContent(content.getBytes());
				mChatMsgList.add(mIMMsg);
				mChatMsgViewAdapter.notifyDataSetChanged();
				mMsgListView.setSelection(mChatMsgList.size() - 1);
				AbFileUtil.deleteFile(file);
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(ChatDetailActivity.this, "消息发送失败！");
				AbFileUtil.deleteFile(file);
			}
		});
	}

	public void startRecording() {
		if (mIMRecorder == null) {
			mIMRecorder = new IMRecorder(this, new IMRecordListener() {
				@Override
				public void onRecording() {
					AbToastUtil.showToast(ChatDetailActivity.this, "正在录音");
				}

				@Override
				public void onPreRecording() {
				}

				@Override
				public void onFinish(File file, long time) {
					if (time < 1000) {
						AbFileUtil.deleteFile(file);
						AbToastUtil.showToast(ChatDetailActivity.this,
								"语音时间太短！");
						return;
					}
					isSendEnable = true;
					ChatmsgDto mIMMsg = new ChatmsgDto();
					mIMMsg.setMsgTime(new Date());
					mIMMsg.setMsgType(3);
					mIMMsg.setMsgContent("".getBytes());
					mIMMsg.setUserBySenderId(iApp.mUser);
					mIMMsg.setUserByReceiverId(toUser);

					sendMessage(mIMMsg, file);
				}

				@Override
				public void onError(int errorCode, String errorMessage) {
					AbToastUtil.showToast(ChatDetailActivity.this, "提示："
							+ errorMessage);
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
		networkWeb.post(
				ConstServer.IM_PERSON_FINDIMSWITHPC(iApp.mUser.getUserId()),
				getParams(), new AbHttpListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						List<ChatmsgDto> dao = (List<ChatmsgDto>) JacksonUtils
								.jsonPageResult2Bean("rows", content,
										ChatmsgDto.class);
						if (null == dao || dao.size() == 0) {
							AbToastUtil.showToast(ChatDetailActivity.this,
									"没有更多内容...");
							mAbPullToRefreshView.onHeaderRefreshFinish();
							return;
						}
						mChatMsgList.addAll(dao);
						Collections.sort(mChatMsgList, new Comparator() {
							@Override
							public int compare(Object lhs, Object rhs) {
								ChatmsgDto msg1 = (ChatmsgDto) lhs;
								ChatmsgDto msg2 = (ChatmsgDto) rhs;
								return msg1.getMsgTime().compareTo(
										msg2.getMsgTime());
							}
						});
						mAbPullToRefreshView.onHeaderRefreshFinish();
						mChatMsgViewAdapter.notifyDataSetChanged();
						mMsgListView.setSelection(mChatMsgList.size() - 1);
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(ChatDetailActivity.this, content);
						mAbPullToRefreshView.onHeaderRefreshFinish();
					}
				});
		nowPage++;
	}

	private AbRequestParams getParams() {
		AbRequestParams params = new AbRequestParams();
		params.put("receiverId", toUser.getUserId() + "");
		params.put("page", nowPage + "");
		params.put("rows", iApp.mConfig.getmCMsgLoadingNumberDto()
				.getImChatMsgNumber() + "");
		return params;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("操作");
		new MenuInflater(this).inflate(R.menu.im_chatuser, menu);
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
		ChatmsgDto msgDto = mChatMsgList.get(position);
		AbRequestParams params = new AbRequestParams();
		params.put("chatmsgDto", JacksonUtils.bean2Json(msgDto));
		networkWeb.post(ConstServer.IM_PERSON_DELETE(iApp.mUser.getUserId()),
				params, new AbHttpListener() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						mChatMsgList.remove(position);
						mChatMsgViewAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(String content) {
						AbToastUtil.showToast(ChatDetailActivity.this, content);
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
			AbToastUtil.showToast(ChatDetailActivity.this, "没有可用的存储卡");
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
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			AbToastUtil.showToast(ChatDetailActivity.this, "未找到系统相机程序");
		}
	}

	/**
	 * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			Uri uri = mIntent.getData();
			String currentFilePath = getPath(uri);
			if (!AbStrUtil.isEmpty(currentFilePath)) {
				Intent intent1 = new Intent(this, CropImageActivity.class);
				intent1.putExtra("PATH", currentFilePath);
				startActivityForResult(intent1, CAMERA_CROP_DATA);
			} else {
				AbToastUtil.showToast(ChatDetailActivity.this, "未在存储卡中找到这个文件");
			}
			break;
		case CAMERA_WITH_DATA:
			AbLogUtil.d(ChatDetailActivity.class, "将要进行裁剪的图片的路径是 = "
					+ mCurrentPhotoFile.getPath());
			String currentFilePath2 = mCurrentPhotoFile.getPath();
			Intent intent2 = new Intent(this, CropImageActivity.class);
			intent2.putExtra("PATH", currentFilePath2);
			startActivityForResult(intent2, CAMERA_CROP_DATA);
			break;
		case CAMERA_CROP_DATA:
			photoPath = mIntent.getStringExtra("PATH");
			AbLogUtil
					.d(ChatDetailActivity.class, "裁剪后得到的图片的路径是 = " + photoPath);

			isSendEnable = false;
			ChatmsgDto mIMMsg = new ChatmsgDto();
			mIMMsg.setMsgTime(new Date());
			mIMMsg.setMsgType(2);
			mIMMsg.setUserBySenderId(iApp.mUser);
			mIMMsg.setUserByReceiverId(toUser);
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
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	@Override
	public void onBackPressed() {
		if (chatAppPanel.getVisibility() == View.VISIBLE) {
			chatAppPanel.setVisibility(View.GONE);
		} else
			super.onBackPressed();
	}
}