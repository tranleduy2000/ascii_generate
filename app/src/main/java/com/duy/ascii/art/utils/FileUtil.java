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

package com.duy.ascii.art.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Pattern;

/**
 * Created by Duy on 03-Jul-17.
 */

public class FileUtil {
    public static final Pattern PATTERN_SPLIT = Pattern.compile("(\")(.*?)(\")", Pattern.DOTALL);
    private static final String TAG = "FileUtil";
    private static final String IMAGE_FOLDER_NAME = "Image";


    public static File getImageDirectory(Context context) {
        if (hasSdCard(context)) {
            File file = new File(Environment.getExternalStorageDirectory(), IMAGE_FOLDER_NAME);
            if (!file.exists()) file.mkdir();
            return file;
        }
        File file = new File(context.getFilesDir(), IMAGE_FOLDER_NAME);
        if (!file.exists()) file.mkdir();
        return file;
    }

    private static boolean hasSdCard(Context context) {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String streamToString(@NonNull InputStream stream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(stream, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    private static byte[] streamToByteArray(@NonNull InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

}
