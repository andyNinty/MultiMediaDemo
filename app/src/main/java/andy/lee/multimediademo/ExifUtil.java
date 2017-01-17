package andy.lee.multimediademo;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.media.ExifInterface;

import java.io.File;
import java.io.IOException;

/**
 * 用于处理应用内图片旋转方向出错的问题
 * 更多细节详见http://developers.googleblog.cn/2017/01/exifinterface.html
 * 随着 25.1.0 支持库的发布，支持库大家庭迎来了一名新成员：ExifInterface 支持库。
 * 由于 Android 7.1 引入了对框架 ExifInterface 的重大改进，
 * 因此只有通过支持库的 ExifInterface 让所有 API 9 以上的设备都能利用这些改进才有意义。
 * Created by andy on 17-1-4.
 */

public class ExifUtil {
    /**
     * 处理bitmap图像的方向
     *
     * @param bitmapFile file
     * @return
     * @throws IOException
     */
    public static int resolveBitmapOrientation(File bitmapFile) throws IOException {
        ExifInterface exif;
        if (bitmapFile != null) {
            exif = new ExifInterface(bitmapFile.getAbsolutePath());
            return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } else {
            return 0;
        }
    }

    /**
     * 将图片旋转为正确的方向
     *
     * @param bitmap      bitmap
     * @param orientation orientation
     * @return bitmap
     */
    public static Bitmap applyOrientation(Bitmap bitmap, int orientation) {
        int rotate;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            //下面这两种还需要在一些特殊机型上面进行测试才可以知道结果,在三星的某些机型上不ok
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                rotate = 90;
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                rotate = 90;
                break;
            default:
                return bitmap;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, mtx, true);
    }
}
