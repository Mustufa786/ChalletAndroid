
package com.octalabs.challetapp.models.ModelCheckout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCheckout {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose

    private Checkout data;

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

    public Checkout getData() {
        return data;
    }

    public void setData(Checkout data) {
        this.data = data;
    }

}
