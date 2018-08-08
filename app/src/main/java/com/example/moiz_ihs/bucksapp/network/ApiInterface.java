package com.example.moiz_ihs.bucksapp.network;

import com.example.moiz_ihs.bucksapp.model.LoginResponse;
import com.example.moiz_ihs.bucksapp.model.PostResponse;
import com.example.moiz_ihs.bucksapp.model.QuestionResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Moiz-IHS on 7/28/2018.
 */

public interface ApiInterface {
    @GET(EndPoints.QUESTIONS)
    Call<List<QuestionResponse>> getAllQuestions();


    @POST(EndPoints.SIGN_UP)
    Call<PostResponse> postUser(@Body Object payload);

    @POST(EndPoints.SIGN_IN)
    Call<LoginResponse> postSignIn(@Body Object payload);

    @Multipart
    @POST(EndPoints.DOCUMENT)
    Call<PostResponse> postDocument(@Path("imei") String imei,@Part MultipartBody.Part file);

    @Multipart
    @POST(EndPoints.IMAGES)
    Call<PostResponse> postImages(@Path("imei") String imei,@Part MultipartBody.Part file);

    @Multipart
    @POST(EndPoints.AUDIO)
    Call<PostResponse> postAudios(@Path("imei") String imei,@Part MultipartBody.Part file);

    @Multipart
    @POST(EndPoints.VIDEO)
    Call<PostResponse> postVideos(@Path("imei") String imei,@Part MultipartBody.Part file);


}
