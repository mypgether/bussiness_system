package net.bussiness.module.base;

import java.util.ArrayList;

import net.bussiness.activities.R;
import net.bussiness.adapter.NavMenuListAdapter;
import net.bussiness.dto.NavMenuItemDto;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.ab.fragment.AbFragment;

public class NavMenuFragment extends AbFragment implements OnItemClickListener {
	private ListView mDrawerList;
	private String[] mNavMenuTitles;
	private TypedArray mNavMenuIconsTypeArray;
	private ArrayList<NavMenuItemDto> mNavDrawerItems;
	private NavMenuListAdapter mAdapter;
	private SLMenuOnItemClickListener mCallback;
	private int selected = -1;

	@Override
	public void onAttach(Activity activity) {
		try {
			mCallback = (SLMenuOnItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnResolveTelsCompletedListener");
		}
		super.onAttach(activity);
	}

	@Override
	public View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_layout, null);
		mDrawerList = (ListView) view.findViewById(R.id.menu_lv);
		ImageView iv = (ImageView) view.findViewById(R.id.menu_user_logo);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.selectLogo();
			}
		});
		findView();
		this.setAbFragmentOnLoadListener(new AbFragmentOnLoadListener() {
			@Override
			public void onLoad() {
				showContentView();
			}
		});
		return view;
	}

	private void findView() {
		mNavMenuTitles = getResources()
				.getStringArray(R.array.nav_drawer_items_normal);
		// nav drawer icons from resources
		mNavMenuIconsTypeArray = getResources().obtainTypedArray(
				R.array.nav_drawer_icons_normal);
		mNavDrawerItems = new ArrayList<NavMenuItemDto>();

		// adding nav drawer items to array
		// Home
		mNavDrawerItems.add(new NavMenuItemDto(mNavMenuTitles[0],
				mNavMenuIconsTypeArray.getResourceId(0, -1)));
		// Find People
		mNavDrawerItems.add(new NavMenuItemDto(mNavMenuTitles[1],
				mNavMenuIconsTypeArray.getResourceId(1, -1)));
		// Photos
		mNavDrawerItems.add(new NavMenuItemDto(mNavMenuTitles[2],
				mNavMenuIconsTypeArray.getResourceId(2, -1)));
		// Communities, Will add a counter here
		mNavDrawerItems.add(new NavMenuItemDto(mNavMenuTitles[3],
				mNavMenuIconsTypeArray.getResourceId(3, -1), true, "22"));
		// Pages
		mNavDrawerItems.add(new NavMenuItemDto(mNavMenuTitles[4],
				mNavMenuIconsTypeArray.getResourceId(4, -1)));
		// What's hot, We will add a counter here
		mNavDrawerItems.add(new NavMenuItemDto(mNavMenuTitles[5],
				mNavMenuIconsTypeArray.getResourceId(5, -1), true, "50+"));

		// Recycle the typed array
		mNavMenuIconsTypeArray.recycle();

		// setting the nav drawer list adapter
		mAdapter = new NavMenuListAdapter(getActivity(), mNavDrawerItems);
		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemClickListener(this);

		if (selected != -1) {
			mDrawerList.setItemChecked(selected, true);
			mDrawerList.setSelection(selected);
		} else {
			mDrawerList.setItemChecked(0, true);
			mDrawerList.setSelection(0);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		if (mCallback != null) {
			mCallback.selectItem(position, mNavMenuTitles[position]);
		}
		selected = position;
	}

	/**
	 * 侧边栏点击监听器
	 */
	public interface SLMenuOnItemClickListener {

		public void selectItem(int position, String title);

		public void selectLogo();
	}
}
