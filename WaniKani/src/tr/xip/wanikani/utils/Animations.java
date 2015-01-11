package tr.xip.wanikani.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;

/**
 * Created by Hikari on 6/14/14.
 */
public class Animations {

    public static Animation FadeIn() {
        Animation anim = new AlphaAnimation(0 ,1);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(500);
        return anim;
    }

    public static Animation FadeOut() {
        Animation anim = new AlphaAnimation(1 ,0);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(500);
        return anim;
    }

    public static LayoutAnimationController FadeInController() {
        LayoutAnimationController animController = new LayoutAnimationController(FadeIn());
        animController.setDelay(LayoutAnimationController.ORDER_NORMAL);
        return animController;
    }
}
