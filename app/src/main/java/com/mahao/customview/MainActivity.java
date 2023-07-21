package com.mahao.customview;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.material.dialog.InsetDialogOnTouchListener;
import com.mahao.customview.activitylife.LifeMainActivity;
import com.mahao.customview.appbarlayout.AppBarLayoutActivity;
import com.mahao.customview.binder.BinderActivity;
import com.mahao.customview.constainlayout.ConstraintLayoutActivity;
import com.mahao.customview.dispatch.DispatchActivity;
import com.mahao.customview.drag.DragAdapter;
import com.mahao.customview.drawable.DrawableActivity;
import com.mahao.customview.fragment.MainFragmentActivity;
import com.mahao.customview.fragment.viewpager2.TestViewPager2Activity;
import com.mahao.customview.glide.GlideActivity;
import com.mahao.customview.livedata.LiveDataActivity;
import com.mahao.customview.multiwindow.ListActivity;
import com.mahao.customview.okhttp.OkHttpActivity;
import com.mahao.customview.recycler.RecyclerViewActivity;
import com.mahao.customview.rxjava.RxjavaActivity;
import com.mahao.customview.stackmanager.AActivity;
import com.mahao.customview.view.TransLateActivity;
import com.mahao.customview.view.ViewActivity;
import com.mahao.customview.viewbind.ViewBindImplActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_dragLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DragAdapter adapter = new DragAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setSoundEffectsEnabled(true);
        recyclerView.playSoundEffect(SoundEffectConstants.CLICK);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.d(TAG, "onGlobalLayout: " + recyclerView.getMeasuredHeight());
                testView(recyclerView);
                testView(recyclerView.getChildAt(3));
            }
        });
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                jumpByPosition(position);
                recyclerView.setSoundEffectsEnabled(true);
                recyclerView.playSoundEffect(SoundEffectConstants.CLICK);
            }
        });
        adapter.setData(getDataList());
    }

    public void jumpByPosition(int position) {
        if (position == 0) {

            goToActivity(CustomViewActivity.class);
       /*     int i = checkOAIDPermission(this);
            Log.d(TAG, "jumpByPosition: " + i);
            try {
                Class idProviderImplClass = Class.forName("com.android.id.impl.IdProviderImpl");
                Method requestOAIDPermission = idProviderImplClass.getDeclaredMethod(
                        "requestOAIDPermission", Activity.class, int.class);
                requestOAIDPermission.invoke(null, this, 100);
                Toast.makeText(this, "请求权限", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            } catch (Throwable e) {
            }*/
        } else if (position == 1) {
            goToActivity(ConstraintLayoutActivity.class);
        } else if (position == 2) {
            goToActivity(AppBarLayoutActivity.class);
        } else if (position == 3) {
            goToActivity(RecyclerViewActivity.class);
        }/* else if (position == 4) {
            goToActivity(ViewBindImplActivity.class);
        }*/ else if (position == 4) {
            goToActivity(TransLateActivity.class);
        } else if (position == 5) {
            goToActivity(MainFragmentActivity.class);
        } else if (position == 6) {
            goToActivity(TestViewPager2Activity.class);
        } else if (position == 7) {
            goToActivity(DispatchActivity.class);
        } else if (position == 8) {

            goToActivity(ListActivity.class);
        } else if (position == 9) {
            goToActivity(BinderActivity.class);
        } else if (position == 10) {
            goToActivity(RxjavaActivity.class);
        } else if (position == 11) {
            goToActivity(DrawableActivity.class);
        } else if (position == 12) {
            goToActivity(LifeMainActivity.class);
        } else if (position == 13) {
            goToActivity(OkHttpActivity.class);
        } else if (position == 14) {
            goToActivity(GlideActivity.class);
        } else if (position == 15) {
            goToActivity(LiveDataActivity.class);
        } else if (position == 16) {
            goToActivity(AActivity.class);
        }
    }

    public List<String> getDataList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("自定义View");
        stringList.add("ConstraintLayout");
        stringList.add("AppBarLayout");
        stringList.add("Recycler");
        //     stringList.add("viewbind");
        stringList.add("view源码分析");
        stringList.add("fragment");
        stringList.add("ViewPager2");
        stringList.add("事件分发");
        stringList.add("多窗口Activity");
        stringList.add("binder");
        stringList.add("rxJava");
        stringList.add("drawable");
        stringList.add("生命周期");
        stringList.add("okHttp");
        stringList.add("glide");
        stringList.add("liveData");
        stringList.add("stackManager");
        return stringList;
    }

    public void goToActivity(Class classex) {
        Intent intent = new Intent(MainActivity.this, classex);
        startActivity(intent);
    }

    public void testView(View view) {
        Log.d(TAG, "testView:  开始执行了");
        Rect rect = new Rect();
        view.requestRectangleOnScreen(rect, true);
        Log.d(TAG, "onGlobalLayout: " + rect.toShortString());
        int[] arr1 = new int[2];
        view.getLocationOnScreen(arr1);  //相对于屏幕
        Log.d(TAG, "onCreate: " + arr1[0] + " " + arr1[1]);
        int[] arr2 = new int[2];
        view.getLocationInWindow(arr2); //相对父view
        Log.d(TAG, "onCreate: " + arr2[0] + " " + arr2[1]);
        Rect rect1 = new Rect();
        view.getLocalVisibleRect(rect1); //返回可以见的大小，相对于父view
        Log.d(TAG, "onCreate: " + rect1.toShortString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: " + requestCode);

    }

    public int checkOAIDPermission(Context context) {
        int result = -2;
        try {
            Class idProviderImplClass = Class.forName("com.android.id.impl.IdProviderImpl");
            Method checkSelfOAIDPermission = idProviderImplClass.getDeclaredMethod(
                    "checkSelfOAIDPermission", Context.class);
            result = (int) checkSelfOAIDPermission.invoke(null, context);
        } catch (Exception e) {
        } catch (Throwable e) {
        }
        return result;
    }
}