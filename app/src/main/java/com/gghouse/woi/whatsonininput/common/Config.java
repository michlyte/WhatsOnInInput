package com.gghouse.woi.whatsonininput.common;

/**
 * Created by michaelhalim on 2/11/17.
 */

public interface Config {
    boolean LOG_ENABLE = true;

    String BASE_URL = "http://jsonplaceholder.typicode.com/";
    int CONNECT_TIMEOUT = 60;
    int READ_TIMEOUT = 60;
}
