
package com.octalabs.challetapp.models.ModelDetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChaletDetails {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("picture")
    @Expose

    private List<String> picture = null;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("pricePerNight")
    @Expose
    private Integer pricePerNight;
    @SerializedName("amenityIds")
    @Expose

    private List<AmenityId> amenityIds = null;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("type")
    @Expose
    private List<String> type = null;
    @SerializedName("for")
    @Expose
    private List<String> _for = null;
    @SerializedName("bookingType")
    @Expose
    private List<String> bookingType = null;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("checkIn")
    @Expose
    private String checkIn;
    @SerializedName("checkOut")
    @Expose
    private String checkOut;
    @SerializedName("countryId")
    @Expose
    private String countryId;
    @SerializedName("stateId")
    @Expose
    private String stateId;
    @SerializedName("cityId")
    @Expose
    private String cityId;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;
    @SerializedName("whatsapp")
    @Expose
    private String whatsapp;


    @SerializedName("facebook")
    @Expose
    private String facebook;


    @SerializedName("twitter")
    @Expose
    private String twitter;


    @SerializedName("email")
    @Expose
    private String email;

    public ChaletDetails() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPicture() {
        return picture;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Integer getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Integer pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public List<AmenityId> getAmenityIds() {
        return amenityIds;
    }

    public void setAmenityIds(List<AmenityId> amenityIds) {
        this.amenityIds = amenityIds;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getFor() {
        return _for;
    }

    public void setFor(List<String> _for) {
        this._for = _for;
    }

    public List<String> getBookingType() {
        return bookingType;
    }

    public void setBookingType(List<String> bookingType) {
        this.bookingType = bookingType;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
