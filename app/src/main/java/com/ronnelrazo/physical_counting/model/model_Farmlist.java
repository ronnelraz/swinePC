package com.ronnelrazo.physical_counting.model;

import com.ronnelrazo.physical_counting.ListItem;

public class model_Farmlist implements ListItem {

    public String orgcode,orgname,farmcode,farmname,bu_code,bu_name,bu_type_code,bu_type_name;

    public model_Farmlist(String orgcode, String orgname, String farmcode, String farmname, String bu_code, String bu_name, String bu_type_code, String bu_type_name) {
        this.orgcode = orgcode;
        this.orgname = orgname;
        this.farmcode = farmcode;
        this.farmname = farmname;
        this.bu_code = bu_code;
        this.bu_name = bu_name;
        this.bu_type_code = bu_type_code;
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

    public String getFarmcode() {
        return farmcode;
    }

    public void setFarmcode(String farmcode) {
        this.farmcode = farmcode;
    }

    public String getFarmname() {
        return farmname;
    }

    public void setFarmname(String farmname) {
        this.farmname = farmname;
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

    public String getBu_type_code() {
        return bu_type_code;
    }

    public void setBu_type_code(String bu_type_code) {
        this.bu_type_code = bu_type_code;
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
