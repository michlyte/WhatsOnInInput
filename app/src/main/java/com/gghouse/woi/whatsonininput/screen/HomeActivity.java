package com.gghouse.woi.whatsonininput.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.WOIInputApplication;
import com.gghouse.woi.whatsonininput.adapter.HomeAdapter;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.common.IntentParam;
import com.gghouse.woi.whatsonininput.listener.HomeOnClickListener;
import com.gghouse.woi.whatsonininput.listener.OnLoadMoreListener;
import com.gghouse.woi.whatsonininput.model.AreaCategory;
import com.gghouse.woi.whatsonininput.model.AreaName;
import com.gghouse.woi.whatsonininput.model.City;
import com.gghouse.woi.whatsonininput.model.Pagination;
import com.gghouse.woi.whatsonininput.model.Store;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.util.Session;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.response.StoreGetResponse;
import com.gghouse.woi.whatsonininput.webservices.response.StoreListResponse;
import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gghouse.woi.whatsonininput.util.Session.getIpAddress;

public class HomeActivity extends AppCompatActivity implements HomeOnClickListener {

    static final int ADD_RESPONSE = 99;
    static final int EDIT_RESPONSE = 98;
    static final int UPLOAD_RESPONSE = 97;
    static final String sort = "storeId,desc";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private HomeOnClickListener mListener;

    /*
     * Pagination
     */
    private Integer mPage;
    private OnLoadMoreListener mOnLoadMoreListener;
    /*
     * Loading
     */
    private DynamicBox mDynamicBox;

    private List<Store> mDataSet;

    /*
     * Job Manager
     */
    JobManager mJobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mJobManager = WOIInputApplication.getInstance().getJobManager();

        /*
         * Session Manager to update ip address.
         */
        ApiClient.generateClientWithNewIP(getIpAddress(this));

        mDataSet = new ArrayList<Store>();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_CH_swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ws_getStores(HomeMode.REFRESH);
            }
        });

        mPage = 0;
        mOnLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.add(null);
                        mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                    }
                });
                ws_getStores(HomeMode.LOAD_MORE);
            }
        };

        mListener = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new HomeAdapter(this, mDataSet, mListener, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        mDynamicBox = new DynamicBox(this, mRecyclerView);

        ws_getStores(HomeMode.REFRESH);
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
                return showAddActivity();
            case R.id.action_upload:
                return showUploadActivity();
            case R.id.action_change_ip:
                return showChangeIPDialog();
            case R.id.action_settings:
                Intent settingsActivity = new Intent(this, SettingsActivityNext.class);
                startActivity(settingsActivity);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Store store) {
        final MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.prompt_retrieving)
                .content(R.string.prompt_please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();

        Call<StoreGetResponse> callStoreGet = ApiClient.getClient().getStore(store.getStoreId());
        callStoreGet.enqueue(new Callback<StoreGetResponse>() {
            @Override
            public void onResponse(Call<StoreGetResponse> call, Response<StoreGetResponse> response) {
                materialDialog.dismiss();

                StoreGetResponse storeGetResponse = response.body();
                if (storeGetResponse.getCode() == Config.CODE_200) {
                    Intent iEditActivity = new Intent(getApplicationContext(), EditActivity.class);
                    iEditActivity.putExtra(IntentParam.STORE, storeGetResponse.getData());
                    startActivityForResult(iEditActivity, EDIT_RESPONSE);
                } else {
                    Logger.log("Failed code: " + storeGetResponse.getCode());
                }
            }

            @Override
            public void onFailure(Call<StoreGetResponse> call, Throwable t) {
                materialDialog.dismiss();
                Logger.log(Config.ON_FAILURE + " : " + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_RESPONSE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        ws_getStores(HomeMode.REFRESH);
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
            case EDIT_RESPONSE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Store store = (Store) data.getSerializableExtra(IntentParam.STORE);
                        int position = -1;
                        for (int i = 0; i < mDataSet.size(); i++) {
                            if (mDataSet.get(i).getStoreId().equals(store.getStoreId())) {
                                position = i;
                                break;
                            }
                        }

                        if (position != -1) {
                            mDataSet.set(position, store);
                            mAdapter.notifyItemChanged(position);
                        } else {
                            Logger.log("Error: Edited data is not found on your list.");
                        }
                        break;
                }
                break;
            case UPLOAD_RESPONSE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        ws_getStores(HomeMode.REFRESH);
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void ws_getStores(HomeMode homeMode) {
        switch (homeMode) {
            case REFRESH:
                mDynamicBox.showLoadingLayout();
                Call<StoreListResponse> callGetStores = ApiClient.getClient().getStores(0, Config.SIZE_PER_PAGE, sort);
                callGetStores.enqueue(new Callback<StoreListResponse>() {
                    @Override
                    public void onResponse(Call<StoreListResponse> call, Response<StoreListResponse> response) {
                        StoreListResponse storeListResponse = response.body();
                        if (storeListResponse.getCode() == Config.CODE_200) {
                            manageOnLoadMoreListener(storeListResponse.getPagination());
                            mRecyclerView.setAdapter(null);
                            mDataSet = storeListResponse.getData();
                            mAdapter.setData(mDataSet);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            Logger.log("Failed code: " + storeListResponse.getCode());
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        mDynamicBox.hideAll();
                    }

                    @Override
                    public void onFailure(Call<StoreListResponse> call, Throwable t) {
                        Logger.log(Config.ON_FAILURE + " : " + t.getMessage());
                        mSwipeRefreshLayout.setRefreshing(false);
                        mDynamicBox.hideAll();
                    }
                });
                break;
            case LOAD_MORE:
                Call<StoreListResponse> callStoreListLoadMore = ApiClient.getClient().getStores(++mPage, Config.SIZE_PER_PAGE, sort);
                callStoreListLoadMore.enqueue(new Callback<StoreListResponse>() {
                    @Override
                    public void onResponse(Call<StoreListResponse> call, Response<StoreListResponse> response) {
                        //Remove loading item
                        mAdapter.remove(mAdapter.getItemCount() - 1);
                        mAdapter.notifyItemRemoved(mAdapter.getItemCount());

                        StoreListResponse storeListResponse = response.body();
                        switch (storeListResponse.getCode()) {
                            case Config.CODE_200:
                                manageOnLoadMoreListener(storeListResponse.getPagination());
                                List<Store> newDataSet = storeListResponse.getData();
                                mDataSet.addAll(newDataSet);
                                break;
                            default:
                                Logger.log("Status" + "[" + storeListResponse.getCode() + "]: " + storeListResponse.getStatus());
                                break;
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<StoreListResponse> call, Throwable t) {
                        Logger.log(Config.ON_FAILURE + ": " + t.getMessage());

                        //Remove loading item
                        mAdapter.remove(mAdapter.getItemCount() - 1);
                        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
                        mAdapter.setLoaded();
                    }
                });
                break;
        }
    }

    private void manageOnLoadMoreListener(Pagination pagination) {
        /*
         * Controlling load more
         */
        mPage = pagination.getNumber();
        if (pagination.isLast()) {
            mAdapter.removeOnLoadMoreListener();
        } else {
            mAdapter.setLoaded();
            mAdapter.setOnLoadMoreListener(mOnLoadMoreListener);
        }
    }

    private enum HomeMode {
        REFRESH,
        LOAD_MORE;
    }

    private boolean showAddActivity() {
        boolean cancel = false;

        City city = Session.getCity(this);
        AreaCategory areaCategory = Session.getAreaCategory(this);
        AreaName areaName = Session.getAreaName(this);

        if (city == null || areaCategory == null || areaName == null) {
            cancel = true;
        }

        if (cancel) {
            new MaterialDialog.Builder(this)
                    .title(R.string.prompt_perhatian)
                    .content(R.string.prompt_pengaturan_prekondisi)
                    .positiveColorRes(R.color.colorPrimary)
                    .positiveText(R.string.prompt_setuju)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    })
                    .show();
        } else {
            Intent iAddActivity = new Intent(this, AddActivity.class);
            iAddActivity.putExtra(IntentParam.CITY, city);
            iAddActivity.putExtra(IntentParam.AREA_CATEGORY, areaCategory);
            iAddActivity.putExtra(IntentParam.AREA_NAME, areaName);
            startActivityForResult(iAddActivity, ADD_RESPONSE);
        }
        return true;
    }

    private boolean showUploadActivity() {
        if (Session.isUploading()) {
            new MaterialDialog.Builder(this)
                    .title(R.string.prompt_perhatian)
                    .content(R.string.prompt_upload_in_progress)
                    .positiveColorRes(R.color.colorPrimary)
                    .positiveText(R.string.prompt_ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        }
                    })
                    .show();
        } else {
            Intent uploadActivity = new Intent(this, UploadActivityNext.class);
            startActivityForResult(uploadActivity, UPLOAD_RESPONSE);
        }
        return true;
    }

    private boolean showChangeIPDialog() {
        String curIP = Session.getIpAddress(this);
        new MaterialDialog.Builder(this)
                .title(R.string.prompt_ip_address)
                .content(null)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.prompt_ip_address), curIP, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        Session.saveIpAddress(getApplicationContext(), input.toString());

                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                })
                .positiveColorRes(R.color.colorPrimary)
                .negativeColorRes(R.color.colorAccent)
                .negativeText(R.string.prompt_batal)
                .show();
        return true;
    }
}
