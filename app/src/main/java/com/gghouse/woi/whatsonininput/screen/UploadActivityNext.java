package com.gghouse.woi.whatsonininput.screen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.WOIInputApplication;
import com.gghouse.woi.whatsonininput.adapter.UploadAdapter;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.job.UploadPhotosJob;
import com.gghouse.woi.whatsonininput.model.MyLocalPhotos;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.util.ImageHelper;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.util.Session;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.response.StoreUploadPhotosResponse;
import com.path.android.jobqueue.JobManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by michael on 3/29/2017.
 */

public class UploadActivityNext extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private UploadAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<StoreFileLocation> mDataSet;
    JobManager mJobManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mJobManager = WOIInputApplication.getInstance().getJobManager();

        MyLocalPhotos myLocalPhotos = Session.getLocalPhotos(this);
        for (StoreFileLocation storeFileLocation : myLocalPhotos.getPhotos()) {
            if (storeFileLocation == null) {
                Logger.log("Storefilelocation is null.");
            } else {
                Logger.log("Path: " + storeFileLocation.getLocation());
            }
        }
        mDataSet = myLocalPhotos.getPhotos();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new UploadAdapter(this, mDataSet, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        switch (Config.runMode) {
            case DUMMY:
                for (int i = 0; i < 3; i++) {
                    StoreFileLocation storeFileLocation = new StoreFileLocation();
                    storeFileLocation.setFileName("Dummy " + i);
                    storeFileLocation.setLocation("Location dummy " + i);

                    mDataSet.add(storeFileLocation);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_send:
                for (StoreFileLocation storeFileLocation: mDataSet) {
                    mJobManager.addJobInBackground(new UploadPhotosJob(storeFileLocation));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
