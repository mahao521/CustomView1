package com.mahao.customview.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.mahao.customview.R;
import com.mahao.customview.recycler.util.DisplayUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 用于测试在layout给imageView设置drawable，是否会一直导致layout
 */
public class ImageLayout extends RelativeLayout {

    private static final String TAG = "ImageLayout";
    private ImageView mImageView;

    public ImageLayout(Context context) {
        this(context, null);
    }

    public ImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImageView = new ImageView(context);
        mImageView.setAdjustViewBounds(true);
        //  mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(mImageView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       /* this.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                if (mImageView.getHeight() != 0) {
                    return;
                }
                DrawableImageViewTarget target = new DrawableImageViewTarget(mImageView);
                target.waitForLayout();
                //
                Glide.with(getContext()).load(R.drawable.img_splashscreens).addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "onResourceReady: " + mImageView.toString());
                        return false;
                    }
                }).into(target);
            }
        });*/
       /* DrawableImageViewTarget target = new DrawableImageViewTarget(mImageView);
        target.waitForLayout();
        Glide.with(getContext())
                .load("https://file.moyublog.com/d/file/2020-12-22/6cd26be6253355531099894d7c76a375.jpg")
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .skipMemoryCache(true)
                .addListener(getRequestListener())
                .transform(new CenterCrop(), new RoundedCorners(DisplayUtil.dp2Px(getContext(), 20f)), new CircleCrop())
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "onResourceReady: " + mImageView.toString());
                        return false;
                    }
                }).into(target);*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    /*    for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            childAt.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(700, MeasureSpec.EXACTLY));
        }
        Log.d(TAG, "onMeasure: ");
        //  setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(700, MeasureSpec.EXACTLY));
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(700, MeasureSpec.EXACTLY));
*/
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "onLayout: " + mImageView.toString());

        Log.d(TAG, "onLayout: " + mImageView.getDrawable());

        Log.d(TAG, "onLayout: " + mImageView.getWidth() + " " + mImageView.getHeight());
        Log.d(TAG, "onLayout: " + mImageView.getLeft() + " " + mImageView.getTop());
        Log.d(TAG, "onLayout: " + mImageView.getRight() + " " + mImageView.getBottom());

        //第一次高度为0  ---- 获取屏幕高度。
        //1、第一次加载图片之前，清理target，setimagedrawable(null),
        //2、第二次requestLayout----图片加载完成之后，给高度为0的imageview设置图片。
        //3、此时imageview有图片，高度就测量出来了。
        //4、此时就不需要再用glide加载了。
        //5、依旧加载。重复1. 将有图片的iamgeView设置成无图片的iamgeView。重复1、肯定要requestLayout。

        //1、为什么不使用ovrride，可以加载成功。
        //原因使用了缓存，第二次加载直接使用了缓存，（没有新加载。开始加载，和清除之前targer请求都会给imageView设置null.）
        //但是使用缓存没有。 如果之前加载未完成或者不使用缓存，则依旧会layout多次。


        DrawableImageViewTarget target = new DrawableImageViewTarget(mImageView);
        // target.waitForLayout();
        Glide.with(getContext()).load("https://file.moyublog.com/d/file/2020-12-22/6cd26be6253355531099894d7c76a375.jpg")
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .addListener(getRequestListener())
                //.skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(target);
    }

    RequestListener<Drawable> listener = null;
    public RequestListener<Drawable> getRequestListener() {
     //   if (listener == null) {
            listener = new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    Log.d(TAG, "onLoadFailed: ");
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    Log.d(TAG, "onResourceReady: " + mImageView.toString());
                    if (mImageView.getHeight() != 0) {
                        //  return  true;
                    }
                    return false;
                }
            };
       // }
        return listener;
    }

}
