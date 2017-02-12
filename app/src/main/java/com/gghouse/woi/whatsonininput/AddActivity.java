package com.gghouse.woi.whatsonininput;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gghouse.woi.whatsonininput.util.Logger;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class AddActivity extends AppCompatActivity {

    private int typeADD = 99;
    private LinearLayout mLLTemplate;
    private ImageView mIVAddImage1;
    private ImageView mIVAddImage2;
    private ImageView mIVAddImage3;
    private EditText mETDaerah;
    private EditText mETKompleks;
    private EditText mETDeveloper;
    private EditText mETAlamat;
    private EditText mETTelepon;
    private EditText mETWeb;
    private EditText mETEmail;

    private List<File> photos;

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

        EasyImage.configuration(this)
                .setImagesFolderName(getResources().getString(R.string.app_name)) //images folder name, default is "EasyImage"
                .setCopyExistingPicturesToPublicLocation(true);
//                .saveInAppExternalFilesDir();
//                .saveInRootPicturesDirectory(); //if you want to use internal memory for storying images - default

        mLLTemplate = (LinearLayout) findViewById(R.id.ll_AA_template);
        mIVAddImage1 = (ImageView) findViewById(R.id.iv_AA_addImage1);
        mIVAddImage2 = (ImageView) findViewById(R.id.iv_AA_addImage2);
        mIVAddImage3 = (ImageView) findViewById(R.id.iv_AA_addImage3);

        mETDaerah = (EditText) findViewById(R.id.et_AM_daerah);
        mETKompleks = (EditText) findViewById(R.id.et_AM_kompleks);
        mETDeveloper = (EditText) findViewById(R.id.et_AM_developer);
        mETAlamat = (EditText) findViewById(R.id.et_AM_alamat);
        mETTelepon = (EditText) findViewById(R.id.et_AM_telepon);
        mETWeb = (EditText) findViewById(R.id.et_AM_web);
        mETEmail = (EditText) findViewById(R.id.et_AM_email);

        mIVAddImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openCamera(AddActivity.this, 1);
            }
        });

        mIVAddImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openCamera(AddActivity.this, 2);
            }
        });

        mIVAddImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openCamera(AddActivity.this, typeADD);
            }
        });

        photos = new ArrayList<File>();
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

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Logger.log("onImagePickerError");
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Logger.log("onImagePicked");

                if (type == typeADD) {
                    switch (photos.size()) {
                        case 0:
                            mIVAddImage1.setVisibility(View.VISIBLE);
                            picassoLoadImage(imageFile, mIVAddImage1);
                            break;
                        case 1:
                            mIVAddImage2.setVisibility(View.VISIBLE);
                            picassoLoadImage(imageFile, mIVAddImage2);
                            break;
                        case 2:
                            mIVAddImage3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EasyImage.openCamera(AddActivity.this, 3);
                                }
                            });
                            picassoLoadImage(imageFile, mIVAddImage3);
                            break;
                    }
                    photos.add(imageFile);
                } else if (type == 1 || type == 2 || type == 3) {
                    switch (type) {
                        case 1:
                            picassoLoadImage(imageFile, mIVAddImage1);
                            break;
                        case 2:
                            picassoLoadImage(imageFile, mIVAddImage2);
                            break;
                        case 3:
                            picassoLoadImage(imageFile, mIVAddImage3);
                            break;
                    }
                    photos.set(type - 1, imageFile);
                } else {
                    Logger.log("Type is unsupported.");
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                Logger.log("onCanceled");
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(AddActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }

    private void picassoLoadImage(File imageFile, ImageView imageView) {
        Picasso.with(getBaseContext())
                .load(imageFile)
                .fit()
                .centerInside()
                .into(imageView);
    }

    private void attemptAdd() {
        mETDaerah.setError(null);
        mETKompleks.setError(null);
        mETDeveloper.setError(null);
        mETAlamat.setError(null);
        mETTelepon.setError(null);
        mETWeb.setError(null);
        mETEmail.setError(null);

        String daerah = mETDaerah.getText().toString();
        String kompleks = mETKompleks.getText().toString();
        String developer = mETDeveloper.getText().toString();
        String alamat = mETAlamat.getText().toString();
        String telepon = mETTelepon.getText().toString();
        String web = mETWeb.getText().toString();
        String email = mETEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(daerah)) {
            mETDaerah.setError(getString(R.string.error_field_required));
            focusView = mETDaerah;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Logger.log("Data OK");
        }
    }
}
