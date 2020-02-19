package com.octalabs.challetapp.models.ModelRegister;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octalabs.challetapp.models.ModelLogin.Login;

public class RegisterModel {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Login data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Login getData() {
        return data;
    }

    public void setData(Login data) {
        this.data = data;
    }


}
