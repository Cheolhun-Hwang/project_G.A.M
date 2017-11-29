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

import com.hchooney.qewqs.gam.Dialog.RecyclerList.JoinEventAdapter;
import com.hchooney.qewqs.gam.Dialog.items.JoinEventItem;
import com.hchooney.qewqs.gam.Dialog.items.WarningItem;
import com.hchooney.qewqs.gam.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinEventDialogFragment extends DialogFragment {
    private Dialog dialog;
    private View view;

    private RecyclerView jeview;

    private ArrayList<JoinEventItem> list;

    public JoinEventDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_join_event_dialog, null);
        dialog.setContentView(view);


        dataLoad();
        init();


        return dialog;
    }

    private void init(){
        jeview = (RecyclerView) view.findViewById(R.id.Dial_joinlist_recyclerview);
        jeview.setHasFixedSize(true);

        jeview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        jeview.setAdapter(new JoinEventAdapter(list, getContext()));
    }

    private void dataLoad(){
        list = new ArrayList<JoinEventItem>();

        //임시
        list.add(new JoinEventItem("임시 이벤트이름", "2017.00.00 00:00", "2017.00.00 00:00", "이벤트 임시 조건 내용", 0, "이벤트 혜택 내용", true));
        list.add(new JoinEventItem("임시 이벤트이름", "2017.00.00 00:00", "2017.00.00 00:00", "이벤트 임시 조건 내용", 0, "이벤트 혜택 내용", false));
        list.add(new JoinEventItem("임시 이벤트이름", "2017.00.00 00:00", "2017.00.00 00:00", "이벤트 임시 조건 내용", 0, "이벤트 혜택 내용", true));
        list.add(new JoinEventItem("임시 이벤트이름", "2017.00.00 00:00", "2017.00.00 00:00", "이벤트 임시 조건 내용", 0, "이벤트 혜택 내용", false));
        list.add(new JoinEventItem("임시 이벤트이름", "2017.00.00 00:00", "2017.00.00 00:00", "이벤트 임시 조건 내용", 0, "이벤트 혜택 내용", true));
    }

}
