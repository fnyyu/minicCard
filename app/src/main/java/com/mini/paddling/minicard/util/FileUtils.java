package com.mini.paddling.minicard.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class FileUtils {
    public static final String JPG = ".jpg";
    public static final String PNG = ".png";
    public static final String BMP = ".bmp";

    /**
     * 应用启动时设值
     */
    public static String sharePicturesPath;

    /**
     * 按时间戳生成文件名
     *
     * @param format
     * @return
     */
    public static String generateFilenameByTimeStamp(String format) {
        long timeStamp = new Date().getTime();

        if (!format.startsWith(".")) {
            format = '.' + format;
        }

        return timeStamp + format;
    }

    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     * @param path
     * @param filename
     * @throws IOException
     */
    public static void saveBitmap(Bitmap bitmap, String path, String filename) throws IOException {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File savedFile = new File(dir, filename);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(savedFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        } finally {
            if (bos != null) {
                bos.flush();
                bos.close();
            }
        }
    }

    /**
     * 删除本地文件
     *
     * @param path
     * @param filename
     */
    public static void deleteFile(String path, String filename) {
        File file = new File(path, filename);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    /**
     * 清空目录，保留目录本身
     *
     * @param path
     */
    public static void clearDir(String path) {
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    public static ByteArrayInputStream getByteArrayInputStream(Context context, File file) {
        return new ByteArrayInputStream(getBytesFromFile(context, file));
    }

    /**
     * ByteArrayInputStream ins = new ByteArrayInputStream(picBytes);
     *
     * @param file
     * @return
     */
    public static byte[] getBytesFromFile(Context context, File file) {
        FileInputStream is = null;
        // 获取文件大小
        long length = file.length();
        // 创建一个数据来保存文件数据
        byte[] fileData = new byte[(int) length];

        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int bytesRead = 0;
        // 读取数据到byte数组中
        while (bytesRead != fileData.length) {
            try {
                bytesRead += is.read(fileData, bytesRead, fileData.length - bytesRead);
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return fileData;
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{
                            MediaStore.Images.ImageColumns.DATA,
                            MediaStore.Video.VideoColumns.DATA,
                    },
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
