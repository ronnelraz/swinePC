package com.ronnelrazo.physical_counting.connection;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {


    @FormUrlEncoded
    @POST("login")
    Call<Object> loginAPI(
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("farmOrg.php")
    Call<Object> farmAPI();


    @FormUrlEncoded
    @POST("farmList.php")
    Call<Object> farmListAPI(
            @Field("orgcode") String orgcode
    );


    //checklist
    @FormUrlEncoded
    @POST("swineChecklist.php")
    Call<Object> checkList(
            @Field("buType") String buType
    );

    //physical count breeder
    @FormUrlEncoded
    @POST("swineBreederCount.php")
    Call<Object> Breeder(
            @Field("orgCode") String orgCode,
            @Field("farmCode") String farmCode
    );


}
