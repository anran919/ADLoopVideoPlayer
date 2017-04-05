package com.anakin.adloopvideoplayer.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import com.dy.videoplayer.interfaces.OnMediaScanListener;

/**
 * Created by lph on 2016/7/25.
 */
public class MediaScannerWrapper implements
        MediaScannerConnection.MediaScannerConnectionClient {
    private OnMediaScanListener mOnMediaScanListener;
    private MediaScannerConnection mConnection;
    private String mPath;
    private String mMimeType;

    // filePath - where to scan;
    // mime type of media to scan i.e. "image/jpeg".
    // use "*/*" for any media
    public MediaScannerWrapper(Context ctx, String mime, OnMediaScanListener onMediaScanListener) {
        mMimeType = mime;
        mConnection = new MediaScannerConnection(ctx, this);
        this.mOnMediaScanListener = onMediaScanListener;
    }

    public void scan(String path) {
        mPath = path;
        if (mConnection.isConnected()) {
            mConnection.scanFile(mPath, mMimeType);
        } else {
            mConnection.connect();
        }
    }

    // start the scan when scanner is ready
    public void onMediaScannerConnected() {
        mConnection.scanFile(mPath, mMimeType);
    }

    public void onScanCompleted(String path, Uri uri) {
        mOnMediaScanListener.onMediaScanFinished(true);
    }


    public void disConnected() {
        if (mConnection.isConnected())
            mConnection.disconnect();
    }
}
