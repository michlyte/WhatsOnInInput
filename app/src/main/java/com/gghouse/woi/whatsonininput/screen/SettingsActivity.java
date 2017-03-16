package com.gghouse.woi.whatsonininput.screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.gghouse.woi.whatsonininput.util.Logger;
import com.github.pwittchen.prefser.library.Prefser;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Prefser mPrefser;

    private EditText mETIPAddress;
    private Spinner mSCSCity;
    private Spinner mSCSAreaCategory;
    private Spinner mSCSAreaName;

    private ArrayAdapter<String> mAdapterCity;
    private ArrayAdapter<String> mAdapterAreaCategory;
    private ArrayAdapter<String> mAdapterAreaName;

    private List<String> mListCity;
    private List<String> mListAreaCategory;
    private List<String> mListAreaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPrefser = new Prefser(this);

        mETIPAddress = (EditText) findViewById(R.id.et_CS_ip);
        mSCSCity = (Spinner) findViewById(R.id.s_CS_city);
        mSCSAreaCategory = (Spinner) findViewById(R.id.s_CS_areaCategory);
        mSCSAreaName = (Spinner) findViewById(R.id.s_CS_areaName);

        if (mPrefser.contains(Config.IP_ADDRESS)) {
            mETIPAddress.setText(mPrefser.get(Config.IP_ADDRESS, String.class, Config.BASE_URL));
        } else {
            mPrefser.put(Config.IP_ADDRESS, Config.BASE_URL);
            mETIPAddress.setText(mPrefser.get(Config.IP_ADDRESS, String.class, Config.BASE_URL));
        }

        mListCity = new ArrayList<String>();
        mListAreaCategory = new ArrayList<String>();
        mListAreaName = new ArrayList<String>();

        mListCity.add("Bandung");
        mListCity.add("Bogor");

        mAdapterCity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mListCity);
        mAdapterAreaCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mListAreaCategory);
        mAdapterAreaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mListAreaName);

        mSCSCity.setAdapter(mAdapterCity);
        mSCSCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    setAreaCategoryData();
                } else {
                    clearAreaCategoryData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSCSAreaCategory.setAdapter(mAdapterAreaCategory);
        mSCSAreaCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPrefser.put(Config.P_AREA_CATEGORY, mListAreaCategory.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mPrefser.contains(Config.P_CITY)) {
            String pCity = mPrefser.get(Config.P_CITY, String.class, "");
            boolean isFound = false;
            int idx = 0;
            for (String city : mListCity) {
                if (pCity.equals(city)) {
                    isFound = true;
                    break;
                }
                idx++;
            }

            if (isFound) {
                mSCSCity.setSelection(idx);
            } else {
                Logger.log("City [" + pCity + "] tidak dapat ditemukan.");
            }
        } else {
            mPrefser.put(Config.P_CITY, "");
        }
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
                                    mPrefser.put(Config.IP_ADDRESS, mETIPAddress.getText().toString());
                                    mPrefser.put(Config.P_CITY, mListCity.get(mSCSCity.getSelectedItemPosition()));
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

    private void onBindView() {

    }

    private void setAreaCategoryData() {
        mListAreaCategory.clear();
        mListAreaCategory.add("1");
        mListAreaCategory.add("2");
        mAdapterAreaCategory.notifyDataSetChanged();
    }

    private void clearAreaCategoryData() {
        mListAreaCategory.clear();
        mAdapterAreaCategory.notifyDataSetChanged();
    }

    private boolean changesDetected() {
        boolean isUpdated = false;
        if (mPrefser.contains(Config.IP_ADDRESS)) {
            if (!mETIPAddress.getText().toString().equals(mPrefser.get(Config.IP_ADDRESS, String.class, Config.BASE_URL))) {
                return true;
            }
        }
        if (mPrefser.contains(Config.P_CITY)) {
            if (!mListCity.get(mSCSCity.getSelectedItemPosition()).equals(mPrefser.get(Config.P_CITY, String.class, ""))) {
                return true;
            }
        }
        return isUpdated;
    }
}