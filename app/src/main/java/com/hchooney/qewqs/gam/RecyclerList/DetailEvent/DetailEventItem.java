package com.hchooney.qewqs.gam.RecyclerList.DetailEvent;

import java.util.ArrayList;

/**
 * Created by qewqs on 2017-11-22.
 */

public class DetailEventItem {
    private int jeid;
    private String title;
    private String limitdate;
    private String photoURL;
    private ArrayList<String> uids;

    public DetailEventItem() {
        this.jeid = -1;
        this.title = "";
        this.limitdate = "";
        this.photoURL = "";
        this.uids = new ArrayList<String>();
    }

    public DetailEventItem(int jeid, String title, String limitdate,
                           String photoURL, ArrayList<String> uids) {
        this.jeid = jeid;
        this.title = title;
        this.limitdate = limitdate;
        this.photoURL = photoURL;
        this.uids = uids;
    }

    public int getJeid() {
        return jeid;
    }

    public void setJeid(int jeid) {
        this.jeid = jeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLimitdate() {
        return limitdate;
    }

    public void setLimitdate(String limitdate) {
        this.limitdate = limitdate;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public ArrayList<String> getUids() {
        return uids;
    }

    public void setUids(ArrayList<String> uids) {
        this.uids = uids;
    }
}
