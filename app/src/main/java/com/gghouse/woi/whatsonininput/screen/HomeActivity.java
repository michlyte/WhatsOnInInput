package com.gghouse.woi.whatsonininput.screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.adapter.HomeAdapter;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.common.IntentParam;
import com.gghouse.woi.whatsonininput.listener.HomeOnClickListener;
import com.gghouse.woi.whatsonininput.model.AreaCategory;
import com.gghouse.woi.whatsonininput.model.AreaName;
import com.gghouse.woi.whatsonininput.model.City;
import com.gghouse.woi.whatsonininput.model.Dummy;
import com.gghouse.woi.whatsonininput.model.Store;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.util.Session;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.response.StoreListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements HomeOnClickListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private HomeOnClickListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ws_getStores();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_CH_swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mListener = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new HomeAdapter(this, Dummy.itemsStore, mListener, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_add:
                boolean cancel = true;
                City city = Session.getCity(this);
                AreaCategory areaCategory = Session.getAreaCategory(this);
                AreaName areaName = Session.getAreaName(this);
                if (city == null || areaCategory == null || areaName == null) {
                    cancel = true;
                }

                if (cancel) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.prompt_perhatian)
                            .content(R.string.prompt_pengaturan_prekondisi)
                            .positiveColorRes(R.color.colorPrimary)
                            .positiveText(R.string.prompt_setuju)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                }
                            })
                            .show();
                } else {
                    Intent addActivity = new Intent(this, AddActivity.class);
                    addActivity.putExtra(IntentParam.CITY, city);
                    addActivity.putExtra(IntentParam.AREA_CATEGORY, areaCategory);
                    addActivity.putExtra(IntentParam.AREA_NAME, areaName);
                    startActivity(addActivity);
                }
                return true;
            case R.id.action_settings:
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Store store) {
        Logger.log(store.getName());
    }

    private void ws_getStores() {
        Call<StoreListResponse> callGetStores = ApiClient.getClient().getStores();
        callGetStores.enqueue(new Callback<StoreListResponse>() {
            @Override
            public void onResponse(Call<StoreListResponse> call, Response<StoreListResponse> response) {
                StoreListResponse responseGetStores = response.body();
                if (responseGetStores.getCode() == Config.CODE_200) {

                } else {
                    Logger.log("Failed code: " + responseGetStores.getCode());
                }
            }

            @Override
            public void onFailure(Call<StoreListResponse> call, Throwable t) {
                Logger.log(Config.ON_FAILURE + " : " + t.getMessage());
            }
        });
    }
}
