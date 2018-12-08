package com.varunjohn1990.assignmentimageupload.httpclient;


/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class ResponseModel<T> {

    public boolean dataFromWiderWindow;
    public String statusCode;
    public String errorMessage;
    public String updateInfoText;
    public int totalCount;
    public T data;

}
