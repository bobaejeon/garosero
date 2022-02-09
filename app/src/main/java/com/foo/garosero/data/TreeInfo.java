package com.foo.garosero.data;

import java.util.HashMap;
import java.util.Map;

public class TreeInfo {
    public TreeInfo() {
    }

    public TreeInfo(String tree_id, String tree_name, String tree_type, String start_date, String location, int xp ) {
        this.tree_name = tree_name;
        this.tree_id = tree_id;
        this.tree_type = tree_type;
        this.start_date = start_date;
        this.location = location;
        this.xp = xp;
    }
    public String getTree_id() {
        return tree_id;
    }

    public void setTree_id(String tree_id) {
        this.tree_id = tree_id;
    }

    public String getTree_name() {
        return tree_name;
    }

    public void setTree_name(String tree_name) {
        this.tree_name = tree_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getTree_type() {
        return tree_type;
    }

    public void setTree_type(String tree_type) {
        this.tree_type = tree_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    @Override
    public String toString() {
        return "TreeInfo{" +
                "tree_id='" + tree_id + '\'' +
                ", tree_name='" + tree_name + '\'' +
                ", start_date='" + start_date + '\'' +
                ", tree_type='" + tree_type + '\'' +
                ", location='" + location + '\'' +
                ", xp=" + xp +
                '}';
    }

    private String tree_id;
    private String tree_name;
    private String start_date;
    private String tree_type;
    private String location;
    private int xp;
}