package ee.locawork.util;

import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

public class AnimationUtil {
    public static void animateBubble(View view) {
        ScaleAnimation scale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
        scale.setDuration(300);
        scale.setInterpolator(new OvershootInterpolator());
        view.startAnimation(scale);
    }
    public static void animateAdd(View view) {
        ScaleAnimation scale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
        scale.setDuration(3000);
        scale.setInterpolator(new OvershootInterpolator());
        view.startAnimation(scale);
    }

}
