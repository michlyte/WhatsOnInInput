package com.gghouse.woi.whatsonininput.screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.common.IntentParam;
import com.gghouse.woi.whatsonininput.model.Store;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.util.Logger;

/**
 * Created by michael on 3/29/2017.
 */

public class EditActivity extends AddEditActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            Logger.log("Store is null.");
        } else {
            Store store = (Store) intent.getSerializableExtra(IntentParam.STORE);
            for (StoreFileLocation storeFileLocation : store.getPhotos()) {
                mDataSet.add(storeFileLocation.getLocation());
            }
            mAdapter.notifyDataSetChanged();

            mBAreaCategory.setText(store.getCategory().getCategoryName());
            mBAreaCategory.setVisibility(View.VISIBLE);

            mETName.setText(store.getName());
            mETDistrict.setText(store.getDistrict());
            mETDescription.setText(store.getDescription());
            mETAddress.setText(store.getAddress());
            mETPhoneNumber.setText(store.getPhoneNo());
            mETWeb.setText(store.getWeb());
            mETEmail.setText(store.getEmail());
            mETFloor.setText(store.getFloor());
            mETBlockNumber.setText(store.getBlockNumber());
            mETTags.setText(store.getStringTags());
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
    protected void onSuccess(String district, String name, String description, String address, String phoneNumber, String web, String email, String floor, String blockNumber, String tags) {

    }
}
