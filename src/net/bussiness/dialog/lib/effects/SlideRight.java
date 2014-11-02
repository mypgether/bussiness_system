package net.bussiness.dialog.lib.effects;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;

public class SlideRight extends BaseEffects {

	@Override
	protected void setupAnimation(View view) {
		getAnimatorSet().playTogether(
				ObjectAnimator.ofFloat(view, "translationX", 300, 0)
						.setDuration(mDuration),
				ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(
						mDuration * 3 / 2)

		);
	}
}
