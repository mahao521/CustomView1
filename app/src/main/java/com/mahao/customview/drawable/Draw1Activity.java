package com.mahao.customview.drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mahao.customview.R;
import com.mahao.customview.recycler.util.DisplayUtil;

import java.util.concurrent.BlockingDeque;

public class Draw1Activity extends AppCompatActivity {

    private static final String TAG = "Draw1Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw1);
        ImageView ivBig = findViewById(R.id.iv_splash_big);
        ImageView ivSmall = findViewById(R.id.iv_splash_small);
        ConstraintLayout clRoot = findViewById(R.id.cl_root);
        ImageView ivWindow = findViewById(R.id.iv_splash_window);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d(TAG, "onCreate: " + metrics.density +" " + metrics.widthPixels +" " + metrics.heightPixels);
     //   ivBig.setImageResource(R.drawable.img_splashscreens);

       /* View layoutView = LayoutInflater.from(Draw1Activity.this).inflate(R.layout.activity_draw1, null);
        Log.d(TAG, "onCreate: " + getWindow().getDecorView().getHeight() + " " + getWindow().getDecorView().getMeasuredHeight());
        layoutView.measure(View.MeasureSpec.makeMeasureSpec(DisplayUtil.getScreenWidth(Draw1Activity.this), View.MeasureSpec.EXACTLY)
                , View.MeasureSpec.makeMeasureSpec(DisplayUtil.getScreenHeight(Draw1Activity.this), View.MeasureSpec.EXACTLY));
        Log.d(TAG, "onCreate: " + layoutView.getMeasuredWidth() + "  " + layoutView.getMeasuredHeight());
        Bitmap bitmapForView = getBitmapForView(layoutView);
        clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),bitmapForView);
                Log.d(TAG, "onClick: " + bitmapDrawable);
                ivWindow.setBackground(bitmapDrawable);
            }
        });*/
    }


  /*  public static Bitmap getBitmapForView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }*/
}