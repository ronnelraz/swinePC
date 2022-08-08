package com.ronnelrazo.physical_counting.model;

public class model_transaction_details {

    public String title,org_code,audit_no,farm_name,audit_date,confirm_flag,farm_code;


    public model_transaction_details(String title, String org_code, String audit_no, String farm_name, String audit_date, String confirm_flag, String farm_code) {
        this.title = title;
        this.org_code = org_code;
        this.audit_no = audit_no;
        this.farm_name = farm_name;
        this.audit_date = audit_date;
        this.confirm_flag = confirm_flag;
        this.farm_code = farm_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getAudit_no() {
        return audit_no;
    }

    public void setAudit_no(String audit_no) {
        this.audit_no = audit_no;
    }

    public String getFarm_name() {
        return farm_name;
    }

    public void setFarm_name(String farm_name) {
        this.farm_name = farm_name;
    }

    public String getAudit_date() {
        return audit_date;
    }

    public void setAudit_date(String audit_date) {
        this.audit_date = audit_date;
    }

    public String getConfirm_flag() {
        return confirm_flag;
    }

    public void setConfirm_flag(String confirm_flag) {
        this.confirm_flag = confirm_flag;
    }

    public String getFarm_code() {
        return farm_code;
    }

    public void setFarm_code(String farm_code) {
        this.farm_code = farm_code;
    }
}
