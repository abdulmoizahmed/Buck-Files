package com.example.moiz_ihs.bucksapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Moiz-IHS on 7/28/2018.
 */

public class PostResponse {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private List<String> message = null;
    @SerializedName("msg")
    @Expose
    private String msg;
   /* @SerializedName("data")
    @Expose
    private List<Object> data = null;

    @SerializedName("data")
    @Expose
    private Object dataLogin;*/


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    /*public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }*/

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
