package com.gghouse.woi.whatsonininput.webservices;

import com.gghouse.woi.whatsonininput.webservices.model.Dummy;
import com.gghouse.woi.whatsonininput.webservices.request.StoreCreateRequest;
import com.gghouse.woi.whatsonininput.webservices.response.AreaCategoryListResponse;
import com.gghouse.woi.whatsonininput.webservices.response.AreaNameListResponse;
import com.gghouse.woi.whatsonininput.webservices.response.CityListResponse;
import com.gghouse.woi.whatsonininput.webservices.response.StoreCreateResponse;
import com.gghouse.woi.whatsonininput.webservices.response.StoreGetResponse;
import com.gghouse.woi.whatsonininput.webservices.response.StoreListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by michael on 9/18/2016.
 * http://square.github.io/retrofit/
 */
public interface ApiService {
    // Michael Halim : Retrofit get only
    @GET("/posts")
    Call<List<Dummy>> dummyGetList();

    // Michael Halim : Retrofit get with parameter
    @GET("/posts/{id}")
    Call<Dummy> dummyGet(@Path("id") int id);

    // Michael Halim : Retrofit post
    @FormUrlEncoded
    @POST("/posts")
    Call<Dummy> dummyPost(@Field("title") String title, @Field("body") String body, @Field("userId") int userId);

    // Michael Halim : Retrofit post with body
    @POST("/posts")
    Call<Dummy> dummyPostBody(@Body Dummy dummy);

    /*
     * PROD
     */
    @GET("/stores")
    Call<StoreListResponse> getStores(@Query("page") int page, @Query("size") int size);

    @GET("/stores/{id}")
    Call<StoreGetResponse> getStore(@Path("id") long id);

    @POST("/stores")
    Call<StoreCreateResponse> createStore(@Body StoreCreateRequest storeCreateRequest);

    @GET("/cities")
    Call<CityListResponse> getCities();

    @GET("/categories?type=area")
    Call<AreaCategoryListResponse> getAreaCategories();

    @GET("/areas")
    Call<AreaNameListResponse> getAreaNames(@Query("category") long categoryId);
}
