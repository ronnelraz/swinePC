package com.ronnelrazo.physical_counting.model;

public class modal_pdf_report {

    public String org_name,farm_name,org_code,farm_code,audit_no,audit_date;
    public boolean isChgecked;
    public String path,url;

    public modal_pdf_report(String org_name, String farm_name, String org_code, String farm_code, String audit_no, String audit_date, boolean isChgecked, String path, String url) {
        this.org_name = org_name;
        this.farm_name = farm_name;
        this.org_code = org_code;
        this.farm_code = farm_code;
        this.audit_no = audit_no;
        this.audit_date = audit_date;
        this.isChgecked = isChgecked;
        this.path = path;
        this.url = url;
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

    public String getAudit_no() {
        return audit_no;
    }

    public void setAudit_no(String audit_no) {
        this.audit_no = audit_no;
    }

    public String getAudit_date() {
        return audit_date;
    }

    public void setAudit_date(String audit_date) {
        this.audit_date = audit_date;
    }

    public boolean isChgecked() {
        return isChgecked;
    }

    public void setChgecked(boolean chgecked) {
        isChgecked = chgecked;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
