package com.example.imagetotext;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.googlecode.tesseract.android.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Tesseract {
    private static String DATA_PATH;
    private static String TESSDATA = "tessdata";

    public static void setUpTesseract(Context context) {
        DATA_PATH = context.getCacheDir().getAbsolutePath() + "/";
        copyTessdata(context, TESSDATA);
    }

    /**
     * Copy tessdata files (located on assets/tessdata) to destination directory
     *
     * @param path - name of directory with .traineddata files
     */
    private static void copyTessdata(Context context, String path) {
        File cacheDir = new File(DATA_PATH + TESSDATA);
        File cacheFile = new File(DATA_PATH + TESSDATA, "eng.traineddata");
        try {
            cacheDir.mkdirs();
            cacheFile.createNewFile();
            InputStream inputStream = context.getAssets().open("tessdata/eng.traineddata");
            try {
                FileOutputStream outputStream = new FileOutputStream(cacheFile);
                try {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                } finally {
                    outputStream.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String convertImage(Bitmap image, int left, int top,
                                      int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        String path = DATA_PATH;
        System.out.println(path);

        TessBaseAPI tesseract = new TessBaseAPI();
        tesseract.init(path, "eng");

        tesseract.setImage(image);
        tesseract.setRectangle(left, top, width, height);

        String text = "";
        try {
            text = tesseract.getUTF8Text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tesseract.end();
        return text;
    }
}
