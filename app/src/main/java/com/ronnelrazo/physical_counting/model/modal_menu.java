package com.ronnelrazo.physical_counting.model;

public class modal_menu {

    public int position,icon;
    public String Title;

    public modal_menu(int position, int icon, String title) {
        this.position = position;
        this.icon = icon;
        this.Title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
