package com.gghouse.woi.whatsonininput.screen;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.adapter.UploadAdapter;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.util.Session;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.request.StoreFileLocationRequest;
import com.gghouse.woi.whatsonininput.webservices.response.StoreUploadPhotosResponse;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by michael on 3/29/2017.
 */

public class UploadActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private UploadAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<StoreFileLocation> mDataSet;
    private List<Target> targetList = new ArrayList<Target>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDataSet = new ArrayList<StoreFileLocation>();
        StoreFileLocation[] storeFileLocations = Session.getPhotos(this);
        mDataSet.addAll(Arrays.asList(storeFileLocations));

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
                targetList.clear();
                for (StoreFileLocation storeFileLocation : mDataSet) {
                    ws_uploadPhoto(storeFileLocation);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private synchronized void ws_uploadPhoto(final StoreFileLocation storeFileLocation) {
        if (storeFileLocation.getStoreId() != null) {
            Target target = new com.squareup.picasso.Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    StoreFileLocationRequest[] storeFileLocationRequests = new StoreFileLocationRequest[1];

                    StoreFileLocationRequest storeFileLocationRequest = new StoreFileLocationRequest(
                            storeFileLocation.getLocation(), storeFileLocation.getFileName(), "A",
                            storeFileLocation.getStoreId(), storeFileLocation.getCreatedBy(), storeFileLocation.getUpdatedBy(),
                            convert(bitmap));

                    storeFileLocationRequests[0] = storeFileLocationRequest;

                    Call<StoreUploadPhotosResponse> callUploadPhotos = ApiClient.getClient().uploadPhotos(storeFileLocationRequests);
                    callUploadPhotos.enqueue(new Callback<StoreUploadPhotosResponse>() {
                        @Override
                        public void onResponse(Call<StoreUploadPhotosResponse> call, Response<StoreUploadPhotosResponse> response) {
                            StoreUploadPhotosResponse storeUploadPhotosResponse = response.body();
                            switch (storeUploadPhotosResponse.getCode()) {
                                case Config.CODE_200:
                                    break;
                                default:
                                    Logger.log("Status" + "[" + storeUploadPhotosResponse.getCode() + "]: " + storeUploadPhotosResponse.getStatus());
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<StoreUploadPhotosResponse> call, Throwable t) {
                            Logger.log(Config.ON_FAILURE + ": " + t.getMessage());
                        }
                    });

                    targetList.remove(this);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    targetList.remove(this);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            targetList.add(target);
            Picasso.with(this)
                    .load(new File(storeFileLocation.getLocation()))
                    .into(target);
        } else {
            Logger.log("StoreId is null with fileName: " + storeFileLocation.getFileName());
        }
    }

    private String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
