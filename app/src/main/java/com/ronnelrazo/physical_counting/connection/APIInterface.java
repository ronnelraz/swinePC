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


    //physical count breeder
    @FormUrlEncoded
    @POST("swineFeedCount.php")
    Call<Object> Feed(
            @Field("orgCode") String orgCode,
            @Field("farmCode") String farmCode,
            @Field("period") String period
    );

    //physical count breeder
    @FormUrlEncoded
    @POST("swineMedCount.php")
    Call<Object> Med(
            @Field("orgCode") String orgCode,
            @Field("farmCode") String farmCode,
            @Field("period") String period
    );


    //physical count breeder
    @FormUrlEncoded
    @POST("trnHeader")
    Call<Object> Header(
            @Field("orgcode") String orgcode,
            @Field("doc_date") String doc_date,
            @Field("audit_date") String audit_date,
            @Field("bu_code") String bu_code,
            @Field("bu_name") String bu_name,
            @Field("bu_type_code") String bu_type_code,
            @Field("bu_type_name") String bu_type_name,
            @Field("farm_code") String farm_code,
            @Field("user_create") String user_create
    );

    //physical count breeder detials
    @FormUrlEncoded
    @POST("trnChecklist")
    Call<Object> Header_checklist(
            @Field("ORG_CODE") String ORG_CODE,
            @Field("DOCUMENT_DATE") String DOCUMENT_DATE,
            @Field("AUDIT_DATE") String AUDIT_DATE,
            @Field("FARM_CODE") String FARM_CODE,
            @Field("BUSINESS_GROUP_CODE") String BUSINESS_GROUP_CODE,
            @Field("BUSINESS_TYPE_CODE") String BUSINESS_TYPE_CODE,
            @Field("MAIN_TOPIC_LIST_CODE") String MAIN_TOPIC_LIST_CODE,
            @Field("MAIN_TOPIC_LIST_DESC") String MAIN_TOPIC_LIST_DESC,
            @Field("MAIN_TOPIC_LIST_SEQ") String MAIN_TOPIC_LIST_SEQ,
            @Field("SUB_TOPIC_LIST_CODE") String SUB_TOPIC_LIST_CODE,
            @Field("SUB_TOPIC_LIST_DESC") String SUB_TOPIC_LIST_DESC,
            @Field("SUB_TOPIC_LIST_SEQ") String SUB_TOPIC_LIST_SEQ,
            @Field("DETAIL_TOPIC_LIST_CODE") String DETAIL_TOPIC_LIST_CODE,
            @Field("DETAIL_TOPIC_LIST_DESC") String DETAIL_TOPIC_LIST_DESC,
            @Field("DETAIL_TOPIC_LIST_SEQ") String DETAIL_TOPIC_LIST_SEQ,
            @Field("CHECK_FLAG") String CHECK_FLAG,
            @Field("REMARK") String REMARK,
            @Field("USER_CREATE") String USER_CREATE,
            @Field("AUDIT_NO") String AUDIT_NO
    );

    //physical count breeder count
    @FormUrlEncoded
    @POST("trnBreederCountList")
    Call<Object> Header_BreederCountList(
            @Field("ORG_CODE") String ORG_CODE,
            @Field("AUDIT_NO") String AUDIT_NO,
            @Field("DOCUMENT_DATE") String DOCUMENT_DATE,
            @Field("PERIOD") String PERIOD,
            @Field("AUDIT_DATE") String AUDIT_DATE,
            @Field("BUSINESS_GROUP_CODE") String BUSINESS_GROUP_CODE,
            @Field("BUSINESS_TYPE_CODE") String BUSINESS_TYPE_CODE,
            @Field("LOCATION") String LOCATION,
            @Field("FARM_CODE") String FARM_CODE,
            @Field("FARM_ORG") String FARM_ORG,
            @Field("FARM_NAME") String FARM_NAME,
            @Field("SYS_FEMALE_STOCK") String SYS_FEMALE_STOCK,
            @Field("SYS_MALE_STOCK") String SYS_MALE_STOCK,
            @Field("SYS_TOTAL_STOCK") String SYS_TOTAL_STOCK,
            @Field("COUNTING_STOCK") String COUNTING_STOCK,
            @Field("REMARK") String REMARK,
            @Field("USER_CREATE") String USER_CREATE
    );




}
