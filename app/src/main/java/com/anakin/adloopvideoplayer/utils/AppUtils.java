package com.anakin.adloopvideoplayer.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by demo on 2017/3/30 0030
 */
public class AppUtils {

    static boolean hasStarted = false;

    // 设置tag 开机只会启动一次 ,避免接受到dyth的主题启动广播后,切换主题再次接收到开机的广播重复启动
    public static void startActivity(Context context, Class clazz) {
        if (!hasStarted) {
            Intent mainActivityIntent = new Intent(context, clazz);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
            hasStarted = true;
        }

    }



    public static boolean isVideo(String name) {
        String s = name.toLowerCase();
        if (s.endsWith(".mp4")
                || name.endsWith(".3gp")
                || name.endsWith(".wmv")
                || name.endsWith(".ts")
                || name.endsWith(".m4v")
                || name.endsWith(".m3u8")
                || name.endsWith(".3gpp")
                || name.endsWith(".3gpp2")
                || name.endsWith(".divx")
                || name.endsWith(".f4v")
                || name.endsWith(".rm")
                || name.endsWith(".asf")
                || name.endsWith(".ram")
                || name.endsWith(".v8")
                || name.endsWith(".swf")
                || name.endsWith(".m2v")
                || name.endsWith(".asx")
                || name.endsWith(".ra")
                || name.endsWith(".ndivx")
                || name.endsWith(".xvid")
                || name.endsWith(".avi")
                || name.endsWith(".flv")
                || name.endsWith(".rmvb")
                || name.endsWith("wmv")
                || name.endsWith(".mkv")
                || name.equals(".mov")
                || name.endsWith(".mpeg")
                || name.endsWith(".mpg")) {
            return true;
        }
        return false;
    }






}
