package com.foo.garosero.data;

public class TreeData {
    private String date = "";
    private String key = "";
    private String name = "";
    private String location = "";
    private String tree_id = "";
    private String tree_location = "";
    private String tree_type = "";
    private String uid = "";
    private String unit = "";

    // todo : check field
    private String field = "";

    // constructor
    public TreeData() {
    }

    // getter & setter
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTree_id() {
        return tree_id;
    }

    public void setTree_id(String tree_id) {
        this.tree_id = tree_id;
    }

    public String getTree_location() {
        return tree_location;
    }

    public void setTree_location(String tree_location) {
        this.tree_location = tree_location;
    }

    public String getTree_type() {
        return tree_type;
    }

    public void setTree_type(String tree_type) {
        this.tree_type = tree_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    // toString
    @Override
    public String toString() {
        return "TreeData{" +
                "date='" + date + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", tree_id='" + tree_id + '\'' +
                ", tree_location='" + tree_location + '\'' +
                ", tree_type='" + tree_type + '\'' +
                ", uid='" + uid + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
