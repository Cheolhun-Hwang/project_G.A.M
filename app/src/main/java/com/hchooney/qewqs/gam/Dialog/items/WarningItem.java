package com.hchooney.qewqs.gam.Dialog.items;

/**
 * Created by hooney on 2017. 11. 28..
 */

public class WarningItem {
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
}
