package com.duy.acsiigenerator.image.converter;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Writes bitmaps and HTML to directories on the external storage directory.
 */
public class AsciiImageWriter {

    private static final DateFormat filenameDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private static final String basePictureDirectory = Environment.getExternalStorageDirectory() + File.separator + "AsciiArt";

    public static String getBasePictureDirectory() {
        return basePictureDirectory;
    }


    public static Pair<String, String> saveImageAndThumbnail(Bitmap image, AsciiConverter.Result asciiResult)
            throws IOException {
        String datestr = filenameDateFormat.format(new Date());
        String dir = getBasePictureDirectory() + File.separator + datestr;
        // make sure image and thumbnail directories exist
        File file = new File(dir);
        if (!file.exists()) file.mkdirs();
        if (!file.isDirectory()) return null;
        String pngPath = saveBitmap(image, dir, datestr);
        String textPath = dir + File.separator + datestr + ".txt";
        FileWriter textOutput = new FileWriter(textPath);
        try {
            writeText(asciiResult, textOutput);
        } finally {
            textOutput.close();
        }
        return new Pair<>(pngPath, textPath);
    }

    public static String saveBitmap(Bitmap bitmap, String dir, String imageName) throws IOException {
        String outputFilePath;
        FileOutputStream output = null;
        try {
            outputFilePath = dir + File.separator + imageName + ".png";
            output = new FileOutputStream(outputFilePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
            output.close();
        } finally {
            if (output != null) output.close();
        }
        return outputFilePath;
    }

    public static void writeText(AsciiConverter.Result result, Writer writer) throws IOException {
        for (int r = 0; r < result.rows; r++) {
            for (int c = 0; c < result.columns; c++) {
                writer.write(result.stringAtRowColumn(r, c));
            }
            writer.write("\n");
        }
    }
}
