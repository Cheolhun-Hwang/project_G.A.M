package com.hchooney.qewqs.gam.RecyclerList.Guide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hchooney.qewqs.gam.DetailGuideActivity;
import com.hchooney.qewqs.gam.MainActivity;
import com.hchooney.qewqs.gam.R;
import com.hchooney.qewqs.gam.RecyclerList.Notify.NotifyHolder;
import com.hchooney.qewqs.gam.RecyclerList.Notify.NotifyItem;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by qewqs on 2017-11-21.
 */

public class GuideAdapter extends RecyclerView.Adapter {
    private ArrayList<GuideItem> list;
    private Context context;

    private Bitmap bitmap=null;

    private GuideHolder hold;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public GuideAdapter(ArrayList<GuideItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 새로운 뷰를 만든다
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide,parent,false);
        GuideHolder holder = new GuideHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        hold = (GuideHolder) holder;
        hold.guide_title.setText(list.get(position).getSpot());

        Picasso.with(context).load("http://203.249.127.32:64001/mobile/search/guide/images?gid="+list.get(position).getGid()).into(hold.guide_image);



        hold.guide_image.setBackground(new ShapeDrawable(new OvalShape()));
        hold.guide_image.setClipToOutline(true);


        hold.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailGuideActivity.class);
                intent.putExtra("guideitem", list.get(position));
                view.getContext().startActivity(intent);
            }
        });

        setAnimation(hold.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // 새로 보여지는 뷰라면 애니메이션을 해줍니다
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
