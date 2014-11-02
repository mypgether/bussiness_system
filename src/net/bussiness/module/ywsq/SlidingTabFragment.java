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
		tabTexts.add("����ͨ��");
		tabTexts.add("�����ܾ�");
		tabTexts.add("������");
	}

	private Fragment addFragment(int flag) {
		BaseYwsq base = new BaseYwsq();
		Bundle b = new Bundle();
		b.putString("url", "http://www.baidu.com");
		base.setArguments(b);
		return base;
	}
}