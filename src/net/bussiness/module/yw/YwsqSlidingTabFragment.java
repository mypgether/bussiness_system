package net.bussiness.module.yw;

import net.bussiness.module.base.BaseSlidingTabFragment;
import android.app.Fragment;
import android.os.Bundle;

public class YwsqSlidingTabFragment extends BaseSlidingTabFragment {

	@Override
	public void initSlidingTab() {
		mFragments.add(addFragment(2));
		mFragments.add(addFragment(1));
		mFragments.add(addFragment(0));

		tabTexts.add("审批拒绝");
		tabTexts.add("审批通过");
		tabTexts.add("待审批");
	}

	private Fragment addFragment(int approveState) {
		BaseYwsqFragment base = new BaseYwsqFragment();
		Bundle b = new Bundle();
		b.putInt("approveState", approveState);
		base.setArguments(b);
		return base;
	}
}