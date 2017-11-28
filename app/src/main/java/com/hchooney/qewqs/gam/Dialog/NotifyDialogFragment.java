package com.hchooney.qewqs.gam.Dialog;


import android.app.Dialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hchooney.qewqs.gam.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotifyDialogFragment extends DialogFragment {
    private Dialog dialog;
    private View view;

    public NotifyDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_notify_dialog, null);
        dialog.setContentView(view);


        return dialog;
    }
}
