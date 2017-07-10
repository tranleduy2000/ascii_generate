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
import android.support.annotation.Nullable;
import android.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Writes bitmaps and HTML to directories on the external storage directory.
 */
public class AsciiImageWriter {

    private static final DateFormat filenameDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
    private static final String PATH_IMAGE = Environment.getExternalStorageDirectory() + "/AsciiArt";
    private static final String PATH_TEXT = Environment.getExternalStorageDirectory() + "/AsciiArt/Text";

    public static Pair<String, String> saveImage(Context context, Bitmap image,
                                                 @Nullable AsciiConverter.Result asciiResult)
            throws IOException {
        String datestr = filenameDateFormat.format(new Date());
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        File imageFile;
        File textFile;

        if (isSDPresent) {
            imageFile = new File(PATH_IMAGE, datestr + ".png");
            textFile = new File(PATH_TEXT, datestr + ".txt");
            if (!imageFile.exists()) {
                imageFile.getParentFile().mkdirs();
                imageFile.createNewFile();
            }
            if (!textFile.exists()) {
                textFile.getParentFile().mkdirs();
                textFile.createNewFile();
            }
        } else {
            imageFile = new File(context.getFilesDir(), datestr + ".png");
            textFile = new File(context.getFilesDir(), datestr + ".txt");
        }
        saveBitmap(image, imageFile);
        FileWriter textOutput = new FileWriter(textFile);
        if (asciiResult != null) {
            writeText(asciiResult, textOutput);
        }
        textOutput.close();
        image.recycle();
        return new Pair<>(imageFile.getPath(), textFile.getPath());
    }

    @Nullable
    private static String saveBitmap(@NonNull Bitmap bitmap, @NonNull File fileToWrite) throws IOException {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(fileToWrite);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
            output.close();
        } finally {
            if (output != null) output.close();
        }
        return fileToWrite.getPath();
    }

    private static void writeText(@NonNull AsciiConverter.Result result, Writer writer) throws IOException {
        for (int r = 0; r < result.rows; r++) {
            for (int c = 0; c < result.columns; c++) {
                writer.write(result.stringAtRowColumn(r, c));
            }
            writer.write("\n");
        }
    }
}
