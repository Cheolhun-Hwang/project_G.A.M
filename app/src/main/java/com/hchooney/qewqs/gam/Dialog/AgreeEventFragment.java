package com.hchooney.qewqs.gam.Dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hchooney.qewqs.gam.JoinEventActivity;
import com.hchooney.qewqs.gam.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgreeEventFragment extends DialogFragment {
    private Dialog dialog;
    private View view;

    private Button agree;
    private Button cancle;


    public AgreeEventFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_agree_event, null);
        dialog.setContentView(view);

        init();

        return dialog;
    }

    private void init(){
        agree = (Button) view.findViewById(R.id.agree_dial_completeBTN);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        cancle = (Button) view.findViewById(R.id.agree_dial_cancelBTN);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ((JoinEventActivity)getActivity()).disAgree();
            }
        });



    }

}
