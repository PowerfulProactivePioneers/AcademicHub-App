package com.example.academichub.responsePackage;

import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("msg")
    public String msg;

    public Status(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Status{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
