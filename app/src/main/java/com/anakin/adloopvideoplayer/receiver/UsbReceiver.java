package com.anakin.adloopvideoplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import com.dy.videoplayer.MainActivity;
import com.dy.videoplayer.utils.L;
import com.dy.videoplayer.utils.UiUtils;

import java.io.File;

/**
 * Created by demo on 2017/4/1 0001
 */
public class UsbReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                //USB设备移除，更新UI
                L.d("SB设备移除，更新UI >>>>> ");
                Toast.makeText(context,"USB设备移除", Toast.LENGTH_SHORT).show();
                System.exit(0);
            } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                //USB设备挂载，更新UI
                // String usbPath = intent.getDataString();（usb在手机上的路径）
                File storage = new File("/storage");
                File[] files = storage.listFiles();
                for (final File file : files) {
                    if (file.canRead()) {
                        if (!file.getName().equals(Environment.getExternalStorageDirectory().getName())) {
                            //满足该条件的文件夹就是u盘在手机上的目录
                            L.d("发现USB设备------------------>>>");
                            Intent fileIntent = new Intent(UiUtils.getContext(), MainActivity.class);
                            fileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            UiUtils.getContext().startActivity(fileIntent);
                            Toast.makeText(UiUtils.getContext(),"发现USB设备", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        }
    }
}
