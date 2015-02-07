package net.bussiness.adapter;

import java.util.List;

import net.bussiness.activities.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class IMChatEmotionViewAdapter extends BaseAdapter {
	private List<Integer> mEmotionList;
	private LayoutInflater inflater;

	public IMChatEmotionViewAdapter(Activity activity,
			List<Integer> mEmotionList) {
		super();
		this.mEmotionList = mEmotionList;
		inflater = LayoutInflater.from(activity);
	}

	@Override
	public int getCount() {
		return mEmotionList.size();
	}

	@Override
	public Object getItem(int position) {
		return mEmotionList.get(position);
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
		viewHodler.iv.setImageResource(mEmotionList.get(position));
		return convertView;
	}

	public static class ViewHodler {
		private ImageView iv;
	}

	public interface OnEmotionClickListener {
		public void onEmotionClick(int clickPosition);
	}
}
