package com.hchooney.qewqs.gam;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hchooney.qewqs.gam.Database.Account;
import com.hchooney.qewqs.gam.Dialog.netWaitDailog;
import com.hchooney.qewqs.gam.Net.SendGet;
import com.hchooney.qewqs.gam.Net.SendPost;
import com.hchooney.qewqs.gam.Net.SendPostReq;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_SIGN_IN = 9001;
    private final static String TAG = "LoginActivity";
    private ImageView LogoImage;
    private SignInButton googleSign;

    private Thread Loading;
    private Handler handler;
    private Account account;

    private GoogleApiClient mGoogleApiClient;

    private netWaitDailog netWaitDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        checkDangerousPermissions();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        this.startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Must be handled in Activity. When intent is started from fragment, requestCode is modified.
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct= result.getSignInAccount();
            account = new Account();
            account.setUid(acct.getId());
            Log.d(TAG, "My UID : " + acct.getId());
            account.setUemail(acct.getEmail());
            account.setUname(acct.getDisplayName());
            account.setUnickname(acct.getDisplayName());
            account.setUisapp((short) 1);
            MainActivity.setUser(account);
            MainActivity.setmGoogleApiClient(this.mGoogleApiClient);

            netWaitDailog.show(getSupportFragmentManager(), "Net Dailog");

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String recieve = new SendGet("login/checkuid", "?uid="+account.getUid()).SendGet().replaceAll(" ", "");
                        Log.d("Login Activity", recieve);

                        // 메시지 얻어오기
                        Message msg = handler.obtainMessage();

                        // 메시지 ID 설정
                        msg.what = 3;
                        msg.obj = recieve;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    netWaitDailog.dismiss();
                }
            });

            t.start();

        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        startAnim();
        super.onStart();
        //Loading.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void init(){
        LogoImage = (ImageView) findViewById(R.id.Login_Logo_Img);
        googleSign = (SignInButton) findViewById(R.id.Login_googleAuthBTN);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        googleSign.startAnimation(fadeInAnimation);
        googleSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        netWaitDailog = com.hchooney.qewqs.gam.Dialog.netWaitDailog.newInstance();
        netWaitDailog.setTitle("로그인");
        netWaitDailog.setMessage("로그인 정보를 확인하고 있습니다");

        Loading = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Message msg = Message.obtain();
                    msg.what =1;
                    handler.sendMessage(msg);

                    Message msg2 = Message.obtain();
                    msg2.what =2;
                    handler.sendMessage(msg2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 1){
                    translateAnim(LogoImage.getX(), LogoImage.getX(), LogoImage.getY(), (float) ( LogoImage.getY()-0.8), 8000, LogoImage);
                }else if(msg.what == 2){
                    googleSign.setVisibility(View.VISIBLE);
                }else if(msg.what == 3){
                    String resString = (String)msg.obj;
                    int parseint = Integer.parseInt(resString.replace("\n", ""));

                    Log.d("Handler", "Recieve : " + parseint);
                    if(parseint == 1){
                        Log.d("Handler", "equals True");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(parseint == 0){
                        Toast.makeText(getApplicationContext(), "현 계정은 제재 대상입니다\n더 이상 서비스를 이용하실 수 없습니다]\n관련사항은 문의주시기 바랍니다", Toast.LENGTH_LONG).show();
                    }else if(parseint == 2){

                        netWaitDailog.setTitle("계정 정보 생성");
                        netWaitDailog.setMessage("첫 로그인으로 인한 계정 정보 생성 중 입니다");
                        netWaitDailog.show(getSupportFragmentManager(), "Net Dailog");
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject postDataParams = new JSONObject();
                                    postDataParams.put("uid", "0001");
                                    postDataParams.put("uname", account.getUname());
                                    postDataParams.put("uemail", account.getUemail());
                                    Log.e("params",postDataParams.toString());
                                    new SendPostReq("login/create", postDataParams).post();



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                netWaitDailog.dismiss();
                            }
                        });

                        t.start();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                return true;
            }
        });
    }

    private void startAnim(){
        translateAnim(LogoImage.getX(), LogoImage.getX(), LogoImage.getY(), (float) ( LogoImage.getY()-0.8), 2000, LogoImage);
        googleSign.setVisibility(View.VISIBLE);
    }
    private void translateAnim(float xStart, float xEnd, float yStart, float yEnd, int duration, ImageView imageView) {

        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,xStart,
                Animation.RELATIVE_TO_SELF, xEnd,
                Animation.RELATIVE_TO_SELF,yStart,
                Animation.RELATIVE_TO_SELF,yEnd);

        translateAnimation.setDuration(duration);

        imageView.startAnimation(translateAnimation);
        translateAnimation.setFillAfter(true);

    }

    /* 사용자 권한 확인 메서드
       - import android.Manifest; 를 시킬 것
     */
    private void checkDangerousPermissions() {
        String[] permissions = {//import android.Manifest;
                android.Manifest.permission.ACCESS_FINE_LOCATION,   //GPS 이용권한
                android.Manifest.permission.ACCESS_COARSE_LOCATION, //네트워크/Wifi 이용 권한
                android.Manifest.permission.READ_EXTERNAL_STORAGE,  //읽기 권한
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,  //쓰기 권한
                android.Manifest.permission.INTERNET                 //인터넷 사용 권한
        };

        //권한을 가지고 있는지 체크
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "권한있음");
        } else {
            Log.d(TAG, "권한없음");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Log.d(TAG, "권한설명란");
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }//end of checkDangerousPermissions

    // 사용자의 권한 확인 후 사용자의 권한에 대한 응답 결과를 확인하는 콜백 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getApplicationContext(), permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "권한 승인");
                } else {
                    //Toast.makeText(getApplicationContext(), permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "권한 승인되지 않음.");
                }
            }
        }
    }//end of onRequestPermissionsResult
}
