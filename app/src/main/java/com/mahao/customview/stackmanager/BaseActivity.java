package com.mahao.customview.stackmanager;

import android.os.Bundle;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private WeakReference<FragmentActivity> mWeakReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeakReference = new WeakReference<>(this);
        ActivityStackManager.getInstance().addActivity(mWeakReference);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityStackManager.getInstance().setCurrentActivity(mWeakReference);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWeakReference != null) {
            ActivityStackManager.getInstance().removeActivity(mWeakReference);
        }
    }
}
