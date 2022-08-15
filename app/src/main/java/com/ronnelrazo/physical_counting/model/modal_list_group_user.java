package com.ronnelrazo.physical_counting.model;

public class modal_list_group_user {
    public String org_code,name;
    public boolean check;

    public modal_list_group_user(String org_code, String name, boolean check) {
        this.org_code = org_code;
        this.name = name;
        this.check = check;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
