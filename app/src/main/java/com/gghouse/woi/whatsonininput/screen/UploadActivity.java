package com.gghouse.woi.whatsonininput.screen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.adapter.UploadAdapter;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.model.MyLocalPhotos;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.util.ImageHelper;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.util.Session;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.response.StoreUploadPhotosResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                new UploadImageTask(this).execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class UploadImageTask extends AsyncTask<Void, Integer, Void> {

        private Context context;
        private MaterialDialog materialDialog;

        public UploadImageTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            materialDialog = new MaterialDialog.Builder(context)
                    .title(R.string.prompt_sending)
                    .content(R.string.prompt_please_wait)
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < mDataSet.size(); i++) {
                Bitmap bitmap = ImageHelper.loadImageFromStorage(mDataSet.get(i).getLocation());
                if (bitmap != null) {
                    mDataSet.get(i).setStrImgBase64(ImageHelper.convertBitmapToString(bitmap));
                } else {
                    Logger.log("Filename: " + mDataSet.get(i).getFileName() + ", path: " + mDataSet.get(i).getLocation() + " is not existed.");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Call<StoreUploadPhotosResponse> callUploadPhotos = ApiClient.getClient().uploadPhotos(mDataSet);
            callUploadPhotos.enqueue(new Callback<StoreUploadPhotosResponse>() {
                @Override
                public void onResponse(Call<StoreUploadPhotosResponse> call, Response<StoreUploadPhotosResponse> response) {
                    materialDialog.dismiss();

                    StoreUploadPhotosResponse storeUploadPhotosResponse = response.body();
                    switch (storeUploadPhotosResponse.getCode()) {
                        case Config.CODE_200:
//                            Session.clearLocalPhotos(getApplicationContext());
                            setResult(Activity.RESULT_OK);
                            finish();
                            break;
                        default:
                            Logger.log("Status" + "[" + storeUploadPhotosResponse.getCode() + "]: " + storeUploadPhotosResponse.getStatus());
                            break;
                    }
                }

                @Override
                public void onFailure(Call<StoreUploadPhotosResponse> call, Throwable t) {
                    materialDialog.dismiss();
                    Logger.log(Config.ON_FAILURE + ": " + t.getMessage());
                }
            });
        }
    }
}
