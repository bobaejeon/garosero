package com.foo.garosero.data;

import java.util.HashMap;
import java.util.Map;

public class DiaryData {
    private String diaryID = "";
    private String schedule = "";
    private int persons = 0;
    private String content = "";
    private String memo = "";
    private String picture = "";
    private Map<String, Object> trees = new HashMap<String, Object>();

    public DiaryData() {

    }

    public String getDiaryID() {
        return diaryID;
    }

    public void setDiaryID(String diaryID) {
        this.diaryID = diaryID;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Map<String, Object> getTrees() {
        return trees;
    }

    public void setTrees(Map<String, Object> trees) {
        this.trees = trees;
    }

    @Override
    public String toString() {
        return "DiaryData{" +
                "diaryID='" + diaryID + '\'' +
                ", schedule='" + schedule + '\'' +
                ", persons=" + persons +
                ", content='" + content + '\'' +
                ", memo='" + memo + '\'' +
                ", picture='" + picture + '\'' +
                ", trees=" + trees +
                '}';
    }

    public Map<String, Object> getHash(){
        Map<String, Object> hash = new HashMap<>();
        hash.put("diaryID", this.diaryID);
        hash.put("schedule", this.schedule);
        hash.put("persons", this.persons);
        hash.put("content", this.content);
        hash.put("memo", this.memo);
        hash.put("picture", this.picture);
        hash.put("trees", this.trees);
        return hash;
    }
}