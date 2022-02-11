package com.foo.garosero.data;

public class TreeApiData {
    public static String STRING_GU_NM = "GU_NM";
    public static String STRING_OBJECTID = "OBJECTID";
    public static String STRING_WDPT_NM = "WDPT_NM";
    public static String STRING_LNG = "LNG";
    public static String STRING_LAT = "LAT";

    String GU_NM; // 구명
    String OBJECTID; // 수목고유번호 (BB 수정)
    String WDPT_NM; // 가로명 (나무종류)
    Double LNG; // 경도
    Double LAT; // 위도

    public TreeApiData(String GU_NM, String OBJECTID, String WDPT_NM, Double LNG, Double LAT) {
        this.GU_NM = GU_NM;
        this.OBJECTID = OBJECTID;
        this.WDPT_NM = WDPT_NM;
        this.LNG = LNG;
        this.LAT = LAT;
    }

    public String getGU_NM() {
        return GU_NM;
    }

    public String getOBJECTID() {
        return OBJECTID;
    }

    public String getWDPT_NM() {
        return WDPT_NM;
    }

    public Double getLNG() {
        return LNG;
    }

    public Double getLAT() {
        return LAT;
    }

    @Override
    public String toString() {
        return "TreeApiData{" +
                "GU_NM='" + GU_NM + '\'' +
                ", OBJECTID='" + OBJECTID + '\'' +
                ", WDPT_NM='" + WDPT_NM + '\'' +
                ", LNG='" + LNG + '\'' +
                ", LAT='" + LAT + '\'' +
                '}';
    }
}

