package com.hchooney.qewqs.gam.RecyclerList.Guide;

import java.io.Serializable;

/**
 * Created by qewqs on 2017-11-21.
 */

public class GuideItem implements Serializable{
    private int gid;
    private String spot;
    private double gpsx;
    private double gpsy;
    private String gAudio;
    private String gImage;
    private String gModifyDate;

    public GuideItem() {
        this.gid = 0;
        this.spot = "";
        this.gpsx = 0;
        this.gpsy = 0;
        this.gAudio = "";
        this.gImage = "";
        this.gModifyDate = "";
    }

    public GuideItem(int gid, String spot, double gpsx, double gpsy,
                     String gAudio, String gImage, String gModifyDate) {
        this.gid = gid;
        this.spot = spot;
        this.gpsx = gpsx;
        this.gpsy = gpsy;
        this.gAudio = gAudio;
        this.gImage = gImage;
        this.gModifyDate = gModifyDate;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public double getGpsx() {
        return gpsx;
    }

    public void setGpsx(double gpsx) {
        this.gpsx = gpsx;
    }

    public double getGpsy() {
        return gpsy;
    }

    public void setGpsy(double gpsy) {
        this.gpsy = gpsy;
    }

    public String getgAudio() {
        return gAudio;
    }

    public void setgAudio(String gAudio) {
        this.gAudio = gAudio;
    }

    public String getgImage() {
        return gImage;
    }

    public void setgImage(String gImage) {
        this.gImage = gImage;
    }

    public String getgModifyDate() {
        return gModifyDate;
    }

    public void setgModifyDate(String gModifyDate) {
        this.gModifyDate = gModifyDate;
    }
}
