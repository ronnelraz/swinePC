package com.ronnelrazo.physical_counting.model;

import java.util.List;

public class modal_user_list {

    public String AD,role_type,role_id;
    public List<modal_list_group_user> grouplist;
    public String status;

    public modal_user_list(String AD, String role_type, String role_id, List<modal_list_group_user> grouplist, String status) {
        this.AD = AD;
        this.role_type = role_type;
        this.role_id = role_id;
        this.grouplist = grouplist;
        this.status = status;
    }

    public String getAD() {
        return AD;
    }

    public void setAD(String AD) {
        this.AD = AD;
    }

    public String getRole_type() {
        return role_type;
    }

    public void setRole_type(String role_type) {
        this.role_type = role_type;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public List<modal_list_group_user> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(List<modal_list_group_user> grouplist) {
        this.grouplist = grouplist;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
