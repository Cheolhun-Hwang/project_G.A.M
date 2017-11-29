package com.hchooney.qewqs.gam.Dialog.items;

/**
 * Created by hooney on 2017. 11. 28..
 */

public class JoinEventItem {
    private String ETitle;
    private String ECreateDate;
    private String EDeadline;
    private String ECordination;
    private int ENum;
    private String EProfit;
    private boolean isResult;

    public JoinEventItem( ) {
        this.ETitle = "";
        this.ECreateDate = "";
        this.EDeadline = "";
        this.ECordination = "";
        this.ENum = 0;
        this.EProfit = "";
        this.isResult = false;
    }

    public JoinEventItem(String ETitle, String ECreateDate, String EDeadline,
                         String ECordination, int ENum, String EProfit, boolean isResult) {
        this.ETitle = ETitle;
        this.ECreateDate = ECreateDate;
        this.EDeadline = EDeadline;
        this.ECordination = ECordination;
        this.ENum = ENum;
        this.EProfit = EProfit;
        this.isResult = isResult;
    }

    public String getETitle() {
        return ETitle;
    }

    public void setETitle(String ETitle) {
        this.ETitle = ETitle;
    }

    public String getECreateDate() {
        return ECreateDate;
    }

    public void setECreateDate(String ECreateDate) {
        this.ECreateDate = ECreateDate;
    }

    public String getEDeadline() {
        return EDeadline;
    }

    public void setEDeadline(String EDeadline) {
        this.EDeadline = EDeadline;
    }

    public String getECordination() {
        return ECordination;
    }

    public void setECordination(String ECordination) {
        this.ECordination = ECordination;
    }

    public int getENum() {
        return ENum;
    }

    public void setENum(int ENum) {
        this.ENum = ENum;
    }

    public String getEProfit() {
        return EProfit;
    }

    public void setEProfit(String EProfit) {
        this.EProfit = EProfit;
    }

    public boolean isResult() {
        return isResult;
    }

    public void setResult(boolean result) {
        isResult = result;
    }
}
