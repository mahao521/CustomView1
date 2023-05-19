package com.mahao.customview.rxjava;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.BooleanSupplier;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Function3;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.internal.functions.Functions;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DefaultSubscriber;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mahao.customview.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RxjavaActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RxjavaActivity";
    private Disposable mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava2);
        findViewById(R.id.btn_rx_1).setOnClickListener(this);
        findViewById(R.id.btn_rx_2).setOnClickListener(this);
        findViewById(R.id.btn_rx_3).setOnClickListener(this);
        findViewById(R.id.btn_rx_4).setOnClickListener(this);
        findViewById(R.id.btn_rx_5).setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rx_1:
                //testFlow();
                // testFlow1();
                //  testFlow2();
                //  testFlow3();
                testFlow4();
                break;
            case R.id.btn_rx_2:
                //  testObservable();
                // testObservable1();
                //  testObserable3();
                // testObserable4();
                // testObservable5();
                // testObserable6();
                //testObservable7();
                // testObservable8();
                //  testObservable9();
                testObservable10();
                break;
            case R.id.btn_rx_3:
                //testZip();
                //testLiveDataZip();
                // testRxjavaZip();
                merge();
                break;
            case R.id.btn_rx_4:
                concat();
                break;
            case R.id.btn_rx_5:
                zipConcurrent();
                break;
        }
    }

    private void zipConcurrent() {
        List<Observable<Integer>> list = new ArrayList();
        list.add(getSource1(true));
        list.add(getSource2(true));
        list.add(getSource3(true));
        Observable.zip(list, Functions.identity()).subscribe(new Consumer<Object[]>() {
            @Override
            public void accept(Object[] objects) throws Throwable {
                Log.d(TAG, "accept: " + Arrays.toString(objects));
            }
        });
       /* Observable.zip(list, new Function<Integer[], Observable<Integer>>() {
            @Override
            public Observable<Integer> apply(Integer[] objects) throws Throwable {
                return null;
            }
        })*/
    }


    public void concat() {
        List<Observable<Integer>> list = new ArrayList();
        list.add(getSource1(true));
        list.add(getSource2(true));
        list.add(getSource3(true));
        Observable.concat(list).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: " + integer);
            }
        });

        /*getSource1(true).concatWith(getSource2(true)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: " + integer);
            }
        });*/
    }


    public void merge() {
        List<Observable<Integer>> list = new ArrayList();
        list.add(getSource1(true));
        list.add(getSource2(true));
        list.add(getSource3(true));
        Observable.merge(list, 2, 128)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        if (integer == 1) {
                            //   Thread.sleep(1000);
                        }
                        Log.d(TAG, "accept: " + integer);
                    }
                });

    }

    public void testRxjavaZip() {
        ZipRxJava rxJavaZip = new ZipRxJava();
        rxJavaZip.rxjavaZip(Observable.just(1, 2, 3).subscribeOn(Schedulers.newThread()),
                Observable.just(4, 5).delay(1, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()));
    }

    public void testLiveDataZip() {
        ZipLiveData liveDataZip = new ZipLiveData();
        liveDataZip.zipTask(liveDataZip.new MyTask(1), liveDataZip.new MyTask(2));
    }


    public void testZip() {
        ZipJava rxjavaZip = new ZipJava();
        rxjavaZip.zipTask(rxjavaZip.new MyTask(2), rxjavaZip.new MyTask(8));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testFlow4() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Throwable {
                        for (int i = 0; i < 100; i++) {
                            emitter.onNext(i);
                            Thread.sleep(10);
                            Log.d(TAG, "subscribe: " + Thread.currentThread().getName() + " " + i);
                        }
                        emitter.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .parallel()
                .collect(Collectors.toList())
                .subscribe(new DefaultSubscriber<List<Integer>>() {
                    @Override
                    public void onNext(List<Integer> integers) {
                        Log.d(TAG, "onNext: " + Arrays.toString(integers.toArray()));
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
       /* Flowable.range(1, 100)
                .parallel()
                .collect(Collectors.toList())
                .subscribeWith(new DefaultSubscriber<List<Integer>>() {
                    @Override
                    public void onNext(List<Integer> integers) {
                        Log.d(TAG, "onNext: " + Arrays.toString(integers.toArray()));
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testFlow3() {
        Flowable.zip(Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> emitter) throws Throwable {
                Log.d(TAG, "subscribe:---1  " + Thread.currentThread().getName());
                //   Thread.sleep(2000);
                emitter.onNext("aaa");
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER), Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> emitter) throws Throwable {
                Log.d(TAG, "subscribe:--2  " + Thread.currentThread().getName());
                //      Thread.sleep(2000);
                emitter.onNext("bbb");
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER), new BiFunction<String, String, String>() {
            @Override
            public String apply(String s, String s2) throws Throwable {
                return s + s2;
            }
        }).parallel().collect(new Collector<String, Integer, Long>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public java.util.function.Supplier<Integer> supplier() {
                return new java.util.function.Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        return 1;
                    }
                };
            }

            @Override
            public BiConsumer<Integer, String> accumulator() {
                return new BiConsumer<Integer, String>() {
                    @Override
                    public void accept(Integer integer, String s) {

                    }
                };
            }

            @Override
            public BinaryOperator<Integer> combiner() {
                return new BinaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) {
                        return integer + integer2;
                    }
                };
            }

            @Override
            public java.util.function.Function<Integer, Long> finisher() {
                return new java.util.function.Function<Integer, Long>() {
                    @Override
                    public Long apply(Integer integer) {
                        return integer.longValue();
                    }
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.emptySet();
            }
        }).subscribe(new DefaultSubscriber<Long>() {

            @Override
            protected void onStart() {
                request(1);

            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "onNext: " + aLong);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    public void testFlow1() {
        Flowable.range(1, 128)
                /* .map(new Function<Integer, Integer>() {
                     @Override
                     public Integer apply(Integer integer) throws Throwable {
                         return integer + 1;
                     }
                 })*/
                .subscribeOn(Schedulers.newThread())
                .blockingSubscribe(new DisposableSubscriber<Integer>() {

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: flow  " + integer);
                        //   request();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void testObservable10() {
       /* Observable.concat(getSource1(true), getSource2(true), getSource3(true))
                .observeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: ---  ---" + integer);
            }
        });*/

        /*Observable.zip(Observable.range(1, 5), Observable.range(10, 5), new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Throwable {
                return integer + integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer r) throws Throwable {

            }
        });*/
      /*  Observable.merge(getSource1(true),
                        getSource2(true),
                        getSource3(true))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Log.d(TAG, "accept: " + integer);
                    }
                });*/


      /*  Observable.zip(getSource1(true), getSource2(true), new BiFunction<Integer, Integer, Observable<*>>() {
            @Override
            public Observable<BaseData<*>> apply(Integer integer, Integer integer2) throws Throwable {
                Log.d(TAG, "apply:  完成====+++++");
                return Observable.fromArray(new BaseData(integer),new  BaseData(integer2));
            }
        }).concatMap(new Function<Observable<Integer>, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Observable<Integer> integerObservable) throws Throwable {
                return integerObservable;
            }
        }).concatMap(new Function<Observable<Integer>, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Observable<Integer> integerObservable) throws Throwable {
                return integerObservable.lift(new ObservableOperator<Integer, Integer>() {
                    @Override
                    public @NonNull Observer<? super Integer> apply(@NonNull Observer<? super Integer> observer) throws Throwable {
                        return new DefaultObserver<Integer>() {
                            @Override
                            public void onNext(@NonNull Integer integer) {
                                observer.onNext(integer);

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                              observer.onError(e);
                            }

                            @Override
                            public void onComplete() {
                                observer.onComplete();
                            }
                        };
                    }
                });
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept:--------- " + integer);
            }
        });*/

     /*   Observable.merge(getSource1(true), getSource2(true)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: " + integer);
            }
        });*/


       /* Flowable.range(1, 10)
                .map(new Function<Integer, Integer>() {

                    @Override
                    public Integer apply(Integer integer) throws Throwable {
                        int millis = new Random().nextInt(4);
                        Thread.sleep(millis * 100);
                        return integer;
                    }
                }).parallel()
                .sequential()
                *//*flatMap(new Function<Integer, Publisher<Integer>>() {
                                @Override
                                public Publisher<Integer> apply(Integer integer) throws Throwable {
                                    return Flowable.just(integer).subscribeOn(Schedulers.newThread()).map(new Function<Integer, Integer>() {
                                        @Override
                                        public Integer apply(Integer integer) throws Throwable {
                                            return integer * integer;
                                        }
                                    });
                                }
                            })*//*
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Log.d(TAG, "accept: " + integer);
                    }
                });*/
    }

    public void testObservable9() {
        Observable<Integer> integerObservable = Observable.zip(getSource1(true), getSource2(true), getSource3(true), new Function3<Integer, Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2, Integer integer3) throws Throwable {
                return integer + integer2 + integer3;
            }
        }).observeOn(AndroidSchedulers.mainThread());
        integerObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: --1--- " + Thread.currentThread().getName() + " " + integer);
            }
        });
      /*  integerObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: --2-- " + Thread.currentThread().getName() +" " + integer);
            }
        });
        integerObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: --3-- " + Thread.currentThread().getName() +" " + integer);
            }
        });*/
    }


    public void testObservable8() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Throwable {
                        Log.d(TAG, "apply: ------ " + Thread.currentThread().getName());
                        Thread.sleep(new Random().nextInt(2000));
                        return integer + 10;
                    }
                })
                .flatMap(new Function<Integer, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Integer integer) throws Throwable {
                        return Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                                emitter.onNext(integer);
                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        //  d.dispose();
                        Log.d(TAG, "onSubscribe: " + d);
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d(TAG, "onNext: " + integer + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public void testObservable7() {
        Observable.range(1, 128)
                /*.map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Throwable {
                        return integer + 1;
                    }
                })*/
                .subscribeOn(Schedulers.computation())

                .blockingSubscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Log.d(TAG, "accept:  observable   " + integer + " " + Thread.currentThread().getId());
                    }
                });
      /*  Observable.range(1, 1000)

                .subscribeOn(Schedulers.computation()).blockingSubscribe(new Consumer<Timed<Integer>>() {
                    @Override
                    public void accept(Timed<Integer> integerTimed) throws Throwable {
                        Log.d(TAG, "accept: " + integerTimed.value());
                    }
                });*/
      /*  Observable.range(1, 1000)
                .buffer(10)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Throwable {
                        Log.d(TAG, "accept: " + Arrays.toString(integers.toArray()));
                    }
                });*/
        /*Observable.range(1,1000)
                .lift(new ObservableOperator<Integer, Integer>() {
                    @Override
                    public @NonNull Observer<? super Integer> apply(@NonNull Observer<? super Integer> observer) throws Throwable {


                        return new DefaultObserver<Integer>() {
                            @Override
                            protected void onStart() {
                                super.onStart();
                                Log.d(TAG, "onStart: ");
                            }

                            @Override
                            public void onNext(@NonNull Integer integer) {
                                observer.onNext(integer);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");
                            }
                        };
                    }
                })
                .blockingSubscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Log.d(TAG, "accept: " + integer);
                    }
                });*/


    }

    public void testObserable6() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                Log.d(TAG, "subscribe: 执行了");
                emitter.onError(new RuntimeException("ssss"));
            }
        }).retryUntil(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() throws Throwable {
                Log.d(TAG, "getAsBoolean: ");
                return false;
            }
        }).take(5).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: " + integer);
            }
        });

      /*  Observable.concat(Observable.just(1),Observable.just(2),Observable.just(3),Observable.just(4)).takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Throwable {
                        return integer % 2 == 1;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Log.d(TAG, "accept: " + integer);
                    }
                });*/

      /*  Observable.just(1, 2, 3).blockingForEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {

                Log.d(TAG, "accept: ---- " + integer);
                Thread.sleep(1000);
            }
        });
        Observable.just(1, 2, 3).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {

                Log.d(TAG, "accept: ==== " + integer);
                Thread.sleep(1000);
            }
        });*/
/*        Observable<Integer> source = Observable.just(1,2,3,4)

                .flatMap(new Function<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(Integer t1) {
                        return Observable.just(t1);
                    }
                });
        Log.d(TAG, "testObserable6: " + source.blockingFirst());*/
/*        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
             //   emitter.onNext(2);
              //  emitter.onComplete();
            }
        }).ambWith(Observable.just(3)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {

            }
        });*/

       /* Observable.zip(Observable.just(11, 22,55), Observable.just(33, 44), new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Throwable {
                return integer * integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer o) throws Throwable {
                Log.d(TAG, "accept: " + o);
            }
        });*/

       /* Observable.switchOnNext(new ObservableSource<ObservableSource<Integer>>() {
            @Override
            public void subscribe(@NonNull Observer<? super ObservableSource<Integer>> observer) {
                observer.onNext(Observable.just(2));
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: " + integer);
            }
        });*/

     /*   Observable.merge(Arrays.asList(Observable.just(1,3,4),Observable.just(1,3,4,5,6,6))).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: " + integer);
            }
        });*/
    }

    public void testObservable5() {
        Observable.fromMaybe(new MaybeSource<Integer>() {
            @Override
            public void subscribe(@NonNull MaybeObserver<? super Integer> observer) {
                observer.onSuccess(111);
                observer.onComplete();
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: " + integer);
            }
        });
        Observable.just(1, 2, 3).concatWith(Observable.error(new RuntimeException("has error"))).subscribe(new DisposableObserver<Integer>() {

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

        Observable.defer(new Supplier<ObservableSource<?>>() {
            @Override
            public ObservableSource<?> get() throws Throwable {
                return null;
            }
        });
    }

    public void testObserable4() {
        Observable.concat(Observable.just(Observable.just(1, 2, 3, 4), Observable.just(5, 6, 7, 8))).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: == " + integer);
            }
        });
     /*   Observable.fromArray(11,22,33)
                .concatArray(Observable.just(1,2,3,4))
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Throwable {
                        Log.d(TAG, "apply: " + integer);
                        return integer * integer;
                    }
                })
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: " + integer);
            }
        });*/
        /*Observable.fromArray(1,2,3)
                        .concat(Observable.just(1,3,4,6,8,10),Observable.just(2,6,8)).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
        Observable.fromArray(1,3,5)
                        .concat(Observable.just(Observable.just(2,4,6))).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Log.d(TAG, "accept: " + integer);
                    }
                });*/
/*
        Observable.fromArray(1, 2, 3)
                .concatMap(new Function<Integer, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Integer integer) throws Throwable {
                        return Observable.create(new ObservableOnSubscribe<Object>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                                Log.d(TAG, "subscribe: " + Thread.currentThread().getId());
                                emitter.onNext(integer * 10);
                                emitter.onComplete();
                            }
                        });
                    }
                }).subscribeOn(Schedulers.computation()).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Throwable {
                        Log.d(TAG, "accept4 : " + o);
                    }
                });*/
    }

    public void testObserable3() {
        Observable.interval(1, TimeUnit.SECONDS)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Throwable {
                        AtomicInteger counter = new AtomicInteger();
                        return counter.getAndIncrement();
                    }
                }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Throwable {
                        AtomicInteger counter = new AtomicInteger();
                        return throwableObservable
                                .takeWhile(new Predicate<Throwable>() {
                                    @Override
                                    public boolean test(Throwable throwable) throws Throwable {
                                        return counter.getAndIncrement() != 3;
                                    }
                                }).flatMap(new Function<Throwable, ObservableSource<?>>() {
                                    @Override
                                    public ObservableSource<?> apply(Throwable throwable) throws Throwable {
                                        Log.d(TAG, "apply:  我是flat map ");
                                        //   return Observable.timer(counter.get(), TimeUnit.SECONDS);
                                        //这里的返回没有任何意义，这里只需要一个Observable对象就行。
                                        return Observable.create(new ObservableOnSubscribe<Object>() {
                                            @Override
                                            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                                                emitter.onNext(1);
                                                emitter.onComplete();
                                            }
                                        });
                                    }
                                });
                    }
                }).blockingSubscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Throwable {
                        Log.d(TAG, "accept:  final " + o);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "accept:  throwable " + throwable.getMessage());
                    }
                });
    }

    public void testObservable2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                emitter.onNext(1);
                int aaa = 1 / 0;
                emitter.onComplete();
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Observable<Throwable> throwableObservable) throws Throwable {

                return Observable.just(1);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.d(TAG, "accept: " + integer.intValue());
            }
        });
    }

    public void testObservable1() {
        Observable.combineLatest(Observable.just(1, 2, 3), Observable.just(4, 5, 6), new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Throwable {
                return integer + integer2;
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Throwable {
                Log.d(TAG, "accept: " + o);
            }
        });
    }

    public void testObservable() {
        Observable.create(new ObservableOnSubscribe<String>() {

                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        Log.d(TAG, "apply:---1---- " + Thread.currentThread().getName());
                        emitter.onNext("zhangsan");
                        emitter.onComplete();
                        //emitter.setDisposable();
                    }
                }).flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Throwable {
                        Log.d(TAG, "apply:---2---- " + Thread.currentThread().getName());
                        return Observable.just(s);
                    }
                }).map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Throwable {
                        Log.d(TAG, "apply:----3---- " + Thread.currentThread().getName());
                        return s;
                    }
                }).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribeWith(new DisposableObserver<String>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        Log.d(TAG, "apply:---4---- " + Thread.currentThread().getName());
                        Log.d(TAG, "onStart:   ");
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d(TAG, "onNext: " + s);
                        Log.d(TAG, "apply:---5---- " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
       /* Observable.create(new ObservableOnSubscribe<String>() {

                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        Log.d(TAG, "apply:---1---- " + Thread.currentThread().getName());
                        emitter.onNext("zhangsan");
                        emitter.onComplete();
                    }
                }).flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Throwable {
                        Log.d(TAG, "apply:---2---- " + Thread.currentThread().getName());
                        return Observable.just(s);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()).map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Throwable {
                        Log.d(TAG, "apply:----3---- " + Thread.currentThread().getName());
                        return s;
                    }
                }).subscribeOn(Schedulers.computation())
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> apply(Throwable throwable) throws Throwable {
                        return Observable.just("lisi");
                    }
                })
                .observeOn(Schedulers.computation())
                .subscribeWith(new DisposableObserver<String>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        Log.d(TAG, "apply:---4---- " + Thread.currentThread().getName());
                        Log.d(TAG, "onStart:   ");
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d(TAG, "apply:---5---- " + Thread.currentThread().getName());
                        Log.d(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });*/
    /*    Observable.just("Hello world!")
                .delay(1, TimeUnit.SECONDS)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.isDisposed());
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });*/
    }

    public void testFlow() {
        mSource = Flowable.create(new FlowableOnSubscribe<String>() {
                    @Override
                    public void subscribe(FlowableEmitter<String> emitter) throws Exception {

                        Log.d(TAG, "subscribe: " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
                        int i = 0;
                        for (; i < 420; ) {
                            //    Log.d(TAG, "subscribe: ////////" + emitter.requested() + " == = " + i);
                            if (emitter.requested() == 0) {
                                //    Log.d(TAG, "subscribe:-----------------缓存----  ");
                                //      continue;
                            }
                            Log.d(TAG, Thread.currentThread().getName() + "subscribe:  发送了 " + i + "  " + emitter.requested());
                            emitter.onNext("abc " + i);
                            i++;
                            //   Thread.sleep(100);
                        }
                        emitter.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<String>() {

                    @Override
                    public void onStart() {
                        request(10);
                    }

                    @Override
                    public void onNext(String t) {
                        Log.d(TAG, Thread.currentThread().getName() + " onNext: 接收了  " + t);
                        try {
                            //      Thread.sleep(10);
                           /* if(t.equals("abc 9")){
                            //    Thread.sleep(100);
                                request(140);
                            }
                            if(t.equals("abc 96")){
                                request(10);
                            }*/
                            request(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //  request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        Log.d(TAG, "onError:  error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete:  done ");
                    }
                });
        //  source.dispose();
    }

    public void testFlow2() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        int i = 0;
                        while (true) {
                            Log.d(TAG, "当前未完成的请求数量-->" + e.requested());
                            if (e.requested() == 0) continue;//此处添加代码，让flowable按需发送数据
                            Log.d(TAG, "发射---->" + i);
                            i++;
                            e.onNext(i);
                            if (i == 1000) {
                                break;
                            }
                        }
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    private Subscription mSubscription;


                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(10);            //设置初始请求数据量为1
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(50);
                            //   Log.d(TAG, "接收------>" + integer);
                            //       mSubscription.request(1);//每接收到一条数据增加一条请求量
                        } catch (Exception ignore) {
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public Observable<Integer> getSource1(boolean isconfirm) {
        Observable<Integer> source1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                //   Log.d(TAG, "subscribe:------ " + Thread.currentThread().getName());
                if (isconfirm) {
                    Thread.sleep(1000);
                } else {
                    Thread.sleep(500 * (new Random().nextInt(10)));
                }

                Log.d(TAG, "subscribe: 步骤1----" + Thread.currentThread().getName());
                Log.d(TAG, "subscribe: 步骤1发送了 " + 1);
                emitter.onNext(1);
                Log.d(TAG, "subscribe: 步骤1发送了 " + 2);
                emitter.onNext(2);
                Log.d(TAG, "subscribe: 步骤1发送了 " + 3);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());
        return source1;
    }

    public Observable<Integer> getSource2(boolean isconfirm) {
        Observable<Integer> integerObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                //  Log.d(TAG, "subscribe:=== " + Thread.currentThread().getName());
                if (isconfirm) {
                    Thread.sleep(1000);
                } else {
                    Thread.sleep(100 * (new Random().nextInt(10)));
                }
                Log.d(TAG, "subscribe: 步骤2 ==== " + Thread.currentThread().getName());
                Log.d(TAG, "subscribe:  步骤2发送了 " + 10);
                emitter.onNext(10);
                Thread.sleep(3000);
                Log.d(TAG, "subscribe: 步骤2发送了 " + 20);
                emitter.onNext(20);

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());
        return integerObservable;
    }

    public Observable<Integer> getSource3(boolean isConfirm) {
        Observable<Integer> integerObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                if (isConfirm) {
                    Thread.sleep(10);
                } else {
                    Thread.sleep(300 * (new Random().nextInt(10)));
                }
                Log.d(TAG, "subscribe: 步骤3++++++  " + Thread.currentThread().getName());
                Log.d(TAG, "subscribe: 步骤3发送了  " + 100);
                emitter.onNext(100);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());
        return integerObservable;
    }

    public Flowable<Integer> getFloawable(int time, int step) {
        Flowable<Integer> integerFlowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Throwable {
                Thread.sleep(time);
                Log.d(TAG, "subscribe: 步骤" + step);
                emitter.onNext(998);
                emitter.onComplete();
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.newThread());
        return integerFlowable;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSource != null) {
            mSource.dispose();
        }
    }

    class BaseData<T> {
        T data;

        public BaseData(T data) {
            this.data = data;
        }
    }
}