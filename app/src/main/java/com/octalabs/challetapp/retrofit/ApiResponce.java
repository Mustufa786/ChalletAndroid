package com.octalabs.challetapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponce<T> {
    @SerializedName("success")
    @Expose
    private boolean isSuccess;

    @SerializedName("message")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private T obj;
}
