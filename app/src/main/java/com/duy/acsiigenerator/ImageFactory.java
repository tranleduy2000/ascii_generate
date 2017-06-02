package com.duy.acsiigenerator;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by Duy on 02-Jun-17.
 */

public class ImageFactory {
    public static Bitmap createImageFromView(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
        view.destroyDrawingCache();
        return bitmap;
    }
}
