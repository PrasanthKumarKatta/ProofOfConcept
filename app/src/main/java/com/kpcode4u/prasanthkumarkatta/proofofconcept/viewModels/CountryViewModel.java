package com.kpcode4u.prasanthkumarkatta.proofofconcept.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kpcode4u.prasanthkumarkatta.proofofconcept.api.Client;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.api.Service;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.model.CountryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryViewModel extends ViewModel {

    //this data fetch Asynchrously
    private MutableLiveData<CountryResponse> countryData;

    //this mothod to get the Data
    public LiveData<CountryResponse> getCountryData() {
        //ifdata is null
        if (countryData == null) {
            countryData = new MutableLiveData<>();
            //this method for API call for Data loading 
            loadCounriesData();
        }
        //Todo:kpk: return mutableData of the API response
        return countryData;

    }

    private void loadCounriesData() {
        try {
            Service service = Client.getClient().create(Service.class);
            Call<CountryResponse> call = service.getCountryData();
            call.enqueue(new Callback<CountryResponse>() {
                @Override
                public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                    CountryResponse countryResponse = response.body();
                    if (countryResponse != null) {
                        //Todo:kpk: we are setting the total API response to Our MutableLibeData
                        countryData.setValue(countryResponse);
                    }
                }

                @Override
                public void onFailure(Call<CountryResponse> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
