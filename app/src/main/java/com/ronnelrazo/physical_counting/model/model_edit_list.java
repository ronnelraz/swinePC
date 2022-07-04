package com.ronnelrazo.physical_counting.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class model_edit_list {

    @SerializedName("org_name")
    @Expose
    public String org_name;

    @SerializedName("org_code")
    @Expose
    public String org_code;

    @SerializedName("farm_code")
    @Expose
    public String farm_code;

    @SerializedName("farm_name")
    @Expose
    public String farm_name;

    @SerializedName("audit_no")
    @Expose
    public String audit_no;

    @SerializedName("doc_date")
    @Expose
    public String doc_date;

    @SerializedName("audit_date")
    @Expose
    public String audit_date;

    @SerializedName("business_group_code")
    @Expose
    public String business_group_code;

    @SerializedName("buisness_type_code")
    @Expose
    public String buisness_type_code;


    public model_edit_list(String org_name, String org_code, String farm_code, String farm_name, String audit_no, String doc_date, String audit_date, String business_group_code, String buisness_type_code) {
        this.org_name = org_name;
        this.org_code = org_code;
        this.farm_code = farm_code;
        this.farm_name = farm_name;
        this.audit_no = audit_no;
        this.doc_date = doc_date;
        this.audit_date = audit_date;
        this.business_group_code = business_group_code;
        this.buisness_type_code = buisness_type_code;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getFarm_code() {
        return farm_code;
    }

    public void setFarm_code(String farm_code) {
        this.farm_code = farm_code;
    }

    public String getFarm_name() {
        return farm_name;
    }

    public void setFarm_name(String farm_name) {
        this.farm_name = farm_name;
    }

    public String getAudit_no() {
        return audit_no;
    }

    public void setAudit_no(String audit_no) {
        this.audit_no = audit_no;
    }

    public String getDoc_date() {
        return doc_date;
    }

    public void setDoc_date(String doc_date) {
        this.doc_date = doc_date;
    }

    public String getAudit_date() {
        return audit_date;
    }

    public void setAudit_date(String audit_date) {
        this.audit_date = audit_date;
    }

    public String getBusiness_group_code() {
        return business_group_code;
    }

    public void setBusiness_group_code(String business_group_code) {
        this.business_group_code = business_group_code;
    }

    public String getBuisness_type_code() {
        return buisness_type_code;
    }

    public void setBuisness_type_code(String buisness_type_code) {
        this.buisness_type_code = buisness_type_code;
    }
}
