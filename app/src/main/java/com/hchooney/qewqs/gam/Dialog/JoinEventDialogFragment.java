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
import com.hchooney.qewqs.gam.Dialog.RecyclerList.JoinEventAdapter;
import com.hchooney.qewqs.gam.Dialog.items.JoinEventItem;
import com.hchooney.qewqs.gam.Dialog.items.WarningItem;
import com.hchooney.qewqs.gam.MainActivity;
import com.hchooney.qewqs.gam.Net.SendGet;
import com.hchooney.qewqs.gam.R;
import com.hchooney.qewqs.gam.RecyclerList.Notify.NotifyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinEventDialogFragment extends DialogFragment {
    private Dialog dialog;
    private View view;

    private RecyclerView jeview;
    private Account account;

    private ArrayList<JoinEventItem> list;
    private netWaitDailog netWaitDailog;

    private Handler handler;


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

        jeview = (RecyclerView) view.findViewById(R.id.Dial_joinlist_recyclerview);
        jeview.setHasFixedSize(true);

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
        jeview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        jeview.setAdapter(new JoinEventAdapter(list, getContext()));
    }

    private void dataLoad(){
        list = new ArrayList<JoinEventItem>();

        //임시
        /*list.add(new JoinEventItem("임시 이벤트이름", "2017.00.00 00:00", "2017.00.00 00:00", "이벤트 임시 조건 내용", 0, "이벤트 혜택 내용", true));
        list.add(new JoinEventItem("임시 이벤트이름", "2017.00.00 00:00", "2017.00.00 00:00", "이벤트 임시 조건 내용", 0, "이벤트 혜택 내용", false));
        list.add(new JoinEventItem("임시 이벤트이름", "2017.00.00 00:00", "2017.00.00 00:00", "이벤트 임시 조건 내용", 0, "이벤트 혜택 내용", true));
        list.add(new JoinEventItem("임시 이벤트이름", "2017.00.00 00:00", "2017.00.00 00:00", "이벤트 임시 조건 내용", 0, "이벤트 혜택 내용", false));
        list.add(new JoinEventItem("임시 이벤트이름", "2017.00.00 00:00", "2017.00.00 00:00", "이벤트 임시 조건 내용", 0, "이벤트 혜택 내용", true));*/

        String res_join = new SendGet("set/joinlist", ("?uid="+account.getUid())).SendGet();
        Log.d("Res NOTICE", "RESULT : " + res_join);

        JSONObject coupon_data = null;
        try {
            coupon_data = new JSONObject(res_join);
            JSONArray jeArray = coupon_data.getJSONArray("join");
            for (int i=0;i<jeArray.length();i++){
                JSONObject obj = (JSONObject) jeArray.get(i);
                JoinEventItem item = new JoinEventItem();
                item.setETitle(((String) obj.get("ENAME")));
                item.setEDeadline((String) obj.get("EDEADLINE").toString().substring(0, 10));
                item.setENum(Integer.parseInt(obj.get("ENUM").toString()));
                item.setECordination((String) obj.get("ECORDINATION"));
                item.setEProfit((String) (obj.get("ECORDINATION")+""));
                if(Integer.parseInt(obj.get("JRESULT").toString())==1){
                    item.setResult(true);
                }else{
                    item.setResult(false);
                }
                list.add(0, item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
