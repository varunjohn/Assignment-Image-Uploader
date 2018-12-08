package com.varunjohn1990.assignmentimageupload.httpclient;

import android.content.Context;

import com.varunjohn1990.assignmentimageupload.util.AppConstants;
import com.varunjohn1990.assignmentimageupload.util.NetworkUtil;
import com.varunjohn1990.assignmentimageupload.R;

import java.io.IOException;

import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public abstract class RestCallBack<T> implements Callback<T> {

    private Context context;

    public RestCallBack(Context context) {
        this.context = context;
    }

    public abstract void onFailure(Call<T> call, String message);

    public abstract void onResponse(Call<T> call, Response<T> restResponse, T response);

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (call.isCanceled()) {
            onFailure(call, "Cancelled");
            return;
        }

        if (NetworkUtil.isNetworkAvailable()) {
            if (t.getLocalizedMessage() != null)
                onFailure(call, t.getLocalizedMessage());
            else
                onFailure(call, "");
        } else
            onFailure(call, context.getString(R.string.no_internet));
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onResponse(call, response, response.body());
        } else {
            onFailure(call, response.code() + " : " + response.message());
        }
    }

    public static boolean isSuccessFull(ResponseModel responseModel) {
        if (responseModel.statusCode.equals(AppConstants.ApiParam.SUCCESS_RESPONSE_CODE))
            return true;
        else
            return false;
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "Response can't be converted into a string";
        }
    }
}
