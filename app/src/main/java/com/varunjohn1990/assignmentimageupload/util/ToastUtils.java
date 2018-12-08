package com.varunjohn1990.assignmentimageupload.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class ToastUtils {

    private static Toast toast;

    public static void show(Context context, String message, int duration) {
        showToast(context, message, duration);
    }

    public static void show(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int messageResId) {
        showToast(context, context.getString(messageResId), Toast.LENGTH_SHORT);
    }

    private static void showToast(Context context, String message, int duration) {
        if (message.isEmpty()) {
            return;
        }

        if (toast == null)
            toast = Toast.makeText(context, message, duration);
        else
            toast.setText(message);

        toast.show();
    }
}
