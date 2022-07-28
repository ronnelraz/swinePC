package com.ronnelrazo.physical_counting.model;

public class model_upload_list {

    public String audit_no,farm;

    public model_upload_list(String audit_no, String farm) {
        this.audit_no = audit_no;
        this.farm = farm;
    }

    public String getAudit_no() {
        return audit_no;
    }

    public void setAudit_no(String audit_no) {
        this.audit_no = audit_no;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }
}
