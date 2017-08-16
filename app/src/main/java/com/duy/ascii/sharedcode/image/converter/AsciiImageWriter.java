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

package com.duy.ascii.sharedcode.image.converter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Writes bitmaps and HTML to directories on the external storage directory.
 */
public class AsciiImageWriter {

    public static final String PATH_IMAGE = new File(Environment.getExternalStorageDirectory(),
            "AsciiArt" + File.separator + "Image").getPath();

    public static final String PATH_FIGLET = new File(Environment.getExternalStorageDirectory(),
            "AsciiArt" + File.separator + "Figlet").getPath();
    private static final DateFormat filenameDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);

    public static String saveImage(Context context, Bitmap image)
            throws IOException {
        String datestr = filenameDateFormat.format(new Date());
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File imageFile;
        if (isSDPresent) {
            imageFile = new File(PATH_IMAGE, datestr + ".png");
            if (!imageFile.exists()) {
                imageFile.getParentFile().mkdirs();
                imageFile.createNewFile();
            }
        } else {
            imageFile = new File(context.getFilesDir(), datestr + ".png");
        }
        saveBitmap(image, imageFile);
        return imageFile.getPath();
    }

    public static boolean saveBitmap(@NonNull Bitmap bitmap, @NonNull File fileToWrite) throws IOException {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(fileToWrite);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
        } catch (Exception e) {
            return false;
        } finally {
            if (output != null) output.close();
        }
        return true;
    }
}
