package com.gghouse.woi.whatsonininput.util;

import android.content.Context;

import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.common.SessionParam;
import com.gghouse.woi.whatsonininput.model.AreaCategory;
import com.gghouse.woi.whatsonininput.model.AreaName;
import com.gghouse.woi.whatsonininput.model.City;
import com.gghouse.woi.whatsonininput.webservices.ApiClient;
import com.github.pwittchen.prefser.library.Prefser;

import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_CATEGORIES;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_CATEGORY_ID;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_NAME;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_AREA_NAME_ID;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_CITIES;
import static com.gghouse.woi.whatsonininput.common.SessionParam.SP_CITY_ID;

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

    public static void saveCityId(Context context, long cityId) {
        Prefser prefser = new Prefser(context);

        prefser.put(SP_CITY_ID, cityId);
    }

    public static Long getCityId(Context context) {
        Prefser prefser = new Prefser(context);

        if (prefser.contains(SP_CITY_ID)) {
            return prefser.get(SP_CITY_ID, Long.class, SessionParam.INIT_VALUE_CITY_ID);
        } else {
            return SessionParam.INIT_VALUE_CITY_ID;
        }
    }

    public static void saveCities(Context context, City[] cities) {
        Prefser prefser = new Prefser(context);

        prefser.put(SP_CITIES, cities);
    }

    public static City[] getCities(Context context) {
        Prefser prefser = new Prefser(context);

        City[] cities = new City[]{};
        if (prefser.contains(SP_CITIES)) {
            cities = prefser.get(SP_CITIES, City[].class, new City[]{});
        } else {
            Logger.log("[getCities]", "Prefser does not contain " + SP_CITIES);
        }

        return cities;
    }

    public static void saveAreaCategoryId(Context context, long areaCategoryId) {
        Prefser prefser = new Prefser(context);

        prefser.put(SP_AREA_CATEGORY_ID, areaCategoryId);
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

    public static AreaCategory[] getAreaCategories(Context context) {
        Prefser prefser = new Prefser(context);

        AreaCategory[] areaCategories = new AreaCategory[]{};
        if (prefser.contains(SP_AREA_CATEGORIES)) {
            areaCategories = prefser.get(SP_AREA_CATEGORIES, AreaCategory[].class, new AreaCategory[]{});
        } else {
            Logger.log("[getAreaCategories]", "Prefser does not contain " + SP_AREA_CATEGORIES);
        }

        return areaCategories;
    }

    public static void saveAreaNameId(Context context, long areaCategoryId) {
        Prefser prefser = new Prefser(context);

        prefser.put(SP_AREA_NAME_ID, areaCategoryId);
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

    public static AreaName[] getAreaNames(Context context, long areaCategoryId) {
        Prefser prefser = new Prefser(context);

        String SP_AREA_NAME_CATEGORY_ID = SP_AREA_NAME + areaCategoryId;

        AreaName[] areaNames = new AreaName[]{};
        if (prefser.contains(SP_AREA_NAME_CATEGORY_ID)) {
            areaNames = prefser.get(SP_AREA_NAME_CATEGORY_ID, AreaName[].class, new AreaName[]{});
        } else {
            Logger.log("[getAreaNames]", "Prefser does not contain " + SP_AREA_NAME_CATEGORY_ID);
        }

        return areaNames;
    }
}
