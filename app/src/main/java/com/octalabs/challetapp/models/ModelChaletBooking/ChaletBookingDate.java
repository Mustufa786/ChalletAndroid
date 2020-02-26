package com.octalabs.challetapp.models.ModelChaletBooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChaletBookingDate {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("bookingFrom")
    @Expose
    private String bookingFrom;
    @SerializedName("bookingTo")
    @Expose
    private String bookingTo;
    @SerializedName("bookingItemId")
    @Expose
    private String bookingItemId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingFrom() {
        return bookingFrom;
    }

    public void setBookingFrom(String bookingFrom) {
        this.bookingFrom = bookingFrom;
    }

    public String getBookingTo() {
        return bookingTo;
    }

    public void setBookingTo(String bookingTo) {
        this.bookingTo = bookingTo;
    }

    public String getBookingItemId() {
        return bookingItemId;
    }

    public void setBookingItemId(String bookingItemId) {
        this.bookingItemId = bookingItemId;
    }

}
