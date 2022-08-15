package com.ronnelrazo.physical_counting.model;

public class modal_menu_access {

    public String mene;
    public boolean check;

    public modal_menu_access(String mene, boolean check) {
        this.mene = mene;
        this.check = check;
    }

    public String getMene() {
        return mene;
    }

    public void setMene(String mene) {
        this.mene = mene;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
