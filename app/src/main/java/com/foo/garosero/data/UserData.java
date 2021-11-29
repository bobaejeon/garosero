package com.foo.garosero.data;

import java.io.Serializable;

public class UserData implements Serializable {
    public String name = "";

    public UserData(String name) {
        this.name = name;
    }

    public UserData() {
    }
}
