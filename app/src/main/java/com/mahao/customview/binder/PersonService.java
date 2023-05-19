package com.mahao.customview.binder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.mahao.customview.IMyAidlInterface;

import java.security.Provider;

import androidx.annotation.Nullable;

public class PersonService extends Service {

    private static final String TAG = "PersonService";
    private String name;

    private IMyAidlInterface.Stub mIperson;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        // mIperson = new IpersonImpl();
        mIperson = new IMyAidlInterface.Stub() {

            @Override
            public void setName(String name) throws RemoteException {
                PersonService.this.setName(name);
            }

            @Override
            public String getName() throws RemoteException {
                return PersonService.this.getName();
            }
        };
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIperson;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
