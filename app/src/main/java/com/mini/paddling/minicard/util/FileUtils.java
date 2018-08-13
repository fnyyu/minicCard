package com.mini.paddling.minicard.util;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
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
}
