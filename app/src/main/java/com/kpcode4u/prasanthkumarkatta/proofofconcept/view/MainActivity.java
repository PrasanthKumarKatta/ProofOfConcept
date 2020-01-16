package com.kpcode4u.prasanthkumarkatta.proofofconcept.view;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kpcode4u.prasanthkumarkatta.proofofconcept.R;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.adapter.CountryAdapter;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.model.Country;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.utils.InternetConnection;
import com.kpcode4u.prasanthkumarkatta.proofofconcept.viewModels.CountryViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String layoutManagerPositionKey = "layoutManagerPositionKey";
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private List<Country> countryList;
    private CountryAdapter adapter;
    private LinearLayoutManager llm;
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
                getResources().getColor(R.color.purple), getResources().getColor(R.color.orange), getResources().getColor(R.color.green));

        loadCounriesDataFromVM();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.scrollToPosition(position);

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    //Todo:kpk: Loading Data froom ViewModel
    private void loadCounriesDataFromVM() {
        CountryViewModel viewModel = ViewModelProviders.of(this).get(CountryViewModel.class);

        viewModel.getCountryData().observe(this, countryResponse -> {
            if (countryResponse != null) {
                String title = countryResponse.getTitle();
                //Todo:kpk: Binding title to ActionBar
                if (!title.isEmpty()) {
                    setTitle(title);
                }
                countryList = countryResponse.getCountryList();
                System.out.println("kpk: length: " + countryList.size());
                //Todo:kpk: parsing List to Recyclerview Adapter
                if (countryList.size() != 0) {
                    adapter = new CountryAdapter(MainActivity.this, countryList);
                    recyclerView.setAdapter(adapter);
                }
            }

        });
    }


    @Override
    public void onRefresh() {
        initViews();
        Toast.makeText(this, getResources().getString(R.string.refresh_sucess_msg), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 2000);
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

        position = savedInstanceState.getInt(layoutManagerPositionKey);

    }
}
