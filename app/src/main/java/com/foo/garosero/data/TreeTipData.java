package com.foo.garosero.data;

public class TreeTipData {
    public String banner_title;

    public String group1_title1;
    public String group1_title2;
    public String group1_content;

    public String group2_title1;
    public String group2_title2;
    public String group2_content;

    public Integer character;
    public Integer bannerColor;

    public TreeTipData(String banner_title,
                       String group1_title1, String group1_title2, String group1_content,
                       String group2_title1, String group2_title2, String group2_content,
                       Integer character, Integer bannerColor) {
        this.banner_title = banner_title;
        this.group1_title1 = group1_title1;
        this.group1_title2 = group1_title2;
        this.group1_content = group1_content;
        this.group2_title1 = group2_title1;
        this.group2_title2 = group2_title2;
        this.group2_content = group2_content;
        this.character = character;
        this.bannerColor = bannerColor;
    }
}
