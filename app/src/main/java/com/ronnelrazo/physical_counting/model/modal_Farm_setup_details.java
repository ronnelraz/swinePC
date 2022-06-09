package com.ronnelrazo.physical_counting.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class modal_Farm_setup_details {

    public String
            ORG_CODE,ORG_NAME,
            BUSINESS_GROUP_CODE,BUSINESS_TYPE_CODE,
            CHECKLIST_AUDIT_FLAG,BREEDER_AUDIT_FLAG,FEED_AUDIT_FLAG,MED_AUDIT_FLAG,
            ADDRESS,PROVINCE_CODE,MUNICIPALITY_CODE,BARANGAY_CODE,ZIP_CODE,
            FARM_MANAGER_CODE,FARM_MANAGER_NAME,FARM_MANAGER_CONTACT_NO,FARM_MANAGER_EMAIL,
            FARM_CLERK_CODE,FARM_CLERK_NAME,FARM_CLERK_CONTACT_NO,FARM_CLERK_EMAIL,
            STR_LONG,STR_LAT;


    public modal_Farm_setup_details(String ORG_CODE, String ORG_NAME, String BUSINESS_GROUP_CODE, String BUSINESS_TYPE_CODE, String CHECKLIST_AUDIT_FLAG, String BREEDER_AUDIT_FLAG, String FEED_AUDIT_FLAG, String MED_AUDIT_FLAG, String ADDRESS, String PROVINCE_CODE, String MUNICIPALITY_CODE, String BARANGAY_CODE, String ZIP_CODE, String FARM_MANAGER_CODE, String FARM_MANAGER_NAME, String FARM_MANAGER_CONTACT_NO, String FARM_MANAGER_EMAIL, String FARM_CLERK_CODE, String FARM_CLERK_NAME, String FARM_CLERK_CONTACT_NO, String FARM_CLERK_EMAIL, String STR_LONG, String STR_LAT) {
        this.ORG_CODE = ORG_CODE;
        this.ORG_NAME = ORG_NAME;
        this.BUSINESS_GROUP_CODE = BUSINESS_GROUP_CODE;
        this.BUSINESS_TYPE_CODE = BUSINESS_TYPE_CODE;
        this.CHECKLIST_AUDIT_FLAG = CHECKLIST_AUDIT_FLAG;
        this.BREEDER_AUDIT_FLAG = BREEDER_AUDIT_FLAG;
        this.FEED_AUDIT_FLAG = FEED_AUDIT_FLAG;
        this.MED_AUDIT_FLAG = MED_AUDIT_FLAG;
        this.ADDRESS = ADDRESS;
        this.PROVINCE_CODE = PROVINCE_CODE;
        this.MUNICIPALITY_CODE = MUNICIPALITY_CODE;
        this.BARANGAY_CODE = BARANGAY_CODE;
        this.ZIP_CODE = ZIP_CODE;
        this.FARM_MANAGER_CODE = FARM_MANAGER_CODE;
        this.FARM_MANAGER_NAME = FARM_MANAGER_NAME;
        this.FARM_MANAGER_CONTACT_NO = FARM_MANAGER_CONTACT_NO;
        this.FARM_MANAGER_EMAIL = FARM_MANAGER_EMAIL;
        this.FARM_CLERK_CODE = FARM_CLERK_CODE;
        this.FARM_CLERK_NAME = FARM_CLERK_NAME;
        this.FARM_CLERK_CONTACT_NO = FARM_CLERK_CONTACT_NO;
        this.FARM_CLERK_EMAIL = FARM_CLERK_EMAIL;
        this.STR_LONG = STR_LONG;
        this.STR_LAT = STR_LAT;
    }


    public String getORG_CODE() {
        return ORG_CODE;
    }
    @Nullable
    public void setORG_CODE(String ORG_CODE) {
        this.ORG_CODE = ORG_CODE;
    }

    public String getORG_NAME() {
        return ORG_NAME;
    }

    public void setORG_NAME(String ORG_NAME) {
        this.ORG_NAME = ORG_NAME;
    }

    public String getBUSINESS_GROUP_CODE() {
        return BUSINESS_GROUP_CODE;
    }

    public void setBUSINESS_GROUP_CODE(String BUSINESS_GROUP_CODE) {
        this.BUSINESS_GROUP_CODE = BUSINESS_GROUP_CODE;
    }

    public String getBUSINESS_TYPE_CODE() {
        return BUSINESS_TYPE_CODE;
    }

    public void setBUSINESS_TYPE_CODE(String BUSINESS_TYPE_CODE) {
        this.BUSINESS_TYPE_CODE = BUSINESS_TYPE_CODE;
    }

    public String getCHECKLIST_AUDIT_FLAG() {
        return CHECKLIST_AUDIT_FLAG;
    }

    public void setCHECKLIST_AUDIT_FLAG(String CHECKLIST_AUDIT_FLAG) {
        this.CHECKLIST_AUDIT_FLAG = CHECKLIST_AUDIT_FLAG;
    }

    public String getBREEDER_AUDIT_FLAG() {
        return BREEDER_AUDIT_FLAG;
    }

    public void setBREEDER_AUDIT_FLAG(String BREEDER_AUDIT_FLAG) {
        this.BREEDER_AUDIT_FLAG = BREEDER_AUDIT_FLAG;
    }

    public String getFEED_AUDIT_FLAG() {
        return FEED_AUDIT_FLAG;
    }

    public void setFEED_AUDIT_FLAG(String FEED_AUDIT_FLAG) {
        this.FEED_AUDIT_FLAG = FEED_AUDIT_FLAG;
    }

    public String getMED_AUDIT_FLAG() {
        return MED_AUDIT_FLAG;
    }

    public void setMED_AUDIT_FLAG(String MED_AUDIT_FLAG) {
        this.MED_AUDIT_FLAG = MED_AUDIT_FLAG;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getPROVINCE_CODE() {
        return PROVINCE_CODE;
    }

    public void setPROVINCE_CODE(String PROVINCE_CODE) {
        this.PROVINCE_CODE = PROVINCE_CODE;
    }

    public String getMUNICIPALITY_CODE() {
        return MUNICIPALITY_CODE;
    }

    public void setMUNICIPALITY_CODE(String MUNICIPALITY_CODE) {
        this.MUNICIPALITY_CODE = MUNICIPALITY_CODE;
    }

    public String getBARANGAY_CODE() {
        return BARANGAY_CODE;
    }

    public void setBARANGAY_CODE(String BARANGAY_CODE) {
        this.BARANGAY_CODE = BARANGAY_CODE;
    }

    public String getZIP_CODE() {
        return ZIP_CODE;
    }

    public void setZIP_CODE(String ZIP_CODE) {
        this.ZIP_CODE = ZIP_CODE;
    }

    public String getFARM_MANAGER_CODE() {
        return FARM_MANAGER_CODE;
    }

    public void setFARM_MANAGER_CODE(String FARM_MANAGER_CODE) {
        this.FARM_MANAGER_CODE = FARM_MANAGER_CODE;
    }

    public String getFARM_MANAGER_NAME() {
        return FARM_MANAGER_NAME;
    }

    public void setFARM_MANAGER_NAME(String FARM_MANAGER_NAME) {
        this.FARM_MANAGER_NAME = FARM_MANAGER_NAME;
    }

    public String getFARM_MANAGER_CONTACT_NO() {
        return FARM_MANAGER_CONTACT_NO;
    }

    public void setFARM_MANAGER_CONTACT_NO(String FARM_MANAGER_CONTACT_NO) {
        this.FARM_MANAGER_CONTACT_NO = FARM_MANAGER_CONTACT_NO;
    }

    public String getFARM_MANAGER_EMAIL() {
        return FARM_MANAGER_EMAIL;
    }

    public void setFARM_MANAGER_EMAIL(String FARM_MANAGER_EMAIL) {
        this.FARM_MANAGER_EMAIL = FARM_MANAGER_EMAIL;
    }

    public String getFARM_CLERK_CODE() {
        return FARM_CLERK_CODE;
    }

    public void setFARM_CLERK_CODE(String FARM_CLERK_CODE) {
        this.FARM_CLERK_CODE = FARM_CLERK_CODE;
    }

    public String getFARM_CLERK_NAME() {
        return FARM_CLERK_NAME;
    }

    public void setFARM_CLERK_NAME(String FARM_CLERK_NAME) {
        this.FARM_CLERK_NAME = FARM_CLERK_NAME;
    }

    public String getFARM_CLERK_CONTACT_NO() {
        return FARM_CLERK_CONTACT_NO;
    }

    public void setFARM_CLERK_CONTACT_NO(String FARM_CLERK_CONTACT_NO) {
        this.FARM_CLERK_CONTACT_NO = FARM_CLERK_CONTACT_NO;
    }

    public String getFARM_CLERK_EMAIL() {
        return FARM_CLERK_EMAIL;
    }

    public void setFARM_CLERK_EMAIL(String FARM_CLERK_EMAIL) {
        this.FARM_CLERK_EMAIL = FARM_CLERK_EMAIL;
    }

    public String getSTR_LONG() {
        return STR_LONG;
    }

    public void setSTR_LONG(String STR_LONG) {
        this.STR_LONG = STR_LONG;
    }


    public String getSTR_LAT() {
        return STR_LAT;
    }

    public void setSTR_LAT(String STR_LAT) {
        this.STR_LAT = STR_LAT;
    }
}
