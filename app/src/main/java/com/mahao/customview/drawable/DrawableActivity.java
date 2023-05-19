package com.mahao.customview.drawable;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mahao.customview.R;
import com.mahao.customview.recycler.util.DisplayUtil;

import java.util.concurrent.TimeUnit;

public class DrawableActivity extends AppCompatActivity {

    private static final String TAG = "DrawableActivity";

    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_drawable);
        View rootView = findViewById(R.id.root_view);
        ImageView imageView = findViewById(R.id.iv_full_screen);
        Button btnAddheight = findViewById(R.id.btn_add_height);
        Drawable drawable = getResources().getDrawable(R.drawable.img_kaiping_normal);
        Log.d(TAG, "onCreate: " + rootView.getMeasuredWidth() + "  " + rootView.getMeasuredHeight());
        Log.d(TAG, "onCreate: " + DisplayUtil.getScreenWidth(this) + " " + DisplayUtil.getScreenHeight(this));
        //imageView.setBackground(null);
        //    imageView.setImageResource(R.drawable.img_splashscreens);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: " + rootView.getMeasuredWidth() + "  " + rootView.getMeasuredHeight());

                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                Log.d(TAG, "run:  draw " + intrinsicWidth + " " + intrinsicHeight);

                float scale = DisplayUtil.getScreenWidth(DrawableActivity.this) * 1.0f / intrinsicWidth;
                float height = intrinsicHeight * scale;
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.height = Float.valueOf(height).intValue();
                float dy = DisplayUtil.getScreenHeight(DrawableActivity.this) - layoutParams.height;
                Log.d(TAG, "run:  imageview height " + height + "  ratio " + scale);
      /*          Drawable imageDrawable = getResources().getDrawable(R.drawable.img_kaiping_normal);
                imageDrawable.setBounds(0, 0, DisplayUtil.getScreenWidth(DrawableActivity.this),
                        DisplayUtil.dp2Px(DrawableActivity.this, layoutParams.height));

                imageView.setImageDrawable(imageDrawable);*/
                imageView.setLayoutParams(layoutParams);
                imageView.setImageResource(R.drawable.img_kaiping_normal);
                Matrix imageMatrix = imageView.getImageMatrix();
                Log.d(TAG, "run: mm ==b  " + imageMatrix.toShortString());
                imageMatrix.setScale(scale, scale);
                Log.d(TAG, "run: mm ==a  " + imageMatrix.toShortString());
              //  imageMatrix.postTranslate(0, -DisplayUtil.dp2Px(DrawableActivity.this, 100f));
                imageView.setImageMatrix(imageMatrix);
            }
        });
        // drawView.setBackground(drawable);
        //  imageView.setAdjustViewBounds(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count % 2 == 0) {
                    imageView.setBackground(drawable);
                    imageView.setImageResource(0);
                } else {
                    imageView.setBackground(null);
                    imageView.setImageResource(R.drawable.img_splashscreens);
                }
            }
        });
        btnAddheight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                Log.d(TAG, "onClick:  height before = " + layoutParams.height + " " + imageView.getHeight());
                layoutParams.height = imageView.getHeight() - DisplayUtil.dp2Px(DrawableActivity.this, 3);
                Log.d(TAG, "onClick: height after  = " + layoutParams.height);
                imageView.setLayoutParams(layoutParams);
            }
        });
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(DrawableActivity.this,Draw1Activity.class);
              startActivity(intent);
            }
        });

    }
}