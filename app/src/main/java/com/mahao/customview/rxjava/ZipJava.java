package com.mahao.customview.rxjava;

import android.util.Log;

import java.util.Arrays;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.operators.SpscLinkedArrayQueue;


public class ZipJava {

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
        }
    }

    public class MyTask implements Runnable {

        final SpscLinkedArrayQueue<Integer> queue;
        private Integer taskValue;

        public MyTask(Integer value) {
            this.taskValue = value;
            queue = new SpscLinkedArrayQueue<>(10);
        }

        @Override
        public void run() {
            queue.offer(taskValue);
            try {
                int aa = (int) taskValue;
                if (aa == 2) {
                    Thread.sleep(1000);
                } else {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            queue.offer(taskValue + taskValue);
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
                Object poll = myTask.queue.poll();
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
