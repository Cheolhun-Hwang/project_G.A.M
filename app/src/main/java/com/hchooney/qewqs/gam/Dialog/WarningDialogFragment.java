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

    private Handler handler;
    private netWaitDailog netWaitDailog;

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

        netWaitDailog.show(getActivity().getSupportFragmentManager(), "Net Dailog");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    dataLoad();
                    // 메시지 얻어오기
                    Message msg = handler.obtainMessage();
                    // 메시지 ID 설정
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                netWaitDailog.dismiss();
            }
        });

        t.start();

        return dialog;
    }

    private void init(){
        account = (Account) getArguments().getSerializable("user");

        netWaitDailog = com.hchooney.qewqs.gam.Dialog.netWaitDailog.newInstance();
        netWaitDailog.setMessage("서버에서 서비스 정보를 받아오는 중입니다.");
        netWaitDailog.setTitle("정보 받아 오는 중");


        warningview = (RecyclerView) view.findViewById(R.id.dialog_warning_recyclerview);
        warningview.setHasFixedSize(true);



        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what ==1){
                    setList();
                }

                return true;
            }
        });
    }

    private void setList(){
        warningview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        warningview.setAdapter(new WarningAdapter(list, getContext()));
    }

    private void dataLoad(){
        list = new ArrayList<WarningItem>();

        //임시
        /*list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));
        list.add(new WarningItem("2017.00.00 00:00", "임시 계정 경고 내역 사항입니다"));*/
        String res_warning = new SendGet("set/warning", ("?uid="+account.getUid())).SendGet();
        Log.d("Res NOTICE", "RESULT : " + res_warning);

        JSONObject warning_data = null;
        try {
            warning_data = new JSONObject(res_warning);
            JSONArray warningArray = warning_data.getJSONArray("warning");
            for (int i=0;i<warningArray.length();i++){
                JSONObject obj = (JSONObject) warningArray.get(i);
                WarningItem item = new WarningItem();
                item.setWWhy(((String) obj.get("WWHY")));
                item.setWWhen((String) obj.get("WWHEN").toString().substring(0, 10));
                list.add(0, item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
