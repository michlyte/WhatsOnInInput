package com.gghouse.woi.whatsonininput.job;

import android.graphics.Bitmap;

import com.gghouse.woi.whatsonininput.WOIInputApplication;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.util.ImageHelper;
import com.gghouse.woi.whatsonininput.util.Logger;
import com.gghouse.woi.whatsonininput.util.Session;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.gghouse.woi.whatsonininput.webservices.response.StoreUploadPhotosResponse;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by michaelhalim on 5/2/17.
 */

public class UploadPhotosJob extends Job {
    public static final int PRIORITY = 1;
    private StoreFileLocation mStoreFileLocation;

    public UploadPhotosJob(StoreFileLocation storeFileLocation) {
        super(new Params(PRIORITY).requireNetwork().persist());
        mStoreFileLocation = storeFileLocation;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        StoreFileLocation updatedStoreFileLocation = mStoreFileLocation;

        Bitmap bitmap = ImageHelper.loadImageFromStorage(mStoreFileLocation.getLocation());
        String bitmapBase64 = "";
        if (bitmap != null) {
            bitmapBase64 = ImageHelper.convertBitmapToString(bitmap);
            updatedStoreFileLocation.setStrImgBase64(bitmapBase64);
        }

        if (!bitmapBase64.isEmpty()) {
            List<StoreFileLocation> storeFileLocationList = new ArrayList<StoreFileLocation>();
            storeFileLocationList.add(updatedStoreFileLocation);

            Call<StoreUploadPhotosResponse> callUploadPhotos = ApiClient.getClient().uploadPhotos(storeFileLocationList);
            StoreUploadPhotosResponse storeUploadPhotosResponse = callUploadPhotos.execute().body();
            switch (storeUploadPhotosResponse.getCode()) {
                case Config.CODE_200:
                    Session.removeLocalPhoto(WOIInputApplication.getAppContext(), mStoreFileLocation);
                    if (WOIInputApplication.getInstance().getJobManager().count() == 0) {
                        Logger.log("This is the last.");
                    }
                    break;
                default:
                    Logger.log("Status" + "[" + storeUploadPhotosResponse.getCode() + "]: " + storeUploadPhotosResponse.getStatus());
                    break;
            }
        }
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
