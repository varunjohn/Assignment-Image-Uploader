package com.varunjohn1990.assignmentimageupload.logic;

import android.support.multidex.MultiDexApplication;

import com.varunjohn1990.assignmentimageupload.model.storage.Prefs;
import com.varunjohn1990.assignmentimageupload.util.NetworkUtil;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class MyApplication extends MultiDexApplication {

    public enum ApiMode {
        TESTING,
        LIVE
    }

    public static final boolean RETROFIT_SHOW_LOG = false;
    public static final boolean SHOW_LOG = true;
    public static final ApiMode apiMode = ApiMode.TESTING;

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkUtil.initialize(this);
        Prefs.initialize(this);
    }
}
