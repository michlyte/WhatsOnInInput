package com.gghouse.woi.whatsonininput.common;

/**
 * Created by michaelhalim on 2/11/17.
 */

public interface Config {
    boolean DUMMY_DEV = true;
    boolean LOG_ENABLE = true;

//    String BASE_URL = "http://jsonplaceholder.typicode.com";
    String BASE_URL = "http://10.0.2.2:8093";
    int CONNECT_TIMEOUT = 60;
    int READ_TIMEOUT = 60;

    // Retrofit
    int CODE_200 = 200;

    // Logger
    String ON_RESPONSE = "onResponse";
    String ON_FAILURE = "onFailure";
}
