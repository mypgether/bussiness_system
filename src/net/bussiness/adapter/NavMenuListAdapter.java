package net.bussiness.adapter;

import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.dto.NavMenuItemDto;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavMenuListAdapter extends BaseAdapter {

	private Context context;
	private List<NavMenuItemDto> navDrawerItems;

	public NavMenuListAdapter(Context context,
			List<NavMenuItemDto> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.menu_lv_row, null);
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

		imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
		txtTitle.setText(navDrawerItems.get(position).getTitle());

		// displaying count
		// check whether it set visible or not
		if (navDrawerItems.get(position).getCounterVisibility()) {
			txtCount.setText(navDrawerItems.get(position).getCount() + "");
		} else {
			txtCount.setVisibility(View.GONE);
		}
		return convertView;
	}
}
