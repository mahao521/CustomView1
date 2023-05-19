package com.mahao.customview.rxjava;

import android.util.Log;

import java.util.Arrays;

import io.reactivex.ObservableSource;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOperator;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DefaultObserver;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.operators.SpscLinkedArrayQueue;

public class ZipRxJava {

    private static final String TAG = "RxJavaZip";
    private Observable[] mObservables;
    private MyObserver[] mObservers;
    private Object[] row;

    public void rxjavaZip(Observable... sources) {
        if (sources == null || sources.length == 0) {
            return;
        }
        mObservables = sources;
        mObservers = new MyObserver[sources.length];
        row = new Object[sources.length];
        for (int i = 0; i < sources.length; i++) {
            MyObserver observer = new MyObserver(128);
            mObservers[i] = observer;
            mObservables[i].subscribe(mObservers[i]);
        }
    }

    private synchronized void drain() {
        Log.d(TAG, "drain:  开始begin====== " + Thread.currentThread().getName());
        boolean hasAllRunComplete = true;
        Object[] objects = row;
        for (int i = 0; i < mObservers.length; i++) {
            MyObserver observer = mObservers[i];
            if (objects[i] == null) {
                Object poll = observer.queue.poll();
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

    class MyObserver<T> implements Observer<T> {

        final SpscLinkedArrayQueue<T> queue;

        public MyObserver(int count) {
            queue = new SpscLinkedArrayQueue<>(count);
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull T t) {
            queue.offer(t);
            drain();
        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
