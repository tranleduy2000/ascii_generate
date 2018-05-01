/*
 * Copyright (c) 2017 by Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.ascii.art.image.converter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.IOException;

import static com.duy.ascii.art.image.converter.AsciiConverter.ColorType;
import static com.duy.ascii.art.image.converter.AsciiConverter.Result;

public class ProcessImageOperation {
    private static final String TAG = "ProcessImageOperation";

    /**
     * Reads the image from the given URI, creates ASCII PNG and HTML files, and writes them to
     * a new directory under the AsciiCam directory in /sdcard. Returns the path to the PNG file.
     */
    @Nullable
    public static String processImage(Context context, Uri uri,
                                      @Nullable ColorType type) throws IOException {
        Log.d(TAG, "processImage() called with: context = [" + context + "], uri = [" + uri + "]");

        ColorType colorType;
        if (type == null)
            colorType = ColorType.NONE;
        else {
            colorType = type;
        }

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        // assume width is always larger
        int displayWidth = Math.max(display.getWidth(), display.getHeight());
        int displayHeight = Math.min(display.getWidth(), display.getHeight());

        final AsciiRenderer renderer = new AsciiRenderer();
        renderer.setMaximumImageSize(displayWidth, displayHeight);

        int minWidth = Math.max(2 * renderer.asciiColumns(), 480);
        int minHeight = Math.max(2 * renderer.asciiRows(), 320);

        Bitmap bitmap = AndroidUtils.scaledBitmapFromURIWithMinimumSize(context, uri, minWidth, minHeight);
        if (bitmap == null) {
            return null;
        }
        renderer.setCameraImageSize(bitmap.getWidth(), bitmap.getHeight());
        renderer.setTextSize(12);

        AsciiConverter converter = new AsciiConverter();
        final Result result = converter.computeResultForBitmap(bitmap,
                renderer.asciiRows(), renderer.asciiColumns(), colorType);

        String path = AsciiImageWriter.saveImage(context, renderer.createBitmap(result));
        if (!bitmap.isRecycled()) bitmap.recycle();
        return path;
    }
}
