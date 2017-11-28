package com.hchooney.qewqs.gam.Dialog.RecyclerList;

import java.io.Serializable;

/**
 * Created by qewqs on 2017-11-27.
 */

public class CouponItem implements Serializable{
    private int cid;
    private String EName;
    private String EProfit;
    private String Edaedlinedate;
    private String CPhotourl;

    public CouponItem( ) {
        this.cid = 0;
        this.EName = "";
        this.EProfit = "";
        this.Edaedlinedate = "";
        this.CPhotourl = "";
    }

    public CouponItem(int cid, String EName, String EProfit,
                      String edaedlinedate, String CPhotourl) {
        this.cid = cid;
        this.EName = EName;
        this.EProfit = EProfit;
        Edaedlinedate = edaedlinedate;
        this.CPhotourl = CPhotourl;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getEName() {
        return EName;
    }

    public void setEName(String EName) {
        this.EName = EName;
    }

    public String getEProfit() {
        return EProfit;
    }

    public void setEProfit(String EProfit) {
        this.EProfit = EProfit;
    }

    public String getEdaedlinedate() {
        return Edaedlinedate;
    }

    public void setEdaedlinedate(String edaedlinedate) {
        Edaedlinedate = edaedlinedate;
    }

    public String getCPhotourl() {
        return CPhotourl;
    }

    public void setCPhotourl(String CPhotourl) {
        this.CPhotourl = CPhotourl;
    }
}
