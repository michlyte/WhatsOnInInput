package com.gghouse.woi.whatsonininput.common;

/**
 * Created by michael on 3/22/2017.
 */

public interface SessionParam {
    String IP_ADDRESS = "IP_ADDRESS";

    String SP_CITIES = "SP_CITIES";
    String SP_CITY_ID = "SP_CITY_ID";
    long INIT_VALUE_CITY_ID = 1L;

    String SP_AREA_CATEGORIES = "SP_AREA_CATEGORIES";
    String SP_AREA_CATEGORY_ID = "SP_AREA_CATEGORY_ID";
    long INIT_VALUE_AREA_CATEGORY_ID = 1L;

    String SP_AREA_NAME = "SP_AREA_NAME_"; // follow by area category id (SP_AREA_NAME_1)
    String SP_AREA_NAME_ID = "SP_AREA_NAME_ID";
    long INIT_VALUE_AREA_NAME_ID = 1L;

//    String SP_PHOTOS = "SP_PHOTOS";
    String SP_STORE_FILE_LOCATION = "SP_STORE_FILE_LOCATION";
    String SP_LOCAL_PHOTOS = "SP_LOCAL_PHOTOS";
}
