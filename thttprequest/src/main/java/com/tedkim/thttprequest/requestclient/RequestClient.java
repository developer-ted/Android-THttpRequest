package com.tedkim.thttprequest.requestclient;

import android.content.Context;

import com.tedkim.thttprequest.manager.APIManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ted
 */
public class RequestClient {

    public static boolean isLog = false;

    private Context mContext;

    public RequestClient(Context context) {
        mContext = context;
    }

    /**
     * Retrofit clients that use a base
     *
     * @return Retrofit Client
     */
    public Retrofit getBaseClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (isLog) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                return chain.proceed(RequestHeaders.getInstance(mContext).getBasicHeader(chain));
            }
        });

        OkHttpClient client = httpClient.build();
        return new Retrofit.Builder()
                .baseUrl(APIManager.getBaseUrl(mContext))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    /**
     * Retrofit clients that use a common
     *
     * @return Retrofit Client
     */
    public Retrofit getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (isLog) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                return chain.proceed(RequestHeaders.getInstance(mContext).getHeader(chain));
            }
        });

        OkHttpClient client = httpClient.build();
        return new Retrofit.Builder()
                .baseUrl(APIManager.getBaseUrl(mContext))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    /**
     * Retrofit clients that use the File Upload
     *
     * @param clazz service interface
     * @return Retrofit Client
     */
    public Retrofit getFileUploadClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (isLog) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                return chain.proceed(RequestHeaders.getInstance(mContext).getFileUploadHeader(chain));
            }
        });

        OkHttpClient client = httpClient.build();
        return new Retrofit.Builder()
                .baseUrl(APIManager.getBaseUrl(mContext))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

}
