package com.example.moiz_ihs.bucksapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Moiz-IHS on 7/28/2018.
 */

public class QuestionResponse {
    @SerializedName("sq_id")
    @Expose
    private String sqId;
    @SerializedName("question")
    @Expose
    private String question;

    public String getSqId() {
        return sqId;
    }

    public void setSqId(String sqId) {
        this.sqId = sqId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString()
    {
        return question;
    }


}
