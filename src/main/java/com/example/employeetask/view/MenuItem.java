package com.example.employeetask.view;

public class MenuItem {
    int id;
    String title;
    State option;

    public MenuItem(int id, String title, State option) {
        this.id = id;
        this.title = title;
        this.option = option;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
