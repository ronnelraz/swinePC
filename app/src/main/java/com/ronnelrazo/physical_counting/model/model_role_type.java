package com.ronnelrazo.physical_counting.model;

public class model_role_type {
    public String role_id,role_type;

    public model_role_type(String role_id, String role_type) {
        this.role_id = role_id;
        this.role_type = role_type;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole_type() {
        return role_type;
    }

    public void setRole_type(String role_type) {
        this.role_type = role_type;
    }
}
