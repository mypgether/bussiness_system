package net.bussiness.module.ywsq;

import net.bussiness.module.base.BaseSlidingTabFragment;
import net.bussiness.tools.ConstServer;
import net.bussiness.tools.IApplication;
import android.app.Fragment;
import android.os.Bundle;

public class YwsqSlidingTabFragment extends BaseSlidingTabFragment {

	@Override
	public void initSlidingTab() {
		mFragments.add(addFragment(1));
		mFragments.add(addFragment(2));
		mFragments.add(addFragment(0));
		tabTexts.add("审批通过");
		tabTexts.add("审批拒绝");
		tabTexts.add("待审批");
	}

	private Fragment addFragment(int approveState) {
		BaseYwsq base = new BaseYwsq();
		Bundle b = new Bundle();
		IApplication iApp = (IApplication) getActivity().getApplication();
		b.putString("url",ConstServer.YWSQ_FINDPROPOSE_WITHPC(iApp.getCurrentUser().getUserId() + ""));
		b.putInt("approveState", approveState);
		base.setArguments(b);
		return base;
	}
}