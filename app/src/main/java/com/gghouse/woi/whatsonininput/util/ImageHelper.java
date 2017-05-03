package com.gghouse.woi.whatsonininput.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by michaelhalim on 5/2/17.
 */

public abstract class ImageHelper {
    public static String getStringFromPath(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 2;  //you can also calculate your inSampleSize
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[16 * 1024];

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        bitmap.recycle();
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
