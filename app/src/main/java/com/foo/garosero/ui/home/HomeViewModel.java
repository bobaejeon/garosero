package com.foo.garosero.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foo.garosero.data.UserData;
import com.google.firebase.database.DatabaseReference;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> pageTitle;
    public static MutableLiveData<String> explain;
    public static DatabaseReference database;
    public static UserData userData;

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

    public static DatabaseReference getDatabase() {
        return database;
    }

    public static void setDatabase(DatabaseReference database) {
        HomeViewModel.database = database;
    }

    public static UserData getUserData() {
        if (userData==null) userData = new UserData("");
        return userData;
    }

    public static void setUserData(UserData userData) {
        HomeViewModel.userData = userData;
    }
}
