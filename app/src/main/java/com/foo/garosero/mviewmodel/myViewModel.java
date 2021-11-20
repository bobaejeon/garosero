package com.foo.garosero.mviewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foo.garosero.data.UserData;

public class myViewModel extends ViewModel {
    private MutableLiveData<String> pageTitle;
    public static MutableLiveData<String> explain;
    public static MutableLiveData<UserData> userData;

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

    public static MutableLiveData<UserData> getUserData() {
        if (userData==null) {
            userData = new MutableLiveData<UserData>();
            UserData ud = new UserData("");
            userData.setValue(ud);
        }
        return userData;
    }

    public static void setUserData(UserData userData) {
        myViewModel.userData.setValue(userData);
    }
}
