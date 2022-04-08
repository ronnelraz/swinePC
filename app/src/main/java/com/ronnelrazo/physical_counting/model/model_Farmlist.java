package com.ronnelrazo.physical_counting.model;

import com.ronnelrazo.physical_counting.ListItem;

public class model_Farmlist implements ListItem {

    public String orgcode,orgname,farmcode,farmname;

    public model_Farmlist(String orgcode, String orgname, String farmcode, String farmname) {
        this.orgcode = orgcode;
        this.orgname = orgname;
        this.farmcode = farmcode;
        this.farmname = farmname;
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

    @Override
    public int getItemType() {
        return ListItem.TYPE_ITEM;
    }
}
