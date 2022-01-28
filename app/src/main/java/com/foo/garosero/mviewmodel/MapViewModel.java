package com.foo.garosero.mviewmodel;

import com.foo.garosero.data.TreeData;
import com.foo.garosero.myUtil.ServerHelper;

import java.util.ArrayList;

public class MapViewModel {
    public static ArrayList<TreeData> treeDataArrayList;

    public static ArrayList<TreeData> getTreeDataArrayList() {
        if (treeDataArrayList==null){
            ServerHelper.readTreeTakenFromFireBase();
            treeDataArrayList = new ArrayList<TreeData>();
        }
        return treeDataArrayList;
    }

    public static void setTreeDataArrayList(ArrayList<TreeData> treeDataArrayList) {
        getTreeDataArrayList();
        MapViewModel.treeDataArrayList = treeDataArrayList;
    }
}
