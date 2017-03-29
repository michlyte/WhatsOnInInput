package com.gghouse.woi.whatsonininput.screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.adapter.UploadAdapter;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.util.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
