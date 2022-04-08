package com.ronnelrazo.physical_counting.model;

import com.ronnelrazo.physical_counting.ListItem_Checklist;

public class modal_checklist_Details implements ListItem_Checklist {

    public String details_code,details;

    public modal_checklist_Details(String details_code, String details) {
        this.details_code = details_code;
        this.details = details;
    }

    public String getDetails_code() {
        return details_code;
    }

    public void setDetails_code(String details_code) {
        this.details_code = details_code;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public int getItemType() {
        return ListItem_Checklist.TYPE_ITEM;
    }
}
