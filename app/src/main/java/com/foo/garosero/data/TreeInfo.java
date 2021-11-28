package com.foo.garosero.data;

public class TreeInfo {
    public TreeInfo() {
    }

    public String getGu_nm() {
        return gu_nm;
    }

    public void setGu_nm(String gu_nm) {
        this.gu_nm = gu_nm;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public String getWdpt_nm() {
        return wdpt_nm;
    }

    public void setWdpt_nm(String wdpt_nm) {
        this.wdpt_nm = wdpt_nm;
    }

    public String getWidth_nm() {
        return width_nm;
    }

    public void setWidth_nm(String width_nm) {
        this.width_nm = width_nm;
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

    public String gu_nm;
    public String lc;
    public String wdpt_nm;
    public String width_nm;
    public String start_date;
    public String uid;
    public String end_date;
    public String tree_id;

//    public int getLevel() {
//        return level;
//    }
//
//    public void setLevel(int level) {
//        this.level = level;
//    }
//
//    public int getXp() {
//        return xp;
//    }
//
//    public void setXp(int xp) {
//        this.xp = xp;
//    }
//
//    public int level;
//    public int xp;

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

    public String tree_name;


    @Override
    public String toString() {
        return "TreeInfo{" +
                "gu_nm='" + gu_nm + '\'' +
                ", lc='" + lc + '\'' +
                ", wdpt_nm='" + wdpt_nm + '\'' +
                ", width_nm='" + width_nm + '\'' +
                ", start_date='" + start_date + '\'' +
                ", uid='" + uid + '\'' +
                ", end_date='" + end_date + '\'' +
                '}';
    }
}
