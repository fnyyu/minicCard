package com.mini.paddling.minicard.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Base64;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CommonUtils {

    /**
     * 获取view截图
     * @param v
     * @return
     */
    public static Bitmap getViewBitmap(View v) {

        if (null == v) {
            return null;
        }

        try{
            v.setDrawingCacheEnabled(true);
            v.buildDrawingCache();

            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));

            v.layout((int) v.getX(), (int) v.getY(), (int) v.getX() + v.getMeasuredWidth(), (int) v.getY() + v.getMeasuredHeight());

            Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

            v.setDrawingCacheEnabled(false);
            v.destroyDrawingCache();
            return bitmap;

        }catch (Exception e){
            return null;
        }

    }

    /**
     * bitmap转Base64
     * @param bitmap
     * @return
     */
    public static String Bitmap2Base(Bitmap bitmap){
        String result = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            byte[] bytes = baos.toByteArray();
            result = Base64.encodeToString(bytes, Base64.DEFAULT);
            return result;
        }catch (Exception e){
            return result;
        }

    }

    public static String byteArray2Base(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * Base64转bitmap
     * @param base
     * @return
     */
    public static Bitmap Base2Bitmap(String base){

        try {
            byte[] bytes = Base64.decode(base , Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }catch (Exception e){
            return null;
        }

    }


    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static Bitmap getVideoThumb(String videoFile) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoFile);
        return retriever.getFrameAtTime();
    }
}
