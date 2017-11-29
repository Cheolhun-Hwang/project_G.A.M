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
import android.widget.Toast;

import com.hchooney.qewqs.gam.Dialog.RecyclerList.CouponAdapter;
import com.hchooney.qewqs.gam.Dialog.RecyclerList.CouponItem;
import com.hchooney.qewqs.gam.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CouponDialogFragment extends DialogFragment {
    private Dialog dialog;
    private View view;

    private ArrayList<CouponItem> list;

    private RecyclerView couponview;

    public CouponDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_coupon_dialog, null);
        dialog.setContentView(view);

        list = (ArrayList<CouponItem>) getArguments().getSerializable("items");
        if(list == null){
            Toast.makeText(getContext(), "쿠폰 정보가 올바르지 않습니다.\n다시 시도해주십시오.", Toast.LENGTH_LONG).show();
            dismiss();
        }

        init();
        setRecycler();

        return dialog;
    }

    private void init(){
        couponview = (RecyclerView) view.findViewById(R.id.dialog_coupon_recyclerview);
        couponview.setHasFixedSize(true);

    }

    private void setRecycler(){
        couponview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        couponview.setAdapter(new CouponAdapter(list, getContext()));
    }

}
