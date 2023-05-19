package com.mahao.customview.binder;

import android.os.RemoteException;
import android.util.Log;

import com.mahao.customview.IMyAidlInterface;

public class IpersonImpl extends IMyAidlInterface.Stub {

    private static final String TAG = "IpersonImpl";
    String name;

    @Override
    public void setName(String name) throws RemoteException {
        Log.d(TAG, "setName: 设置name " + name);
        this.name = name;
    }

    @Override
    public String getName() throws RemoteException {
        Log.d(TAG, "getName: 获取name  " + name);
        return name + "  &  mahao";
    }


}
