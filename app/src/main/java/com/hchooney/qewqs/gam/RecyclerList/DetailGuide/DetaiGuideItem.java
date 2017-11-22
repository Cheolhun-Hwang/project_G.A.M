package com.hchooney.qewqs.gam.RecyclerList.DetailGuide;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qewqs on 2017-11-22.
 */

public class DetaiGuideItem implements Serializable {
    private String Context;
    private ArrayList<String> imageList;

    public DetaiGuideItem() {
        this.Context = "";
        this.imageList = new ArrayList<String>();
    }

    public DetaiGuideItem(String context, ArrayList<String> imageList) {
        this.Context = context;
        this.imageList = imageList;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String context) {
        Context = context;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }
}
