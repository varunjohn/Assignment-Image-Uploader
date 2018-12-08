package com.varunjohn1990.assignmentimageupload.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.varunjohn1990.assignmentimageupload.R;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class NetworkUtil {

    private static Context context;
    private static ConnectivityManager connectivityManager;

    public static void initialize(Context context) {
        NetworkUtil.context = context;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void handleNoConnection(Context context) {
        ToastUtils.show(context, R.string.no_internet);
    }
}
