package com.tedkim.thttprequest.services;

import com.tedkim.thttprequest.config.APIAddress;
import com.tedkim.thttprequest.vo.auth.VerificationVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


/**
 * Created by Ted
 */
public interface AuthServices {

    /**
     * Refresh 토큰 요청
     *
     * @param vo VerificationVO
     * @return call object
     */
    @POST(APIAddress.REFRESH)
    Call<Object> requestAuthRefresh(@Body VerificationVO vo);


}
