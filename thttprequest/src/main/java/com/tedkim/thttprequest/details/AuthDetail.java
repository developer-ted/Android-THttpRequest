package com.tedkim.thttprequest.details;

import android.content.Context;

import com.tedkim.thttprequest.interfaces.APIResponseListener;
import com.tedkim.thttprequest.manager.TokenManager;
import com.tedkim.thttprequest.requestclient.APIRequestManager;
import com.tedkim.thttprequest.requestclient.APIRequestVO;
import com.tedkim.thttprequest.requestclient.RequestClient;
import com.tedkim.thttprequest.services.AuthServices;
import com.tedkim.thttprequest.utils.APIUtils;
import com.tedkim.thttprequest.vo.ErrorVO;
import com.tedkim.thttprequest.vo.auth.AuthVO;
import com.tedkim.thttprequest.vo.auth.VerificationVO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Auth API Collection
 * Created by Ted
 */
public class AuthDetail {

    private static final String TAG = AuthDetail.class.getSimpleName();

    private Context mContext;
    private APIRequestManager mAPIRequestManager = null;
    private APIResponseListener mAPIResponseListener = null;

    private APIRequestVO<Object> mRequestItem;
    private String mUniqueID;

    public AuthDetail(Context context) {
        mContext = context;
        mAPIRequestManager = APIRequestManager.getInstance();
        mUniqueID = APIUtils.getAPIRandomCode();
    }

    /**
     * Refresh 토큰 요청
     */
    public static void requestAuthRefresh(final Context context, final APIResponseListener listener) {
        String refresh = TokenManager.getRefreshToken(context);
        String sid = TokenManager.getSID(context);

        APIRequestVO<Object> requestItem = new APIRequestVO<>();
        final Retrofit retrofit = new RequestClient(context).getBaseClient();
        final AuthServices service = retrofit.create(AuthServices.class);
        final String uniqueID = APIUtils.getAPIRandomCode();
        requestItem.setCall(service.requestAuthRefresh(new VerificationVO(true, refresh, sid)));
        requestItem.setCallback(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    AuthVO vo = (AuthVO) APIUtils.toJsonObject(response.body(), AuthVO.class);
                    // FIXME
//                    TokenManager.setToken(context, vo.getAuth().getToken());
//                    User.setUser(context, vo.getAuth().getUser());
                    APIRequestManager.getInstance().retryAPIRequest();//이전 api call 재시도
                } else {
                    ErrorVO errorVO = APIUtils.parseError(retrofit, response.errorBody());
                    APIUtils.errorResponse(context, null, errorVO, null);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                APIUtils.errorFailure(context, uniqueID, t, listener);
            }
        });
        APIRequestManager.getInstance().addRequestCall(uniqueID, requestItem);
    }

    /**
     * Set api response listener
     *
     * @param listener APIResponseListener
     */
    public AuthDetail setListener(APIResponseListener listener) {
        mAPIResponseListener = listener;
        return this;
    }

    /**
     * API Request build
     */
    public void build() {
        mAPIRequestManager.addRequestCall(mUniqueID, mRequestItem);
    }
}
