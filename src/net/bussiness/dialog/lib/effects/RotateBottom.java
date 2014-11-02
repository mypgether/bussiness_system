package net.bussiness.dialog.lib.effects;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;

public class RotateBottom extends BaseEffects {

	@Override
	protected void setupAnimation(View view) {
		getAnimatorSet().playTogether(
				ObjectAnimator.ofFloat(view, "rotationX", 90, 0).setDuration(
						mDuration),
				ObjectAnimator.ofFloat(view, "translationY", 300, 0)
						.setDuration(mDuration),
				ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(
						mDuration * 3 / 2)

		);
	}
}
