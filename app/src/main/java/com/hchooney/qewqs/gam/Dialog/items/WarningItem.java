package com.hchooney.qewqs.gam.Dialog.items;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by hooney on 2017. 11. 28..
 */

public class WarningItem implements Parcelable {
    private String WWhen;
    private String WWhy;

    public WarningItem( ) {
        this.WWhen = WWhen;
        this.WWhy = WWhy;
    }

    public WarningItem(String WWhen, String WWhy) {
        this.WWhen = WWhen;
        this.WWhy = WWhy;
    }

    protected WarningItem(Parcel in) {
        WWhen = in.readString();
        WWhy = in.readString();
    }

    public static final Creator<WarningItem> CREATOR = new Creator<WarningItem>() {
        @Override
        public WarningItem createFromParcel(Parcel in) {
            return new WarningItem(in);
        }

        @Override
        public WarningItem[] newArray(int size) {
            return new WarningItem[size];
        }
    };

    public String getWWhen() {
        return WWhen;
    }

    public void setWWhen(String WWhen) {
        this.WWhen = WWhen;
    }

    public String getWWhy() {
        return WWhy;
    }

    public void setWWhy(String WWhy) {
        this.WWhy = WWhy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(WWhen);
        parcel.writeString(WWhy);
    }
}
