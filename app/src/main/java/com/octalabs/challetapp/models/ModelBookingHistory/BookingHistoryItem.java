
package com.octalabs.challetapp.models.ModelBookingHistory;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingHistoryItem {


    @SerializedName("bookingItemIds")
    @Expose
    private List<BookingHistoryDetails> bookingItemIds = null;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("bookingDates")
    @Expose
    private List<BookingDate> bookingDates = null;
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public List<BookingHistoryDetails> getBookingItemIds() {
        return bookingItemIds;
    }

    public void setBookingItemIds(List<BookingHistoryDetails> bookingItemIds) {
        this.bookingItemIds = bookingItemIds;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BookingDate> getBookingDates() {
        return bookingDates;
    }

    public void setBookingDates(List<BookingDate> bookingDates) {
        this.bookingDates = bookingDates;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }


}
