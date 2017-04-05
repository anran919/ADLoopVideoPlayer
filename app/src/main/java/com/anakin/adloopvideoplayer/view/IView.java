package com.anakin.adloopvideoplayer.view;

import java.util.List;

/**
 * Created by demo on 2017/3/23 0023
 */
public interface IView {

    void showProgress();
    void hideProgress();
    void showError();
    void showData(List<String> data);

}
