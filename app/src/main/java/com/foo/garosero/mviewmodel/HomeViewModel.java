package com.foo.garosero.mviewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.data.UserInfo;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
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
            UserInfo ud = new UserInfo("",new ArrayList<TreeInfo>());
            userInfo.setValue(ud);
        }
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        getUserInfo();
        HomeViewModel.userInfo.setValue(userInfo);
    }
}