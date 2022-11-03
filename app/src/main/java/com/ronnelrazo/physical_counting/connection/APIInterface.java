package com.ronnelrazo.physical_counting.connection;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface APIInterface {


    @FormUrlEncoded
    @POST("login")
    Call<Object> loginAPI(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("farmOrg.php")
    Call<Object> farmAPI(
            @Field("role") String role,
            @Field("org_code") String org_code,
            @Field("farm_code") String farm_code
    );


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
            @Field("farmCode") String farmCode,
            @Field("cutOffDate") String cutoff
    );


    //physical count breeder
    @FormUrlEncoded
    @POST("swineFeedCount.php")
    Call<Object> Feed(
            @Field("orgCode") String orgCode,
            @Field("farmCode") String farmCode,
            @Field("period") String period,
            @Field("cutOffDate") String cutoff
    );

    //physical count breeder
    @FormUrlEncoded
    @POST("swineMedCount.php")
    Call<Object> Med(
            @Field("orgCode") String orgCode,
            @Field("farmCode") String farmCode,
            @Field("period") String period,
            @Field("cutOffDate") String cutoff
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
            @Field("user_create") String user_create,
            @Field("org_name") String org_name,
             @Field("farm_name") String farm_name
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
            @Field("USER_CREATE") String USER_CREATE,
            @Field("VARIANCE") String variance,
            @Field("UNPOST") String unpost,
            @Field("ACTIVE_VAR") String active_var
    );


    //physical count breeder count
    @FormUrlEncoded
    @POST("trnFeedCountList")
    Call<Object> Header_FeedCountList(
            @Field("ORG_CODE") String ORG_CODE,
            @Field("AUDIT_NO") String AUDIT_NO,
            @Field("DOCUMENT_DATE") String DOCUMENT_DATE,
            @Field("PERIOD") String PERIOD,
            @Field("AUDIT_DATE") String AUDIT_DATE,
            @Field("BUSINESS_GROUP_CODE") String BUSINESS_GROUP_CODE,
            @Field("BUSINESS_TYPE_CODE") String BUSINESS_TYPE_CODE,
            @Field("FARM_CODE") String FARM_CODE,
            @Field("FARM_ORG") String FARM_ORG,
            @Field("FARM_NAME") String FARM_NAME,
            @Field("FEED_CODE") String FEED_CODE,
            @Field("FEED_NAME") String FEED_NAME,
            @Field("SYS_FEED_STOCK_QTY") String SYS_FEED_STOCK_QTY,
            @Field("SYS_FEED_STOCK_WGH") String SYS_FEED_STOCK_WGH,
            @Field("STOCK_UNIT") String STOCK_UNIT,
            @Field("COUNTING_STOCK") String COUNTING_STOCK,
            @Field("REMARK") String REMARK,
            @Field("USER_CREATE") String USER_CREATE,
            @Field("VARIANCE") String variance,
            @Field("UNPOST") String unpost,
            @Field("ACTIVE_VAR") String active_var
    );



    @FormUrlEncoded
    @POST("trnMedCountList")
    Call<Object> Header_MedCountList(
            @Field("ORG_CODE") String ORG_CODE,
            @Field("AUDIT_NO") String AUDIT_NO,
            @Field("DOCUMENT_DATE") String DOCUMENT_DATE,
            @Field("PERIOD") String PERIOD,
            @Field("AUDIT_DATE") String AUDIT_DATE,
            @Field("BUSINESS_GROUP_CODE") String BUSINESS_GROUP_CODE,
            @Field("BUSINESS_TYPE_CODE") String BUSINESS_TYPE_CODE,
            @Field("FARM_CODE") String FARM_CODE,
            @Field("FARM_ORG") String FARM_ORG,
            @Field("FARM_NAME") String FARM_NAME,
            @Field("MED_CODE") String MED_CODE,
            @Field("MED_NAME") String MED_NAME,
            @Field("SYS_MED_STOCK_QTY") String SYS_MED_STOCK_QTY,
            @Field("SYS_MED_STOCK_WGH") String SYS_MED_STOCK_WGH,
            @Field("STOCK_UNIT") String STOCK_UNIT,
            @Field("COUNTING_STOCK") String COUNTING_STOCK,
            @Field("REMARK") String REMARK,
            @Field("USER_CREATE") String USER_CREATE,
            @Field("VARIANCE") String VARIANCE,
            @Field("UNPOST") String UNPOST,
            @Field("ACTIVE_VAR") String ACTIVE_VAR
    );


    @FormUrlEncoded
    @POST("A1_1Form")
    Call<Object> A1_1Form(
            @Field("BU_CODE") String BU_CODE
    );


    @FormUrlEncoded
    @POST("PDFReportList")
    Call<Object> ReportPDF(
            @Field("username") String username,
            @Field("org_code") String org_code,
            @Field("audit_date") String audit_date
    );

    @FormUrlEncoded
    @POST("autoComplete_org_code")
    Call<Object> autoCompleteOrg_code(
            @Field("username") String username
    );


    @FormUrlEncoded
    @POST("autoComplete_org_code_cancel")
    Call<Object> autoCompleteOrg_code_cancel(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("autoComplete_org_code_generate")
    Call<Object> autoCompleteOrg_code_generate(
            @Field("username") String username
    );

    @GET
    Call<Object> PDFURLCHECKER(@Url String url);


    @POST("getFarm_org")
    Call<Object> getFarm_org();

    @POST("getAUDIT_BUSINESS_TYPE")
    Call<Object> getAUDIT_BUSINESS_TYPE();

    @FormUrlEncoded
    @POST("getFarm_name")
    Call<Object> getFarm_name(
            @Field("org_code") String org_code
    );

//    form 3
    @POST("getProvince")
    Call<Object> getProvince();

    @FormUrlEncoded
    @POST("getMunicipal")
    Call<Object> getMunicipal(
            @Field("province_code") String province_code
    );


    @FormUrlEncoded
    @POST("getBarangay")
    Call<Object> getBarangay(
            @Field("MUNICIPAL_CODE") String MUNICIPAL_CODE
    );


    @FormUrlEncoded
    @POST("Save_FarmSetup")
    Call<Object> Save_FarmSetup(
            @Field("ORG_CODE") String ORG_CODE,
            @Field("ORG_NAME") String ORG_NAME,
            @Field("BUSINESS_GROUP_CODE") String BUSINESS_GROUP_CODE,
            @Field("BUSINESS_TYPE_CODE") String BUSINESS_TYPE_CODE,
            @Field("CHECKLIST_AUDIT_FLAG") String CHECKLIST_AUDIT_FLAG,
            @Field("BREEDER_AUDIT_FLAG") String BREEDER_AUDIT_FLAG,
            @Field("FEED_AUDIT_FLAG") String FEED_AUDIT_FLAG,
            @Field("MED_AUDIT_FLAG") String MED_AUDIT_FLAG,
            @Field("ADDRESS") String ADDRESS,
            @Field("PROVINCE_CODE") String PROVINCE_CODE,
            @Field("MUNICIPALITY_CODE") String MUNICIPALITY_CODE,
            @Field("BARANGAY_CODE") String BARANGAY_CODE,
            @Field("ZIP_CODE") String ZIP_CODE,
            @Field("FARM_MANAGER_CODE") String FARM_MANAGER_CODE,
            @Field("FARM_MANAGER_NAME") String FARM_MANAGER_NAME,
            @Field("FARM_MANAGER_CONTACT_NO") String FARM_MANAGER_CONTACT_NO,
            @Field("FARM_MANAGER_EMAIL") String FARM_MANAGER_EMAIL,
            @Field("FARM_CLERK_CODE") String FARM_CLERK_CODE,
            @Field("FARM_CLERK_NAME") String FARM_CLERK_NAME,
            @Field("FARM_CLERK_CONTACT_NO") String FARM_CLERK_CONTACT_NO,
            @Field("FARM_CLERK_EMAIL") String FARM_CLERK_EMAIL,
            @Field("LONGITUDE") String LONGITUDE,
            @Field("LATITUDE") String LATITUDE,
            @Field("USER_CREATE") String USER_CREATE
    );


    @POST("get_FarmSetup")
    Call<Object> get_FarmSetup();


    @FormUrlEncoded
    @POST("modify_farm_details")
    Call<Object> modify_farm_details(
            @Field("ORG_CODE") String ORG_CODE,
            @Field("ORG_NAME") String ORG_NAME,
            @Field("BUSINESS_GROUP_CODE") String BUSINESS_GROUP_CODE,
            @Field("BUSINESS_TYPE_CODE") String BUSINESS_TYPE_CODE,
            @Field("CHECKLIST_AUDIT_FLAG") String CHECKLIST_AUDIT_FLAG,
            @Field("BREEDER_AUDIT_FLAG") String BREEDER_AUDIT_FLAG,
            @Field("FEED_AUDIT_FLAG") String FEED_AUDIT_FLAG,
            @Field("MED_AUDIT_FLAG") String MED_AUDIT_FLAG,
            @Field("ADDRESS") String ADDRESS,
            @Field("PROVINCE_CODE") String PROVINCE_CODE,
            @Field("MUNICIPALITY_CODE") String MUNICIPALITY_CODE,
            @Field("BARANGAY_CODE") String BARANGAY_CODE,
            @Field("ZIP_CODE") String ZIP_CODE,
            @Field("FARM_MANAGER_CODE") String FARM_MANAGER_CODE,
            @Field("FARM_MANAGER_NAME") String FARM_MANAGER_NAME,
            @Field("FARM_MANAGER_CONTACT_NO") String FARM_MANAGER_CONTACT_NO,
            @Field("FARM_MANAGER_EMAIL") String FARM_MANAGER_EMAIL,
            @Field("FARM_CLERK_CODE") String FARM_CLERK_CODE,
            @Field("FARM_CLERK_NAME") String FARM_CLERK_NAME,
            @Field("FARM_CLERK_CONTACT_NO") String FARM_CLERK_CONTACT_NO,
            @Field("FARM_CLERK_EMAIL") String FARM_CLERK_EMAIL,
            @Field("LONGITUDE") String LONGITUDE,
            @Field("LATITUDE") String LATITUDE,
            @Field("USER_CREATE") String USER_CREATE,
            @Field("ORG_CODE_ID") String ORG_CODE_ID
    );


    @FormUrlEncoded
    @POST("edit_pdf_list")
    Call<Object> edit_pdf_list(
            @Field("username") String username,
            @Field("org_code") String orgcode,
            @Field("date") String date
    );


    @FormUrlEncoded
    @POST("edit_header")
    Call<Object> edit_header(
            @Field("org_code") String org_code,
            @Field("farm_code") String farm_code
    );


    @FormUrlEncoded
    @POST("edit_checklist")
    Call<Object> edit_checklist(
            @Field("audit_no") String audit_no
    );


    @FormUrlEncoded
    @POST("edit_breeder")
    Call<Object> edit_breeder(
            @Field("audit_no") String audit_no
    );

    @FormUrlEncoded
    @POST("edit_feed")
    Call<Object> edit_feed(
            @Field("audit_no") String audit_no
    );


    @FormUrlEncoded
    @POST("edit_med")
    Call<Object> edit_med(
            @Field("audit_no") String audit_no
    );

    @FormUrlEncoded
    @POST("update_checklist")
    Call<Object> update_checklist(
            @Field("check") String check,
            @Field("remark") String remark,
            @Field("audit_no") String audit_no,
            @Field("main_code") String main_code,
            @Field("sub_code") String sub_code,
            @Field("details_code") String details_code
    );

    @FormUrlEncoded
    @POST("update_breeder")
    Call<Object> update_breeder_idana(
            @Field("counting") String counting,
            @Field("remark") String remark,
            @Field("org_code") String org_code,
            @Field("audit_no") String audit_no,
            @Field("farm_org") String farm_org,
            @Field("variance") String variance,
            @Field("unpost") String unpost,
            @Field("actual") String actual,
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("update_feed")
    Call<Object> update_feed(
            @Field("counting") String counting,
            @Field("remark") String remark,
            @Field("org_code") String org_code,
            @Field("audit_no") String audit_no,
            @Field("farm_org") String farm_org,
            @Field("feed_code") String feed_code,
            @Field("variance") String variance,
            @Field("unpost") String unpost,
            @Field("actual") String actual,
            @Field("user") String user

    );

    @FormUrlEncoded
    @POST("update_med")
    Call<Object> update_med(
            @Field("counting") String counting,
            @Field("remark") String remark,
            @Field("org_code") String org_code,
            @Field("audit_no") String audit_no,
            @Field("farm_org") String farm_org,
            @Field("med_code") String med_code,
            @Field("variance") String variance,
            @Field("unpost") String unpost,
            @Field("actual") String actual,
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("confirm_list")
    Call<Object> confirm_list(
            @Field("user") String user,
            @Field("org_code") String org_code,
            @Field("audit") String audit
    );


    @FormUrlEncoded
    @POST("cancel_list")
    Call<Object> cancel_list(
            @Field("user") String user,
            @Field("org_code") String org_code,
            @Field("audit") String audit
    );

    @FormUrlEncoded
    @POST("flag")
    Call<Object> flag(
            @Field("audit_no") String audit_no,
            @Field("flag") String flag,
            @Field("user") String user
    );


    @FormUrlEncoded
    @POST("UploadPDFFile")
    Call<Object> uploadPDFFiles(
            @Field("audit_no") String audit_no,
            @Field("pdfname") String pdfname,
            @Field("pdffile") String flag,
            @Field("user") String user,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("updatePDFFile")
    Call<Object> updatePDFFile(
            @Field("filename") String filename,
            @Field("fileData") String fileData
    );


    @FormUrlEncoded
    @POST("autoComplete_audit")
    Call<Object> autoComplete_audit(
            @Field("user") String user,
            @Field("filter") String filter
    );

    @FormUrlEncoded
    @POST("LoadUploadFile")
    Call<Object> LoadUploadFile(
            @Field("user") String user,
            @Field("audit") String audit,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("ViewFiles")
    Call<Object> ViewFiles(
            @Field("audit") String audit
    );


    @FormUrlEncoded
    @POST("getfarmname_header")
    Call<Object> getfarmname_header(
            @Field("org_code") String audit
    );


    @FormUrlEncoded
    @POST("transaction_org_code")
    Call<Object> transaction_org_code(
            @Field("user") String user
    );


    @FormUrlEncoded
    @POST("transaction_detials")
    Call<Object> transaction_detials(
            @Field("user") String user,
            @Field("org_code") String org_code,
            @Field("to") String to,
            @Field("from") String from,
            @Field(("types")) String types
    );


    @FormUrlEncoded
    @POST("view_attach_file_")
    Call<Object> view_attach_file_(
            @Field("audit") String audit
    );



    @FormUrlEncoded
    @POST("remove_attach_file")
    Call<Object> remove_attach_file(
            @Field("audit") String audit,
            @Field("filename") String filename
    );


    @POST("org_code_list")
    Call<Object> org_code_list();


    @POST("get_offline_authorize")
    Call<Object> get_offline_authorize();





    @POST("role_type")
    Call<Object> role_type();

    @FormUrlEncoded
    @POST("adduser_master")
    Call<Object> adduser_master(
            @Field("AD") String AD,
            @Field("ROLE") String ROLE,
            @Field("ORG_CODE") String ORG_CODE,
            @Field("USER") String USER
    );


    @FormUrlEncoded
    @POST("menu_map_setup")
    Call<Object> menu_map_setup(
            @Field("AD") String AD,
            @Field("USER") String USER,
            @Field("ROLEID") String ROLEID
    );


    @FormUrlEncoded
    @POST("list_users_map")
    Call<Object> list_users_map(
            @Field("ad") String ad,
            @Field("role") String role,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST(" update_status")
    Call<Object>  update_status(
            @Field("status") String status,
            @Field("users") String users,
            @Field("AD") String ad
    );


    @FormUrlEncoded
    @POST("CONTACT_TAB")
    Call<Object> CONTACT_TAB(
            @Field("orgCode") String orgCode
    );









}
