package com.gghouse.woi.whatsonininput.screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.common.Type;
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
    private Spinner mSType;
    private ArrayAdapter<Type> mAdapterType;
    private List<Type> mListType;
    private Integer mTypeId;

    private Spinner mSCity;
    private ArrayAdapter<City> mAdapterCity;
    private List<City> mListCity;
    private Long mCityId;

    private Spinner mSAreaCategory;
    private ArrayAdapter<AreaCategory> mAdapterAreaCategory;
    private List<AreaCategory> mListAreaCategory;
    private Long mAreaCategoryId;

    private Spinner mSAreaName;
    private ArrayAdapter<AreaName> mAdapterAreaName;
    private List<AreaName> mListAreaName;
    private Long mAreaNameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_next);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
         * City
         */
        mListCity = new ArrayList<City>();
        mSCity = (Spinner) findViewById(R.id.s_city);
        mAdapterCity = new ArrayAdapter<City>(this, android.R.layout.simple_spinner_dropdown_item, mListCity);
        mSCity.setAdapter(mAdapterCity);
        mSCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mListCity.size() > 0 && position < mListCity.size()) {
                    City city = mListCity.get(position);
                    mCityId = city.getCityId();
                    Session.saveCityId(getApplicationContext(), mCityId);
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
        mSAreaCategory = (Spinner) findViewById(R.id.s_areaCategory);
        mAdapterAreaCategory = new ArrayAdapter<AreaCategory>(this, android.R.layout.simple_spinner_dropdown_item, mListAreaCategory);
        mSAreaCategory.setAdapter(mAdapterAreaCategory);
        mSAreaCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mListAreaCategory.size() > 0 && position < mListAreaCategory.size()) {
                    AreaCategory areaCategory = mListAreaCategory.get(position);
                    mAreaCategoryId = areaCategory.getCategoryId();
                    Session.saveAreaCategoryId(getApplicationContext(), mAreaCategoryId);

//                    AreaName[] areaNames = Session.getAreaNames(getApplicationContext(), mAreaCategoryId);
//                    if (areaNames != null) {
//                        mListAreaName.clear();
//                        mListAreaName.addAll(Arrays.asList(areaNames));
//                        mAdapterAreaName.notifyDataSetChanged();
//                        setSpinnerSelection(Settings.AREA_NAME);
//                    } else {
                        ws_getAreaNames(areaCategory.getCategoryId());
//                    }
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
        mSAreaName = (Spinner) findViewById(R.id.s_areaName);
        mAdapterAreaName = new ArrayAdapter<AreaName>(this, android.R.layout.simple_spinner_dropdown_item, mListAreaName);
        mSAreaName.setAdapter(mAdapterAreaName);
        mSAreaName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mListAreaName.size() > 0 && position < mListAreaName.size()) {
                    AreaName areaName = mListAreaName.get(position);
                    mAreaNameId = areaName.getAreaId();
                    Session.saveAreaNameId(getApplicationContext(), mAreaNameId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mListType = new ArrayList<Type>();
        mSType = (Spinner) findViewById(R.id.s_type);
        mAdapterType = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_dropdown_item, mListType);
        mSType.setAdapter(mAdapterType);
        mSType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Session.saveTypeId(getApplicationContext(), mListType.get(position).getId());
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

        mListType.addAll(Arrays.asList(Type.values()));
        mAdapterType.notifyDataSetChanged();
        setSpinnerSelection(Settings.TYPE);

        /*
         * Debug
         */
        Logger.log("CityId: " + Session.getCityId(this));
        Logger.log("AreaCategoryId: " + Session.getAreaCategoryId(this));
        Logger.log("AreaNameId: " + Session.getAreaNameId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSpinnerSelection(Settings settings) {
        switch (settings) {
            case CITY:
                Logger.log("Cities: " + mListCity.toString());
                Logger.log("GetCity: " + Session.getCity(this));
                mCityId = Session.getCityId(this);
                int initCityPosition = 0;
                for (int i = 0; i < mListCity.size(); i++) {
                    if (mListCity.get(i).getCityId() == mCityId) {
                        initCityPosition = i;
                        break;
                    }
                }
                mSCity.setSelection(initCityPosition);
                break;
            case AREA_CATEGORY:
                Logger.log("AreaCategories: " + mListAreaCategory.toString());
                Logger.log("GetAreaCategory: " + Session.getAreaCategory(this));
                mAreaCategoryId = Session.getAreaCategoryId(this);
                int initAreaCategoryPosition = 0;
                for (int i = 0; i < mListAreaCategory.size(); i++) {
                    if (mListAreaCategory.get(i).getCategoryId() == mAreaCategoryId) {
                        initAreaCategoryPosition = i;
                        break;
                    }
                }
                mSAreaCategory.setSelection(initAreaCategoryPosition);
                break;
            case AREA_NAME:
                Logger.log("AreaNames: " + mListAreaName.toString());
                Logger.log("GetAreaName: " + Session.getAreaName(this));
                mAreaNameId = Session.getAreaNameId();
                int initAreaNamePosition = 0;
                for (int i = 0; i < mListAreaName.size(); i++) {
                    if (mListAreaName.get(i).getAreaId() == mAreaNameId) {
                        initAreaNamePosition = i;
                        break;
                    }
                }
                mSAreaName.setSelection(initAreaNamePosition);
                break;
            case TYPE:
                Logger.log("Type: " + mListCity.toString());
                Logger.log("GetTypeId: " + Session.getTypeId());
                mTypeId = Session.getTypeId();
                int initTypePosition = 0;
                for (int i = 0; i < mListType.size(); i++) {
                    if (mListType.get(i).getId() == mTypeId) {
                        initTypePosition = i;
                        break;
                    }
                }
                mSType.setSelection(initTypePosition);
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

                        setSpinnerSelection(Settings.AREA_CATEGORY);
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
        CITY,
        AREA_CATEGORY,
        AREA_NAME,
        TYPE;
    }
}