package com.anakin.adloopvideoplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.anakin.adloopvideoplayer.presenter.IVideoPresenter;
import com.anakin.adloopvideoplayer.presenter.VideoPresenter;
import com.anakin.adloopvideoplayer.service.UsbService;
import com.anakin.adloopvideoplayer.utils.L;
import com.anakin.adloopvideoplayer.utils.VideoControl;
import com.anakin.adloopvideoplayer.view.CustomVideoView;
import com.anakin.adloopvideoplayer.view.IView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IView {

    private static final int READ_EXTERNAL_STORAGE_CODE = 0;
    private IVideoPresenter presenter;
    private ProgressBar mProgressBar;
    private SeekBar mSeekBar;
    private CustomVideoView mVideoView;
    private Handler mHandler;
    private static final int UPDATE = 1;
    private static final int KEYCODE_UP = 19;
    private static final int KEYCODE_DOWN = 20;
    public static final int KEYCODE_LAST = 21;
    public static final int KEYCODE_NEXT = 22;
    public static final int KEYCODE_PAUSE = 23;
    private VideoControl mControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(" MainActivity onCreate ()--------------->>>>> ");
        initView();
//        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
        //开启一个服务一直运行在后台 用于接收usb的状态
        Intent service = new Intent(this, UsbService.class);
        startService(service);
        L.d(" MainActivity onResume ()--------------->>>>> ");
    }

    private void initView() {
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        mVideoView = (CustomVideoView) findViewById(R.id.vv_video);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mSeekBar = (SeekBar) findViewById(R.id.seedBar);
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE:
                        try {
                            mSeekBar.setProgress(mVideoView.getCurrentPosition());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessageDelayed(UPDATE, 500);
                        break;
                }

                return false;
            }
        });
    }

    private void initData() {
        checkAppPermission();
    }

    private void checkAppPermission() {
        presenter = new VideoPresenter(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请READ_EXTERNAL_STORAGE
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_CODE);
        } else {
            presenter.getData();
        }

    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError() {
        Toast.makeText(this, "重新扫描", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showData(final List<String> data) {
        L.d("播放视频文件列表 :::::>>>>>>>>>>>> "+data);
        mControl = new VideoControl();
        mControl.setData(data, mVideoView);
        mVideoView.setVideoPath(data.get(0));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekBar.setMax(mp.getDuration());
                mp.start();
                showSeekBar();
                mHandler.sendEmptyMessage(UPDATE);
//                mp.setLooping(true);

            }
        });

        mVideoView
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mControl.playVideo(MainActivity.this);
                    }
                });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//                nextVideo();
                // 拔出u盘 或者出错,退出程序
                System.exit(0);
                return false;
            }
        });
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KEYCODE_LAST:   // 左键
                mControl.slowPlay();
                break;
            case KEYCODE_NEXT:   // 右键
                mControl.fastPlay();
                break;
            case KEYCODE_UP:   // 上键
                mControl.nextVideo();
                break;
            case KEYCODE_DOWN:   // 下
                mControl.lastVideo();
                break;
            case KEYCODE_PAUSE:  // ok键
                mControl.pauseVideo();
                break;
        }
        showSeekBar(); // 有按键操作显示进度条
        return super.onKeyDown(keyCode, event);

    }


    /**
     * 当用户按下快进快退显示进度条
     */
    private void showSeekBar() {
        mSeekBar.setVisibility(View.VISIBLE);
        //
        new Handler().postDelayed(new Runnable(){
            public void run() {
                //execute the task
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSeekBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }, 2000);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);

    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                presenter.getData();
            } else {
                // Permission Denied
                System.exit(0);
            }
        }
        //退出应用

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
