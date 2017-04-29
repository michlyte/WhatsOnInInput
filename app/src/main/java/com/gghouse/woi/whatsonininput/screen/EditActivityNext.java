package com.gghouse.woi.whatsonininput.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by michael on 3/29/2017.
 */

public class EditActivityNext extends AddEditActivityNext {
    private Store mStore;
    private HashMap<Integer, StoreFileLocation> mHmInitPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            Logger.log("Store is null.");
        } else {
            mHmInitPhoto = new HashMap<Integer, StoreFileLocation>();

            mStore = (Store) intent.getSerializableExtra(IntentParam.STORE);
            tempPhotoName = mStore.getStoreId() + "";
            /*
             * Local photos
             */
            List<StoreFileLocation> localPhotos = Session.getLocalPhotosByStoreId(this, mStore.getStoreId());
            for (int i = 0; i < localPhotos.size(); i++) {
                mHmPhoto.put((i + 1), localPhotos.get(i));
            }

            /*
             * Server photos
             */
            for (int i = 0; i < mStore.getPhotos().size(); i++) {
                boolean isExist = localPhotos.contains(mStore.getPhotos().get(i));

                if (!isExist) {
                    for (Map.Entry<Integer, StoreFileLocation> entry : mHmPhoto.entrySet()) {
                        StoreFileLocation storeFileLocation = entry.getValue();
                        if (storeFileLocation == null) {
                            mStore.getPhotos().get(i).setLocal(false);
                            entry.setValue(mStore.getPhotos().get(i));
                            break;
                        }
                    }
                }
            }

            /*
             * Save initial value
             */
            for (Map.Entry<Integer, StoreFileLocation> entry : mHmPhoto.entrySet()) {
                mHmInitPhoto.put(entry.getKey(), entry.getValue());
            }

            /*
             * Set photo view
             */
            for (Map.Entry<Integer, StoreFileLocation> entry : mHmPhoto.entrySet()) {
                ImageView imageView = getImageView(entry.getKey());
                StoreFileLocation storeFileLocation = entry.getValue();
                if (imageView != null && storeFileLocation != null && storeFileLocation.getLocation() != null) {
                    if (storeFileLocation.isLocal()) {
                        Picasso.with(this)
                                .load(new File(storeFileLocation.getLocation()))
                                .fit()
                                .centerCrop()
                                .into(imageView);
                    } else {
                        Picasso.with(this)
                                .load(storeFileLocation.getLocation())
                                .fit()
                                .centerCrop()
                                .into(imageView);
                    }
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

        final MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.prompt_sending)
                .content(R.string.prompt_please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();

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
                materialDialog.dismiss();

                StoreEditResponse storeListResponse = response.body();
                switch (storeListResponse.getCode()) {
                    case Config.CODE_200:
                        for (Map.Entry<Integer, StoreFileLocation> entry : mHmPhoto.entrySet()) {
                            StoreFileLocation storeFileLocation = entry.getValue();
                            StoreFileLocation storeFileLocationInit = mHmInitPhoto.get(entry.getKey());
                            if (storeFileLocationInit != null &&
                                    (storeFileLocation == null || (storeFileLocation != null && !storeFileLocation.getFileName().equals(storeFileLocationInit.getFileName())))) {
                                deleteImageByPath(storeFileLocationInit.getLocation());
                            }
                        }

                        List<StoreFileLocation> storeFileLocationList = new ArrayList<StoreFileLocation>(mHmPhoto.values());
                        Session.saveLocalPhotosByStoreId(getApplicationContext(), mStore.getStoreId(), storeFileLocationList);

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
                materialDialog.dismiss();
                Logger.log(Config.ON_FAILURE + ": " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        clearPhotos();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    protected void resetPhoto(ImageView iv, int idx) {
        StoreFileLocation storeFileLocation = mHmPhoto.get(idx);
        StoreFileLocation storeFileLocationInit = mHmInitPhoto.get(idx);

        if (storeFileLocation != null) {
            if (storeFileLocation.isLocal() &&
                    (storeFileLocationInit == null || !storeFileLocationInit.getFileName().equals(storeFileLocation.getFileName()))) {
                deleteImageByPath(storeFileLocation.getLocation());
            }

            Picasso.with(this)
                    .load(R.drawable.no_image)
                    .fit()
                    .centerCrop()
                    .into(iv);
            mHmPhoto.put(idx, null);
        }
    }

    @Override
    protected void clearPhotos() {
        for (Map.Entry<Integer, StoreFileLocation> entry : mHmPhoto.entrySet()) {
            StoreFileLocation storeFileLocation = entry.getValue();
            StoreFileLocation storeFileLocationInit = mHmInitPhoto.get(entry.getKey());
            if ((storeFileLocationInit == null && storeFileLocation != null) ||
                    (storeFileLocationInit != null && storeFileLocation != null && !storeFileLocationInit.getFileName().equals(storeFileLocation.getFileName()))) {
                deleteImageByPath(storeFileLocation.getLocation());
            }
        }
    }

    private void deleteImageByPath(String path) {
        File image = new File(path);
        if (image.exists()) {
            image.delete();
            Logger.log("[deleteImageByPath] Path: " + path + " is deleted.");
        }
    }
}
