package com.ronnelrazo.physical_counting.model;

import com.ronnelrazo.physical_counting.ListItem;

public class model_farm implements ListItem {

    public String orgcode,orgname,companyType;


    public model_farm(String orgcode, String orgname, String companyType) {
        this.orgcode = orgcode;
        this.orgname = orgname;
        this.companyType = companyType;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    @Override
    public int getItemType() {
        return ListItem.TYPE_ITEM;
    }
}
