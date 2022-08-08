package com.ronnelrazo.physical_counting.model;

public class model_file_upload {


    public String audit_no,file;

    public model_file_upload(String audit_no, String file) {
        this.audit_no = audit_no;
        this.file = file;
    }

    public String getAudit_no() {
        return audit_no;
    }

    public void setAudit_no(String audit_no) {
        this.audit_no = audit_no;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
