package com.foo.garosero.mviewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foo.garosero.data.UserInfo;

public class myViewModel extends ViewModel {
    private MutableLiveData<String> pageTitle;
    public static MutableLiveData<UserInfo> userInfo;

    public MutableLiveData<String> getPageTitle() {
        if (pageTitle == null){
            pageTitle = new MutableLiveData<String>();
            pageTitle.setValue("");
        }
        return pageTitle;
    }

    public static MutableLiveData<UserInfo> getUserInfo() {
        if (userInfo ==null) {
            userInfo = new MutableLiveData<UserInfo>();
            UserInfo ud = new UserInfo("",null);
            userInfo.setValue(ud);
        }
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        myViewModel.userInfo.setValue(userInfo);
    }
}
