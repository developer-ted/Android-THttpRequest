package com.tedkim.thttprequest.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.tedkim.thttprequest.R;
import com.tedkim.thttprequest.config.APIPreferencesKey;
import com.tedkim.thttprequest.utils.APIUtils;
import com.tedkim.thttprequest.vo.auth.AuthTokenVO;

/**
 * Access Token and Refresh Token Manager Class
 * Created by ted
 */
public class TokenManager {

    /**
     * Get base token
     *
     * @param context context
     * @return base token
     */
    public static String getBasicToken(Context context) {
        String clientID = context.getString(R.string.api_client_id);
        String clientSecret = context.getString(R.string.api_client_secret);
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String token = clientID + ":" + APIUtils.getMD5encode(clientSecret + timeStamp) + ":" + timeStamp;
        return APIUtils.removeNewLineCharacter(APIUtils.getBase64encode(token));
    }

    /**
     * Token setting
     *
     * @param context context
     * @param vo      AuthTokenVO
     */
    public static void setToken(Context context, AuthTokenVO vo) {
        setAccessToken(context, vo.getTk_id());
        setRefreshToken(context, vo.getRf_id());
        setExpireTime(context, vo.getExpires());
    }

    /**
     * Set access token
     */
    private static void setAccessToken(Context context, String token) {
        SharedPreferences pref = context.getSharedPreferences(APIPreferencesKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(APIPreferencesKey.ACCESS_TOKEN, token);
        edit.apply();
    }

    /**
     * Get access token
     *
     * @return Access token
     */
    public static String getAccessToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(APIPreferencesKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getString(APIPreferencesKey.ACCESS_TOKEN, null);
    }

    /**
     * Set refresh token
     */
    private static void setRefreshToken(Context context, String token) {
        SharedPreferences pref = context.getSharedPreferences(APIPreferencesKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(APIPreferencesKey.REFRESH_TOKEN, token);
        edit.apply();
    }

    /**
     * Get refresh token
     *
     * @return refresh token
     */
    public static String getRefreshToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(APIPreferencesKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getString(APIPreferencesKey.REFRESH_TOKEN, null);
    }

    /**
     * Set expire time
     */
    private static void setExpireTime(Context context, long token) {
        SharedPreferences pref = context.getSharedPreferences(APIPreferencesKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putLong(APIPreferencesKey.EXPIRE_TIME, token);
        edit.apply();
    }

    /**
     * Get expire time
     *
     * @return Access token
     */
    public static long getExpireTime(Context context) {
        SharedPreferences pref = context.getSharedPreferences(APIPreferencesKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getLong(APIPreferencesKey.EXPIRE_TIME, 0);
    }


    /**
     * Token initialization logout
     */
    public static void resetToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(APIPreferencesKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.remove(APIPreferencesKey.ACCESS_TOKEN);
        edit.remove(APIPreferencesKey.REFRESH_TOKEN);
        edit.remove(APIPreferencesKey.EXPIRE_TIME);
        edit.apply();
    }

    /**
     * Get SID
     *
     * @param context context
     * @return sid
     */
    public static String getSID(Context context) {
        SharedPreferences pref = context.getSharedPreferences(APIPreferencesKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String sid = pref.getString(APIPreferencesKey.SID, null);
        if (sid == null) {
            sid = APIUtils.getRandomCode(10);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(APIPreferencesKey.SID, sid);
            edit.apply();
        }

        return sid;
    }
}
