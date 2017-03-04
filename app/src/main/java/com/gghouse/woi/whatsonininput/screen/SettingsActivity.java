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

    private Spinner mSCSPOI;
    private Spinner mSCSPOIName;
    private EditText mETIPAddress;

    private ArrayAdapter<String> mPOIAdapter;
    private ArrayAdapter<String> mPOINameAdapter;

    private List<String> mPOIList;
    private List<String> mPOINameList;

    private Prefser mPrefser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPrefser = new Prefser(this);

        mSCSPOI = (Spinner) findViewById(R.id.s_CS_poi);
        mSCSPOIName = (Spinner) findViewById(R.id.s_CS_poiName);
        mETIPAddress = (EditText) findViewById(R.id.et_CS_ip);

        if (mPrefser.contains(Config.IP_ADDRESS)) {
            mETIPAddress.setText(mPrefser.get(Config.IP_ADDRESS, String.class, Config.BASE_URL));
        } else {
            mPrefser.put(Config.IP_ADDRESS, Config.BASE_URL);
            mETIPAddress.setText(mPrefser.get(Config.IP_ADDRESS, String.class, Config.BASE_URL));
        }

        mPOIList = new ArrayList<String>();
        mPOINameList = new ArrayList<String>();

        mPOIList.add("Automobile");
        mPOIList.add("Business Services");
        mPOIList.add("Computers");
        mPOIList.add("Education");
        mPOIList.add("Personal");
        mPOIList.add("Travel");

        mPOIAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mPOIList);
        mPOINameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mPOINameList);

        mSCSPOI.setAdapter(mPOIAdapter);
        mSCSPOI.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    setPOINameData();
                } else {
                    setPOINameClearData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSCSPOIName.setAdapter(mPOINameAdapter);
        mSCSPOIName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPrefser.put(Config.P_POI_NAME, mPOINameList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mPrefser.contains(Config.P_POI)) {
            String pPOI = mPrefser.get(Config.P_POI, String.class, "");
            boolean isFound = false;
            int idx = 0;
            for (String poi : mPOIList) {
                if (pPOI.equals(poi)) {
                    isFound = true;
                    break;
                }
                idx++;
            }

            if (isFound) {
                Logger.log("POI [" + pPOI + "] index [" + idx + "]");
                mSCSPOI.setSelection(idx);
            } else {
                Logger.log("POI [" + pPOI + "] tidak dapat ditemukan.");
            }
        } else {
            mPrefser.put(Config.P_POI, "");
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
                                    mPrefser.put(Config.P_POI, mPOIList.get(mSCSPOI.getSelectedItemPosition()));
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

    private void setPOINameData() {
        mPOINameList.clear();
        mPOINameList.add("1");
        mPOINameList.add("2");
        mPOINameAdapter.notifyDataSetChanged();
    }

    private void setPOINameClearData() {
        mPOINameList.clear();
        mPOINameAdapter.notifyDataSetChanged();
    }

    private boolean changesDetected() {
        boolean isUpdated = false;
        if (mPrefser.contains(Config.IP_ADDRESS)) {
            if (!mETIPAddress.getText().toString().equals(mPrefser.get(Config.IP_ADDRESS, String.class, Config.BASE_URL))) {
                return true;
            }
        }
        if (mPrefser.contains(Config.P_POI)) {
            if (!mPOIList.get(mSCSPOI.getSelectedItemPosition()).equals(mPrefser.get(Config.P_POI, String.class, ""))) {
                return true;
            }
        }
        return isUpdated;
    }
}