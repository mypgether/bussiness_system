package net.bussiness.adapter;

import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.dto.YwpjDto;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class YwnrPjAdapter extends BaseAdapter {
	private List<YwpjDto> mYwpjList;
	private LayoutInflater mInflater;

	public YwnrPjAdapter() {
	}

	public YwnrPjAdapter(Context context, List<YwpjDto> mYwpjList) {
		this.mYwpjList = mYwpjList;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mYwpjList.size();
	}

	@Override
	public Object getItem(int position) {
		return mYwpjList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		YwpjDto ywpjDto = mYwpjList.get(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ywpj, null);
			viewHolder = new ViewHolder();
			viewHolder.remarkerName = (TextView) convertView
					.findViewById(R.id.remarkerName);
			viewHolder.remark = (TextView) convertView
					.findViewById(R.id.remark);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.remarkerName.setText(ywpjDto.getUserByRemarkerId()
				.getUserName() + ":");
		viewHolder.remark.setText(new String(ywpjDto.getRemark()));
		return convertView;
	}

	static class ViewHolder {
		public TextView remarkerName;
		public TextView remark;
	}
}
