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
import android.os.Environment;
import android.support.annotation.NonNull;

import com.duy.ascii.art.utils.FileUtil;

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

    private static final DateFormat filenameDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);

    static {
        File sdCard = Environment.getExternalStorageDirectory();
    }

    public static String saveImage(Context context, Bitmap image)
            throws IOException {
        String datestr = filenameDateFormat.format(new Date());
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File imageFile;
        if (isSDPresent) {
            imageFile = new File(FileUtil.getImageDirectory(context), datestr + ".png");
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
