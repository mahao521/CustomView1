package com.mahao.customview.rxjava;

import android.util.Log;

import java.util.Arrays;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.operators.SpscLinkedArrayQueue;

public class ZipLiveData {

    private static final String TAG = "RxjavaZip";

    private MyTask[] mMyTasks;
    private Object[] row;

    public void zipTask(MyTask... myTasks) {
        this.mMyTasks = myTasks;
        row = new Object[mMyTasks.length];
        for (int i = 0; i < myTasks.length; i++) {
            MyTask task = myTasks[i];
            Thread thread = new Thread(task);
            thread.setName("thread-" + i);
            thread.start();
            //  task.mLiveData.observe();
        }
    }

    public class MyTask<T> implements Runnable {

        private MutableLiveData<T> mLiveData = new MutableLiveData<>();
        private T taskValue;

        public MyTask(T value) {
            taskValue = value;
        }

        @Override
        public void run() {
            int value = (int) taskValue;
            mLiveData.postValue(taskValue);
            if (value == 1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            drain();
        }
    }

    private synchronized void drain() {
        Log.d(TAG, "drain:  开始begin====== " + Thread.currentThread().getName());
        boolean hasAllRunComplete = true;
        Object[] objects = row;
        for (int i = 0; i < mMyTasks.length; i++) {
            MyTask myTask = mMyTasks[i];
            if (objects[i] == null) {
                Object poll = myTask.mLiveData.getValue();
                if (poll == null) {
                    hasAllRunComplete = false;
                    break;
                } else {
                    objects[i] = poll;
                }
            }
            Log.d(TAG, "drain: 数组   " + Arrays.toString(objects));
        }
        Log.d(TAG, "drain: 数组f " + Arrays.toString(objects));
        if (hasAllRunComplete) {
            for (int i = 0; i < row.length; i++) {
                Log.d(TAG, "drain: " + row[i]);
            }
            Arrays.fill(objects, null);
        }
    }


}
