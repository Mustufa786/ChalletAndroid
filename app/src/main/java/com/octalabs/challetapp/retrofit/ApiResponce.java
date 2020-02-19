package com.octalabs.challetapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponce<T> {
    @SerializedName("success")
    @Expose
    public boolean isSuccess;

    @SerializedName("message")
    @Expose
    public String msg;

    @SerializedName("data")
    @Expose
    public T obj;
}
