package com.varunjohn1990.assignmentimageupload.model.storage;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shawnlin.preferencesmanager.PreferencesManager;
import com.varunjohn1990.assignmentimageupload.model.ImageServer;
import com.varunjohn1990.assignmentimageupload.util.AppConstants;
import com.varunjohn1990.assignmentimageupload.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class Prefs {

    public static Context context;
    private static Prefs myPreferences;
    private Gson gson;

    private Prefs(Context context) {
        this.context = context;
        gson = new Gson();
    }

    public static Prefs initialize(Context context) {
        new PreferencesManager(context).setName(AppConstants.Preferences.PREFS_NAME).init();

        if (myPreferences == null) {
            myPreferences = new Prefs(context);
        }
        return myPreferences;
    }

    public static Prefs getInstance() {
        if (myPreferences == null) {
            LogUtils.debug("MyPreferences not initialized");
        }
        return myPreferences;
    }

    public List<ImageServer> getImages() {
        try {
            return gson.fromJson(PreferencesManager.getString(AppConstants.Preferences.IMAGES_LIST), new TypeToken<List<ImageServer>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.debug("getCities error " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public void saveImages(List<ImageServer> imageServerList) {
        try {
            PreferencesManager.putString(AppConstants.Preferences.IMAGES_LIST, gson.toJson(imageServerList).toString());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.debug("saveCities error " + e.getMessage());
        }
    }

    public void addImage(ImageServer imageServer) {
        List<ImageServer> images = getImages();
        if (images == null) {
            images = new ArrayList<>(1);
        }
        images.add(0, imageServer);
        saveImages(images);
    }
}
