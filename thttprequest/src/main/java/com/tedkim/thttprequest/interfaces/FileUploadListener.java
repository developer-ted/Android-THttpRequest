package com.tedkim.thttprequest.interfaces;

/**
 * Created by ted
 */
public interface FileUploadListener {

    void onComplete(String fileType, String fileUrl);
    void onError(int type);

}
