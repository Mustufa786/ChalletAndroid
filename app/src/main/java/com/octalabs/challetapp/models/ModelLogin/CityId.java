package com.octalabs.challetapp.models.ModelLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityId {

    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("countryId")
    @Expose
    private String countryId;
    @SerializedName("stateId")
    @Expose
    private String stateId;
    @SerializedName("__v")
    @Expose
    private Integer v;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}