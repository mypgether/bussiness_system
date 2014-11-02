package net.bussiness.module.ywsq;

import net.bussiness.module.base.BaseSlidingTabFragment;
import android.app.Fragment;
import android.os.Bundle;

public class SlidingTabFragment extends BaseSlidingTabFragment {

	@Override
	public void initSlidingTab() {
		mFragments.add(addFragment(1));
		mFragments.add(addFragment(2));
		mFragments.add(addFragment(0));
		tabTexts.add("审批通过");
		tabTexts.add("审批拒绝");
		tabTexts.add("待审批");
	}

	private Fragment addFragment(int flag) {
		BaseYwsq base = new BaseYwsq();
		Bundle b = new Bundle();
		b.putString("url", "http://www.baidu.com");
		base.setArguments(b);
		return base;
	}
}