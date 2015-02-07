package net.bussiness.interfaces;

import android.view.MotionEvent;
import android.view.View;

public abstract class OnDoubleClickListener implements View.OnTouchListener {
	private int count = 0;
	private long firClick;
	private long secClick;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (MotionEvent.ACTION_DOWN == event.getAction()) {
			count++;
			if (count == 1) {
				firClick = System.currentTimeMillis();
			} else if (count == 2) {
				secClick = System.currentTimeMillis();
				if (secClick - firClick < 1000) {
					// 双击事件
					onDoubleClick();
				}
				count = 0;
				firClick = 0;
				secClick = 0;
			}
		}
		return true;
	}

	public abstract void onDoubleClick();
}