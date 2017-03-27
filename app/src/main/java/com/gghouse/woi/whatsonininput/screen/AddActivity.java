package com.gghouse.woi.whatsonininput.screen;

import android.support.v7.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

//    private int typeADD = 99;
//    private LinearLayout mLLTemplate;
//
//    /*
//     * City, AreaCategory, AreaName
//     */
//    private City mCity;
//    private AreaCategory mAreaCategory;
//    private AreaName mAreaName;
//
//    private Button mBCity;
//    private Button mBAreaCategory;
//    private Button mBAreaName;
//
//    private ImageView mIVAddImage1;
//    private ImageView mIVAddImage2;
//    private ImageView mIVAddImage3;
//    private EditText mETName;
//    private EditText mETDistrict;
//    private EditText mETDescription;
//    private EditText mETAddress;
//    private EditText mETPhoneNumber;
//    private EditText mETWeb;
//    private EditText mETEmail;
//    private EditText mETFloor;
//    private EditText mETBlockNumber;
//    private EditText mETTags;
//
//    private List<File> photos;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add);
//
//        Nammu.init(this);
//
//        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            Nammu.askForPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
//                @Override
//                public void permissionGranted() {
//                    //Nothing, this sample saves to Public gallery so it needs permission
//                }
//
//                @Override
//                public void permissionRefused() {
//                    finish();
//                }
//            });
//        }
//
//        EasyImage.configuration(this)
//                .setImagesFolderName(getResources().getString(R.string.app_name)) //images folder name, default is "EasyImage"
//                .setCopyExistingPicturesToPublicLocation(true);
////                .saveInAppExternalFilesDir();
////                .saveInRootPicturesDirectory(); //if you want to use internal memory for storying images - default
//
//        Intent intent = getIntent();
//        if (intent != null) {
//            mCity = (City) intent.getSerializableExtra(IntentParam.CITY);
//            mAreaCategory = (AreaCategory) intent.getSerializableExtra(IntentParam.AREA_CATEGORY);
//            mAreaName = (AreaName) intent.getSerializableExtra(IntentParam.AREA_NAME);
//        }
//
//        mLLTemplate = (LinearLayout) findViewById(R.id.ll_AA_template);
//        mIVAddImage1 = (ImageView) findViewById(R.id.iv_AA_addImage1);
//        mIVAddImage2 = (ImageView) findViewById(R.id.iv_AA_addImage2);
//        mIVAddImage3 = (ImageView) findViewById(R.id.iv_AA_addImage3);
//
//        mBCity = (Button) findViewById(R.id.b_AA_city);
//        mBAreaCategory = (Button) findViewById(R.id.b_AA_areaCategory);
//        mBAreaName = (Button) findViewById(R.id.b_AA_areaName);
//
//        mETDistrict = (EditText) findViewById(R.id.et_AA_district);
//        mETName = (EditText) findViewById(R.id.et_AA_name);
//        mETDescription = (EditText) findViewById(R.id.et_AA_description);
//        mETAddress = (EditText) findViewById(R.id.et_AA_address);
//        mETPhoneNumber = (EditText) findViewById(R.id.et_AA_phoneNumber);
//        mETWeb = (EditText) findViewById(R.id.et_AA_web);
//        mETEmail = (EditText) findViewById(R.id.et_AM_email);
//        mETFloor = (EditText) findViewById(R.id.et_AA_floor);
//        mETBlockNumber = (EditText) findViewById(R.id.et_AA_blockNumber);
//        mETTags = (EditText) findViewById(R.id.et_AA_tags);
//
//        mIVAddImage1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EasyImage.openCamera(AddActivity.this, 1);
//            }
//        });
//
//        mIVAddImage2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EasyImage.openCamera(AddActivity.this, 2);
//            }
//        });
//
//        mIVAddImage3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EasyImage.openCamera(AddActivity.this, typeADD);
//            }
//        });
//
//        photos = new ArrayList<File>();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_add, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (item.getItemId()) {
//            case R.id.action_add:
//                attemptAdd();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
//            @Override
//            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
//                Logger.log("onImagePickerError");
//            }
//
//            @Override
//            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
//                Logger.log("onImagePicked");
//
//                if (type == typeADD) {
//                    switch (photos.size()) {
//                        case 0:
//                            mIVAddImage1.setVisibility(View.VISIBLE);
//                            picassoLoadImage(imageFile, mIVAddImage1);
//                            break;
//                        case 1:
//                            mIVAddImage2.setVisibility(View.VISIBLE);
//                            picassoLoadImage(imageFile, mIVAddImage2);
//                            break;
//                        case 2:
//                            mIVAddImage3.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    EasyImage.openCamera(AddActivity.this, 3);
//                                }
//                            });
//                            picassoLoadImage(imageFile, mIVAddImage3);
//                            break;
//                    }
//                    photos.add(imageFile);
//                } else if (type == 1 || type == 2 || type == 3) {
//                    switch (type) {
//                        case 1:
//                            picassoLoadImage(imageFile, mIVAddImage1);
//                            break;
//                        case 2:
//                            picassoLoadImage(imageFile, mIVAddImage2);
//                            break;
//                        case 3:
//                            picassoLoadImage(imageFile, mIVAddImage3);
//                            break;
//                    }
//                    photos.set(type - 1, imageFile);
//                } else {
//                    Logger.log("Type is unsupported.");
//                }
//            }
//
//            @Override
//            public void onCanceled(EasyImage.ImageSource source, int type) {
//                Logger.log("onCanceled");
//                if (source == EasyImage.ImageSource.CAMERA) {
//                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(AddActivity.this);
//                    if (photoFile != null) photoFile.delete();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    protected void onDestroy() {
//        EasyImage.clearConfiguration(this);
//        super.onDestroy();
//    }
//
//    private void picassoLoadImage(File imageFile, ImageView imageView) {
//        Picasso.with(getBaseContext())
//                .load(imageFile)
//                .fit()
//                .centerInside()
//                .into(imageView);
//    }
//
//    private void attemptAdd() {
//        mETDistrict.setError(null);
//        mETName.setError(null);
//        mETDescription.setError(null);
//        mETAddress.setError(null);
//        mETPhoneNumber.setError(null);
//        mETWeb.setError(null);
//        mETEmail.setError(null);
//        mETFloor.setError(null);
//        mETBlockNumber.setError(null);
//
//        String district = mETDistrict.getText().toString();
//        String name = mETName.getText().toString();
//        String description = mETDescription.getText().toString();
//        String address = mETAddress.getText().toString();
//        String phoneNumber = mETPhoneNumber.getText().toString();
//        String web = mETWeb.getText().toString();
//        String email = mETEmail.getText().toString();
//        String floor = mETFloor.getText().toString();
//        String blockNumber = mETBlockNumber.getText().toString();
//        String tags = mETTags.getText().toString();
//
//        boolean cancel = false;
//        View focusView = null;
//
//        if (TextUtils.isEmpty(name)) {
//            mETName.setError(getString(R.string.error_field_required));
//            focusView = mETName;
//            cancel = true;
//        }
//
//        if (cancel) {
//            focusView.requestFocus();
//        } else {
//            ws_createStore(district, name, description, address, phoneNumber, web, email, floor, blockNumber, tags);
//        }
//    }
//
//    private void ws_createStore(String district, String name, String description, String address, String phoneNumber, String web, String email, String floor, String blockNumber, String tags) {
//        StoreCreateRequest storeCreateRequest = new StoreCreateRequest();
//        storeCreateRequest.setDistrict(district);
//        storeCreateRequest.setName(name);
//        storeCreateRequest.setDescription(description);
//        storeCreateRequest.setAddress(address);
//        storeCreateRequest.setPhoneNo(phoneNumber);
//        storeCreateRequest.setWeb(web);
//        storeCreateRequest.setEmail(email);
//        storeCreateRequest.setFloor(floor);
//        storeCreateRequest.setBlockNumber(blockNumber);
//        storeCreateRequest.setStringTags(tags);
//
//        storeCreateRequest.setCategory(mAreaCategory);
//        storeCreateRequest.setAreaId(mAreaName.getAreaId());
//
//        Call<StoreCreateResponse> callCreateStore = ApiClient.getClient().createStore(storeCreateRequest);
//        callCreateStore.enqueue(new Callback<StoreCreateResponse>() {
//            @Override
//            public void onResponse(Call<StoreCreateResponse> call, Response<StoreCreateResponse> response) {
//                StoreCreateResponse storeCreateResponse = response.body();
//                if (storeCreateResponse.getCode() == Config.CODE_200) {
//
//                } else {
//                    Logger.log("Failed code: " + storeCreateResponse.getCode());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<StoreCreateResponse> call, Throwable t) {
//                Logger.log(Config.ON_FAILURE + " : " + t.getMessage());
//            }
//        });
//    }
}
