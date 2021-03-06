package com.gghouse.woi.whatsonininput.util;

import android.util.Log;

import static com.gghouse.woi.whatsonininput.common.Config.LOG_ENABLE;

/**
 * Created by michaelhalim on 9/17/16.
 */
public class Logger {

    public static final String TAG = Logger.class.getSimpleName();

    public static void log(Object o, String log) {
        if (LOG_ENABLE) {
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            Log.d(TAG + " [" + o.getClass().getName() + ":" + lineNumber + "]", log);
        }
    }

    public static void log(String log) {
        if (LOG_ENABLE) {
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            String className = Thread.currentThread().getStackTrace()[3].getClassName();
            Log.d(TAG + " [" + className + ":" + lineNumber + "]", log);
        }
    }

    public static void log(Class<?> c, String log) {
        if (LOG_ENABLE) {
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            Log.d(TAG + " [" + c.getName() + ":" + lineNumber + "]", log);
        }
    }

    public static void log(String c, String log) {
        if (LOG_ENABLE) {
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            Log.d(TAG + " [" + c + ":" + lineNumber + "]", log);
        }
    }
}
