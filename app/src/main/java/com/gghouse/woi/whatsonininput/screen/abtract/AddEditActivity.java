package com.gghouse.woi.whatsonininput.screen.abtract;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.mindorks.paracamera.Camera;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Created by michael on 3/29/2017.
 */

public abstract class AddEditActivity extends AppCompatActivity {

    protected String tempPhotoName = "${temp}";

    /*
     * Photos
     */
    private Camera mCamera;
    private Integer idx;
    protected ImageView mIvPhoto1;
    protected ImageView mIvPhoto2;
    protected ImageView mIvPhoto3;
    protected Button mBPhotoDelete1;
    protected Button mBPhotoDelete2;
    protected Button mBPhotoDelete3;
    protected HashMap<Integer, StoreFileLocation> mHmPhoto;

    /*
     * Settings
     */
    protected Button mBCity;
    protected Button mBAreaCategory;
    protected Button mBAreaName;

    /*
     * TextFields
     */
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

    private String tempFilename;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

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

        /*
         * Photos
         */

        mHmPhoto = new HashMap<Integer, StoreFileLocation>();
        mHmPhoto.put(1, null);
        mHmPhoto.put(2, null);
        mHmPhoto.put(3, null);

        mIvPhoto1 = (ImageView) findViewById(R.id.iv_photo_1);
        mIvPhoto2 = (ImageView) findViewById(R.id.iv_photo_2);
        mIvPhoto3 = (ImageView) findViewById(R.id.iv_photo_3);
        mBPhotoDelete1 = (Button) findViewById(R.id.b_photoDelete_1);
        mBPhotoDelete2 = (Button) findViewById(R.id.b_photoDelete_2);
        mBPhotoDelete3 = (Button) findViewById(R.id.b_photoDelete_3);
        mIvPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(mIvPhoto1, 1);
            }
        });
        mIvPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(mIvPhoto2, 2);
            }
        });
        mIvPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(mIvPhoto3, 3);
            }
        });
        mBPhotoDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPhoto(mIvPhoto1, 1);
            }
        });
        mBPhotoDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPhoto(mIvPhoto2, 2);
            }
        });
        mBPhotoDelete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPhoto(mIvPhoto3, 3);
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
            if (mCamera.getCameraBitmap() != null) {
                postPhoto(this.idx, tempFilename+".JPG", mCamera.getCameraBitmapPath());
                mCamera = null;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void initCamera() {
        if (mCamera == null) {
            tempFilename = "IMG" + "_" + tempPhotoName + "_" + System.currentTimeMillis();
            mCamera = new Camera.Builder()
                    .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                    .setTakePhotoRequestCode(1)
                    .setDirectory("WOIInput")
                    .setName(tempFilename)
                    .setImageFormat(Camera.IMAGE_JPEG)
                    .setCompression(75)
                    .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                    .build(this);
        }
    }

    private void takePhoto(ImageView iv, int idx) {
        this.idx = idx;

        // Call the camera takePicture method to open the existing camera
        initCamera();
        try {
            mCamera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ImageView getImageView(int idx) {
        ImageView imageView = null;
        switch (idx) {
            case 1:
                imageView = mIvPhoto1;
                break;
            case 2:
                imageView = mIvPhoto2;
                break;
            case 3:
                imageView = mIvPhoto3;
                break;
        }
        return imageView;
    }

    protected abstract void resetPhoto(ImageView iv, int idx);

    protected abstract void clearPhotos();

    private void postPhoto(int idx, String filename, String cameraBitmapPath) {
        StoreFileLocation storeFileLocation = new StoreFileLocation(filename, cameraBitmapPath);

        ImageView imageView = getImageView(idx);
        if (imageView != null) {
            resetPhoto(imageView, idx);
            mHmPhoto.put(idx, storeFileLocation);
            Picasso.with(this)
                    .load(new File(storeFileLocation.getLocation()))
                    .fit()
                    .centerCrop()
                    .into(imageView);
            printPhotos();
        }
    }

    protected void printPhotos() {
        for (Map.Entry<Integer, StoreFileLocation> entry : mHmPhoto.entrySet()) {
            Logger.log("Key " + entry.getKey() + ": " + entry.getValue());
            StoreFileLocation storeFileLocation = entry.getValue();
            if (storeFileLocation != null) {
                Logger.log("Key " + entry.getKey() + ": " + storeFileLocation.getFileName());
            }
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