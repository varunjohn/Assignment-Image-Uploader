package com.varunjohn1990.assignmentimageupload.util;

import android.util.Log;

import com.varunjohn1990.assignmentimageupload.logic.MyApplication;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class LogUtils {

    public static void debug(String msg) {
        if (MyApplication.SHOW_LOG) {
            Log.d("VarunDebug", msg);
        }
    }
}
