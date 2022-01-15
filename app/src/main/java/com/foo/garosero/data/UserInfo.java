package com.foo.garosero.data;

import java.util.ArrayList;

public class UserInfo {
	String name;

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "UserInfo{" +
				"name='" + name + '\'' +
				", treeList=" + treeList +
				"}, token=" +token;
	}

	public boolean isEmpty() {
		return (this.treeList == null) || (this.treeList.size() == 0);
	}
	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<TreeInfo> getTreeList() {
		return treeList;
	}

	public void setTreeList(ArrayList<TreeInfo> treeList) {
		this.treeList = treeList;
	}

	public UserInfo(String name, ArrayList<TreeInfo> treeList) {
		this.name = name;
		this.treeList = treeList;
	}

	public UserInfo(){
		this.name = "";
		this.treeList = null;
	}

	ArrayList<TreeInfo> treeList;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	String token;
}