package com.mahao.customview.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomDrawable extends Drawable {

    private Paint mPaint = new Paint();
    private Paint mCornerPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private float radius = 50f;
    private float showdowLength = radius;
    final int innerRadius = 20;

    public void init() {
        mCornerPaint.setStyle(Paint.Style.FILL);
        mCornerPaint.setColor(Color.RED);
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(Color.GREEN);
    }

    //考虑使用clipRect实现，应该更简单 取异  DIFFERENCE是第一次不同于第二次的部分显示出来
    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        Rect bounds = getBounds();
        canvas.drawPath(getLeftTopPath(), mCornerPaint);
        canvas.drawPath(getRightTopPath(), mCornerPaint);
        canvas.drawRect(getTopRect(), mRectPaint);
        canvas.restore();
        int save = canvas.save();
        canvas.rotate(90, bounds.width() * 1.0f / 2f, bounds.width() * 1.0f / 2);
        canvas.drawPath(getRightTopPath(), mCornerPaint);
        canvas.drawRect(getTopRect(), mRectPaint);
        canvas.restoreToCount(save);
        int save1 = canvas.save();
        canvas.rotate(180, bounds.width() * 1.0f / 2f, bounds.width() * 1.0f / 2);
        canvas.drawPath(getRightTopPath(), mCornerPaint);
        canvas.drawRect(getTopRect(), mRectPaint);
        canvas.restoreToCount(save1);
        int save2 = canvas.save();
        canvas.rotate(270, bounds.width() * 1.0f / 2f, bounds.width() * 1.0f / 2);
        canvas.drawPath(getRightTopPath(), mCornerPaint);
        canvas.drawRect(getTopRect(), mRectPaint);
        canvas.restoreToCount(save2);
        canvas.translate(100, 120);
        canvas.drawLine(0, 10, 300, 400, mRectPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        mCornerPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public Path getLeftTopPath() {
        RectF rect = new RectF(0, 0, radius * 2, radius * 2);
        RectF innerRect = new RectF(rect);
        innerRect.inset(radius - innerRadius, radius - innerRadius);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(radius, 0);
        path.lineTo(radius, radius - innerRadius);
        path.arcTo(innerRect, 270, -90, false);
        path.lineTo(0, radius);
        path.arcTo(rect, 180, 90, false);
        path.close();
        mCornerPaint.setShader(new RadialGradient(radius, radius, radius, new int[]{Color.RED, Color.RED, Color.GREEN}, new float[]{0f, 0.4f, 1.0f}, Shader.TileMode.CLAMP));
        return path;
    }

    public Path getRightTopPath() {
        Rect bounds = getBounds();
        RectF rect = new RectF(bounds.right - 2 * radius, 0, bounds.right, radius * 2);
        RectF innerRect = new RectF(rect);
        innerRect.inset(radius - innerRadius, radius - innerRadius);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(bounds.right - radius, 0);
        path.lineTo(bounds.right - radius, radius - innerRadius);
        path.arcTo(innerRect, 270, 90, false);
        path.lineTo(bounds.right, radius);
        path.arcTo(rect, 0, -90, false);
        path.close();
        mCornerPaint.setShader(new RadialGradient(bounds.right - radius, radius, radius, new int[]{Color.RED, Color.RED, Color.GREEN}, new float[]{0f, 0.4f, 1.0f}, Shader.TileMode.CLAMP));
        return path;
    }

    public RectF getTopRect() {
        RectF rectF = new RectF(radius, 0, getBounds().right - radius, radius - innerRadius);
        mRectPaint.setShader(new LinearGradient(0, radius, 0, 0, new int[]{Color.RED, Color.RED, Color.GREEN}, new float[]{0f, 0.4f, 1f}, Shader.TileMode.CLAMP));
        return rectF;
    }

}
