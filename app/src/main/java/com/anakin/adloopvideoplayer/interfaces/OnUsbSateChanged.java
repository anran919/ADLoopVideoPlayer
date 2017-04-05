package com.anakin.adloopvideoplayer.interfaces;

/**
 * Created by demo on 2017/3/31 0031
 */
public interface OnUsbSateChanged {
    /**
     * usb状态发生了改变
     * @param isConnected
     * @param path
     */
    void onUsbSateChange(boolean isConnected, String path);
}
