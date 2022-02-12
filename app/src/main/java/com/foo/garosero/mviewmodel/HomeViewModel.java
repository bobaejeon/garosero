package com.foo.garosero.mviewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.data.UserInfo;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> pageTitle;
    public static MutableLiveData<UserInfo> userInfo;
    public static MutableLiveData<ArrayList<TreeInfo>> approveInfo;
    public static MutableLiveData<ArrayList<TreeInfo>> candidateInfo;

    // page title
    public MutableLiveData<String> getPageTitle() {
        if (pageTitle == null){
            pageTitle = new MutableLiveData<String>();
            pageTitle.setValue("");
        }
        return pageTitle;
    }

    // userInfo
    public static MutableLiveData<UserInfo> getUserInfo() {
        if (userInfo ==null) {
            userInfo = new MutableLiveData<UserInfo>();
            UserInfo ud = new UserInfo("",new ArrayList<>());
            userInfo.setValue(ud);
        }
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        getUserInfo();
        HomeViewModel.userInfo.setValue(userInfo);
    }

    // approveInfo
    public static MutableLiveData<ArrayList<TreeInfo>> getApproveInfo() {
        if (approveInfo == null){
            approveInfo = new MutableLiveData<>();
            approveInfo.setValue(new ArrayList<>());
        }
        return approveInfo;
    }

    public static void setApproveInfo(ArrayList<TreeInfo> approveInfo) {
        getApproveInfo();
        HomeViewModel.approveInfo.setValue(approveInfo);
    }

    // candidateInfo
    public static MutableLiveData<ArrayList<TreeInfo>> getCandidateInfo() {
        if (candidateInfo == null){
            candidateInfo = new MutableLiveData<>();
            candidateInfo.setValue(new ArrayList<>());
        }
        return candidateInfo;
    }

    public static void setCandidateInfo(ArrayList<TreeInfo> candidateInfo) {
        getCandidateInfo();
        HomeViewModel.candidateInfo.setValue(candidateInfo);
    }
}