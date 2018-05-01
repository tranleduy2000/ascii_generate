/*
 *     Copyright (C) 2018 Tran Le Duy
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
