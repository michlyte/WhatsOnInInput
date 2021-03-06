package com.gghouse.woi.whatsonininput.util;

import android.content.Context;
import android.support.annotation.Nullable;

import com.gghouse.woi.whatsonininput.WOIInputApplication;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.common.SessionParam;
import com.gghouse.woi.whatsonininput.model.AreaCategory;
import com.gghouse.woi.whatsonininput.model.AreaName;
import com.gghouse.woi.whatsonininput.model.City;
import com.gghouse.woi.whatsonininput.model.MyLocalPhotos;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.github.pwittchen.prefser.library.Prefser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_CATEGORIES;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_CATEGORY_ID;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_NAME;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_NAME_ID;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_CITIES;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_CITY_ID;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_LOCAL_PHOTOS;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_UPLOADING;

/**
 * Created by michael on 3/22/2017.
 */

public abstract class Session {
    public static void saveIpAddress(Context context, String newIPAddress) {
        Prefser prefser = new Prefser(context);

        prefser.put(SessionParam.IP_ADDRESS, newIPAddress);
        /*
         * Update IP in APIService
         */
        ApiClient apiClient = new ApiClient();
        apiClient.generateClientWithNewIP(newIPAddress);
    }

    public static String getIpAddress() {
        Prefser prefser = WOIInputApplication.getInstance().getPrefser();

        if (prefser.contains(SessionParam.IP_ADDRESS)) {
            return prefser.get(SessionParam.IP_ADDRESS, String.class, Config.BASE_URL);
        } else {
            prefser.put(SessionParam.IP_ADDRESS, Config.BASE_URL);
            return prefser.get(SessionParam.IP_ADDRESS, String.class, Config.BASE_URL);
        }
    }

    @Nullable
    public static City getCity(Context context) {
        Prefser prefser = new Prefser(context);

        if (prefser.contains(SP_CITY_ID) && prefser.contains(SP_CITIES)) {
            Long cityId = prefser.get(SP_CITY_ID, Long.class, 1L);
            City[] cities = prefser.get(SP_CITIES, City[].class, new City[]{});

            for (City city : cities) {
                if (city.getCityId() == cityId) {
                    return city;
                }
            }
        }
        return null;
    }

    public static void saveCityId(Context context, Long cityId) {
        Prefser prefser = new Prefser(context);

        if (cityId != null) {
            prefser.put(SP_CITY_ID, cityId);
        } else {
            Logger.log("saveCityId is null.");
        }
    }

    @Nullable
    public static Long getCityId(Context context) {
        Prefser prefser = new Prefser(context);

        if (prefser.contains(SP_CITY_ID)) {
            return prefser.get(SP_CITY_ID, Long.class, SessionParam.INIT_VALUE_CITY_ID);
        } else {
            return null;
        }
    }

    public static void saveCities(Context context, City[] cities) {
        Prefser prefser = new Prefser(context);

        prefser.put(SP_CITIES, cities);
    }

    @Nullable
    public static City[] getCities(Context context) {
        Prefser prefser = new Prefser(context);

        if (prefser.contains(SP_CITIES)) {
            return prefser.get(SP_CITIES, City[].class, new City[]{});
        } else {
            Logger.log("[getCities]", "Prefser does not contain " + SP_CITIES);
            return null;
        }
    }

    public static void clearCities() {
        Prefser prefser = WOIInputApplication.getInstance().getPrefser();

        prefser.remove(SP_CITIES);
    }

    @Nullable
    public static AreaCategory getAreaCategory(Context context) {
        Prefser prefser = new Prefser(context);

        if (prefser.contains(SP_AREA_CATEGORY_ID) && prefser.contains(SP_AREA_CATEGORIES)) {
            Long areaCategoryId = prefser.get(SP_AREA_CATEGORY_ID, Long.class, 1L);
            AreaCategory[] areaCategories = prefser.get(SP_AREA_CATEGORIES, AreaCategory[].class, new AreaCategory[]{});

            for (AreaCategory areaCategory : areaCategories) {
                if (areaCategory.getCategoryId() == areaCategoryId) {
                    return areaCategory;
                }
            }
        }
        return null;
    }

    public static void saveAreaCategoryId(Context context, Long areaCategoryId) {
        Prefser prefser = new Prefser(context);

        if (areaCategoryId != null) {
            prefser.put(SP_AREA_CATEGORY_ID, areaCategoryId);
        } else {
            Logger.log("saveAreaCategoryId is null.");
        }
    }

    public static Long getAreaCategoryId(Context context) {
        Prefser prefser = new Prefser(context);

        if (prefser.contains(SP_AREA_CATEGORY_ID)) {
            return prefser.get(SP_AREA_CATEGORY_ID, Long.class, SessionParam.INIT_VALUE_AREA_CATEGORY_ID);
        } else {
            return SessionParam.INIT_VALUE_AREA_CATEGORY_ID;
        }
    }

    public static void saveAreaCategories(Context context, AreaCategory[] areaCategories) {
        Prefser prefser = new Prefser(context);

        prefser.put(SP_AREA_CATEGORIES, areaCategories);
    }

    public static void clearAreaCategories() {
        Prefser prefser = WOIInputApplication.getInstance().getPrefser();

        prefser.remove(SP_AREA_CATEGORIES);
    }

    @Nullable
    public static AreaCategory[] getAreaCategories(Context context) {
        Prefser prefser = new Prefser(context);

        if (prefser.contains(SP_AREA_CATEGORIES)) {
            return prefser.get(SP_AREA_CATEGORIES, AreaCategory[].class, new AreaCategory[]{});
        } else {
            Logger.log("[getAreaCategories]", "Prefser does not contain " + SP_AREA_CATEGORIES);
            return null;
        }
    }

    @Nullable
    public static AreaName getAreaName() {
        Prefser prefser = WOIInputApplication.getInstance().getPrefser();

        if (prefser.contains(SP_AREA_NAME_ID)) {
            Long areaNameId = prefser.get(SP_AREA_NAME_ID, Long.class, 1L);
            AreaName[] areaNames = prefser.get(SP_AREA_NAME, AreaName[].class, new AreaName[]{});

            for (AreaName areaName : areaNames) {
                if (areaName.getAreaId() == areaNameId) {
                    return areaName;
                }
            }
        }
        return null;
    }

    public static void saveAreaNameId(Context context, Long areaNameId) {
        Prefser prefser = new Prefser(context);

        if (areaNameId != null) {
            prefser.put(SP_AREA_NAME_ID, areaNameId);
        } else {
            Logger.log("saveAreaNameId is null.");
        }
    }

    public static Long getAreaNameId() {
        Prefser prefser = WOIInputApplication.getInstance().getPrefser();

        if (prefser.contains(SP_AREA_NAME_ID)) {
            return prefser.get(SP_AREA_NAME_ID, Long.class, SessionParam.INIT_VALUE_AREA_NAME_ID);
        } else {
            return SessionParam.INIT_VALUE_AREA_NAME_ID;
        }
    }

    public static void saveAreaNames(AreaName[] areaNames) {
        Prefser prefser = WOIInputApplication.getInstance().getPrefser();
        prefser.put(SP_AREA_NAME, areaNames);
    }

    @Nullable
    public static AreaName[] getAreaNames() {
        Prefser prefser = WOIInputApplication.getInstance().getPrefser();

        if (prefser.contains(SP_AREA_NAME)) {
            return prefser.get(SP_AREA_NAME, AreaName[].class, new AreaName[]{});
        } else {
            Logger.log("[getAreaNames]", "Prefser does not contain " + SP_AREA_NAME);
            return null;
        }
    }

    public static void clearAreaNames() {
        Prefser prefser = WOIInputApplication.getInstance().getPrefser();
        prefser.remove(SP_AREA_NAME);
    }

    /*
     * Uploading
     */

    public static void setUploading(boolean value) {
        Prefser prefser = WOIInputApplication.getInstance().getPrefser();
        prefser.put(SP_UPLOADING, value);
    }

    public static boolean isUploading() {
        Prefser prefser = WOIInputApplication.getInstance().getPrefser();

        if (prefser.contains(SP_UPLOADING)) {
            return prefser.get(SP_UPLOADING, Boolean.class, SessionParam.INIT_VALUE_UPLOADING);
        } else {
            return SessionParam.INIT_VALUE_UPLOADING;
        }
    }

    /*
     * Local Photos
     */
    public static MyLocalPhotos getLocalPhotos(Context context) {
        Prefser prefser = new Prefser(context);

        MyLocalPhotos myLocalPhotos = new MyLocalPhotos(new ArrayList<StoreFileLocation>());
        if (prefser.contains(SP_LOCAL_PHOTOS)) {
            myLocalPhotos = prefser.get(SP_LOCAL_PHOTOS, MyLocalPhotos.class, new MyLocalPhotos(new ArrayList<StoreFileLocation>()));
        }
        return myLocalPhotos;
    }

    public static List<StoreFileLocation> getLocalPhotosByStoreId(Context context, long storeId) {
        List<StoreFileLocation> storeFileLocationList = new ArrayList<StoreFileLocation>();
        MyLocalPhotos myLocalPhotos = getLocalPhotos(context);
        for (StoreFileLocation storeFileLocation : myLocalPhotos.getPhotos()) {
            if (storeFileLocation.getFileName().startsWith("IMG_" + storeId + "_")) {
                storeFileLocationList.add(storeFileLocation);
            }
        }
        return storeFileLocationList;
    }

    public static void saveLocalPhoto(Context context, StoreFileLocation storeFileLocation) {
        Prefser prefser = new Prefser(context);

        MyLocalPhotos myLocalPhotos = getLocalPhotos(context);
        if (!myLocalPhotos.getPhotos().contains(storeFileLocation)) {
            myLocalPhotos.getPhotos().add(0, storeFileLocation);
            prefser.put(SP_LOCAL_PHOTOS, myLocalPhotos);
            Logger.log("[Session] Filename: " + storeFileLocation.getFileName() + " with path: " + storeFileLocation.getLocation() + " is saved.");
        }
    }

    public static void saveLocalPhotosByStoreId(Context context, long storeId, List<StoreFileLocation> storeFileLocationList) {
        Prefser prefser = new Prefser(context);

        MyLocalPhotos myLocalPhotos = getLocalPhotos(context);
        List<StoreFileLocation> storeFileLocationsToBeDeleted = new ArrayList<StoreFileLocation>();
        for (StoreFileLocation storeFileLocation : myLocalPhotos.getPhotos()) {
            if (storeFileLocation.getFileName().startsWith("IMG_" + storeId + "_")) {
                storeFileLocationsToBeDeleted.add(storeFileLocation);
            }
        }

        myLocalPhotos.getPhotos().removeAll(storeFileLocationsToBeDeleted);

        for (StoreFileLocation storeFileLocation : storeFileLocationList) {
            if (storeFileLocation != null && storeFileLocation.isLocal()) {
                myLocalPhotos.getPhotos().add(storeFileLocation);
            }
        }

        prefser.put(SP_LOCAL_PHOTOS, myLocalPhotos);
    }

    public static void removeLocalPhoto(Context context, StoreFileLocation storeFileLocation) {
        Prefser prefser = new Prefser(context);

        MyLocalPhotos myLocalPhotos = getLocalPhotos(context);
        for (StoreFileLocation sfl : myLocalPhotos.getPhotos()) {
            if (sfl.getFileName().equals(storeFileLocation.getFileName())) {
                Logger.log("[removeLocalPhoto] Filename: " + storeFileLocation.getFileName() + " is removed.");
                myLocalPhotos.getPhotos().remove(sfl);
                break;
            }
        }
        prefser.put(SP_LOCAL_PHOTOS, myLocalPhotos);
    }

    public static void clearLocalPhotos(Context context) {
        Prefser prefser = new Prefser(context);

        prefser.remove(SP_LOCAL_PHOTOS);
    }

    public static void removeNullFromLocalPhotos(Context context) {
        Prefser prefser = new Prefser(context);
        MyLocalPhotos myLocalPhotos = getLocalPhotos(context);
        myLocalPhotos.getPhotos().removeAll(Collections.singleton(null));
        prefser.put(SP_LOCAL_PHOTOS, myLocalPhotos);
    }
}
