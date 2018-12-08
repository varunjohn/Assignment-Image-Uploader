package com.varunjohn1990.assignmentimageupload.model;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class Media implements Serializable {

    public enum UploadStatus {
        WAITING,
        DOWNLOADING,
        COMPLETED,
        FAILED
    }

    private String path;
    private Uri uri;
    private UploadStatus uploadStatus = UploadStatus.WAITING;

    private int downloadingProgress;

    public Media(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getDownloadingProgress() {
        return downloadingProgress;
    }

    public void setDownloadingProgress(int downloadingProgress) {
        this.downloadingProgress = downloadingProgress;
    }

    public UploadStatus getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(UploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
