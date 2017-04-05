package com.anakin.adloopvideoplayer.model;

import android.content.Context;
import android.os.Environment;

import com.dy.videoplayer.presenter.OnVideoListener;
import com.dy.videoplayer.utils.AppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by demo on 2017/4/2 0002
 */
public class VideoModel implements IVideoModel {

    private List<String> mVideoInfos;
    private Subscription mSubscribe;

    @Override
    public void loadVideo(final OnVideoListener listener, final Context context) {
        mVideoInfos = new ArrayList<>();
        // 遍历根目录
        File storage = new File("/storage");
        File[] files = storage.listFiles();

        Observable<File> fileObservable = Observable.from(files)
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {

                        return Observable.just(file);
                    }
                })
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {  // 判断是可读USB目录
                        return file.canRead() && !file.getName().equals(Environment.getExternalStorageDirectory().getName());
                    }
                })
                .flatMap(new Func1<File, Observable<File>>() {  // 遍历usb目录
                    @Override
                    public Observable<File> call(File file) {
                        return Observable.from(file.listFiles());
                    }
                });


        Observable<File> file1 = fileObservable
                .filter(new Func1<File, Boolean>() {     //判断是文件夹
                    @Override
                    public Boolean call(File file) {
                        return file.isDirectory();
                    }
                })
                .flatMap(new Func1<File, Observable<File>>() {  // 遍历文件夹
                    @Override
                    public Observable<File> call(File file) {
                        return Observable.from(file.listFiles());
                    }
                });

        Observable<File> filter2 = fileObservable
                .filter(new Func1<File, Boolean>() {     //判断是文件
                    @Override
                    public Boolean call(File file) {
                        return file.isFile();
                    }
                });

        Observable<String> observable1 = filterFile(file1);
        Observable<String> observable2 = filterFile(filter2);
        mSubscribe = Observable.merge(observable1, observable2)
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.timer(3, TimeUnit.SECONDS);
                    }
                })
                .distinct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        listener.onSuccess(mVideoInfos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFail();
                    }

                    @Override
                    public void onNext(String s) {
                        mVideoInfos.add(s);
                    }
                });

    }


    private Observable<String> filterFile(Observable<File> file) {
        Observable<String> observable = file.filter(new Func1<File, Boolean>() {   // 过滤视频文件
            @Override
            public Boolean call(File file) {
                return AppUtils.isVideo(file.getAbsolutePath());
            }
        })
                .map(new Func1<File, String>() {  // 转换 返回文件路径
                    @Override
                    public String call(File file) {
                        return file.getAbsolutePath();
                    }
                });

        return observable;
    }

    @Override
    public void unSubscribe() {
        mSubscribe.unsubscribe();
    }
}
