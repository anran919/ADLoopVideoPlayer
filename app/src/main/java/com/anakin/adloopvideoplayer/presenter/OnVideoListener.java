package com.anakin.adloopvideoplayer.presenter;

import java.util.List;

/**
 * Created by demo on 2017/3/23 0023
 */
public interface OnVideoListener {
    void onSuccess(List<String> result);
    void onFail();
}
