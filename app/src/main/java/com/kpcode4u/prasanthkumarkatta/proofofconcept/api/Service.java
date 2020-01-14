package com.kpcode4u.prasanthkumarkatta.proofofconcept.api;

import com.kpcode4u.prasanthkumarkatta.proofofconcept.model.CountryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET("/s/2iodh4vg0eortkl/facts.json")
    Call<CountryResponse> getCountryData();
}
