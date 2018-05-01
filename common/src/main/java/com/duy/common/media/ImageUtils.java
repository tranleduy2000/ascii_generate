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

package com.duy.common.media;

import android.content.ContentValues;
import android.content.Context;

import java.io.File;

import static android.provider.MediaStore.Images;
import static android.provider.MediaStore.MediaColumns;

/**
 * Created by Duy on 28-Dec-17.
 */

public class ImageUtils {
    /**
     * store image file on media store provider
     */
    public static boolean addImageToGallery(final String filePath, final Context context) {
        try {
            ContentValues values = new ContentValues();

            values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaColumns.DATA, filePath);

            context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * store image file on media store provider
     */
    public static boolean addImageToGallery(File file, Context context) {
        return addImageToGallery(file.getPath(), context);
    }
}
