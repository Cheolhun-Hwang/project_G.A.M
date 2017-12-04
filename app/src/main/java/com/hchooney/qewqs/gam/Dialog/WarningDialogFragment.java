package com.hchooney.qewqs.gam.Dialog;


import android.app.Dialog;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hchooney.qewqs.gam.Database.Account;
import com.hchooney.qewqs.gam.Dialog.RecyclerList.WarningAdapter;
import com.hchooney.qewqs.gam.Dialog.items.JoinEventItem;
import com.hchooney.qewqs.gam.Dialog.items.WarningItem;
import com.hchooney.qewqs.gam.Net.SendGet;
import com.hchooney.qewqs.gam.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WarningDialogFragment extends DialogFragment {
    private Dialog dialog;
    private View view;

    private ArrayList<WarningItem> list;
    private RecyclerView warningview;



    private Account account;

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

        init();

        setList();



        return dialog;
    }

    private void init(){
        account = (Account) getArguments().getSerializable("user");
        list =  getArguments().getParcelableArrayList("list");

        warningview = (RecyclerView) view.findViewById(R.id.dialog_warning_recyclerview);
        warningview.setHasFixedSize(true);




    }

    private void setList(){
        warningview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        warningview.setAdapter(new WarningAdapter(list, getContext()));
    }

}
