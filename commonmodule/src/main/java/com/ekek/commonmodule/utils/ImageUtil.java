package com.ekek.commonmodule.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageUtil {
    public static Bitmap decodeTempIdentifyBitmap(Context context , int id, int viewWidth, int viewHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        BitmapFactory.decodeResource(context.getResources(),id,options);
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        float scale = 1.0f;
        if(bitmapWidth > viewWidth || bitmapHeight > viewHeight){
            //double widthScale =  (float) viewWidth / (float) bitmapWidth);
            float widthScale = (float) viewWidth / bitmapWidth;
            float heightScale =  (float) viewHeight / bitmapHeight ;

            if (widthScale > heightScale) {
                if (heightScale < 1) scale = heightScale;
            }else {
                if (widthScale < 1) scale = widthScale;
                scale = widthScale;
            }
        }

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),id,options);
        return bitmapScale(bitmap, (float) scale);
    }

    public static Bitmap decodeTempIdentifyBitmap(Context context , int id, int viewWidth, int viewHeight,float scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        BitmapFactory.decodeResource(context.getResources(),id,options);
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        if(bitmapWidth > viewWidth || bitmapHeight > viewHeight){
            //double widthScale =  (float) viewWidth / (float) bitmapWidth);
            float widthScale = (float) viewWidth / bitmapWidth;
            float heightScale =  (float) viewHeight / bitmapHeight ;

            if (widthScale > heightScale) {
                if (heightScale < 1) scale = heightScale;
            }else {
                if (widthScale < 1) scale = widthScale;
                scale = widthScale;
            }
        }

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),id,options);
        return bitmapScale(bitmap, (float) scale);
    }

    public static Bitmap decodeBitmapForScale(Context context, int id, float scale) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        return bitmapScale(bitmap, scale);
    }


    private static Bitmap bitmapScale(Bitmap bitmap, float scale) {
        if (bitmap == null) return null;
        //scale = 1.0f;
        LogUtil.d("scale------------->" + scale);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != resizeBmp) {
            bitmap.recycle();
        }
        return resizeBmp;
    }

    /**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     *
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        if (widthOrg >= edgeLength && heightOrg >= edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;
            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }
            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;
            LogUtil.d("xTopLeft---->" + xTopLeft);
            LogUtil.d("yTopLeft---->" + yTopLeft);
            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }


    /**
     * 矩形将图片的四角圆化
     *
     * @param bitmap
     *            传入Bitmap对象
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // 得到画布
        Canvas canvas = new Canvas(output);
        // 将画布的四角圆化
        final int color = Color.RED;
        final Paint paint = new Paint();
        // 得到与图像相同大小的区域 由构造的四个值决定区域的位置以及大小
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        // 值越大角度越明显
        final float roundPx = 20;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // drawRoundRect的第2,3个参数一样则画的是正圆的一角，如果数值不同则是椭圆的一角
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
