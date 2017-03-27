package com.gghouse.woi.whatsonininput.screen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.common.IntentParam;
import com.gghouse.woi.whatsonininput.model.AreaCategory;
import com.gghouse.woi.whatsonininput.model.AreaName;
import com.gghouse.woi.whatsonininput.model.City;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.request.StoreCreateRequest;
import com.gghouse.woi.whatsonininput.webservices.response.StoreCreateResponse;
import com.mindorks.paracamera.Camera;
import com.squareup.picasso.Picasso;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddParaCameraActivity extends AppCompatActivity implements View.OnClickListener {

    private int typeADD = 99;
    private LinearLayout mLLTemplate;

    /*
     * City, AreaCategory, AreaName
     */
    private City mCity;
    private AreaCategory mAreaCategory;
    private AreaName mAreaName;

    private Button mBCity;
    private Button mBAreaCategory;
    private Button mBAreaName;

    private ImageView mIVAddImage1;
    private ImageView mIVAddImage2;
    private ImageView mIVAddImage3;

    private EditText mETName;
    private EditText mETDistrict;
    private EditText mETDescription;
    private EditText mETAddress;
    private EditText mETPhoneNumber;
    private EditText mETWeb;
    private EditText mETEmail;
    private EditText mETFloor;
    private EditText mETBlockNumber;
    private EditText mETTags;

    private Camera mCamera;

    private Integer imageViewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

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

        // Build the camera
        mCamera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);

        mBCity = (Button) findViewById(R.id.b_AA_city);
        mBAreaCategory = (Button) findViewById(R.id.b_AA_areaCategory);
        mBAreaName = (Button) findViewById(R.id.b_AA_areaName);

        Intent intent = getIntent();
        if (intent != null) {
            mCity = (City) intent.getSerializableExtra(IntentParam.CITY);
            mAreaCategory = (AreaCategory) intent.getSerializableExtra(IntentParam.AREA_CATEGORY);
            mAreaName = (AreaName) intent.getSerializableExtra(IntentParam.AREA_NAME);

            if (mCity != null) {
                mBCity.setText(mCity.getCityName());
                mBCity.setVisibility(View.VISIBLE);
            } else {
                mBCity.setVisibility(View.GONE);
            }

            if (mAreaCategory != null) {
                mBAreaCategory.setText(mAreaCategory.getCategoryName());
                mBAreaCategory.setVisibility(View.VISIBLE);
            } else {
                mBAreaCategory.setVisibility(View.GONE);
            }

            if (mAreaName != null) {
                mBAreaName.setText(mAreaName.getName());
                mBAreaName.setVisibility(View.VISIBLE);
            } else {
                mBAreaName.setVisibility(View.GONE);
            }
        }

        mLLTemplate = (LinearLayout) findViewById(R.id.ll_AA_template);
        mIVAddImage1 = (ImageView) findViewById(R.id.iv_AA_addImage1);
        mIVAddImage2 = (ImageView) findViewById(R.id.iv_AA_addImage2);
        mIVAddImage3 = (ImageView) findViewById(R.id.iv_AA_addImage3);
        mIVAddImage1.setOnClickListener(this);
        mIVAddImage2.setOnClickListener(this);
        mIVAddImage3.setOnClickListener(this);

        mETDistrict = (EditText) findViewById(R.id.et_AA_district);
        mETName = (EditText) findViewById(R.id.et_AA_name);
        mETDescription = (EditText) findViewById(R.id.et_AA_description);
        mETAddress = (EditText) findViewById(R.id.et_AA_address);
        mETPhoneNumber = (EditText) findViewById(R.id.et_AA_phoneNumber);
        mETWeb = (EditText) findViewById(R.id.et_AA_web);
        mETEmail = (EditText) findViewById(R.id.et_AM_email);
        mETFloor = (EditText) findViewById(R.id.et_AA_floor);
        mETBlockNumber = (EditText) findViewById(R.id.et_AA_blockNumber);
        mETTags = (EditText) findViewById(R.id.et_AA_tags);

//        photos = new ArrayList<File>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_add:
                attemptAdd();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = mCamera.getCameraBitmap();

            ImageView imageView = null;
            switch (imageViewId) {
                case R.id.iv_AA_addImage1:
                    imageView = mIVAddImage1;
                    break;
                case R.id.iv_AA_addImage2:
                    imageView = mIVAddImage2;
                    break;
                case R.id.iv_AA_addImage3:
                    imageView = mIVAddImage3;
                    break;
            }

            if (bitmap != null) {
                Picasso.with(this)
                        .load(mCamera.getCameraBitmapPath())
                        .fit()
                        .centerCrop()
                        .into(imageView);
            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        mCamera.deleteImage();
        super.onDestroy();
    }

    private void attemptAdd() {
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
            ws_createStore(district, name, description, address, phoneNumber, web, email, floor, blockNumber, tags);
        }
    }

    private void ws_createStore(String district, String name, String description, String address, String phoneNumber, String web, String email, String floor, String blockNumber, String tags) {
        StoreCreateRequest storeCreateRequest = new StoreCreateRequest();
        storeCreateRequest.setDistrict(district);
        storeCreateRequest.setName(name);
        storeCreateRequest.setDescription(description);
        storeCreateRequest.setAddress(address);
        storeCreateRequest.setPhoneNo(phoneNumber);
        storeCreateRequest.setWeb(web);
        storeCreateRequest.setEmail(email);
        storeCreateRequest.setFloor(floor);
        storeCreateRequest.setBlockNumber(blockNumber);
        storeCreateRequest.setStringTags(tags);

        storeCreateRequest.setCategory(mAreaCategory);
        storeCreateRequest.setAreaId(mAreaName.getAreaId());

        Call<StoreCreateResponse> callCreateStore = ApiClient.getClient().createStore(storeCreateRequest);
        callCreateStore.enqueue(new Callback<StoreCreateResponse>() {
            @Override
            public void onResponse(Call<StoreCreateResponse> call, Response<StoreCreateResponse> response) {
                StoreCreateResponse storeCreateResponse = response.body();
                if (storeCreateResponse.getCode() == Config.CODE_200) {

                } else {
                    Logger.log("Failed code: " + storeCreateResponse.getCode());
                }
            }

            @Override
            public void onFailure(Call<StoreCreateResponse> call, Throwable t) {
                Logger.log(Config.ON_FAILURE + " : " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_AA_addImage1:
            case R.id.iv_AA_addImage2:
            case R.id.iv_AA_addImage3:
                imageViewId = v.getId();
                // Call the camera takePicture method to open the existing camera
                try {
                    mCamera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
