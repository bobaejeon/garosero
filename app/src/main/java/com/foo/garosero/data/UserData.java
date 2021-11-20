package com.foo.garosero.data;

import java.io.Serializable;

public class UserData implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name = "";
    public String district = "";
    public String kind = "";
    public String location = "";
    public String road = "";
    public String start_date = "";

    public UserData(){}

    public String getTree_name() {
        return tree_name;
    }

    public void setTree_name(String tree_name) {
        this.tree_name = tree_name;
    }

    public String tree_name;

    public UserData(String name){
        this.district = "";
        this.kind = "아직 나무가 없어요";
        this.location = "";
        this.road = "";
        this.start_date = "";
        this.name = name;
        this.tree_name = "아직 나무가 없어요";
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
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

    // 비어있는 인스턴스인지 확인
    public Boolean isEmpty(){
        if (kind.equals("아직 나무가 없어요")){
            return true;
        }
        return false;
    }

    public String getCarbon_amt(){
        String carbon_amt;
        switch (getKind()){
            case "은행나무":
                carbon_amt = "33.7kg";
                break;
            case "소나무":
                carbon_amt = "47.5kg";
                break;
            case "양버즘나무":
                carbon_amt = "361.6kg";
                break;
            default:
                carbon_amt = "없음";
        }
        return carbon_amt;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", district='" + district + '\'' +
                ", kind='" + kind + '\'' +
                ", location='" + location + '\'' +
                ", road='" + road + '\'' +
                ", start_date='" + start_date + '\'' +
                ", tree_name='" + tree_name + '\'' +
                '}';
    }
}
