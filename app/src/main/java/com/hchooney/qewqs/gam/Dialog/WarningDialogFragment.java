package com.hchooney.qewqs.gam.Dialog;


import android.app.Dialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hchooney.qewqs.gam.Dialog.RecyclerList.WarningAdapter;
import com.hchooney.qewqs.gam.Dialog.items.WarningItem;
import com.hchooney.qewqs.gam.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WarningDialogFragment extends DialogFragment {
    private Dialog dialog;
    private View view;

    private ArrayList<WarningItem> list;
    private RecyclerView warningview;

    public WarningDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_warning_dialog, null);
        dialog.setContentView(view);

        dataLoad();
        init();

        return dialog;
    }

    private void init(){
        warningview = (RecyclerView) view.findViewById(R.id.dialog_warning_recyclerview);
        warningview.setHasFixedSize(true);

        warningview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        warningview.setAdapter(new WarningAdapter(list, getContext()));
    }

    private void dataLoad(){
        list = new ArrayList<WarningItem>();

        //임시
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
    }

}
