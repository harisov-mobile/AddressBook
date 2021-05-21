package ru.internetcloud.addressbook;

//*****************************************************
// для  работы с изображениями
//*****************************************************

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PictureUtils {

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        // Чтение размеров изображения на диске
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true; // Если включить (true) этот параметр, то система не будет создавать Bitmap, а только вернет информацию об изображении в следующих полях:
        // outWidth – ширина
        // outHeight – высота
        // outMimeType – mimetype

        BitmapFactory.decodeFile(path, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // Вычисление степени масштабирования
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;
            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize; // Позволяет указать коэффициент уменьшения размера изображения при чтении.
        // Он должен быть кратным 2. Если зададите другое число, то оно будет изменено на ближайшее число меньшее вашего и кратное 2.

        // Чтение данных и создание итогового изображения
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmapForIcon(String path, Activity activity, int iconWidthDp, int iconHeightDp) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;

        int iconWidthPx = iconWidthDp * (densityDpi / 160);
        int iconHeightPx = iconHeightDp * (densityDpi / 160);

        return getScaledBitmap(path, iconWidthPx, iconHeightPx);
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
}
