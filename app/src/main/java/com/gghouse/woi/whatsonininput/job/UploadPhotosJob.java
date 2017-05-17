package com.gghouse.woi.whatsonininput.job;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.WOIInputApplication;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.util.ImageHelper;
import com.gghouse.woi.whatsonininput.util.LocalNotificationHelper;
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
        String bitmapBase64 = ImageHelper.getStringFromPath(mStoreFileLocation.getLocation());
        if (bitmapBase64 != null) {
            mStoreFileLocation.setStrImgBase64(bitmapBase64);
            List<StoreFileLocation> storeFileLocationList = new ArrayList<StoreFileLocation>();
            storeFileLocationList.add(mStoreFileLocation);

            Call<StoreUploadPhotosResponse> callUploadPhotos = ApiClient.getClient().uploadPhotos(storeFileLocationList);
            StoreUploadPhotosResponse storeUploadPhotosResponse = callUploadPhotos.execute().body();
            switch (storeUploadPhotosResponse.getCode()) {
                case Config.CODE_200:
                    Session.removeLocalPhoto(WOIInputApplication.getInstance().getAppContext(), mStoreFileLocation);
                    if (WOIInputApplication.getInstance().getJobManager().count() == 0) {
                        Session.setUploading(false);
                        LocalNotificationHelper.postNotification(
                                WOIInputApplication.getInstance().getAppContext().getString(R.string.app_name),
                                "Upload photos has completed.");
                    }
                    break;
                default:
                    Logger.log("Status" + "[" + storeUploadPhotosResponse.getCode() + "]: " + storeUploadPhotosResponse.getStatus());
                    if (WOIInputApplication.getInstance().getJobManager().count() == 0) {
                        Session.setUploading(false);
                    }
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
