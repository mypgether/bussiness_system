package net.bussiness.module.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bussiness.activities.R;
import net.bussiness.dialog.lib.Effectstype;
import net.bussiness.dialog.lib.NiftyDialogBuilder;
import net.bussiness.dto.ChatUserDto;
import net.bussiness.dto.ChatmsgDto;
import net.bussiness.dto.GroupmsgDto;
import net.bussiness.dto.NavMenuItemDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.Constants;
import net.bussiness.global.IApplication;
import net.bussiness.interfaces.OnPushMessageListener;
import net.bussiness.module.base.NavMenuFragment.SLMenuOnItemClickListener;
import net.bussiness.module.im.ChatGroupDetailFragment;
import net.bussiness.module.im.ChatGroupInfoActivity;
import net.bussiness.module.im.ChatUserFragment;
import net.bussiness.module.user.EditProfileActivity;
import net.bussiness.module.user.IndividualCenterFragment;
import net.bussiness.module.user.LoginActivity;
import net.bussiness.module.yw.YwsqAddActivity;
import net.bussiness.module.yw.YwsqSlidingTabFragment;
import net.bussiness.module.ywnr.YwnrFragment;
import net.bussiness.receiver.PushMessageReceiver;
import net.bussiness.tools.BDPushUtils;
import net.bussiness.tools.JacksonUtils;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.db.storage.AbSqliteStorageListener.AbDataSelectListener;
import com.ab.db.storage.AbStorageQuery;
import com.ab.util.AbToastUtil;
import com.ab.view.slidingmenu.SlidingMenu;
import com.ab.view.slidingmenu.SlidingMenu.CanvasTransformer;
import com.ab.view.slidingmenu.SlidingMenu.OnOpenedListener;
import com.ab.view.titlebar.AbTitleBar;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

@SuppressLint("UseSparseArrays")
public class NavActivity extends FragmentActivity implements
		SLMenuOnItemClickListener, OnOpenedListener, OnPushMessageListener {
	private static boolean isFristOpen = true;
	private int nowIndex = 0;
	private SlidingMenu menu;
	public AbTitleBar mAbTitleBar;
	private List<Fragment> list;
	private Map<Integer, Fragment> index2Fragment;
	private IApplication iApp = null;

	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFristOpen) {
			loadBdPushConfig();
			isFristOpen = false;
		}
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.sliding_menu_content);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		mAbTitleBar = new AbTitleBar(this);
		mAbTitleBar.setTitleText(R.string.individual_center_title);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(
				R.drawable.button_selector_menu);
		LinearLayout titleBarLinearLayout = (LinearLayout) this
				.findViewById(R.id.titleBar);
		LinearLayout.LayoutParams layoutParamsFF = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleBarLinearLayout.addView(mAbTitleBar, layoutParamsFF);
		mAbTitleBar.getLogoView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				menu.toggle();
			}
		});

		// 添加Fragment
		getFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new IndividualCenterFragment())
				.commit();

		// SlidingMenu
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		// slidingmenu设置为TOUCHMODE_MARGIN
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// menu添加到Fragment当中
		menu.setMenu(R.layout.sliding_menu_menu);
		menu.setOnOpenedListener(this);
		getFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new NavMenuFragment()).commit();
		menu.setBehindCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.drawColor(NavActivity.this.getResources().getColor(
						R.color.gray_white));
				canvas.translate(
						0,
						canvas.getHeight()
								* (1 - interp.getInterpolation(percentOpen)));
			}
		});

		// init data
		list = new ArrayList<Fragment>();
		iApp = (IApplication) this.getApplication();
		list.add(new IndividualCenterFragment());
		list.add(new ChatUserFragment());
		list.add(new ChatGroupDetailFragment());
		list.add(new YwsqSlidingTabFragment());
		list.add(new YwnrFragment());
		index2Fragment = new HashMap<Integer, Fragment>();
		index2Fragment.put(0, new IndividualCenterFragment());
		setCustomRightAbTitleBar(0);
	}

	private void loadBdPushConfig() {
		PushMessageReceiver.mActivity = NavActivity.this;
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				BDPushUtils.getMetaValue(NavActivity.this, "api_key"));
		// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
		// PushManager.enableLbs(getApplicationContext());
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			exitApp();
		}
	}

	private void switchAccount() {
		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
				.getInstance(this);
		dialogBuilder.withTitle("切换账号").withTitleColor("#FFFFFF")
				.withDividerColor("#11000000").withMessage("确定切换账号?")
				.withMessageColor("#FFFFFFFF").withDialogColor("#FFE74C3C")
				.withIcon(getResources().getDrawable(R.drawable.ic_launcher))
				.withDuration(100) // def
				.withEffect(Effectstype.Slidetop) // def Effectstype.Slidetop
				.withButton1Text("确定") // def gone
				.withButton2Text("取消") // def gone
				.isCancelableOnTouchOutside(false) // def | isCancelable(true)
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						iApp.switchAccount();
						startActivity(new Intent(NavActivity.this,
								LoginActivity.class));
						dialogBuilder.dismiss();
						finish();
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();
	}

	private void exitApp() {
		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
				.getInstance(this);
		dialogBuilder.withTitle("退出系统")
				// .withTitle(null) no title
				.withTitleColor("#FFFFFF")
				// def
				.withDividerColor("#11000000")
				// def
				.withMessage("确定退出出差管理系统?")
				// .withMessage(null) no Msg
				.withMessageColor("#FFFFFFFF")
				// def | withMessageColor(int resid)
				.withDialogColor("#FFE74C3C")
				// def | withDialogColor(int resid)
				.withIcon(getResources().getDrawable(R.drawable.ic_launcher))
				.withDuration(300) // def
				.withEffect(Effectstype.Newspager) // def Effectstype.Slidetop
				.withButton1Text("确定") // def gone
				.withButton2Text("取消") // def gone
				.isCancelableOnTouchOutside(true) // def | isCancelable(true)
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						System.exit(0);
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();
	}

	@Override
	public void selectItem(int position, String title) {
		if (nowIndex == position) {
			menu.toggle();
			refreshNavMenu(position, 2);
		} else if (position == 5) {
			switchAccount();
		} else {
			switchContent(position, title);
		}
	}

	/**
	 * 切换主屏幕的内容
	 */
	public void switchContent(int position, String title) {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.hide(index2Fragment.get(nowIndex));
		nowIndex = position;
		if (index2Fragment.containsKey(nowIndex)) {
			transaction.show(index2Fragment.get(nowIndex));
		} else {
			index2Fragment.put(nowIndex, list.get(nowIndex));
			transaction.add(R.id.content_frame, list.get(nowIndex));
		}
		mAbTitleBar.setTitleText(title);
		setCustomRightAbTitleBar(position);
		transaction.commit();
		refreshNavMenu(position, 2);
		if (menu.isMenuShowing())
			menu.toggle();
	}

	/**
	 * <pre>
	 * Purpose:针对不同模块个性化标题栏右边按钮
	 * @author Myp
	 * Create Time: 2015-1-10 下午2:49:44
	 * @param position
	 * Version: 1.0
	 * </pre>
	 */
	private void setCustomRightAbTitleBar(final int position) {
		/*
		 * 业务申请和业务签到模块中，右侧栏右边添加个性化
		 */
		mAbTitleBar.clearRightView();
		if (position == 0) {
			View rightViewMore = LayoutInflater.from(this).inflate(
					R.layout.edit_btn, null);
			mAbTitleBar.addRightView(rightViewMore);
			Button editBtn = (Button) rightViewMore.findViewById(R.id.editBtn);
			editBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(NavActivity.this,
							EditProfileActivity.class));
				}
			});
		} else if (position == 2) {
			View rightViewMore = LayoutInflater.from(this).inflate(
					R.layout.more_btn, null);
			mAbTitleBar.addRightView(rightViewMore);
			Button moreBtn = (Button) rightViewMore.findViewById(R.id.moreBtn);
			moreBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NavActivity.this,
							ChatGroupInfoActivity.class);
					intent.putExtra("mDept", iApp.mUser.getDept());
					startActivityForResult(intent,
							ChatGroupDetailFragment.CLEAR_IGROUPCHATMSG);
				}
			});
		} else if (position == 3 && iApp.mUserIdentity == 2) {
			View rightViewMore = LayoutInflater.from(this).inflate(
					R.layout.add_btn, null);
			mAbTitleBar.addRightView(rightViewMore);
			Button addBtn = (Button) rightViewMore.findViewById(R.id.addBtn);
			addBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(NavActivity.this,
							YwsqAddActivity.class));
				}
			});
		}
	}

	@Override
	public void selectLogo() {
		startActivity(new Intent(NavActivity.this, EditProfileActivity.class));
	}

	@Override
	public void onOpened() {
		View view = getFragmentManager().findFragmentById(R.id.menu_frame)
				.getView();
		// shake logo
		view.findViewById(R.id.menu_user_logo).startAnimation(
				AnimationUtils.loadAnimation(NavActivity.this, R.anim.shake));
		String logoPath = iApp.mUser.getPhotoPath();
		if (null != logoPath) {
			iApp.mAbImageLoader.display((ImageView) view
					.findViewById(R.id.menu_user_logo), ConstServer
					.USER_DOWNLOADLOGO(iApp.mUser.getUserId(),
							iApp.mUser.getPhotoPath()));
		}
		// shake counter
		ListView lv = (ListView) view.findViewById(R.id.menu_lv);
		for (int i = 0; i < lv.getChildCount(); i++) {
			RelativeLayout layout = (RelativeLayout) lv.getChildAt(i);
			TextView cntTv = (TextView) layout.findViewById(R.id.counter);
			if (!"".equals(cntTv.getText())) {
				cntTv.startAnimation(AnimationUtils.loadAnimation(
						NavActivity.this, R.anim.shake));
			}
		}
	}

	/**
	 * <pre>
	 * Purpose:刷新菜单栏
	 * @author Myp
	 * Create Time: 2015-1-15 下午10:18:45
	 * @param index 刷新的位置
	 * @param refreshType 刷新模式，1为增加，2为清空
	 * </pre>
	 */
	private void refreshNavMenu(int index, int refreshType) {
		NavMenuFragment menuFragment = (NavMenuFragment) getFragmentManager()
				.findFragmentById(R.id.menu_frame);
		try {
			NavMenuItemDto temp = menuFragment.getmNavDrawerItems().get(index);
			if (refreshType == 1) {
				temp.setCounterVisibility(true);
				temp.setCount(temp.getCount() + 1);
			} else if (refreshType == 2) {
				temp.setCounterVisibility(false);
				temp.setCount(0);
			}
			menuFragment.getmAdapter().notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReceiveIMMessage(String content, int msgType,
			int notificationId) {
		if (msgType == 1) {
			refreshNavMenu(1, 1);
			ChatmsgDto msg = (ChatmsgDto) JacksonUtils.json2Bean(content,
					ChatmsgDto.class);
			saveIMMessage(msg);
			sendIMPersonBoardcast(msg, notificationId);
		} else if (msgType == 0) {
			if (nowIndex != 2)
				refreshNavMenu(2, 1);
			if (index2Fragment.containsKey(2)) {
				ChatGroupDetailFragment fragment = (ChatGroupDetailFragment) index2Fragment
						.get(2);
				GroupmsgDto msg = (GroupmsgDto) JacksonUtils.json2Bean(content,
						GroupmsgDto.class);
				fragment.onReceiveGroupMessage(msg, notificationId);
			}
		}
	}

	@Override
	public void onReceiveYWSQMessage(String content, int notificationId) {
		refreshNavMenu(3, 1);
	}

	@Override
	public void onReceiveYWSPMessage(String content, int notificationId) {
		refreshNavMenu(3, 1);
	}

	@Override
	public void onReceiveYWPJMessage(String content, int notificationId) {
		refreshNavMenu(4, 1);
	}

	@Override
	public void onReceiveYWNRMessage(String content, int notificationId) {
		refreshNavMenu(4, 1);
	}

	private boolean isChatUserExist = false;
	private int idColumn = 0;

	private void saveIMMessage(ChatmsgDto msg) {
		// 组装ChatUserDto
		final ChatUserDto mChatUserDto = new ChatUserDto();
		mChatUserDto.setSenderId(msg.getUserBySenderId().getUserId());
		mChatUserDto.setSenderName(msg.getUserBySenderId().getUserName());
		mChatUserDto.setSenderLogoPath(msg.getUserBySenderId().getPhotoPath());
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
		mAbStorageQuery.equals("senderId", msg.getUserBySenderId().getUserId());
		iApp.mAbSqliteStorage.findData(mAbStorageQuery, iApp.mUserDao,
				new AbDataSelectListener() {
					@Override
					public void onSuccess(List<?> paramList) {
						if (paramList.size() != 0) {
							isChatUserExist = true;
							idColumn = ((ChatUserDto) paramList.get(0)).getId();
						}
						if (!isChatUserExist) {
							iApp.mAbSqliteStorage.insertData(mChatUserDto,
									iApp.mUserDao, null);
						} else {
							mChatUserDto.setId(idColumn);
							iApp.mAbSqliteStorage.updateData(mChatUserDto,
									iApp.mUserDao, null);
						}
					}

					@Override
					public void onFailure(int errorCode, String errorMessage) {
						AbToastUtil.showToast(NavActivity.this, errorMessage);
						iApp.mAbSqliteStorage.insertData(mChatUserDto,
								iApp.mUserDao, null);
					}
				});
	}

	private void sendIMPersonBoardcast(ChatmsgDto msg, int notificationId) {
		Intent intent = new Intent(Constants.IM_PERSON_MESSAGE_ACTION);
		intent.putExtra("MESSAGE", msg);
		intent.putExtra("notificationId", notificationId);
		sendBroadcast(intent);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (nowIndex == 2) {
			try {
				ChatGroupDetailFragment fragment = (ChatGroupDetailFragment) getFragmentManager()
						.findFragmentById(R.id.content_frame);
				fragment.onActivityResult(arg0, arg1, arg2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}