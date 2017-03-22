package com.gghouse.woi.whatsonininput.webservices.response;

import com.gghouse.woi.whatsonininput.model.Pagination;
import com.gghouse.woi.whatsonininput.model.Store;

import java.util.List;

/**
 * Created by michael on 3/16/2017.
 */

public class StoreListResponse extends GenericResponse {
    private List<Store> data;
    private Pagination pagination;

    public List<Store> getData() {
        return data;
    }

    public void setData(List<Store> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
