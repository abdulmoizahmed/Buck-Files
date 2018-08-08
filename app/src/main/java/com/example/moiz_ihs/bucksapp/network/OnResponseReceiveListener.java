package com.example.moiz_ihs.bucksapp.network;

/**
 * Created by Moiz-IHS on 7/28/2018.
 */

public interface OnResponseReceiveListener {
    void onSuccessReceived(Object response);
    void onFailureReceived(String errorMessage);
}
