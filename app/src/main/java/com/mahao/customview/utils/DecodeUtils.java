package com.mahao.customview.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.mahao.customview.MApplication;
import com.mahao.customview.recycler.util.DisplayUtil;

import java.io.IOException;
import java.io.InputStream;


public class DecodeUtils {
    public static final int MARK_POSITION = 10 * 1024 * 1024;

    public static int[] getDimensions(InputStream is,
                                      ArrayPool mArrayPool) {
        int[] result = new int[2];
        byte[] bytesForOptions = null;
        try {
            is.mark(MARK_POSITION);
            TransformationUtils.getBitmapDrawableLock().lock();
            BitmapFactory.Options whOption = new BitmapFactory.Options();
            whOption.inJustDecodeBounds = true;
            bytesForOptions = mArrayPool.get(ArrayPool.STANDARD_BUFFER_SIZE_BYTES, byte[].class);
            whOption.inTempStorage = bytesForOptions;
            BitmapFactory.decodeStream(is, null, whOption);
            result[0] = whOption.outWidth;
            result[1] = whOption.outHeight;
            is.reset();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            TransformationUtils.getBitmapDrawableLock().unlock();
            if (bytesForOptions != null) {
                mArrayPool.put(bytesForOptions);
            }

        }
        return result;
    }

    public static int getSampleSizeByWidth(int outWidth) {
        int sampleSize = 1;
        if (outWidth > 4096) {
            try {
                sampleSize = (int) Math.floor(outWidth * 1d / DisplayUtil.getScreenWidth(MApplication.mContext));
            } catch (Exception e) {

            }
        }
        return sampleSize;
    }

    public static Bitmap createTopClipBitmap(InputStream is, int outWidth, int outHeight) throws IOException {
        int sampleSize = DecodeUtils.getSampleSizeByWidth(outWidth);
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inSampleSize = sampleSize;
        bitmapFactoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);
        int cutHeight = getCutHeight(outWidth, outHeight);
//      按宽裁
        Rect sRect = new Rect(0, 0, outWidth, cutHeight);
        Bitmap bitmap = decoder.decodeRegion(sRect, bitmapFactoryOptions);
        return bitmap;
    }

    public static Bitmap createCenterClipBitmap(InputStream is, int outWidth, int outHeight) throws IOException {
        int sampleSize = DecodeUtils.getSampleSizeByWidth(outWidth);
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inSampleSize = sampleSize;
        bitmapFactoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);

        int cutHeight = getCutHeight(outWidth, outHeight);

        Rect sRect;
        if (outHeight <= cutHeight) {
            sRect = new Rect(0, 0, outWidth, outHeight);
        } else {
            int top = outHeight / 2 - cutHeight / 2;

            int bottom = top + cutHeight;

            sRect = new Rect(0, top, outWidth, bottom);

        }
        Bitmap bitmap = decoder.decodeRegion(sRect, bitmapFactoryOptions);
        return bitmap;
    }


    public static Bitmap createCenterHorizontalClipBitmap(InputStream is, int outWidth, int outHeight) throws IOException {
        int sampleSize = DecodeUtils.getSampleSizeByWidth(outWidth);
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inSampleSize = sampleSize;
        bitmapFactoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);

        int cutWidth = getCutWidth(outWidth, outHeight);

        Rect sRect;
        if (outWidth <= cutWidth) {
            sRect = new Rect(0, 0, outWidth, outHeight);
        } else {
            int left = outWidth / 2 - cutWidth / 2;

            int right = left + cutWidth;

            sRect = new Rect(left, 0, right, outHeight);

        }
        Bitmap bitmap = decoder.decodeRegion(sRect, bitmapFactoryOptions);
        return bitmap;
    }



    public static boolean isLongImage(int outWidth, int outHeight) {
        if (outWidth == 0 || outHeight == 0) {
            return false;
        }
        int expectHeight = (int) (outHeight * 1f / outWidth * DisplayUtil.getScreenWidth(MApplication.mContext));
        return expectHeight > 4096 || (outHeight * 1.0f / outWidth * 1.0f > 3);
    }


    public static boolean isWideImage(int outWidth, int outHeight) {
        if (outWidth == 0 || outHeight == 0) {
            return false;
        }
        return outWidth * 1.0f / outHeight * 1.0f >= 6;

    }

    public static int getCutWidth(int outWidth, int outHeight) {
        int expectWidth = (int) (outWidth * 1f / outHeight * DisplayUtil.getScreenHeight(MApplication.mContext));
        if (expectWidth < 4096) {
            return outWidth;
        }
        int cutWidth = (int) (4096 * 1f / expectWidth * outWidth);
        cutWidth = Math.min(cutWidth, 4096);

        return cutWidth;
    }


    public static int getCutHeight(int outWidth, int outHeight) {
        int expectHeight = (int) (outHeight * 1f / outWidth * DisplayUtil.getScreenWidth(MApplication.mContext));
        if (expectHeight < 4096) {
            return outHeight;
        }
        int cutHeight = (int) (4096 * 1f / expectHeight * outHeight);
//        cutHeight = Math.min(cutHeight, 4096);
        return cutHeight;
    }

}
