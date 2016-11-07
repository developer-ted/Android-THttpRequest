package com.tedkim.thttprequest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.tedkim.thttprequest.config.APIConfig;
import com.tedkim.thttprequest.config.APIPreferencesKey;
import com.tedkim.thttprequest.config.ErrorType;
import com.tedkim.thttprequest.details.AuthDetail;
import com.tedkim.thttprequest.interfaces.APIResponseListener;
import com.tedkim.thttprequest.manager.TokenManager;
import com.tedkim.thttprequest.requestclient.APIRequestManager;
import com.tedkim.thttprequest.requestclient.RequestClient;
import com.tedkim.thttprequest.vo.ErrorVO;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * API Utils
 * Created by Ted
 */
public class APIUtils {

    private static final String TAG = APIUtils.class.getSimpleName();

    /**
     * Not Exist Token Error 떨어졌을 경우 한번 더 API 요청하기 위한 flag 값
     */
    private static boolean isRetryInvalidAccessToken = false;

    /**
     * API request error
     * response received but request not successful (like 400,401,403,404 etc)
     *
     * @param context  context
     * @param uniqueID API unique ID
     * @param errorVO      ErrorVO
     * @param listener APIResponseListener
     */
    public static void errorResponse(Context context, String uniqueID, ErrorVO errorVO, APIResponseListener listener) {
        if (errorVO != null) {
            if (RequestClient.isLog)
                Log.d(TAG, "Error code : " + errorVO.getCode() + ", message : " + errorVO.getMessage());
            String errorType = errorVO.getCode();

            if (errorType != null) {
                switch (errorType) {
                    case ErrorType.AUTH.EXPIRED_TOKEN:
                    case ErrorType.COMMON.INVALID_TOKEN:
                    case ErrorType.COMMON.EXPIRED_TOKEN:
                        // 토큰 만료 오류 시
                        APIRequestManager.getInstance().cancelAllRequest(false);
                        APIRequestManager.getInstance().responseTokenError();
                        AuthDetail.requestAuthRefresh(context, listener);
                        break;

                    case ErrorType.COMMON.NOT_EXISTS_TOKEN:
                        // 유효하지 않은 access_token 일 경우 한번 더 api call 시도하고
                        // 다시 한번 error 가 떨어질 경우 logout 시킴
                        if (!isRetryInvalidAccessToken) {
                            Log.d(TAG, "EXPIRED_TOKEN");
                            isRetryInvalidAccessToken = true;
                            APIRequestManager.getInstance().cancelAllRequest(false);
                            APIRequestManager.getInstance().responseTokenError();

                            APIRequestManager apiRequestManager = APIRequestManager.getInstance();
                            apiRequestManager.retryAPIRequest();
                        } else {
                            Log.d(TAG, "EXPIRED_TOKEN 2");
                            isRetryInvalidAccessToken = false;
                            APIRequestManager.getInstance().cancelAllRequest(true);
                            TokenManager.resetToken(context);
                            listener.getError(errorVO);// 웹에 로그아웃 이벤트 전달하기 위해 뷰로 반환
                        }
                        break;

                    default:
                        APIRequestManager.getInstance().removeRequestCall(uniqueID);
                        if (listener != null)
                            listener.getError(errorVO);
                        break;
                }
            }
        }
    }

    /**
     * API request failure
     *
     * @param context  context
     * @param t        error object
     * @param listener api response listener
     */
    public static void errorFailure(Context context, String uniqueID, Throwable t, APIResponseListener listener) {
        if (RequestClient.isLog) Log.e(TAG, "[errorFailure]" + t.getMessage());
        APIRequestManager.getInstance().removeRequestCall(uniqueID);
    }

    /**
     * Handling api request failure
     */
    public static void onError(Context context, ErrorVO errorVO) {
        Toast.makeText(context, errorVO.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Parse ErrorVO
     * @param retrofit Retrofit
     * @param response ResponseBody
     * @return ErrorVO
     */
    public static ErrorVO parseError(Retrofit retrofit, ResponseBody response) {
        Converter<ResponseBody, ErrorVO> converter =
                retrofit.responseBodyConverter(ErrorVO.class, new Annotation[0]);

        ErrorVO error;

        try {
            error = converter.convert(response);
        } catch (IOException e) {
            return new ErrorVO();
        }

        return error;
    }

    /**
     * Get api random code
     *
     * @return random code
     */
    public static String getAPIRandomCode() {
        return getRandomCode(20);
    }

    /**
     * Get random code (english and number)
     *
     * @param length random code length
     * @return random code
     */
    public static String getRandomCode(int length) {
        Random rnd = new Random();
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < length; i++) {
            if (rnd.nextBoolean()) {
                buf.append((char) (rnd.nextInt(26) + 97));
            } else {
                buf.append((rnd.nextInt(10)));
            }
        }

        return String.valueOf(buf);
        //return "fdsafa2f";
    }

    /**
     * Get device country
     *
     * @return country code
     */
    public static String getDeviceCountryCode() {
        return Locale.getDefault().getCountry();
    }

    /**
     * Get device language
     *
     * @return language code
     */
    public static String getDeviceLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * Get device timezone offset
     *
     * @return timezone offest
     */
    public static String getCurrentTimezoneOffset() {
        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = -mTimeZone.getRawOffset();

        return String.valueOf(TimeUnit.MINUTES.convert(mGMTOffset, TimeUnit.MILLISECONDS));
    }

    /**
     * Get app version code
     *
     * @param context     context
     * @param packageName app package name
     * @return version code
     */
    public static int getVersionCode(Context context, String packageName) {
        int v = 0;
        try {
            v = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    /**
     * Make a vo to json string
     *
     * @param json  json string
     * @param clazz convert class
     * @return vo object
     */
    public static Object toJsonString(String json, Class clazz) {
        Gson gson = new Gson();
        TypeAdapter adapter = gson.getAdapter(clazz);
        try {
            return adapter.fromJson(json);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Make a vo to json object
     *
     * @param object json object
     * @param clazz  convert class
     * @return vo object
     */
    public static Object toJsonObject(Object object, Class clazz) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();
        return gson.fromJson(jsonObject, clazz);
    }

    /**
     * Make to json
     *
     * @param obj vo object
     * @return Json String
     */
    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

    /**
     * Base64 encoding
     *
     * @param content encoding string
     * @return Base64-encoded string
     */
    public static String getBase64encode(String content) {
        return Base64.encodeToString(content.getBytes(), 0);
    }

    /**
     * MD5 encoding
     *
     * @param content encoding string
     * @return MD5-encoded string
     */
    public static String getMD5encode(String content) {
        MessageDigest m = null;
        String hash = null;

        try {
            m = MessageDigest.getInstance("MD5");
            m.update(content.getBytes(), 0, content.length());
            hash = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }

    /**
     * Get New-line character remove
     *
     * @param message changed message
     * @return Removed New-line character
     */
    public static String removeNewLineCharacter(String message) {
        return message.replace(System.getProperty("line.separator"), "");
    }


    /**
     * Check true / false
     *
     * @param value int vaule
     * @return true / false
     */
    public static boolean isValue(int value) {
        return value == 1;
    }

    /**
     * Set app language code
     *
     * @param context context
     * @param code    language code
     */
    public static void setAppLanguageCode(Context context, String code) {
        SharedPreferences pref = context.getSharedPreferences(APIPreferencesKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(APIPreferencesKey.APP_LANGUAGE, code);
        edit.apply();
    }

    /**
     * Get app language code
     *
     * @param context context
     * @return app language code
     */
    public static String getAppLanguageCode(Context context) {
        try {
            SharedPreferences pref = context.getSharedPreferences(APIPreferencesKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
            return pref.getString(APIPreferencesKey.APP_LANGUAGE, null);
        }catch (Exception e) {
            return APIConfig.LANGUAGE_EN;
        }
    }
}
