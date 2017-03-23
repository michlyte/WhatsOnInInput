package com.gghouse.woi.whatsonininput.screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.model.AreaCategory;
import com.gghouse.woi.whatsonininput.model.AreaName;
import com.gghouse.woi.whatsonininput.model.City;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.util.Session;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.response.AreaCategoryListResponse;
import com.gghouse.woi.whatsonininput.webservices.response.AreaNameListResponse;
import com.gghouse.woi.whatsonininput.webservices.response.CityListResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private EditText mETIPAddress;

    private Spinner mSCSCity;
    private ArrayAdapter<City> mAdapterCity;
    private List<City> mListCity;
    private Long mCityId;

    private Spinner mSCSAreaCategory;
    private ArrayAdapter<AreaCategory> mAdapterAreaCategory;
    private List<AreaCategory> mListAreaCategory;
    private Long mAreaCategoryId;

    private Spinner mSCSAreaName;
    private ArrayAdapter<AreaName> mAdapterAreaName;
    private List<AreaName> mListAreaName;
    private Long mAreaNameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
         * SwipeRefreshLayout
         */
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_CS_swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Session.clearAreaNames(getApplicationContext());
                ws_getCities();
                ws_getAreaCategories();
                ws_getAreaNames(mAreaCategoryId);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        /*
         * IP Address
         */
        mETIPAddress = (EditText) findViewById(R.id.et_CS_ip);
        mETIPAddress.setText(Session.getIpAddress(this));

        /*
         * City
         */
        mListCity = new ArrayList<City>();
        mSCSCity = (Spinner) findViewById(R.id.s_CS_city);
        mAdapterCity = new ArrayAdapter<City>(this, android.R.layout.simple_spinner_dropdown_item, mListCity);
        mSCSCity.setAdapter(mAdapterCity);
        mSCSCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mListCity.size() > 0 && position < mListCity.size()) {
                    City city = mListCity.get(position);
                    mCityId = city.getCityId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
         * Area Category
         */
        mListAreaCategory = new ArrayList<AreaCategory>();
        mSCSAreaCategory = (Spinner) findViewById(R.id.s_CS_areaCategory);
        mAdapterAreaCategory = new ArrayAdapter<AreaCategory>(this, android.R.layout.simple_spinner_dropdown_item, mListAreaCategory);
        mSCSAreaCategory.setAdapter(mAdapterAreaCategory);
        mSCSAreaCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mListAreaCategory.size() > 0 && position < mListAreaCategory.size()) {
                    AreaCategory areaCategory = mListAreaCategory.get(position);
                    mAreaCategoryId = areaCategory.getCategoryId();

                    AreaName[] areaNames = Session.getAreaNames(getApplicationContext(), mAreaCategoryId);
                    if (areaNames != null) {
                        mListAreaName.clear();
                        mListAreaName.addAll(Arrays.asList(areaNames));
                        mAdapterAreaName.notifyDataSetChanged();
                        setSpinnerSelection(Settings.AREA_NAME);
                    } else {
                        ws_getAreaNames(areaCategory.getCategoryId());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
         * Area Name
         */
        mListAreaName = new ArrayList<AreaName>();
        mSCSAreaName = (Spinner) findViewById(R.id.s_CS_areaName);
        mAdapterAreaName = new ArrayAdapter<AreaName>(this, android.R.layout.simple_spinner_dropdown_item, mListAreaName);
        mSCSAreaName.setAdapter(mAdapterAreaName);
        mSCSAreaName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mListAreaName.size() > 0 && position < mListAreaName.size()) {
                    AreaName areaName = mListAreaName.get(position);
                    mAreaNameId = areaName.getAreaId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
         * Populate data
         */
        City[] cities = Session.getCities(this);
        if (cities == null) {
            ws_getCities();
        } else {
            mListCity.addAll(Arrays.asList(cities));
            mAdapterCity.notifyDataSetChanged();
            setSpinnerSelection(Settings.CITY);
        }

        AreaCategory[] areaCategories = Session.getAreaCategories(this);
        if (areaCategories == null) {
            ws_getAreaCategories();
        } else {
            mListAreaCategory.addAll(Arrays.asList(areaCategories));
            mAdapterAreaCategory.notifyDataSetChanged();
            setSpinnerSelection(Settings.AREA_CATEGORY);
        }

        /*
         * Debug
         */
        Logger.log("IPAddress: " + Session.getIpAddress(this));
        Logger.log("CityId: " + Session.getCityId(this));
        Logger.log("AreaCategoryId: " + Session.getAreaCategoryId(this));
        Logger.log("AreaNameId: " + Session.getAreaNameId(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (changesDetected()) {
                    new MaterialDialog.Builder(this)
                            .title(R.string.prompt_konfirmasi)
                            .content(R.string.prompt_pengaturan_konfirmasi)
                            .positiveColorRes(R.color.colorPrimary)
                            .negativeColorRes(R.color.colorAccent)
                            .positiveText(R.string.prompt_setuju)
                            .negativeText(R.string.prompt_tidak)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Session.saveIpAddress(getApplicationContext(), mETIPAddress.getText().toString());
                                    Session.saveCityId(getApplicationContext(), mCityId);
                                    Session.saveAreaCategoryId(getApplicationContext(), mAreaCategoryId);
                                    Session.saveAreaNameId(getApplicationContext(), mAreaNameId);
                                    finish();
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                }
                            })
                            .show();
                    return true;
                } else {
                    return false;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean changesDetected() {
        boolean isUpdated = false;
        if (isChanged(Settings.IP_ADDRESS, Session.getIpAddress(this), mETIPAddress.getText().toString())) {
            isUpdated = true;
        }
        if (isChanged(Settings.CITY, Session.getCityId(this), mCityId)) {
            isUpdated = true;
        }
        if (isChanged(Settings.AREA_CATEGORY, Session.getAreaCategoryId(this), mAreaCategoryId)) {
            isUpdated = true;
        }
        if (isChanged(Settings.AREA_NAME, Session.getAreaNameId(this), mAreaNameId)) {
            isUpdated = true;
        }
        return isUpdated;
    }

    private boolean isChanged(Settings settings, String beforeString, String afterString) {
        String type = "";
        if (!beforeString.equals(afterString)) {
            switch (settings) {
                case IP_ADDRESS:
                    type = "IP_ADDRESS";
                    break;
                case CITY:
                    type = "CITY";
                    break;
                case AREA_CATEGORY:
                    type = "AREA_CATEGORY";
                    break;
                case AREA_NAME:
                    type = "AREA_NAME";
                    break;
            }
            Logger.log(type + " Before: " + beforeString + ", After: " + afterString);
            return true;
        }
        return false;
    }

    private boolean isChanged(Settings settings, Long beforeId, Long afterId) {
        String type = "";
        if (beforeId == null || afterId == null) {
            return false;
        } else if (beforeId != afterId) {
            switch (settings) {
                case IP_ADDRESS:
                    type = "IP_ADDRESS";
                    break;
                case CITY:
                    type = "CITY";
                    break;
                case AREA_CATEGORY:
                    type = "AREA_CATEGORY";
                    break;
                case AREA_NAME:
                    type = "AREA_NAME";
                    break;
            }
            Logger.log(type + " Before: " + beforeId + ", After: " + afterId);
            return true;
        }
        return false;
    }

    private void setSpinnerSelection(Settings settings) {
        switch (settings) {
            case CITY:
                mCityId = Session.getCityId(this);
                int initCityPosition = 0;
                for (int i = 0; i < mListCity.size(); i++) {
                    if (mListCity.get(i).getCityId() == mCityId) {
                        initCityPosition = i;
                        break;
                    }
                }
                mSCSCity.setSelection(initCityPosition);
                break;
            case AREA_CATEGORY:
                mAreaCategoryId = Session.getAreaCategoryId(this);
                int initAreaCategoryPosition = 0;
                for (int i = 0; i < mListAreaCategory.size(); i++) {
                    if (mListAreaCategory.get(i).getCategoryId() == mAreaCategoryId) {
                        initAreaCategoryPosition = i;
                        break;
                    }
                }
                mSCSAreaCategory.setSelection(initAreaCategoryPosition);
                break;
            case AREA_NAME:
                mAreaNameId = Session.getAreaNameId(this);
                int initAreaNamePosition = 0;
                for (int i = 0; i < mListAreaName.size(); i++) {
                    if (mListAreaName.get(i).getAreaId() == mAreaNameId) {
                        initAreaNamePosition = i;
                        break;
                    }
                }
                mSCSAreaName.setSelection(initAreaNamePosition);
                break;
        }
    }

    private void ws_getCities() {
        Call<CityListResponse> callGetCities = ApiClient.getClient().getCities();
        callGetCities.enqueue(new Callback<CityListResponse>() {
            @Override
            public void onResponse(Call<CityListResponse> call, Response<CityListResponse> response) {
                CityListResponse cityListResponse = response.body();
                switch (cityListResponse.getCode()) {
                    case Config.CODE_200:
                        Session.saveCities(getApplicationContext(), cityListResponse.getData());

                        mListCity.clear();
                        mListCity.addAll(Arrays.asList(cityListResponse.getData()));
                        mAdapterCity.notifyDataSetChanged();

                        setSpinnerSelection(Settings.CITY);
                        break;
                    default:
                        Logger.log("Failed code: " + cityListResponse.getCode());
                        break;
                }
            }

            @Override
            public void onFailure(Call<CityListResponse> call, Throwable t) {
                Logger.log(Config.ON_FAILURE + " : " + t.getMessage());
            }
        });
    }

    private void ws_getAreaCategories() {
        Call<AreaCategoryListResponse> callGetAareCategories = ApiClient.getClient().getAreaCategories();
        callGetAareCategories.enqueue(new Callback<AreaCategoryListResponse>() {
            @Override
            public void onResponse(Call<AreaCategoryListResponse> call, Response<AreaCategoryListResponse> response) {
                AreaCategoryListResponse areaCategoryListResponse = response.body();
                switch (areaCategoryListResponse.getCode()) {
                    case Config.CODE_200:
                        Session.saveAreaCategories(getApplicationContext(), areaCategoryListResponse.getData());

                        mListAreaCategory.clear();
                        mListAreaCategory.addAll(Arrays.asList(areaCategoryListResponse.getData()));
                        mAdapterAreaCategory.notifyDataSetChanged();
                        break;
                    default:
                        Logger.log("Failed code: " + areaCategoryListResponse.getCode());
                        break;
                }
            }

            @Override
            public void onFailure(Call<AreaCategoryListResponse> call, Throwable t) {
                Logger.log(Config.ON_FAILURE + " : " + t.getMessage());
            }
        });
    }

    private void ws_getAreaNames(final long categoryId) {
        Call<AreaNameListResponse> callGetAreaNames = ApiClient.getClient().getAreaNames(categoryId);
        callGetAreaNames.enqueue(new Callback<AreaNameListResponse>() {
            @Override
            public void onResponse(Call<AreaNameListResponse> call, Response<AreaNameListResponse> response) {
                AreaNameListResponse areaNameListResponse = response.body();
                switch (areaNameListResponse.getCode()) {
                    case Config.CODE_200:
                        Session.saveAreaNames(getApplicationContext(), categoryId, areaNameListResponse.getData());

                        mListAreaName.clear();
                        mListAreaName.addAll(Arrays.asList(areaNameListResponse.getData()));
                        mAdapterAreaName.notifyDataSetChanged();
                        setSpinnerSelection(Settings.AREA_NAME);
                        break;
                    default:
                        Logger.log("Failed code: " + areaNameListResponse.getCode());
                        break;
                }
            }

            @Override
            public void onFailure(Call<AreaNameListResponse> call, Throwable t) {
                Logger.log(Config.ON_FAILURE + " : " + t.getMessage());
            }
        });
    }

    private enum Settings {
        IP_ADDRESS,
        CITY,
        AREA_CATEGORY,
        AREA_NAME;
    }
}