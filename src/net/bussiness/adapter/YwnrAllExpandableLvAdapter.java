package net.bussiness.adapter;

import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.dto.YwnrDto;
import net.bussiness.dto.YwnrPhotosDto;
import net.bussiness.dto.YwpjDto;
import net.bussiness.module.ywnr.YwnrDetailActivity;
import net.bussiness.view.SubGridview;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.util.AbToastUtil;

public class YwnrAllExpandableLvAdapter extends BaseExpandableListAdapter {
	private List<YwnrDto> group; // 组列表
	private List<List<List<YwnrPhotosDto>>> child; // 子列表
	private List<List<YwpjDto>> child2;
	private YwnrDetailActivity activity;

	public YwnrAllExpandableLvAdapter(Activity activity, List<YwnrDto> group,
			List<List<List<YwnrPhotosDto>>> child, List<List<YwpjDto>> child2) {
		super();
		this.activity = (YwnrDetailActivity) activity;
		this.group = group;
		this.child = child;
		this.child2 = child2;
	}

	// -----------------Child----------------//
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return child.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	private ChildViewHolder holder = null;

	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (getGroupCount() == 1 && null == getGroup(groupPosition)) {
			return null;
		}
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) activity
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(
					R.layout.yw_detail_all_child_listitem, null);
			holder = new ChildViewHolder();
			holder.gv1 = (SubGridview) convertView.findViewById(R.id.gridview);
			holder.lv1 = (ListView) convertView.findViewById(R.id.yw_detail_lv);
			holder.et1 = (EditText) convertView
					.findViewById(R.id.yw_detail_child_remark);
			holder.btn1 = (Button) convertView
					.findViewById(R.id.yw_detail_child_submitBtn);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}
		if (child.size() > groupPosition && child.get(groupPosition) != null) {
			List<YwnrPhotosDto> photos = child.get(groupPosition).get(
					childPosition);
			if (photos != null) {
				holder.gv1.setVisibility(View.VISIBLE);
				holder.gvAdapter = new YwnrDetailGridviewAdapter(activity,
						photos);
				holder.gv1.setAdapter(holder.gvAdapter);
			}
		} else {
			holder.gv1.setVisibility(View.GONE);
		}
		if (child2.size() > groupPosition && child2.get(groupPosition) != null) {
			holder.lv1.setVisibility(View.VISIBLE);
			holder.lvAdapter = new YwnrPjAdapter(activity,
					child2.get(groupPosition));
			holder.lv1.setAdapter(holder.lvAdapter);
		} else {
			holder.lv1.setVisibility(View.GONE);
		}
		holder.et1.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String str = holder.et1.getText().toString().trim();
				int length = str.length();
				if (length > 0) {
					holder.btn1.setVisibility(View.VISIBLE);
				} else {
					holder.btn1.setVisibility(View.GONE);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				int nSelStart = 0;
				int nSelEnd = 0;
				nSelStart = holder.et1.getSelectionStart();
				nSelEnd = holder.et1.getSelectionEnd();
				String str = holder.et1.getText().toString().trim();
				if (str.getBytes().length > 4 * 1024) {
					AbToastUtil.showToast(activity, "文字超出4K！");
					s.delete(nSelStart - 1, nSelEnd);
					holder.et1.setTextKeepState(s);
				}
			}
		});
		holder.btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AbToastUtil.showToast(activity, "正在发表评论...");
				activity.addYwnrPjs((YwnrDto) getGroup(groupPosition),
						groupPosition, holder.et1.getText().toString().trim());
				holder.et1.setText("");
			}
		});
		return convertView;
	}

	// ----------------Group----------------//
	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public int getGroupCount() {
		return group.size();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder holder = null;
		LayoutInflater mInflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (getGroupCount() == 1 && null == getGroup(groupPosition)) {
			convertView = mInflater.inflate(
					R.layout.yw_detail_all_group_listitem, null);
			convertView.findViewById(R.id.yw_detail_group_noitems)
					.setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.yw_detail_group_items).setVisibility(
					View.GONE);
			holder = getGroupViewHolder(convertView, holder);
			convertView.setTag(holder);
			return convertView;
		}
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.yw_detail_all_group_listitem, null);
			convertView.findViewById(R.id.yw_detail_group_noitems)
					.setVisibility(View.GONE);
			convertView.findViewById(R.id.yw_detail_group_items).setVisibility(
					View.VISIBLE);
			holder = getGroupViewHolder(convertView, holder);
			convertView.setTag(holder);
		} else {
			convertView.findViewById(R.id.yw_detail_group_noitems)
					.setVisibility(View.GONE);
			convertView.findViewById(R.id.yw_detail_group_items).setVisibility(
					View.VISIBLE);
			holder = (GroupViewHolder) convertView.getTag();
		}
		YwnrDto groupItems = group.get(groupPosition);
		holder.tv1.setText("业务时间：" + groupItems.getNrTime().toLocaleString());
		holder.tv2.setText("业务地点：" + groupItems.getNrLocation());
		holder.tv3.setText("实际费用：" + groupItems.getFee());
		holder.tv4.setText("备注：" + groupItems.getDescription());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private GroupViewHolder getGroupViewHolder(View convertView,
			GroupViewHolder holder) {
		holder = new GroupViewHolder();
		holder.tv1 = (TextView) convertView
				.findViewById(R.id.yw_detail_group_tv1);
		holder.tv2 = (TextView) convertView
				.findViewById(R.id.yw_detail_group_tv2);
		holder.tv3 = (TextView) convertView
				.findViewById(R.id.yw_detail_group_tv3);
		holder.tv4 = (TextView) convertView
				.findViewById(R.id.yw_detail_group_tv4);
		return holder;
	}

	public static class GroupViewHolder {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
	}

	public static class ChildViewHolder {
		public SubGridview gv1;
		public YwnrDetailGridviewAdapter gvAdapter;
		public ListView lv1;
		public YwnrPjAdapter lvAdapter;
		public EditText et1;
		public Button btn1;
	}
}