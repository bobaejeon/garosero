package com.foo.garosero.mviewmodel;

import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.myUtil.ServerHelper;

import java.util.ArrayList;

public class MapViewModel {
    public static ArrayList<String> treeDataArrayList;

    public static ArrayList<String> getTreeDataArrayList() {
        if (treeDataArrayList==null){
            treeDataArrayList = new ArrayList<>();
        }
        return treeDataArrayList;
    }

    public static void setTreeDataArrayList(ArrayList<String> treeDataArrayList) {
        getTreeDataArrayList();
        MapViewModel.treeDataArrayList = treeDataArrayList;
    }
}
