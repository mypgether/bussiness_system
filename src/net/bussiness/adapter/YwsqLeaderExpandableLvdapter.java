package net.bussiness.adapter;

import java.util.List;

import net.bussiness.activities.R;
import net.bussiness.dto.YwsqDto;
import net.bussiness.tools.TimeUtils;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class YwsqLeaderExpandableLvdapter extends BaseExpandableListAdapter {
	private List<YwsqDto> group; // 组列表
	private List<List<YwsqDto>> child; // 子列表
	private Context mContext;
	private boolean isApproving = false;

	public YwsqLeaderExpandableLvdapter(Context context, List<YwsqDto> group,
			List<List<YwsqDto>> child, boolean isApproving) {
		super();
		this.mContext = context;
		this.group = group;
		this.child = child;
		this.isApproving = isApproving;
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
		return child.get(groupPosition).size();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder holder = null;
		if (getGroupCount() == 1 && null == getGroup(groupPosition)) {
			return null;
		}
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) mContext
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(
					R.layout.ywsq_leader_child_listitem, null);
			convertView.findViewById(R.id.ywsq_child_noitems).setVisibility(
					View.GONE);
			convertView.findViewById(R.id.ywsq_child_items).setVisibility(
					View.VISIBLE);
			holder = new ChildViewHolder();
			holder.tv1 = (TextView) convertView
					.findViewById(R.id.ywsq_child_tv1);
			holder.tv2 = (TextView) convertView
					.findViewById(R.id.ywsq_child_tv2);
			holder.tv3 = (TextView) convertView
					.findViewById(R.id.ywsq_child_tv3);
			holder.tv4 = (TextView) convertView
					.findViewById(R.id.ywsq_child_tv4);
			holder.tv5 = (TextView) convertView
					.findViewById(R.id.ywsq_child_tv5);
			holder.tv6 = (TextView) convertView
					.findViewById(R.id.ywsq_child_tv6);
			holder.tv7 = (TextView) convertView
					.findViewById(R.id.ywsq_child_tv7);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}
		YwsqDto childItems = child.get(groupPosition).get(childPosition);
		holder.tv1.setText("业务编号：" + childItems.getYwId() + "");
		if (isApproving) {
			holder.tv2.setText("申请人ID："
					+ childItems.getUserByProposerId().getUserId());
			holder.tv3.setText("申请人："
					+ childItems.getUserByProposerId().getUserName());
			holder.tv4.setText("申请原因：" + childItems.getReason());
			holder.tv5.setVisibility(View.GONE);
			holder.tv6.setVisibility(View.GONE);
			holder.tv7.setVisibility(View.GONE);
		} else {
			holder.tv2.setText("申请人ID："
					+ childItems.getUserByProposerId().getUserId());
			holder.tv3.setText("申请人："
					+ childItems.getUserByProposerId().getUserName());
			holder.tv4.setText("申请时间："
					+ childItems.getApplyTime().toLocaleString());
			holder.tv5.setText("申请原因：" + childItems.getReason());
			holder.tv6.setText("业务开始时间："
					+ TimeUtils.getTime(childItems.getTimestamp().getTime(),
							TimeUtils.DATE_FORMAT_DATE));
			holder.tv7.setText("业务地点：" + childItems.getLocation());
		}
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
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (getGroupCount() == 1 && null == getGroup(groupPosition)) {
			convertView = mInflater.inflate(
					R.layout.ywsq_leader_group_listitem, null);
			convertView.findViewById(R.id.ywsq_group_noitems).setVisibility(
					View.VISIBLE);
			convertView.findViewById(R.id.ywsq_group_items).setVisibility(
					View.GONE);
			holder = getGroupViewHolder(convertView, holder);
			convertView.setTag(holder);
			return convertView;
		}
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.ywsq_leader_group_listitem, null);
			convertView.findViewById(R.id.ywsq_group_noitems).setVisibility(
					View.GONE);
			convertView.findViewById(R.id.ywsq_group_items).setVisibility(
					View.VISIBLE);
			holder = getGroupViewHolder(convertView, holder);
			convertView.setTag(holder);
		} else {
			convertView.findViewById(R.id.ywsq_group_noitems).setVisibility(
					View.GONE);
			convertView.findViewById(R.id.ywsq_group_items).setVisibility(
					View.VISIBLE);
			holder = (GroupViewHolder) convertView.getTag();
		}
		YwsqDto groupItems = group.get(groupPosition);
		if (isApproving) {
			holder.tv1.setText("申请时间："
					+ groupItems.getApplyTime().toLocaleString());
			holder.tv2.setText(groupItems.getLocation());
			holder.tv3.setText("业务开始时间："
					+ TimeUtils.getTime(groupItems.getTimestamp().getTime(),
							TimeUtils.DATE_FORMAT_DATE));
			holder.tv4.setText("审批原因：" + groupItems.getReason());
		} else {
			holder.tv1.setText("审批时间："
					+ groupItems.getApproveTime().toLocaleString());
			holder.tv2.setText(groupItems.getUserByProposerId().getUserName());
			holder.tv3.setText(groupItems.getApproveReason());
			holder.tv4.setText("审批人："
					+ groupItems.getUserByApproverId().getUserName());
		}
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
		holder.tv1 = (TextView) convertView.findViewById(R.id.ywsq_group_tv1);
		holder.tv2 = (TextView) convertView.findViewById(R.id.ywsq_group_tv2);
		holder.tv3 = (TextView) convertView.findViewById(R.id.ywsq_group_tv3);
		holder.tv4 = (TextView) convertView.findViewById(R.id.ywsq_group_tv4);
		return holder;
	}

	public static class GroupViewHolder {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
	}

	public static class ChildViewHolder {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
		public TextView tv5;
		public TextView tv6;
		public TextView tv7;
	}
}