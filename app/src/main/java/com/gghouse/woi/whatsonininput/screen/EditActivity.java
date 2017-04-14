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
import com.gghouse.woi.whatsonininput.model.Store;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.response.StoreEditResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by michael on 3/29/2017.
 */

public class EditActivity extends AddEditActivity {
    private Store mStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBAddphoto.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent == null) {
            Logger.log("Store is null.");
        } else {
            mStore = (Store) intent.getSerializableExtra(IntentParam.STORE);
            for (StoreFileLocation storeFileLocation : mStore.getPhotos()) {
                mDataSet.add(storeFileLocation.getLocation());
            }
            mAdapter.notifyDataSetChanged();

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
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<StoreEditResponse> call, Throwable t) {
                Logger.log(Config.ON_FAILURE + ": " + t.getMessage());
            }
        });
    }
}
