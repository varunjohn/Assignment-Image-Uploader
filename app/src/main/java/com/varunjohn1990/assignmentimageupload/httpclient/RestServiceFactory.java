package com.varunjohn1990.assignmentimageupload.httpclient;

import com.varunjohn1990.assignmentimageupload.util.AppConstants;
import com.varunjohn1990.assignmentimageupload.logic.MyApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class RestServiceFactory {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit retrofit;

    private static ApiService apiService;

    private static <S> S createService(Class<S> serviceClass) {
        if (apiService == null) {

            if (MyApplication.RETROFIT_SHOW_LOG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(logging);
            }

            httpClient.readTimeout(15, TimeUnit.SECONDS);
            httpClient.connectTimeout(15, TimeUnit.SECONDS);
            httpClient.writeTimeout(15, TimeUnit.MINUTES);

            String baseUrl = "";

            switch (MyApplication.apiMode) {
                case TESTING:
                    baseUrl = AppConstants.Url.BASE_SERVICE_TESTING;
                    break;
                case LIVE:
                    baseUrl = AppConstants.Url.BASE_SERVICE_LIVE;
                    break;
                default:
            }

            Retrofit.Builder builder =
                    new Retrofit.Builder()

                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create());
            retrofit = builder.client(httpClient.build()).build();
            apiService = (ApiService) retrofit.create(serviceClass);
        }
        return (S) apiService;
    }

    public static ApiService createService() {
        return createService(ApiService.class);
    }
}
