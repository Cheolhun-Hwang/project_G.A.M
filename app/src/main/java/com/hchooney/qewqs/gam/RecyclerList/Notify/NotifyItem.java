package com.hchooney.qewqs.gam.RecyclerList.Notify;

import java.io.Serializable;

/**
 * Created by qewqs on 2017-11-21.
 */

public class NotifyItem implements Serializable{
    private String Nid;
    private String TItle;
    private String Date;
    private String Context;
    private String Who;

    public NotifyItem() {
        this.Nid = "";
        this.TItle = "";
        this.Date = "";
        this.Context = "";
        this.Who = "";
    }

    public NotifyItem(String nid, String tItle, String date, String context, String who) {
        this.Nid = nid;
        this.TItle = tItle;
        this.Date = date;
        this.Context = context;
        this.Who = who;
    }

    public String getNid() {
        return Nid;
    }

    public void setNid(String nid) {
        Nid = nid;
    }

    public String getTItle() {
        return TItle;
    }

    public void setTItle(String TItle) {
        this.TItle = TItle;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String context) {
        Context = context;
    }

    public String getWho() {
        return Who;
    }

    public void setWho(String who) {
        Who = who;
    }
}
