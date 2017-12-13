package com.hchooney.qewqs.gam.Dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hchooney.qewqs.gam.Database.Account;
import com.hchooney.qewqs.gam.Net.SendPostReq;
import com.hchooney.qewqs.gam.Net.SendPutReq;
import com.hchooney.qewqs.gam.R;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideAdapter;
import com.hchooney.qewqs.gam.RecyclerList.Notify.NotifyItem;
import com.hchooney.qewqs.gam.SettingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyNickFragment extends DialogFragment {
    private Dialog dialog;
    private View view;

    private EditText nickEdit;
    private TextView charCount;
    private Button sendPost;

    private Handler handler;

    private String nowNick;

    private Account account;

    public ModifyNickFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_modify_nick, null);
        dialog.setContentView(view);

        init();


        return dialog;
    }

    private void init(){
        account = (Account) getArguments().getSerializable("user");
        if(account==null){
            Toast.makeText(getContext(), "정보가 없습니다.\n다시 시도해주세요.", Toast.LENGTH_LONG).show();
            dismiss();
        }

        nickEdit = (EditText) view.findViewById(R.id.Setting_Nickname_edittext);
        nickEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = nickEdit.getText().length();
                if(length>10){
                    charCount.setTextColor(Color.RED);
                }else{
                    charCount.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = nickEdit.getText().length();
                charCount.setText(length+"/10");
            }
        });
        charCount = (TextView) view.findViewById(R.id.Setting_Nickname_charcount);
        sendPost = (Button) view.findViewById(R.id.Setting_Nickname_sendPost);
        sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkNick()){
                    if(nickEdit.getText().length()<=10){
                        nowNick = nickEdit.getText().toString();
                        Thread t= new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject postDataParams = new JSONObject();
                                    postDataParams.put("nick", nowNick);
                                    postDataParams.put("uid", account.getUid());
                                    SendPutReq sendPutReq = new SendPutReq("set/nic", postDataParams);
                                    String result=sendPutReq.put();
                                    if(result.equals("OK")){
                                        Message msg = handler.obtainMessage();
                                        // 메시지 ID 설정
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                    }else{
                                        Message msg = handler.obtainMessage();
                                        // 메시지 ID 설정
                                        msg.what = 2;
                                        handler.sendMessage(msg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        t.start();
                    }else{
                        Toast.makeText(getContext(), "조건을 만족하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==1){
                    ((SettingActivity)getActivity()).changeNickname(nowNick);
                    Toast.makeText(getContext(), "수정완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }else if(msg.what==2){
                    Toast.makeText(getContext(), "수정이 완료되지 않았습니다.\n잠시 후 다시 시도해주십시오.", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
    }

    private boolean checkNick(){
        String temp = nickEdit.getText().toString();
        if(!temp.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")){
            Toast.makeText(getContext(), "특수문자가 포함되어 있는지 확인해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }


}
