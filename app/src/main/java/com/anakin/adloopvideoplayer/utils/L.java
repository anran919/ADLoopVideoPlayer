package com.anakin.adloopvideoplayer.utils;


import android.util.Log;

public class L {
    public static boolean DEBUG = true;
    public static String TAG = "Diyomate";

    private L(){
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void i(String info, Object... args){
        if(DEBUG){
            try {
                Log.i(getCaller(), String.format(info, args));
            }catch (Exception e){
                Log.i(getCaller(), info);
            }
        }
    }
    
    public static void d(String info, Object... args){
        if(DEBUG){
            try {
                Log.d(getCaller(), String.format(info, args));
            }catch (Exception e){
                Log.d(getCaller(), info);
            }
        }
    }

    public static void w(String info, Object... args){
        if(DEBUG){
            try {
                Log.w(getCaller(), String.format(info, args));
            }catch (Exception e){
                Log.w(getCaller(), info);
            }
        }
    }

    public static void e(String info, Object... args){
        if(DEBUG){
            try {
                Log.e(getCaller(), String.format(info, args));
            }catch (Exception e){
                Log.e(getCaller(), info);
            }
        }
    }

    public static String getCaller() {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        String str = "";
        if(stack.length > 2) {
            StackTraceElement s = stack[2];
            str = TAG + ":" + s.getClassName() + ":" + s.getMethodName() + ":" + s.getLineNumber();
        }
        return str;
    }
    
}
