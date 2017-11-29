package com.hchooney.qewqs.gam.Dialog;


import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hchooney.qewqs.gam.R;
import com.hchooney.qewqs.gam.RecyclerList.Notify.NotifyItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotifyDialogFragment extends DialogFragment {
    private Dialog dialog;
    private View view;

    private TextView title;
    private TextView date_write;
    private TextView context;
    private Button completeBTN;

    private NotifyItem item;



    public NotifyDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_notify_dialog, null);
        dialog.setContentView(view);

        init();


        return dialog;
    }

    private void init(){

        item = (NotifyItem) getArguments().getSerializable("item");
        if(item==null){
            Toast.makeText(getContext(), "정보가 없습니다.\n다시 시도해주세요.", Toast.LENGTH_LONG).show();
            dismiss();
        }

        title = (TextView) view.findViewById(R.id.notify_dial_title);
        date_write = (TextView) view.findViewById(R.id.notify_dial_date_writer);
        context = (TextView) view.findViewById(R.id.notify_dial_context);
        completeBTN = (Button) view.findViewById(R.id.notify_dial_completeBTN);
        completeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        title.setText(item.getTItle());
        date_write.setText(item.getDate()+" ("+item.getWho()+")");
        context.setText(item.getContext());
    }


}
