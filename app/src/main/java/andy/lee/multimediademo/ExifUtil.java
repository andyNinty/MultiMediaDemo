package andy.lee.multimediademo;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.IOException;

/**
 * andy.lee.multimediademo
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
