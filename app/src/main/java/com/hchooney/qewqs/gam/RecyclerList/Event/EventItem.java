package com.hchooney.qewqs.gam.RecyclerList.Event;

import java.io.Serializable;

/**
 * Created by qewqs on 2017-11-21.
 */

public class EventItem implements Serializable {
    private int eid;
    private String eName;
    private String eLimitDate;
    private String eCordination;
    private String eSpot;
    private int eNum;
    private double eGpsx;
    private double eGpsy;
    private String eProfit;
    private String ePhoto;

    public EventItem() {
        this.eid = 0;
        this.eName = "";
        this.eLimitDate = "";
        this.eCordination = "";
        this.eSpot = "";
        this.eNum = 0;
        this.eGpsx = 0.0;
        this.eGpsy = 0.0;
        this.eProfit = "";
        this.ePhoto="";
    }

    public EventItem(int eid, String eName, String eLimitDate,
                     String eCordination, String eSpot, int eNum,
                     double eGpsx, double eGpsy, String eProfit, String ePhoto) {
        this.eid = eid;
        this.eName = eName;
        this.eLimitDate = eLimitDate;
        this.eCordination = eCordination;
        this.eSpot = eSpot;
        this.eNum = eNum;
        this.eGpsx = eGpsx;
        this.eGpsy = eGpsy;
        this.eProfit = eProfit;
        this.ePhoto = ePhoto;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteLimitDate() {
        return eLimitDate;
    }

    public void seteLimitDate(String eLimitDate) {
        this.eLimitDate = eLimitDate;
    }

    public String geteCordination() {
        return eCordination;
    }

    public void seteCordination(String eCordination) {
        this.eCordination = eCordination;
    }

    public String geteSpot() {
        return eSpot;
    }

    public void seteSpot(String eSpot) {
        this.eSpot = eSpot;
    }

    public int geteNum() {
        return eNum;
    }

    public void seteNum(int eNum) {
        this.eNum = eNum;
    }

    public double geteGpsx() {
        return eGpsx;
    }

    public void seteGpsx(double eGpsx) {
        this.eGpsx = eGpsx;
    }

    public double geteGpsy() {
        return eGpsy;
    }

    public void seteGpsy(double eGpsy) {
        this.eGpsy = eGpsy;
    }

    public String geteProfit() {
        return eProfit;
    }

    public void seteProfit(String eProfit) {
        this.eProfit = eProfit;
    }

    public String getePhoto() {
        return ePhoto;
    }

    public void setePhoto(String ePhoto) {
        this.ePhoto = ePhoto;
    }
}
