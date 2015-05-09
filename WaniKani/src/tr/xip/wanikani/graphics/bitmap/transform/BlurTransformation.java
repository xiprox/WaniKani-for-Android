package tr.xip.wanikani.graphics.bitmap.transform;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

import tr.xip.wanikani.utils.Blur;

public class BlurTransformation implements Transformation {

    Context context;

    final int BLUR_RADIUS = 5;

    public BlurTransformation(Context context) {
        this.context = context;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        Bitmap blurredBitmap = new Blur().fastblur(context, bitmap, BLUR_RADIUS);
        bitmap.recycle();
        return blurredBitmap;
    }

    @Override
    public String key() {
        return "blur";
    }
}
