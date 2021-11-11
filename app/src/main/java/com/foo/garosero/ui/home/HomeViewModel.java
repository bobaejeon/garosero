package com.foo.garosero.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> pageTitle;
    public static MutableLiveData<String> explain;

    public MutableLiveData<String> getPageTitle() {
        if (pageTitle == null){
            pageTitle = new MutableLiveData<String>();
            pageTitle.setValue("");
        }
        return pageTitle;
    }

    public static MutableLiveData<String> getExplain() {
        if (explain == null){
            explain = new MutableLiveData<String>();
            explain.setValue("");
        }
        return explain;
    }

    public static void setExplain(String explain) {
        getExplain().setValue(explain);
    }
}
