package com.ronnelrazo.physical_counting.connection;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface APIInterfaceDownload {

    @GET("download.php")
    Call<Object> download();








}
