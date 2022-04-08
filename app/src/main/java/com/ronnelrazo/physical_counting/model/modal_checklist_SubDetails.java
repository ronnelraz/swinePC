package com.ronnelrazo.physical_counting.model;

import com.ronnelrazo.physical_counting.ListItem_Checklist;

public class modal_checklist_SubDetails implements ListItem_Checklist {

    public String bu_code,main_code,sub_code,sub_details;

    public modal_checklist_SubDetails(String bu_code, String main_code, String sub_code, String sub_details) {
        this.bu_code = bu_code;
        this.main_code = main_code;
        this.sub_code = sub_code;
        this.sub_details = sub_details;
    }

    public String getBu_code() {
        return bu_code;
    }

    public void setBu_code(String bu_code) {
        this.bu_code = bu_code;
    }

    public String getMain_code() {
        return main_code;
    }

    public void setMain_code(String main_code) {
        this.main_code = main_code;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_details() {
        return sub_details;
    }

    public void setSub_details(String sub_details) {
        this.sub_details = sub_details;
    }

    @Override
    public int getItemType() {
        return ListItem_Checklist.TYPE_HEADER;
    }
}
