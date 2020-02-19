package com.octalabs.challetapp.models.modelseatchcity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octalabs.challetapp.models.ModelLogin.CountryId;
import com.octalabs.challetapp.models.ModelLogin.StateId;

public class ModelCity {

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
    private CountryId countryId;
    @SerializedName("stateId")
    @Expose
    private StateId stateId;
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

    public CountryId getCountryId() {
        return countryId;
    }

    public void setCountryId(CountryId countryId) {
        this.countryId = countryId;
    }

    public StateId getStateId() {
        return stateId;
    }

    public void setStateId(StateId stateId) {
        this.stateId = stateId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
