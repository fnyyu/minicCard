package com.mini.paddling.minicard.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;

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

}
