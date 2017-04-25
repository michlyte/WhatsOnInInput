package com.gghouse.woi.whatsonininput.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.common.IntentParam;
import com.gghouse.woi.whatsonininput.model.Store;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.util.Session;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.response.StoreEditResponse;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by michael on 3/29/2017.
 */

public class EditActivityNext extends AddEditActivityNext {
    private Store mStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            Logger.log("Store is null.");
        } else {
            mStore = (Store) intent.getSerializableExtra(IntentParam.STORE);
            for (int i = 0; i < mStore.getPhotos().size(); i++) {
                mHmPhoto.put((i + 1), mStore.getPhotos().get(i));
            }

            StoreFileLocation[] storeFileLocations = Session.getPhotos(this);
            for (final StoreFileLocation storeFileLocation : storeFileLocations) {
                if (storeFileLocation.getFileName().startsWith("IMG_" + mStore.getStoreId() + "_")) {
                    boolean isExist = false;
                    for (Map.Entry<Integer, StoreFileLocation> entry : mHmPhoto.entrySet()) {
                        if (entry.getValue() != null && entry.getValue().getFileName().equals(storeFileLocation.getFileName())) {
                            isExist = true;
                            break;
                        }
                    }

                    if (!isExist) {
                        for (Map.Entry<Integer, StoreFileLocation> entry : mHmPhoto.entrySet()) {
                            if (entry.getValue() == null) {
                                entry.setValue(storeFileLocation);
                                break;
                            }
                        }
                    }
                }
            }

            printPhotos();

            for (Map.Entry<Integer, StoreFileLocation> entry : mHmPhoto.entrySet()) {
                ImageView imageView = getImageView(entry.getKey());
                StoreFileLocation storeFileLocation = entry.getValue();
                if (imageView != null && storeFileLocation != null && storeFileLocation.getLocation() != null) {
                    Picasso.with(this)
                            .load(new File(storeFileLocation.getLocation()))
                            .fit()
                            .centerCrop()
                            .into(imageView);
                }
            }

            mBAreaCategory.setText(mStore.getCategory().getCategoryName());
            mBAreaCategory.setVisibility(View.VISIBLE);

            mETName.setText(mStore.getName());
            mETDistrict.setText(mStore.getDistrict());
            mETDescription.setText(mStore.getDescription());
            mETAddress.setText(mStore.getAddress());
            mETPhoneNumber.setText(mStore.getPhoneNo());
            mETWeb.setText(mStore.getWeb());
            mETEmail.setText(mStore.getEmail());
            mETFloor.setText(mStore.getFloor());
            mETBlockNumber.setText(mStore.getBlockNumber());
            mETTags.setText(mStore.getStringTags());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_edit:
                attemptAddOrEdit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSuccess(String district, String name, String description,
                             String address, String phoneNumber, String web, String email,
                             String floor, String blockNumber, String tags) {

        mStore.setDistrict(district);
        mStore.setName(name);
        mStore.setDescription(description);
        mStore.setAddress(address);
        mStore.setPhoneNo(phoneNumber);
        mStore.setWeb(web);
        mStore.setEmail(email);
        mStore.setFloor(floor);
        mStore.setBlockNumber(blockNumber);
        mStore.setStringTags(tags);

        Call<StoreEditResponse> callStoreListLoadMore = ApiClient.getClient().editStore(mStore);
        callStoreListLoadMore.enqueue(new Callback<StoreEditResponse>() {
            @Override
            public void onResponse(Call<StoreEditResponse> call, Response<StoreEditResponse> response) {
                StoreEditResponse storeListResponse = response.body();
                switch (storeListResponse.getCode()) {
                    case Config.CODE_200:
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(IntentParam.STORE, mStore);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        break;
                    default:
                        Logger.log("Status" + "[" + storeListResponse.getCode() + "]: " + storeListResponse.getStatus());
                        break;
                }
            }

            @Override
            public void onFailure(Call<StoreEditResponse> call, Throwable t) {
                Logger.log(Config.ON_FAILURE + ": " + t.getMessage());
            }
        });
    }
}
