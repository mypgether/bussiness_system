package net.bussiness.adapter;

import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.dto.ChatUserDto;
import net.bussiness.global.ConstServer;
import net.bussiness.module.im.EmotionParser;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.image.AbImageLoader;

public class IMChatUserdapter extends BaseAdapter {

	private Context mContext;
	private List<ChatUserDto> mChatUser;
	// 图片下载器
	private AbImageLoader mAbImageLoader = null;
	private EmotionParser mEmotionParser;

	public IMChatUserdapter(Context context, List<ChatUserDto> mChatUser) {
		this.mContext = context;
		this.mChatUser = mChatUser;

		mEmotionParser = new EmotionParser(mContext);
		// 图片下载器
		mAbImageLoader = new AbImageLoader(context);
		mAbImageLoader.setMaxWidth(0);
		mAbImageLoader.setMaxHeight(0);
		mAbImageLoader.setLoadingImage(R.drawable.image_loading);
		mAbImageLoader.setErrorImage(R.drawable.image_error);
		mAbImageLoader.setEmptyImage(R.drawable.image_empty);
	}

	@Override
	public int getCount() {
		return mChatUser.size();
	}

	@Override
	public Object getItem(int position) {
		return mChatUser.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		ChatUserDto dto = mChatUser.get(position);
		ViewHolder viewHolder = null;
		if (getCount() == 1 && null == dto) {
			convertView = mInflater.inflate(R.layout.im_chatuser, null);
			convertView.findViewById(R.id.im_chatuser_noitems).setVisibility(
					View.VISIBLE);
			convertView.findViewById(R.id.im_chatuser_items).setVisibility(
					View.GONE);
			viewHolder = getViewHolder(convertView, viewHolder);
			convertView.setTag(viewHolder);
			return convertView;
		}
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.im_chatuser, null);
			convertView.findViewById(R.id.im_chatuser_noitems).setVisibility(
					View.GONE);
			convertView.findViewById(R.id.im_chatuser_items).setVisibility(
					View.VISIBLE);
			viewHolder = getViewHolder(convertView, viewHolder);
			convertView.setTag(viewHolder);
		} else {
			convertView.findViewById(R.id.im_chatuser_noitems).setVisibility(
					View.GONE);
			convertView.findViewById(R.id.im_chatuser_items).setVisibility(
					View.VISIBLE);
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.lastChatTime.setText(dto.getLastChatTime());
		viewHolder.lastChatContent.setText(mEmotionParser.decode(dto
				.getLastChatContent()));
		viewHolder.senderName.setText(dto.getSenderName());
		if (null != dto.getSenderLogoPath()) {
			mAbImageLoader.display(
					viewHolder.senderLogo,
					ConstServer.USER_DOWNLOADLOGO(dto.getSenderId(),
							dto.getSenderLogoPath()));
		} else {
			viewHolder.senderLogo.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.app_panel_friend));
		}
		return convertView;
	}

	private ViewHolder getViewHolder(View convertView, ViewHolder viewHolder) {
		viewHolder = new ViewHolder();
		viewHolder.senderLogo = (ImageView) convertView
				.findViewById(R.id.im_chatuser_logo);
		viewHolder.lastChatTime = (TextView) convertView
				.findViewById(R.id.im_chatuser_time);
		viewHolder.lastChatContent = (TextView) convertView
				.findViewById(R.id.im_chatuser_content);
		viewHolder.senderName = (TextView) convertView
				.findViewById(R.id.im_chatuser_name);
		return viewHolder;
	}

	public final class ViewHolder {
		private TextView senderName;
		private TextView lastChatTime;
		private TextView lastChatContent;
		private ImageView senderLogo;
	}
}
