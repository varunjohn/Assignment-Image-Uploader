package com.varunjohn1990.assignmentimageupload.util;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class AppConstants {

    public static final String INTENT_DATA_IMAGE_PATH = "image_path";
    public static final int REQUEST_CODE_CAMERA_IMAGE = 101;

    public static class Url {

        public static String BASE_SERVICE_LIVE = "https://qa-api.myu.co/";
        public static String BASE_SERVICE_TESTING = "https://qa-api.myu.co/";

        public static final String MEDIA_UPLOAD_CHAT = "/mediaservice/api/v3/media/directs3";

    }

    public static class ApiParam {

        public static final String MEDIA_TYPE = "mediaType";
        public static final String MEDIA_FOR = "mediaFor";

        public static final String SUCCESS_RESPONSE_CODE = "2XX";
    }

    public class Preferences {
        public static final String PREFS_NAME = "image_uploader_pref";
        public static final String IMAGES_LIST = "images_list";
    }
}
