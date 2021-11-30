package com.foo.garosero.mviewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foo.garosero.data.DiaryData;

import java.util.ArrayList;

public class DiaryViewModel extends ViewModel {
    private static DiaryData diaryData;
    public static MutableLiveData<ArrayList<DiaryData>> diaryDataList;

    public static DiaryData getDiaryData() {
        if (diaryData==null) setEmpty();
        return diaryData;
    }

    public static void setDiaryData(DiaryData diaryData) {
        DiaryViewModel.diaryData = diaryData;
    }

    public static void setEmpty(){
        diaryData = new DiaryData();
    }

    public static Boolean isEmpty(){
        Boolean answer = true;
        try {
            //answer = diaryData.getDiaryID() == new DiaryData().getDiaryID();
        }catch (Exception e){}
        return answer;
    }

    public static MutableLiveData<ArrayList<DiaryData>> getDiaryDataList() {
        if (diaryDataList==null) {
            diaryDataList = new MutableLiveData<ArrayList<DiaryData>>();
        }
        return diaryDataList;
    }

    public static void setDiaryDataList(MutableLiveData<ArrayList<DiaryData>> diaryDataList) {
        getDiaryDataList();
        DiaryViewModel.diaryDataList = diaryDataList;
    }
}
