package com.hchooney.qewqs.gam;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.hchooney.qewqs.gam.Dialog.AgreeEventFragment;

import java.io.FileNotFoundException;
import java.io.IOException;

public class JoinEventActivity extends AppCompatActivity {
    final static int SIGNAL_toGallery = 4004;

    private ImageButton Back;
    private ImageView addGalley;

    private AgreeEventFragment agreeEventFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);

        init();
        showAgree();


    }

    private void init(){


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



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGNAL_toGallery){
            if(resultCode == Activity.RESULT_OK){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(data.getData()+""));

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
