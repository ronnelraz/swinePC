package com.ronnelrazo.physical_counting.model;

public class model_upload_list {

    public String ORG_CODE,FARM_CODE,FARM_NAME,AUDIT_NO,AUDIT_DATE;
    public boolean attachfile;

    public model_upload_list(String ORG_CODE, String FARM_CODE, String FARM_NAME, String AUDIT_NO, String AUDIT_DATE, boolean attachfile) {
        this.ORG_CODE = ORG_CODE;
        this.FARM_CODE = FARM_CODE;
        this.FARM_NAME = FARM_NAME;
        this.AUDIT_NO = AUDIT_NO;
        this.AUDIT_DATE = AUDIT_DATE;
        this.attachfile = attachfile;
    }

    public String getORG_CODE() {
        return ORG_CODE;
    }

    public void setORG_CODE(String ORG_CODE) {
        this.ORG_CODE = ORG_CODE;
    }

    public String getFARM_CODE() {
        return FARM_CODE;
    }

    public void setFARM_CODE(String FARM_CODE) {
        this.FARM_CODE = FARM_CODE;
    }

    public String getFARM_NAME() {
        return FARM_NAME;
    }

    public void setFARM_NAME(String FARM_NAME) {
        this.FARM_NAME = FARM_NAME;
    }

    public String getAUDIT_NO() {
        return AUDIT_NO;
    }

    public void setAUDIT_NO(String AUDIT_NO) {
        this.AUDIT_NO = AUDIT_NO;
    }

    public String getAUDIT_DATE() {
        return AUDIT_DATE;
    }

    public void setAUDIT_DATE(String AUDIT_DATE) {
        this.AUDIT_DATE = AUDIT_DATE;
    }

    public boolean isAttachfile() {
        return attachfile;
    }

    public void setAttachfile(boolean attachfile) {
        this.attachfile = attachfile;
    }
}
