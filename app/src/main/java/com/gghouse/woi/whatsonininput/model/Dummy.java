package com.gghouse.woi.whatsonininput.model;

import com.gghouse.woi.whatsonininput.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelhalim on 2/11/17.
 */

public abstract class Dummy {
    private static final int COUNT_STORE = 5;

    public static List<Store> itemsStore;

    static {
        itemsStore = new ArrayList<Store>();

        for (Long i = 0L; i < COUNT_STORE; i++) {
            itemsStore.add(new Store(i, "Store " + (i + 1), "Description " + (i + 1), R.mipmap.ic_launcher
            ));
        }
    }
}
