package net.bussiness.dialog.lib;

import net.bussiness.dialog.lib.effects.BaseEffects;
import net.bussiness.dialog.lib.effects.FadeIn;
import net.bussiness.dialog.lib.effects.Fall;
import net.bussiness.dialog.lib.effects.FlipH;
import net.bussiness.dialog.lib.effects.FlipV;
import net.bussiness.dialog.lib.effects.NewsPaper;
import net.bussiness.dialog.lib.effects.RotateBottom;
import net.bussiness.dialog.lib.effects.RotateLeft;
import net.bussiness.dialog.lib.effects.Shake;
import net.bussiness.dialog.lib.effects.SideFall;
import net.bussiness.dialog.lib.effects.SlideBottom;
import net.bussiness.dialog.lib.effects.SlideLeft;
import net.bussiness.dialog.lib.effects.SlideRight;
import net.bussiness.dialog.lib.effects.SlideTop;
import net.bussiness.dialog.lib.effects.Slit;

public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    private Class<? extends BaseEffects> effectsClazz;

    private Effectstype(Class<? extends BaseEffects> mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        BaseEffects bEffects=null;
	try {
		bEffects = effectsClazz.newInstance();
	} catch (ClassCastException e) {
		throw new Error("Can not init animatorClazz instance");
	} catch (InstantiationException e) {
		throw new Error("Can not init animatorClazz instance");
	} catch (IllegalAccessException e) {
		throw new Error("Can not init animatorClazz instance");
	}
	return bEffects;
    }
}
