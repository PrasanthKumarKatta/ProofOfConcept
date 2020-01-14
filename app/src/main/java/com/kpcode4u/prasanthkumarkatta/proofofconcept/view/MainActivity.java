package com.kpcode4u.prasanthkumarkatta.proofofconcept.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.R;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.adapter.CountryAdapter;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.api.Client;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.api.Service;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.model.Country;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.model.CountryResponse;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    List<Country> countryList;
    CountryAdapter adapter;

    private LinearLayoutManager llm;
    private static final String layoutManagerPositionKey = "layoutManagerPositionKey";
    private static final String listKey = "listKey";
    private static final String titleKey = "listKey";

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        llm = new LinearLayoutManager(this);
        checkInternet();
    }

    private void checkInternet() {
        if (InternetConnection.isNetworkAvailable(this)) {
            Toast.makeText(this, getResources().getString(R.string.internet_connected_success_message), Toast.LENGTH_SHORT).show();
            initViews();
        }
    }
    private void initViews() {
        countryList = new ArrayList<>();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue),
                getResources().getColor(R.color.purple),getResources().getColor(R.color.orange),getResources().getColor(R.color.green));

        loadCounriesData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadCounriesData() {
        try {
            Client client = new Client();
            Service service = Client.getClient().create(Service.class);
            Call<CountryResponse> call = service.getCountryData();
            call.enqueue(new Callback<CountryResponse>() {
                @Override
                public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                    CountryResponse countryResponse = response.body();
                    if (countryResponse != null) {
                        String title = countryResponse.getTitle();
                        //Todo:kpk: Binding title to ActionBar
                        if (!title.isEmpty()) {
                            setTitle(title);
                        }
                        countryList = countryResponse.getCountryList();
                        System.out.println("kpk: length: " + countryList.size());
                        if (countryList.size() != 0) {
                            adapter = new CountryAdapter(MainActivity.this, countryList);
                            recyclerView.setAdapter(adapter);
                        }
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

    @Override
    public void onRefresh() {
        Toast.makeText(this, getResources().getString(R.string.refresh_sucess_msg), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (countryList != null) {
            position = llm.findFirstVisibleItemPosition();
            outState.putInt(layoutManagerPositionKey, position);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null){
            position = savedInstanceState.getInt(layoutManagerPositionKey);

        }
    }
}
