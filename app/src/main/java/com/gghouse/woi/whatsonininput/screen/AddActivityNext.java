package com.gghouse.woi.whatsonininput.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.common.IntentParam;
import com.gghouse.woi.whatsonininput.model.AreaCategory;
import com.gghouse.woi.whatsonininput.model.AreaName;
import com.gghouse.woi.whatsonininput.model.City;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.util.Session;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.request.StoreCreateRequest;
import com.gghouse.woi.whatsonininput.webservices.response.StoreCreateResponse;

import java.io.File;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivityNext extends AddEditActivityNext {
    private City mCity;
    private AreaCategory mAreaCategory;
    private AreaName mAreaName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            mCity = (City) intent.getSerializableExtra(IntentParam.CITY);
            mAreaCategory = (AreaCategory) intent.getSerializableExtra(IntentParam.AREA_CATEGORY);
            mAreaName = (AreaName) intent.getSerializableExtra(IntentParam.AREA_NAME);

            if (mCity != null) {
                mBCity.setText(mCity.getCityName());
                mBCity.setVisibility(View.VISIBLE);
            }

            if (mAreaCategory != null) {
                mBAreaCategory.setText(mAreaCategory.getCategoryName());
                mBAreaCategory.setVisibility(View.VISIBLE);
            }

            if (mAreaName != null) {
                mBAreaName.setText(mAreaName.getName());
                mBAreaName.setVisibility(View.VISIBLE);
            }
        }

//        mETName.setText("Stradivarius");
//        mETDistrict.setText("Sukajadi");
//        mETDescription.setText("Fashion");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_add:
                attemptAddOrEdit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSuccess(String district, String name, String description, String address, String phoneNumber, String web, String email, String floor, String blockNumber, String tags) {
        ws_createStore(district, name, description, address, phoneNumber, web, email, floor, blockNumber, tags);
    }

    private void ws_createStore(String district, String name, String description, String address, String phoneNumber, String web, String email, String floor, String blockNumber, String tags) {
        /*
         * Create reqeuest
         */
        final StoreCreateRequest storeCreateRequest = new StoreCreateRequest();
        storeCreateRequest.setDistrict(district);
        storeCreateRequest.setName(name);
        storeCreateRequest.setDescription(description);
        storeCreateRequest.setAddress(address);
        storeCreateRequest.setPhoneNo(phoneNumber);
        storeCreateRequest.setWeb(web);
        storeCreateRequest.setEmail(email);
        storeCreateRequest.setFloor(floor);
        storeCreateRequest.setBlockNumber(blockNumber);
        storeCreateRequest.setStringTags(tags);

        storeCreateRequest.setCategory(mAreaCategory);
        storeCreateRequest.setAreaId(mAreaName.getAreaId());

        Call<StoreCreateResponse> callCreateStore = ApiClient.getClient().createStore(storeCreateRequest);
        callCreateStore.enqueue(new Callback<StoreCreateResponse>() {
            @Override
            public void onResponse(Call<StoreCreateResponse> call, Response<StoreCreateResponse> response) {
                StoreCreateResponse storeCreateResponse = response.body();
                if (storeCreateResponse.getCode() == Config.CODE_200) {

                    for (Map.Entry<Integer, StoreFileLocation> entry : mHmPhoto.entrySet()) {
                        StoreFileLocation storeFileLocation = entry.getValue();
                        if (storeFileLocation != null) {
                            storeFileLocation.setStoreId(storeCreateResponse.getData().getStoreId());
                            // Replace fileName with StoreId
                            String replacedFileName = storeFileLocation.getFileName().replace(
                                    tempPhotoName, storeCreateResponse.getData().getStoreId() + "");
                            // Replace location with StoreId
                            File photoFile = new File(storeFileLocation.getLocation());
                            String replacedLocation = storeFileLocation.getLocation().replace(
                                    storeFileLocation.getFileName(), replacedFileName);
                            File newPhotoFile = new File(replacedLocation);
                            boolean success = photoFile.renameTo(newPhotoFile);
                            if (!success)
                                Logger.log("Rename " + storeFileLocation.getLocation() + " failed.");
                            storeFileLocation.setLocation(replacedLocation);
                            storeFileLocation.setFileName(replacedFileName);
                            entry.setValue(storeFileLocation);

//                            Session.addPhoto(getApplicationContext(), storeFileLocation);
                            Session.saveLocalPhoto(getApplicationContext(), storeFileLocation);
                        }
                    }

                    StoreFileLocation[] storeFileLocations = Session.getPhotos(getApplicationContext());
                    int count = 0;
                    for (StoreFileLocation storeFileLocation : storeFileLocations) {
                        System.out.println(++count + ". Filename: " + storeFileLocation.getFileName() + ", path: " + storeFileLocation.getLocation());
                    }

                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    Logger.log("Failed code: " + storeCreateResponse.getCode());
                }
            }

            @Override
            public void onFailure(Call<StoreCreateResponse> call, Throwable t) {
                Logger.log(Config.ON_FAILURE + " : " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        clearPhotos();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
