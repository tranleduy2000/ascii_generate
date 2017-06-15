package com.duy.acsiigenerator.image.converter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.duy.acsiigenerator.image.util.AndroidUtils;

import java.io.IOException;

import static com.duy.acsiigenerator.image.converter.AsciiConverter.ColorType;
import static com.duy.acsiigenerator.image.converter.AsciiConverter.Result;

public class ProcessImageOperation {
    private static final String TAG = "ProcessImageOperation";
    /**
     * Reads the image from the given URI, creates ASCII PNG and HTML files, and writes them to
     * a new directory under the AsciiCam directory in /sdcard. Returns the path to the PNG file.
     */
    public static String processImage(Context context, Uri uri) throws IOException {
        Log.d(TAG, "processImage() called with: context = [" + context + "], uri = [" + uri + "]");

        ColorType colorType = ColorType.ANSI_COLOR;

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        // assume width is always larger
        int displayWidth = Math.max(display.getWidth(), display.getHeight());
        int displayHeight = Math.min(display.getWidth(), display.getHeight());

        final AsciiRenderer renderer = new AsciiRenderer();
        renderer.setMaximumImageSize(displayWidth, displayHeight);

        int minWidth = Math.max(2*renderer.asciiColumns(), 480);
        int minHeight = Math.max(2*renderer.asciiRows(), 320);
        Bitmap bitmap = AndroidUtils.scaledBitmapFromURIWithMinimumSize(context, uri, minWidth, minHeight);
        renderer.setCameraImageSize(bitmap.getWidth(), bitmap.getHeight());

        AsciiConverter converter = new AsciiConverter();
        final Result result = converter.computeResultForBitmap(bitmap,
                renderer.asciiRows(), renderer.asciiColumns(), colorType);

        return AsciiImageWriter.saveImageAndThumbnail(renderer.createBitmap(result),
                renderer.createThumbnailBitmap(result),
                result);
    }
}
