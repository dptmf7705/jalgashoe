package com.dankook.jalgashoe.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by yeseul on 2018-05-09.
 */

public class BitmapTextUtil {

    public static Bitmap writeTextOnBitmap(Bitmap bitmap, String text){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextAlign(Paint.Align.CENTER);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, bitmap.getWidth()/2, bitmap.getHeight()/2, paint);

        return bitmap;
    }

    public static Bitmap writeTextOnDrawable(Resources resource, int drawableId, int size, String text){
        Bitmap bitmap = BitmapFactory.decodeResource(resource, drawableId).copy(Bitmap.Config.ARGB_8888, true);

        return BitmapTextUtil.writeTextOnBitmap(Bitmap.createScaledBitmap(bitmap, size, size, false), text);
    }

    public static Bitmap writeTextOnDrawable(Resources resource, int drawableId, int size, int index){
        return writeTextOnDrawable(resource, drawableId, size, String.valueOf(index));
    }

}
