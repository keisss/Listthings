package com.keiss.listthings.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by hekai on 16/5/27.
 */
public class ScareImage {
    private static Drawable newBitmapDrawable;
    public static Drawable setScareImage(String filePath){


        Bitmap rawBitmap = BitmapFactory.decodeFile(filePath, null);
        int rawHeight = rawBitmap.getHeight();
        int rawWidth = rawBitmap.getWidth();

        if (rawHeight>4096||rawWidth>4096) {
            float heightScale = ((float) 4096) / rawHeight;
            float widthScale = ((float) 4096) / rawWidth;
            Matrix matrix = new Matrix();
            matrix.postScale(heightScale, widthScale);
            Bitmap newBitmap = Bitmap.createBitmap(rawBitmap, 0, 0, rawWidth, rawWidth, matrix, true);
             newBitmapDrawable = new BitmapDrawable(newBitmap);
        }else {
            newBitmapDrawable = new BitmapDrawable(rawBitmap);
        }
        return newBitmapDrawable;
    }


}
