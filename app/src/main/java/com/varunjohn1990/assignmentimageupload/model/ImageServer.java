package com.varunjohn1990.assignmentimageupload.model;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class ImageServer implements java.io.Serializable {
    private static final long serialVersionUID = -1895096268926742297L;
    private String s3MediaUrl;
    private String s3MediaThumbnailUrl;

    private Integer width;
    private Integer height;

    public String getS3MediaUrl() {
        return s3MediaUrl;
    }

    public void setS3MediaUrl(String s3MediaUrl) {
        this.s3MediaUrl = s3MediaUrl;
    }

    public String getS3MediaThumbnailUrl() {
        return s3MediaThumbnailUrl;
    }

    public void setS3MediaThumbnailUrl(String s3MediaThumbnailUrl) {
        this.s3MediaThumbnailUrl = s3MediaThumbnailUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}