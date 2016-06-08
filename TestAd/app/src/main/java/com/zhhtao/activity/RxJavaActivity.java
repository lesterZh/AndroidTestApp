package com.zhhtao.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.zhhtao.testad.R;
import com.zhhtao.utils.LogUtil;

import butterknife.ButterKnife;
import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxJavaActivity extends AppCompatActivity {

    @Bind(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        ButterKnife.bind(this);

//        testRxJava();
//        testRxJava2();
//        testRxJava3();
//        testRxJava4();
//        testRxJava5();
        testRxJava6();
    }

    private void testRxJava6() {
        Observable.just(R.drawable.debug_image_girl)
                .map(new Func1<Integer, Drawable>() {
                    @Override
                    public Drawable call(Integer integer) {
//                        SystemClock.sleep(2000);
                        return getResources().getDrawable(integer);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Drawable>() {
                    @Override
                    public void call(Drawable drawable) {
                        imageView.setImageDrawable(drawable);
                    }
                });
    }

    private void testRxJava5() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                LogUtil.w("call "+Thread.currentThread().getName());
                subscriber.onNext(R.drawable.btn_chat_blue);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Func1<Integer, Drawable>() {
                    @Override
                    public Drawable call(Integer integer) {
                        LogUtil.w("map " + Thread.currentThread().getName());
                        return getResources().getDrawable(integer);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Drawable>() {
                    @Override
                    public void call(Drawable drawable) {
                        LogUtil.w("set " + Thread.currentThread().getName());
                        imageView.setImageDrawable(drawable);
                    }
                });

    }

    private void testRxJava4() {
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(R.drawable.debug_image_girl);
                subscriber.onNext(drawable);
            }
        }).subscribe(new Action1<Drawable>() {
            @Override
            public void call(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }
        });
    }
    
    private void testRxJava3() {
        String[] ss = {"a--", "b--", "c--"};
        Observable.from(ss)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtil.i(s + " 3");
                    }
                });
    }

    private void testRxJava2() {
        Observable<String> observable = Observable.just("hello world2", "second 02");
        observable
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + "-ZHT";
                    }
                })
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.hashCode();
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return String.valueOf(integer);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtil.w(s);
                    }
                });
    }


    private void testRxJava() {
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("hello world");
                        subscriber.onCompleted();
                    }
                });

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                LogUtil.i("complete");
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e("ZHT", e.toString());
            }

            @Override
            public void onNext(String s) {
                LogUtil.i(s);
            }
        };

        myObservable.subscribe(mySubscriber);
    }

}
