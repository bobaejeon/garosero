package com.foo.garosero.data;

public class TreeInfo {
    public TreeInfo() {
    }

    public String district;
    public String location;
    public String road;
    public String start_date;
    public String uid;
    public String end_date;
    public String tree_id;
    public String tree_name;
    public int level;
    public int xp;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
                "district='" + district + '\'' +
                ", location='" + location + '\'' +
                ", road='" + road + '\'' +
                ", start_date='" + start_date + '\'' +
                ", uid='" + uid + '\'' +
                ", end_date='" + end_date + '\'' +
                ", tree_id='" + tree_id + '\'' +
                ", tree_name='" + tree_name + '\'' +
                ", level=" + level +
                ", xp=" + xp +
                '}';
    }
}
