package com.ronnelrazo.physical_counting.model;

import com.ronnelrazo.physical_counting.ListItem;

public class model_header_farm_org implements ListItem {
    public String header;

    public model_header_farm_org(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public int getItemType() {
        return ListItem.TYPE_HEADER;
    }
}
