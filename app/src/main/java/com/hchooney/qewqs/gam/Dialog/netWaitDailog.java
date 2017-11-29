package com.hchooney.qewqs.gam.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by hooney on 2017. 11. 29..
 */

public class netWaitDailog extends DialogFragment {

    private String title;
    private String Message;

    public static netWaitDailog newInstance() {
        return new netWaitDailog();
    }

    public Dialog onCreateDialog(Bundle savedInstancestate) {

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(title);
        dialog.setMessage(Message);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return dialog;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
