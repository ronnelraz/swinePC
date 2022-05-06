package com.ronnelrazo.physical_counting.model;

import com.ronnelrazo.physical_counting.ListItem;

public class model_farm implements ListItem {

    public String orgcode,orgname,companyType,business_type,bu_code,bu_name,bu_type_name;


    public model_farm(String orgcode, String orgname, String companyType, String business_type, String bu_code, String bu_name, String bu_type_name) {
        this.orgcode = orgcode;
        this.orgname = orgname;
        this.companyType = companyType;
        this.business_type = business_type;
        this.bu_code = bu_code;
        this.bu_name = bu_name;
        this.bu_type_name = bu_type_name;
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

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getBu_code() {
        return bu_code;
    }

    public void setBu_code(String bu_code) {
        this.bu_code = bu_code;
    }

    public String getBu_name() {
        return bu_name;
    }

    public void setBu_name(String bu_name) {
        this.bu_name = bu_name;
    }

    public String getBu_type_name() {
        return bu_type_name;
    }

    public void setBu_type_name(String bu_type_name) {
        this.bu_type_name = bu_type_name;
    }

    @Override
    public int getItemType() {
        return ListItem.TYPE_ITEM;
    }
}
