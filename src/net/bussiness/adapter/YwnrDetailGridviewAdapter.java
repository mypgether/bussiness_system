package net.bussiness.adapter;

import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.dto.YwnrPhotosDto;
import net.bussiness.global.ConstServer;
import net.bussiness.global.IApplication;
import net.bussiness.interfaces.OnDragListener;
import net.bussiness.module.ywnr.YwnrDetailActivity;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ab.image.AbImageLoader;
import com.ab.util.AbDialogUtil;

public class YwnrDetailGridviewAdapter extends BaseAdapter {
	private List<YwnrPhotosDto> photosDtos;
	private YwnrDetailActivity activity;
	private LayoutInflater inflater;
	// 图片下载器
	private AbImageLoader mAbImageLoader = null;

	public YwnrDetailGridviewAdapter(Activity activity,
			List<YwnrPhotosDto> photosDtos) {
		super();
		this.photosDtos = photosDtos;
		this.activity = (YwnrDetailActivity) activity;
		inflater = LayoutInflater.from(activity);
		mAbImageLoader = ((IApplication) activity.getApplication()).mAbImageLoader;
	}

	@Override
	public int getCount() {
		return photosDtos.size();
	}

	@Override
	public Object getItem(int position) {
		return photosDtos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.yw_detail_gridview_item,
					null);
			viewHodler = new ViewHodler();
			viewHodler.iv = (ImageView) convertView
					.findViewById(R.id.ItemImage);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		mAbImageLoader.display(viewHodler.iv, ConstServer
				.YWNR_PHOTO_DOWNLOADPHOTO(photosDtos.get(position).getYwnr()
						.getYwsq().getUserByProposerId().getUserId(),
						photosDtos.get(position).getYwnr().getYwsq().getYwId(),
						photosDtos.get(position).getYwnr().getId(), photosDtos
								.get(position).getPhotoPath()));
		viewHodler.iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final View mView = inflater.inflate(
						R.layout.photo_click_expand, null);
				final ImageView iv = (ImageView) mView
						.findViewById(R.id.photo_clicked);
				iv.setOnTouchListener(new OnDragListener() {
					@Override
					public void onStartTouch() {
						iv.setScaleType(ScaleType.MATRIX);
					}
				});
				mAbImageLoader.display(iv, ConstServer
						.YWNR_PHOTO_DOWNLOADPHOTO(photosDtos.get(position)
								.getYwnr().getYwsq().getUserByProposerId()
								.getUserId(), photosDtos.get(position)
								.getYwnr().getYwsq().getYwId(),
								photosDtos.get(position).getYwnr().getId(),
								photosDtos.get(position).getPhotoPath()));
				AbDialogUtil.showFullScreenDialog(mView);
			}
		});
		return convertView;
	}

	public static class ViewHodler {
		public ImageView iv;
	}
}
