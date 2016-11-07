package com.tedkim.thttprequest.manager;

import android.content.Context;

import com.madsquare.android.snackk.api.R;

/**
 * API Manager
 * Created by ted
 */
public class APIManager {

    /**
     * true : develop, false : release
     */
    public static boolean isDebug = false;
    private static APIManager mInstance;

    public static APIManager getInstance() {
        if (mInstance == null) {
            mInstance = new APIManager();
        }
        return mInstance;
    }

    /**
     * Get base api url
     *
     * @return base api rul
     */
    public static String getBaseUrl(Context context) {
        if (isDebug)
            return context.getString(R.string.develop_base_url);
        else
            return context.getString(R.string.release_base_url);
    }

    /**
     * Get api client id
     *
     * @return client id key
     */
    public static String getClientID(Context context) {
        return context.getString(R.string.api_client_id);
    }

    /**
     * Get api client secret
     *
     * @return client secret key
     */
    public static String getClientSecret(Context context) {
        return context.getString(R.string.api_client_secret);
    }
}