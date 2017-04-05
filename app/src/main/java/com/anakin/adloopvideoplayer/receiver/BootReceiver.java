package com.anakin.adloopvideoplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dy.videoplayer.MainActivity;
import com.dy.videoplayer.utils.L;
import com.dy.videoplayer.utils.UiUtils;

/**
 * Created by demo on 2017/3/29 0029
 */
public class BootReceiver extends BroadcastReceiver {
    static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        L.d(" 接收到开机启动的广播 ------------------>>>.");
        if (intent.getAction().equals(BOOT_COMPLETED)) {
            Intent fileIntent = new Intent(UiUtils.getContext(), MainActivity.class);
            fileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            UiUtils.getContext().startActivity(fileIntent);
            L.d(" BootReceiver  打开MainActivity--------------->>>.");
        }
    }
}
