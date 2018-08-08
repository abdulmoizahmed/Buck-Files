package com.example.moiz_ihs.bucksapp.network;

import android.util.Log;

import com.example.moiz_ihs.bucksapp.model.LoginResponse;
import com.example.moiz_ihs.bucksapp.model.PostResponse;
import com.example.moiz_ihs.bucksapp.model.QuestionResponse;
import com.example.moiz_ihs.bucksapp.service.BackUpService;
import com.example.moiz_ihs.bucksapp.utils.CommonUtils;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Moiz-IHS on 7/28/2018.
 */

public class ApiServices {

    public static final String ERROR_MESSAGE = "No Response from Server";
    public static final String SUCCESS_CODE = "200";
    public static final String FAILURE_CODE = "201";

    public static void getAllQuestions(final OnResponseReceiveListener listener) {
        RestClient.getAuthAdapter().getAllQuestions().enqueue(new Callback<List<QuestionResponse>>() {
            @Override
            public void onResponse(Call<List<QuestionResponse>> call, Response<List<QuestionResponse>> response) {
                if (response.body() != null)
                    listener.onSuccessReceived(response.body());
                else
                    listener.onFailureReceived("No Response from Server");
            }

            @Override
            public void onFailure(Call<List<QuestionResponse>> call, Throwable t) {
                listener.onFailureReceived(ERROR_MESSAGE);
            }
        });
    }

    public static void postSignUp(Object payload, final OnResponseReceiveListener listener) {
        RestClient.getAuthAdapter().postUser(payload).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.body() != null) {
                    if (response.body().getCode().equals("200"))
                        listener.onSuccessReceived(response.body().getMsg()!= null?response.body().getMsg():response.body().getMessage().get(0));
                    else
                        listener.onFailureReceived(response.body().getMsg()!= null?response.body().getMsg():response.body().getMessage().get(0));
                } else
                    listener.onFailureReceived(ERROR_MESSAGE);
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                listener.onFailureReceived(ERROR_MESSAGE);
            }
        });
    }


    public static void postSignIn(Object payload, final OnResponseReceiveListener listener) {
        RestClient.getAuthAdapter().postSignIn(payload).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null) {
                    if (response.body().getCode().equals(SUCCESS_CODE))
                        listener.onSuccessReceived(response.body().getMsg()!= null?response.body().getMsg():response.body().getMessage());
                    else
                        listener.onFailureReceived(response.body().getMsg()!= null?response.body().getMsg():response.body().getMessage());
                } else
                    listener.onFailureReceived(ERROR_MESSAGE);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                listener.onFailureReceived(ERROR_MESSAGE);
            }
        });
    }

    public static void postDocuments(String imei, final File file, final OnResponseReceiveListener listener) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file.getAbsolutePath());

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("data[]", file.getName(), requestFile);

        RestClient.getAuthAdapter().postDocument(imei, multipartBody).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.body() != null) {
                    if (response.body().getCode().equals(SUCCESS_CODE))
                        listener.onSuccessReceived(SUCCESS_CODE);
                    else
                        listener.onFailureReceived(file.getAbsolutePath());
                }

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                listener.onFailureReceived(file.getAbsolutePath());
            }
        });

    }

    public static void postVideos(String imei, final File file, final OnResponseReceiveListener listener) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file.getAbsolutePath());

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("data[]", file.getName(), requestFile);

        RestClient.getAuthAdapter().postVideos(imei, multipartBody).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.body() != null) {
                    if (response.body().getCode() != null && response.body().getCode().equals(SUCCESS_CODE))
                        listener.onSuccessReceived(SUCCESS_CODE);
                    else
                        listener.onFailureReceived(file.getAbsolutePath());
                }

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                listener.onFailureReceived(file.getAbsolutePath());
            }
        });

    }

    public static void postImages(String imei, final File file, final OnResponseReceiveListener listener) {
         //RequestBody.create(MediaType.parse("multipart/form-data"), file.getAbsolutePath());
        RequestBody requestFile =RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("data[]", file.getName(), requestFile);

        RestClient.getAuthAdapter().postImages(imei, multipartBody).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.body() != null) {
                    if (response.body().getCode() != null && response.body().getCode().equals(SUCCESS_CODE))
                        listener.onSuccessReceived(SUCCESS_CODE);
                    else
                        listener.onFailureReceived(file.getAbsolutePath());
                }

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                listener.onFailureReceived(file.getAbsolutePath());
            }
        });
    }


    public static void postAudios(String imei, final File file, final OnResponseReceiveListener listener) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/*"), file.getAbsolutePath());

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("data[]", file.getName(), requestFile);

        RestClient.getAuthAdapter().postAudios(imei, multipartBody).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.body() != null) {
                    if (response.body().getCode() != null && response.body().getCode().equals(SUCCESS_CODE))
                        listener.onSuccessReceived(SUCCESS_CODE);
                    else
                        listener.onFailureReceived(file.getAbsolutePath());
                }

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                listener.onFailureReceived(file.getAbsolutePath());
            }
        });

    }
}
