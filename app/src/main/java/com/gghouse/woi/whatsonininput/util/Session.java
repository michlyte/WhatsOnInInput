package com.gghouse.woi.whatsonininput.util;

import android.content.Context;
import android.support.annotation.Nullable;

import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.common.SessionParam;
import com.gghouse.woi.whatsonininput.model.AreaCategory;
import com.gghouse.woi.whatsonininput.model.AreaName;
import com.gghouse.woi.whatsonininput.model.City;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.github.pwittchen.prefser.library.Prefser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_CATEGORIES;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_CATEGORY_ID;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_NAME;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_NAME_ID;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_CITIES;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_CITY_ID;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_STORE_FILE_LOCATION;

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

    public static String getIpAddress(Context context) {
        Prefser prefser = new Prefser(context);

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
    public static AreaName getAreaName(Context context) {
        Prefser prefser = new Prefser(context);

        if (prefser.contains(SP_AREA_NAME_ID)) {
            Long areaCategoryId = prefser.get(SP_AREA_CATEGORY_ID, Long.class, -1L);
            if (areaCategoryId != -1L && prefser.contains(SP_AREA_NAME + areaCategoryId)) {
                Long areaNameId = prefser.get(SP_AREA_NAME_ID, Long.class, 1L);
                AreaName[] areaNames = prefser.get(SP_AREA_NAME + areaCategoryId, AreaName[].class, new AreaName[]{});

                for (AreaName areaName : areaNames) {
                    if (areaName.getAreaId() == areaNameId) {
                        return areaName;
                    }
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

    public static Long getAreaNameId(Context context) {
        Prefser prefser = new Prefser(context);

        if (prefser.contains(SP_AREA_NAME_ID)) {
            return prefser.get(SP_AREA_NAME_ID, Long.class, SessionParam.INIT_VALUE_AREA_NAME_ID);
        } else {
            return SessionParam.INIT_VALUE_AREA_NAME_ID;
        }
    }

    public static void saveAreaNames(Context context, long areaCategoryId, AreaName[] areaNames) {
        Prefser prefser = new Prefser(context);

        String SP_AREA_NAME_CATEGORY_ID = SP_AREA_NAME + areaCategoryId;

        prefser.put(SP_AREA_NAME_CATEGORY_ID, areaNames);
    }

    @Nullable
    public static AreaName[] getAreaNames(Context context, long areaCategoryId) {
        Prefser prefser = new Prefser(context);

        String SP_AREA_NAME_CATEGORY_ID = SP_AREA_NAME + areaCategoryId;

        if (prefser.contains(SP_AREA_NAME_CATEGORY_ID)) {
            return prefser.get(SP_AREA_NAME_CATEGORY_ID, AreaName[].class, new AreaName[]{});
        } else {
            Logger.log("[getAreaNames]", "Prefser does not contain " + SP_AREA_NAME_CATEGORY_ID);
            return null;
        }
    }

    public static void clearAreaNames(Context context) {
        Prefser prefser = new Prefser(context);

        if (prefser.contains(SP_AREA_CATEGORIES)) {
            AreaCategory[] areaCategories = prefser.get(SP_AREA_CATEGORIES, AreaCategory[].class, new AreaCategory[]{});

            for (AreaCategory areaCategory : areaCategories) {
                String SP_AREA_NAME_CATEGORY_ID = SP_AREA_NAME + areaCategory.getCategoryId();
                if (prefser.contains(SP_AREA_NAME_CATEGORY_ID)) {
                    prefser.remove(SP_AREA_NAME_CATEGORY_ID);
                    Logger.log("Key [" + SP_AREA_NAME_CATEGORY_ID + "] is removed.");
                }
            }
        }
    }

    public static void addPhotos(Context context, StoreFileLocation photoPath) {
        Prefser prefser = new Prefser(context);

        StoreFileLocation[] photoPathArr = getPhotos(context);
        if (photoPathArr.length > 0) {
            List<StoreFileLocation> photoPathList = new ArrayList<StoreFileLocation>();
            photoPathList.addAll(Arrays.asList(photoPathArr));
            photoPathList.add(photoPath);
            StoreFileLocation[] photoPathArrUpdated = new StoreFileLocation[photoPathList.size()];
            photoPathList.toArray(photoPathArrUpdated);
            prefser.put(SP_STORE_FILE_LOCATION, photoPathArrUpdated);
        } else {
            prefser.put(SP_STORE_FILE_LOCATION, new StoreFileLocation[]{photoPath});
        }
    }

    public static StoreFileLocation[] getPhotos(Context context) {
        Prefser prefser = new Prefser(context);

        StoreFileLocation[] photoPath = new StoreFileLocation[]{};
        if (prefser.contains(SP_STORE_FILE_LOCATION)) {
            photoPath = prefser.get(SP_STORE_FILE_LOCATION, StoreFileLocation[].class, new StoreFileLocation[]{});
        }
        return photoPath;
    }
}
