package com.gghouse.woi.whatsonininput;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    private EditText mETDaerah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mETDaerah = (EditText) findViewById(R.id.et_AM_daerah);
    }
}
