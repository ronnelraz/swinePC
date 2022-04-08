package com.ronnelrazo.physical_counting.model;

import com.ronnelrazo.physical_counting.ListItem;
import com.ronnelrazo.physical_counting.ListItem_Checklist;

public class modal_checklist_maintopic implements ListItem_Checklist {

    public String mainTopic;

    public modal_checklist_maintopic(String mainTopic) {
        this.mainTopic = mainTopic;
    }

    public String getMainTopic() {
        return mainTopic;
    }

    public void setMainTopic(String mainTopic) {
        this.mainTopic = mainTopic;
    }

    @Override
    public int getItemType() {
        return ListItem_Checklist.TYPE_MAIN;
    }
}
