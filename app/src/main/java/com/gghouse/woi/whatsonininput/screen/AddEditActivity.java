package com.gghouse.woi.whatsonininput.screen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.adapter.PhotoAdapter;
import com.mindorks.paracamera.Camera;

import java.util.ArrayList;
import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Created by michael on 3/29/2017.
 */

public abstract class AddEditActivity extends AppCompatActivity {

    private Camera mCamera;
    protected Button mBAddphoto;
    protected RecyclerView mRecyclerView;
    protected PhotoAdapter mAdapter;
    protected LinearLayoutManager mLayoutManager;
    protected List<String> mDataSet;

    protected Button mBCity;
    protected Button mBAreaCategory;
    protected Button mBAreaName;

    protected EditText mETName;
    protected EditText mETDistrict;
    protected EditText mETDescription;
    protected EditText mETAddress;
    protected EditText mETPhoneNumber;
    protected EditText mETWeb;
    protected EditText mETEmail;
    protected EditText mETFloor;
    protected EditText mETBlockNumber;
    protected EditText mETTags;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_para_camera);

        Nammu.init(this);

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    //Nothing, this sample saves to Public gallery so it needs permission
                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }

        mDataSet = new ArrayList<String>();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_AAPC_recyclerView);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PhotoAdapter(this, mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        mBAddphoto = (Button) findViewById(R.id.b_AAPC_addPhoto);
        mBAddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the camera takePicture method to open the existing camera
                initCamera(mETName.getText().toString());
                try {
                    mCamera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mBCity = (Button) findViewById(R.id.b_AA_city);
        mBCity.setVisibility(View.GONE);
        mBAreaCategory = (Button) findViewById(R.id.b_AA_areaCategory);
        mBAreaCategory.setVisibility(View.GONE);
        mBAreaName = (Button) findViewById(R.id.b_AA_areaName);
        mBAreaName.setVisibility(View.GONE);

        mETName = (EditText) findViewById(R.id.et_AA_name);
        mETDistrict = (EditText) findViewById(R.id.et_AA_district);
        mETDescription = (EditText) findViewById(R.id.et_AA_description);
        mETAddress = (EditText) findViewById(R.id.et_AA_address);
        mETPhoneNumber = (EditText) findViewById(R.id.et_AA_phoneNumber);
        mETWeb = (EditText) findViewById(R.id.et_AA_web);
        mETEmail = (EditText) findViewById(R.id.et_AM_email);
        mETFloor = (EditText) findViewById(R.id.et_AA_floor);
        mETBlockNumber = (EditText) findViewById(R.id.et_AA_blockNumber);
        mETTags = (EditText) findViewById(R.id.et_AA_tags);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
//            Bitmap bitmap = mCamera.getCameraBitmap();

            if (mCamera.getCameraBitmap() != null) {
                mDataSet.add(mCamera.getCameraBitmapPath());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void initCamera(String name) {
        if (mCamera == null) {
            mCamera = new Camera.Builder()
                    .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                    .setTakePhotoRequestCode(1)
                    .setDirectory("WOIInput")
                    .setName((name.isEmpty() ? "woi" : name) + "_" + System.currentTimeMillis())
                    .setImageFormat(Camera.IMAGE_JPEG)
                    .setCompression(75)
                    .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                    .build(this);
        }
    }

    protected void attemptAddOrEdit() {
        mETDistrict.setError(null);
        mETName.setError(null);
        mETDescription.setError(null);
        mETAddress.setError(null);
        mETPhoneNumber.setError(null);
        mETWeb.setError(null);
        mETEmail.setError(null);
        mETFloor.setError(null);
        mETBlockNumber.setError(null);

        String district = mETDistrict.getText().toString();
        String name = mETName.getText().toString();
        String description = mETDescription.getText().toString();
        String address = mETAddress.getText().toString();
        String phoneNumber = mETPhoneNumber.getText().toString();
        String web = mETWeb.getText().toString();
        String email = mETEmail.getText().toString();
        String floor = mETFloor.getText().toString();
        String blockNumber = mETBlockNumber.getText().toString();
        String tags = mETTags.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            mETName.setError(getString(R.string.error_field_required));
            focusView = mETName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            onSuccess(district, name, description, address, phoneNumber, web, email, floor, blockNumber, tags);
        }
    }

    protected abstract void onSuccess(String district, String name, String description, String address, String phoneNumber, String web, String email, String floor, String blockNumber, String tags);
}