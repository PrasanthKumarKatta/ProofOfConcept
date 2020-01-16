package com.kpcode4u.prasanthkumarkatta.proofofconcept.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryResponse {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rows")
    @Expose
    private List<Country> countryList;

    public String getTitle() {
        return title;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

}
