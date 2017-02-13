package com.gghouse.woi.whatsonininput.screen;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.github.pwittchen.prefser.library.Prefser;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Spinner mSCSPOI;
    private Spinner mSCSPOIName;

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
                mPrefser.put(Config.P_POI, mPOIList.get(position));

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
}