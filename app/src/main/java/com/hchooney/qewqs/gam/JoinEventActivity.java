package com.hchooney.qewqs.gam;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hchooney.qewqs.gam.Database.Account;
import com.hchooney.qewqs.gam.Dialog.AgreeEventFragment;
import com.hchooney.qewqs.gam.Net.SendMultiPartformImage;
import com.hchooney.qewqs.gam.Net.SendPostReq;
import com.hchooney.qewqs.gam.RecyclerList.Event.EventItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class JoinEventActivity extends AppCompatActivity {
    final static int SIGNAL_toGallery = 4004;

    private ImageButton Back;
    private ImageView addGalley;
    private Button Join_CompleteBTN;
    private EditText Join_Teams;
    private EditText Join_Title;
    private Button Join_teams_search;
    private TextView joinEvent_showTeam_Textview;

    private EventItem eventItem;
    private Account user;

    private ArrayList<String> userList;

    private AgreeEventFragment agreeEventFragment;
    private Handler handler;

    private Uri uri;
    private String path;

    private Bitmap saveBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);

        init();
        showAgree();


    }

    private void init(){
        user = (Account)getIntent().getSerializableExtra("user");
        eventItem = (EventItem)getIntent().getSerializableExtra("event");
        if(user == null || eventItem==null){
            Toast.makeText(getApplicationContext(), "정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        userList =new ArrayList<String>();


        Back = (ImageButton) findViewById(R.id.Join_arrowBack);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        addGalley = (ImageView) findViewById(R.id.Join_gellay_photo);
        addGalley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SIGNAL_toGallery);
            }
        });

        Join_Title = (EditText) findViewById(R.id.Join_Title);
        Join_CompleteBTN = (Button) findViewById(R.id.Join_CompleteBTN);
        Join_CompleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Send Server", "Send Image Post END JOIN");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", Locale.KOREA );
                        Date currentTime = new Date( );
                        String dTime = formatter.format ( currentTime );
                        Log.d("Time", "Current Time : " + dTime);

                        File file = new File(uri.getPath());
                        final String result = new SendMultiPartformImage("event/join").
                                uploadFile(file,
                                            eventItem.getEid()+"_"+dTime.replaceAll(" ", "-")+"_T_"+user.getUid(),
                                            user.getUid(),
                                            eventItem.getEid(),
                                            dTime,
                                            Join_Title.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("UPLOAD RES", "IMAGE RES : " + result);
                            }
                        });
                    }
                }).start();
            }
        });

        Join_Teams = (EditText)findViewById(R.id.Join_Teams);
        joinEvent_showTeam_Textview = (TextView) findViewById(R.id.joinEvent_showTeam_Textview);
        Join_teams_search = (Button) findViewById(R.id.Join_teams_search);
        Join_teams_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTeams();
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 1){
                    joinEvent_showTeam_Textview.setText("");
                    String temp = "";
                    for(int i=0;i<userList.size();i++){
                        temp+="# "+userList.get(i)+"\n";
                    }
                    if(eventItem.geteNum() > userList.size()){
                        temp+="팀원을 추가해주세요.";
                    }
                    joinEvent_showTeam_Textview.setText(temp);
                }else if(msg.what==2){

                }
                return true;
            }
        });

    }

    private void searchTeams(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject user_data = null;
                try {
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("uemail", Join_Teams.getText().toString());
                    postDataParams.put("gpsx", eventItem.geteGpsy());
                    postDataParams.put("gpsy", eventItem.geteGpsx());
                    Log.e("params",postDataParams.toString());
                    String res_user = new SendPostReq("event/addteam", postDataParams).post();
                    Log.d("Res NOTICE", "RESULT : " + res_user);

                    user_data = new JSONObject(res_user);
                    JSONArray userArray = user_data.getJSONArray("add");
                    for (int i=0;i<userArray.length();i++){
                        JSONObject obj = (JSONObject) userArray.get(i);
                        userList.add(obj.get("UNAME").toString());
                    }

                    // 메시지 얻어오기
                    Message msg = handler.obtainMessage();
                    // 메시지 ID 설정
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGNAL_toGallery){
            if(resultCode == Activity.RESULT_OK){
                try {
                    Log.d("Galley Image URL", "URL : " + data.getData());
                    uri = Uri.parse(data.getData()+"");
                    path = data.getDataString();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);

                    //리사이즈
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();

                    Bitmap resized = null;

                    //높이가 800이상 일때
                    while (height > 200) {
                        resized = Bitmap.createScaledBitmap(bitmap, (width * 200) / height, 200, true);
                        height = resized.getHeight();
                        width = resized.getWidth();
                    }

                    saveBitmap = resized;
                    //배치해놓은 ImageView에 set
                    addGalley.setImageBitmap(resized);

                    //imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    addGalley.setImageResource(R.drawable.no_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{
                //실패
            }
        }else{

        }
    }


    private void showAgree(){
        AgreeEventFragment fragment = new AgreeEventFragment();

        fragment.show(getSupportFragmentManager(), "AgreeEventFragment");
    }

    public void disAgree(){
        this.finish();
    }
}
