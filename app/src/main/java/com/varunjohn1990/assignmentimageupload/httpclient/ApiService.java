package com.varunjohn1990.assignmentimageupload.httpclient;

import com.varunjohn1990.assignmentimageupload.util.AppConstants;
import com.varunjohn1990.assignmentimageupload.model.ImageServer;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public interface ApiService {

    @Multipart
    @POST(AppConstants.Url.MEDIA_UPLOAD_CHAT)
    Call<ResponseModel<ImageServer>> uploadMediaImageChat(@Part MultipartBody.Part media,
                                                          @Query(AppConstants.ApiParam.MEDIA_TYPE) String mediaType,
                                                          @Query(AppConstants.ApiParam.MEDIA_FOR) String mediaFor);
}
