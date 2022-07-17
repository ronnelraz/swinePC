package com.ronnelrazo.physical_counting.model;

public class model_cancel_list {

    public String org_code,audit_no,doc_date,audit_date,farm_code,org_name,farm_name,confirm;
    public boolean isSelected;

    public model_cancel_list(String org_code, String audit_no, String doc_date, String audit_date, String farm_code, String org_name, String farm_name, String confirm, boolean isSelected) {
        this.org_code = org_code;
        this.audit_no = audit_no;
        this.doc_date = doc_date;
        this.audit_date = audit_date;
        this.farm_code = farm_code;
        this.org_name = org_name;
        this.farm_name = farm_name;
        this.confirm = confirm;
        this.isSelected = isSelected;
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

    public String getFarm_code() {
        return farm_code;
    }

    public void setFarm_code(String farm_code) {
        this.farm_code = farm_code;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getFarm_name() {
        return farm_name;
    }

    public void setFarm_name(String farm_name) {
        this.farm_name = farm_name;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
