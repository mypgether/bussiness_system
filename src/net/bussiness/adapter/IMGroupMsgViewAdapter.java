package net.bussiness.adapter;

import java.io.File;
import java.util.Date;
import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.dto.GroupmsgDto;
import net.bussiness.dto.UserDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.interfaces.OnDragListener;
import net.bussiness.module.base.NavActivity;
import net.bussiness.module.im.EmotionParser;
import net.bussiness.module.im.IMPlayer;
import net.bussiness.module.im.IMPlayer.OnMediaCompletionListener;
import net.bussiness.module.user.DetailProfileActivity;
import net.bussiness.tools.NetworkUtils;
import net.bussiness.tools.TimeUtils;
import net.bussiness.tools.VibrateUtils;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.ab.http.AbFileHttpResponseListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDialogUtil;

public class IMGroupMsgViewAdapter extends BaseAdapter {
	private List<GroupmsgDto> mChatMsgList;
	private EmotionParser mEmotionParser;
	private Context mContext;
	private NavActivity activity = null;

	private LayoutInflater mInflater;
	private IApplication application = null;
	private NetworkUtils net = null;
	private SoundPool sound = null;

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	public IMGroupMsgViewAdapter(Context context, List<GroupmsgDto> list,
			EmotionParser mEmotionParser) {
		this.mContext = context;
		this.mChatMsgList = list;
		this.mEmotionParser = mEmotionParser;
		this.mInflater = LayoutInflater.from(context);
		activity = (NavActivity) context;
		application = (IApplication) activity.getApplication();
		net = NetworkUtils.newInstance(context);
		sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
		sound.load(mContext, R.raw.tone_im_record_play_start, 1);
		sound.load(mContext, R.raw.tone_im_record_play_end, 1);
	}

	@Override
	public int getCount() {
		return mChatMsgList.size();
	}

	@Override
	public Object getItem(int position) {
		return mChatMsgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		GroupmsgDto mChatMsg = mChatMsgList.get(position);
		if (mChatMsg.getUserBySenderId().getUserName()
				.equals(application.mUser.getUserName())) {
			return IMsgViewType.IMVT_TO_MSG;
		}
		return IMsgViewType.IMVT_COM_MSG;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final GroupmsgDto mChatMsg = mChatMsgList.get(position);
		final boolean isComMsg = (getItemViewType(position) == IMsgViewType.IMVT_COM_MSG);
		final ViewHolder viewHolder;
		final UserDto sender = mChatMsg.getUserBySenderId();
		if (convertView == null) {
			if (isComMsg) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
			}
			viewHolder = new ViewHolder();
			viewHolder.sendTime = (TextView) convertView
					.findViewById(R.id.sendTime);
			viewHolder.chatContent = (TextView) convertView
					.findViewById(R.id.chatContent);
			viewHolder.chatImage = (ImageView) convertView
					.findViewById(R.id.chatImage);
			viewHolder.chatRecord = (Button) convertView
					.findViewById(R.id.chatRecord);
			viewHolder.userHead = (ImageView) convertView
					.findViewById(R.id.userHead);
			viewHolder.isComMsg = isComMsg;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			viewHolder.sendTime.setText(getSendTime(mChatMsg.getMsgTime()));
			viewHolder.sendTime.setVisibility(View.VISIBLE);
		} else {
			long time1 = mChatMsgList.get(position - 1).getMsgTime().getTime();
			long time2 = mChatMsg.getMsgTime().getTime();
			if ((time1 - time2 > 0 && time1 - time2 < 60 * 1000)
					|| (time2 - time1 > 0 && time2 - time1 < 60 * 1000)) {
				viewHolder.sendTime.setVisibility(View.GONE);
			} else {
				viewHolder.sendTime.setVisibility(View.VISIBLE);
				viewHolder.sendTime.setText(getSendTime(mChatMsg.getMsgTime()));
			}
		}
		if (mChatMsg.getMsgType() == 1) {
			viewHolder.chatContent.setText(mEmotionParser.decode(new String(
					mChatMsg.getMsgContent())));
			viewHolder.chatContent.setVisibility(View.VISIBLE);
			viewHolder.chatImage.setVisibility(View.GONE);
			viewHolder.chatRecord.setVisibility(View.GONE);
		} else if (mChatMsg.getMsgType() == 2) {
			application.mAbImageLoader.display(viewHolder.chatImage,
					ConstServer.IM_GROUP_DOWNLOADPHOTO(sender.getUserId(),
							new String(mChatMsg.getMsgContent())));
			viewHolder.chatImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final View mView = LayoutInflater.from(mContext).inflate(
							R.layout.photo_click_expand, null);
					final ImageView iv = (ImageView) mView
							.findViewById(R.id.photo_clicked);
					iv.setOnTouchListener(new OnDragListener() {
						@Override
						public void onStartTouch() {
							iv.setScaleType(ScaleType.MATRIX);
						}
					});
					application.mAbImageLoader.display(
							iv,
							ConstServer.IM_GROUP_DOWNLOADPHOTO(
									sender.getUserId(),
									new String(mChatMsg.getMsgContent())));
					AbDialogUtil.showFullScreenDialog(mView);
				}
			});
			viewHolder.chatContent.setVisibility(View.GONE);
			viewHolder.chatImage.setVisibility(View.VISIBLE);
			viewHolder.chatRecord.setVisibility(View.GONE);
		} else if (mChatMsg.getMsgType() == 3) {
			viewHolder.chatContent.setVisibility(View.GONE);
			viewHolder.chatImage.setVisibility(View.GONE);
			viewHolder.chatRecord.setVisibility(View.VISIBLE);
			viewHolder.chatRecord.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					sound.play(1, 1, 1, 0, 0, 1);
					VibrateUtils.Vibrate(activity, VibrateUtils.LONG_TIME);
					AbRequestParams params = new AbRequestParams();
					net.post(
							ConstServer.IM_GROUP_DOWNLOADRECORD(
									sender.getUserId(),
									new String(mChatMsg.getMsgContent())),
							params, new AbFileHttpResponseListener() {
								@Override
								public void onFailure(int statusCode,
										String content, Throwable error) {
								}

								@Override
								public void onStart() {
								}

								@Override
								public void onSuccess(int statusCode, File file) {
									IMPlayer player = IMPlayer.newInstance();
									player.setPath(file.getAbsolutePath());
									player.setOnMediaCompletionListener(new OnMediaCompletionListener() {
										@Override
										public void onCompletion() {
											VibrateUtils.Vibrate(activity,
													VibrateUtils.LONG_TIME);
											sound.play(2, 1, 1, 0, 0, 1);
										}
									});
									player.play();
								}

								@Override
								public void onFinish() {
								}
							});
				}
			});
		}
		if (null != sender.getPhotoPath()) {
			application.mAbImageLoader.display(
					viewHolder.userHead,
					ConstServer.USER_DOWNLOADLOGO(sender.getUserId(),
							sender.getPhotoPath()));
		} else {
			viewHolder.userHead.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.app_panel_friend));
		}
		viewHolder.userHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity,
						DetailProfileActivity.class);
				intent.putExtra("mUserDto", sender);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	private String getSendTime(Date msgTime) {
		try {
			if (TimeUtils.isThisWeek(msgTime)) {
				return TimeUtils.getDayForWeek(msgTime) + " " + getSXW(msgTime)
						+ getTime(msgTime);
			}
			return msgTime.getMonth() + "月" + msgTime.getDay() + "日" + " "
					+ getSXW(msgTime) + getTime(msgTime);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String getSXW(Date msgTime) {
		if (msgTime.getHours() < 12) {
			return "早上";
		}
		if (msgTime.getHours() < 18) {
			return "下午";
		}
		return "晚上";
	}

	private String getTime(Date msgTime) {
		String min = msgTime.getMinutes() + "";
		if (msgTime.getMinutes() < 10) {
			min = "0" + min;
		}
		return msgTime.getHours() + ":" + min;
	}

	static class ViewHolder {
		public TextView sendTime;
		public TextView chatContent;
		public ImageView chatImage;
		public Button chatRecord;
		public ImageView userHead;
		public boolean isComMsg = true;
	}

}
