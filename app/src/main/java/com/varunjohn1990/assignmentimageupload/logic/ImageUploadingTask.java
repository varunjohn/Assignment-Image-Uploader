package com.varunjohn1990.assignmentimageupload.logic;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.developers.imagezipper.ImageZipper;
import com.varunjohn1990.assignmentimageupload.httpclient.ProgressRequestBody;
import com.varunjohn1990.assignmentimageupload.httpclient.ResponseModel;
import com.varunjohn1990.assignmentimageupload.httpclient.RestCallBack;
import com.varunjohn1990.assignmentimageupload.httpclient.RestServiceFactory;
import com.varunjohn1990.assignmentimageupload.model.ImageServer;
import com.varunjohn1990.assignmentimageupload.model.Media;
import com.varunjohn1990.assignmentimageupload.util.LogUtils;

import java.io.File;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class ImageUploadingTask implements ProgressRequestBody.UploadCallbacks {

    public interface Callback {
        void onUploadingStart();

        void onNextImage(Media media, List<Media> mediaList);

        void onImageUploadingProcess(Media media, int currentProgress, int overallProgress);

        void onImageUploadingFailed(Media media, String message);

        void onImageUploadingSuccess(Media media, ImageServer imageServer);

        void onImageUploadingCompressionFailed(Media media, String message);

        void onImageUploadingCompressionSuccess(Media media);

        void onUploadingComplete();
    }

    private Call<ResponseModel<ImageServer>> mediaUploadCall;
    private Callback callback;
    private List<Media> list;
    private int currentMediaPosition = -1;
    private Context context;
    private int percentageSegment;
    private boolean isCompletedUploading;

    public ImageUploadingTask(Context context, List<Media> list, Callback callback) {
        this.context = context;
        this.list = list;
        this.callback = callback;
        percentageSegment = 100 / list.size();
    }

    public boolean isUploadingCompleted() {
        return isCompletedUploading;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void start() {
        next();
        callback.onUploadingStart();
    }

    private File compress(Media media, File file) {

        File compressedFile = file;

        if (compressedFile == null) {
            return null;
        }

        try {
            compressedFile = new ImageZipper(context)
                    .setMaxWidth(1280)
                    .setMaxHeight(1280)
                    .setQuality(85)
                    .compressToFile(file);

            callback.onImageUploadingCompressionSuccess(media);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onImageUploadingCompressionFailed(media, e.getMessage());
        }

        return compressedFile;
    }

    private void uploadImage(final Media media) {
        File compressedFile = compress(media, getFile(media));

        if (compressedFile == null) {
            callback.onImageUploadingFailed(media, "File is NULL");
            next();

        } else {
            ProgressRequestBody fileBody = new ProgressRequestBody(compressedFile, this);

            MultipartBody.Part filePart = MultipartBody.Part.createFormData("media", compressedFile.getName(), fileBody);
            mediaUploadCall = RestServiceFactory.createService().uploadMediaImageChat(
                    filePart,
                    "image",
                    "chat");

            mediaUploadCall.enqueue(new RestCallBack<ResponseModel<ImageServer>>(context) {
                @Override
                public void onFailure(Call<ResponseModel<ImageServer>> call, String message) {
                    callback.onImageUploadingFailed(media, message);
                }

                @Override
                public void onResponse(Call<ResponseModel<ImageServer>> call, Response<ResponseModel<ImageServer>> restResponse, ResponseModel<ImageServer> response) {
                    if (RestCallBack.isSuccessFull(response)) {
                        if (response.data != null) {
                            callback.onImageUploadingSuccess(media, response.data);
                            next();
                        }
                    } else {
                        callback.onImageUploadingFailed(media, "Null response from server");
                    }
                }
            });
        }
    }

    private File getFile(Media media) {
        String path = null;
        if (media.getPath() == null) {
            path = getFileFromUri(media.getUri());
        } else {
            path = media.getPath();
        }

        if (path == null) {
            return null;
        }

        return new File(path);
    }

    private void next() {
        if (list.size() > currentMediaPosition + 1) {
            currentMediaPosition++;
            uploadImage(list.get(currentMediaPosition));
            callback.onNextImage(list.get(currentMediaPosition), list);
        } else {
            isCompletedUploading = true;
            callback.onUploadingComplete();
        }
    }

    private String getFileFromUri(Uri uri) {

        try {
            if (uri.getScheme().equalsIgnoreCase("content")) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                } catch (Exception e) {
                    LogUtils.debug("Cannot get file path from uri " + uri + " " + e.getMessage());
                }
            } else if (uri.getScheme().equalsIgnoreCase("file")) {
                return uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.debug("Cannot get file path from uri " + uri + " " + e.getMessage());
        }

        return null;
    }

    @Override
    public void onProgressUpdate(int percentage) {
        if (percentage != 0 && (percentage % 5 == 0 || percentage % 7 == 0 || percentage == 100)) {
            LogUtils.debug("onProgressUpdate " + percentage);
            int overallProgress = currentMediaPosition * percentageSegment + (int) (percentageSegment * ((float) percentage / 100f));
            LogUtils.debug("percentageSegment " + percentageSegment);
            callback.onImageUploadingProcess(list.get(currentMediaPosition), percentage, overallProgress);
        }
    }

    @Override
    public void onError() {
        LogUtils.debug("onError");
    }

    @Override
    public void onFinish() {
        LogUtils.debug("onUploadMediaFileQueueCompleted");
    }

}
