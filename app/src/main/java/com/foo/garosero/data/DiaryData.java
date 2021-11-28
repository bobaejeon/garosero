package com.foo.garosero.data;

import android.graphics.Bitmap;

public class DiaryData {
    private int diaryID = 0;
    private String schedule = "";
    private int persons = 0;
    private String content = "";
    private String memo = "";
    private Bitmap picture = null;

    public DiaryData() {

    }

    public int getDiaryID() {
        return diaryID;
    }

    public void setDiaryID(int diaryID) {
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

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "DiaryData{" +
                "diaryID=" + diaryID +
                ", schedule='" + schedule + '\'' +
                ", persons=" + persons +
                ", content='" + content + '\'' +
                ", memo='" + memo + '\'' +
                ", picture=" + picture +
                '}';
    }
}
