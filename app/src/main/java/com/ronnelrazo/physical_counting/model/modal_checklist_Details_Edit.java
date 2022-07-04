package com.ronnelrazo.physical_counting.model;

import com.ronnelrazo.physical_counting.ListItem_Checklist;

public class modal_checklist_Details_Edit implements ListItem_Checklist {

    public String details_code,details,details_seq, m_code,m_desc,m_seq, s_code,s_desc,s_seq, bu_code,bu_type;
    public String org_code,farm_code,check_item,remark;


    public modal_checklist_Details_Edit(String details_code, String details, String details_seq, String m_code, String m_desc, String m_seq, String s_code, String s_desc, String s_seq, String bu_code, String bu_type, String org_code, String farm_code, String check_item, String remark) {
        this.details_code = details_code;
        this.details = details;
        this.details_seq = details_seq;
        this.m_code = m_code;
        this.m_desc = m_desc;
        this.m_seq = m_seq;
        this.s_code = s_code;
        this.s_desc = s_desc;
        this.s_seq = s_seq;
        this.bu_code = bu_code;
        this.bu_type = bu_type;
        this.org_code = org_code;
        this.farm_code = farm_code;
        this.check_item = check_item;
        this.remark = remark;
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

    public String getDetails_seq() {
        return details_seq;
    }

    public void setDetails_seq(String details_seq) {
        this.details_seq = details_seq;
    }

    public String getM_code() {
        return m_code;
    }

    public void setM_code(String m_code) {
        this.m_code = m_code;
    }

    public String getM_desc() {
        return m_desc;
    }

    public void setM_desc(String m_desc) {
        this.m_desc = m_desc;
    }

    public String getM_seq() {
        return m_seq;
    }

    public void setM_seq(String m_seq) {
        this.m_seq = m_seq;
    }

    public String getS_code() {
        return s_code;
    }

    public void setS_code(String s_code) {
        this.s_code = s_code;
    }

    public String getS_desc() {
        return s_desc;
    }

    public void setS_desc(String s_desc) {
        this.s_desc = s_desc;
    }

    public String getS_seq() {
        return s_seq;
    }

    public void setS_seq(String s_seq) {
        this.s_seq = s_seq;
    }

    public String getBu_code() {
        return bu_code;
    }

    public void setBu_code(String bu_code) {
        this.bu_code = bu_code;
    }

    public String getBu_type() {
        return bu_type;
    }

    public void setBu_type(String bu_type) {
        this.bu_type = bu_type;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getFarm_code() {
        return farm_code;
    }

    public void setFarm_code(String farm_code) {
        this.farm_code = farm_code;
    }

    public String getCheck_item() {
        return check_item;
    }

    public void setCheck_item(String check_item) {
        this.check_item = check_item;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int getItemType() {
        return ListItem_Checklist.TYPE_ITEM;
    }
}
