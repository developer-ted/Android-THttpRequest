package com.tedkim.thttprequest.interfaces;

import com.tedkim.thttprequest.vo.ErrorVO;

/**
 * API response listener
 * Created by Ted
 */
public interface APIResponseListener {

    void getData(Object obj);
    void getError(ErrorVO errorVO);

}
