package com.mahao.customview;

import android.app.Application;
import android.content.Context;

public class MApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
      //  mContext = this.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mContext = base;
    }
}
