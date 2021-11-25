package com.foo.garosero.mviewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foo.garosero.data.UserData;

public class myViewModel extends ViewModel {
    private MutableLiveData<String> pageTitle;
    public static MutableLiveData<UserData> userData;

    public MutableLiveData<String> getPageTitle() {
        if (pageTitle == null){
            pageTitle = new MutableLiveData<String>();
            pageTitle.setValue("");
        }
        return pageTitle;
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
