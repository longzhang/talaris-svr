package me.ele.talaris.napos.model;

import java.util.List;

public class Group {
    private String name;
    private String type;
    private List<Item> Items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Item> getItems() {
        return Items;
    }

    public void setItems(List<Item> items) {
        Items = items;
    }

    public Group() {
        super();
    }

}
