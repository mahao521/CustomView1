package com.mahao.customview.binder.interfaces;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.mahao.customview.binder.PersonService;

import java.security.Provider;

import androidx.annotation.Nullable;

public class PersonService1 extends Service implements ISetNameInterface {

    private MySetNameBinder mISetNameInterface;

    private String name;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mISetNameInterface = new MySetNameBinder();
        return mISetNameInterface;
    }


    @Override
    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public String getName() {
        return name + " &  哈哈哈";
    }


    class MySetNameBinder extends Binder implements ISetNameInterface {


        @Override
        public void setName(@Nullable String name) {
            PersonService1.this.setName(name);
        }

        @Nullable
        @Override
        public String getName() {
            return PersonService1.this.getName();
        }
    }


}
