package com.foo.garosero.mviewmodel;

import androidx.lifecycle.ViewModel;

import com.foo.garosero.data.DiaryData;

public class DiaryViewModel extends ViewModel {
    private static DiaryData diaryData;

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
            answer = diaryData.getDiaryID() == new DiaryData().getDiaryID();
        }catch (Exception e){}
        return answer;
    }
}
