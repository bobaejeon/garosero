package com.foo.garosero.data;

public class TreeApiData {
    public static String STRING_GU_NM = "GU_NM";
    public static String STRING_TRE_IDN = "TRE_IDN";
    public static String STRING_WDPT_NM = "WDPT_NM";
    public static String STRING_LNG = "LNG";
    public static String STRING_LAT = "LAT";

    String GU_NM; // 구명
    String TRE_IDN; // 수목고유번호
    String WDPT_NM; // 가로명
    Double LNG; // 경도
    Double LAT; // 위도

    public TreeApiData(String GU_NM, String TRE_IDN, String WDPT_NM, Double LNG, Double LAT) {
        this.GU_NM = GU_NM;
        this.TRE_IDN = TRE_IDN;
        this.WDPT_NM = WDPT_NM;
        this.LNG = LNG;
        this.LAT = LAT;
    }

    public String getGU_NM() {
        return GU_NM;
    }

    public String getTRE_IDN() {
        return TRE_IDN;
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
                ", TRE_IDN='" + TRE_IDN + '\'' +
                ", WDPT_NM='" + WDPT_NM + '\'' +
                ", LNG='" + LNG + '\'' +
                ", LAT='" + LAT + '\'' +
                '}';
    }
}

