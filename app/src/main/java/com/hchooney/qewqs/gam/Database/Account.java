package com.hchooney.qewqs.gam.Database;

import java.io.Serializable;

/**
 * Created by qewqs on 2017-11-21.
 */

public class Account implements Serializable{
    private String uid;
    private String uname;
    private String unickname;
    private String uemail;
    private short uisapp;  //0 : not use, 1: use, 2: no data;

    public Account() {
        this.uid = "";
        this.uname = "";
        this.unickname = "";
        this.uemail = "";
        this.uisapp = 1;
    }

    public Account(String uid, String uname, String unickname, String uemail, short uisapp) {
        this.uid = uid;
        this.uname = uname;
        this.unickname = unickname;
        this.uemail = uemail;
        this.uisapp = uisapp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUnickname() {
        return unickname;
    }

    public void setUnickname(String unickname) {
        this.unickname = unickname;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public short getUisapp() {
        return uisapp;
    }

    public void setUisapp(short uisapp) {
        this.uisapp = uisapp;
    }
}
