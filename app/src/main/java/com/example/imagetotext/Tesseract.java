package com.example.imagetotext;

import com.googlecode.tesseract.android.*;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Tesseract {
    private static String DATA_PATH;

    public static void setUpTesseract(Context context) {
        DATA_PATH = context.getCacheDir().getAbsolutePath() + "/";
        copyTessdata(context);
    }

    /**
     * Copy tessdata files (located on assets/tessdata) to destination directory
     */
    private static void copyTessdata(Context context) {
        String TESSDATA = "tessdata";
        File cacheDir = new File(DATA_PATH + TESSDATA);
        File cacheFile = new File(DATA_PATH + TESSDATA, "eng.traineddata");
        try {
            boolean unused = cacheDir.mkdirs();
            if (cacheFile.createNewFile()) {
                try (InputStream is = context.getAssets().open("tessdata/eng.traineddata");
                     FileOutputStream os = new FileOutputStream(cacheFile)) {

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        os.write(buf, 0, len);
                    }
                }
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

    private static Bitmap preprocessBitmap() {
        return null;
    }
}
