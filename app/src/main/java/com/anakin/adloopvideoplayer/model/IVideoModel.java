package com.anakin.adloopvideoplayer.model;

import android.content.Context;

import com.dy.videoplayer.presenter.OnVideoListener;

/**
 * Created by demo on 2017/3/23 0023
 */
public interface IVideoModel {
    void loadVideo(OnVideoListener listener, Context context);

    void unSubscribe();
}
