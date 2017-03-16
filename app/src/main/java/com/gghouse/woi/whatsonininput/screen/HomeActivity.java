package com.gghouse.woi.whatsonininput.screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.adapter.HomeAdapter;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.listener.HomeOnClickListener;
import com.gghouse.woi.whatsonininput.model.Dummy;
import com.gghouse.woi.whatsonininput.model.Store;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.response.ResponseGetStores;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements HomeOnClickListener {

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
                Intent addActivity = new Intent(this, AddActivity.class);
                startActivity(addActivity);
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
        Call<ResponseGetStores> callGetStores = ApiClient.getClient().getStores();
        callGetStores.enqueue(new Callback<ResponseGetStores>() {
            @Override
            public void onResponse(Call<ResponseGetStores> call, Response<ResponseGetStores> response) {
                Logger.log(Config.ON_RESPONSE);
                ResponseGetStores responseGetStores = response.body();
                if (responseGetStores.getCode() == Config.CODE_200) {

                } else {
                    Logger.log("Failed code: " + responseGetStores.getCode());
                }
            }

            @Override
            public void onFailure(Call<ResponseGetStores> call, Throwable t) {
                Logger.log(Config.ON_FAILURE + " : " + t.getMessage());
            }
        });
    }
}
