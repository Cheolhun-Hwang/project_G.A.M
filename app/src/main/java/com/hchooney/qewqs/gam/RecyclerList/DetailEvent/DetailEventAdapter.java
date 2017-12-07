package com.hchooney.qewqs.gam.RecyclerList.DetailEvent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hchooney.qewqs.gam.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by qewqs on 2017-11-22.
 */

public class DetailEventAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<DetailEventItem> list;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public DetailEventAdapter(Context context, ArrayList<DetailEventItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detailevent,parent,false);
        DetailEventHolder holder = new DetailEventHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DetailEventHolder hold = (DetailEventHolder) holder;

        hold.jetitle.setText(list.get(position).getTitle());
        hold.jelimitdate.setText(list.get(position).getLimitdate());

        //이미지부분
        //hold.jephotoURL.setImageBitmap();

        Log.d("EVENT URL", "http://203.249.127.32:64001/mobile/mobile/event/join/images?jeid="+list.get(position).getJeid());
        Picasso.with(context).load("http://203.249.127.32:64001/mobile/event/join/images?jeid="+list.get(position).getJeid()).into(hold.jephotoURL);

        String temp = "태그 : ";
        for(int i =0; i<list.get(position).getUids().size();i++){
            temp+="@" + list.get(position).getUids().get(i);
            if(i != list.get(position).getUids().size()-1.){
                temp += ", ";
            }
        }

        Log.d("UIDS" ,  "UIDS : " + temp);
        hold.jetag.setText(temp);

        setAnimation(hold.itemView, position);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // 새로 보여지는 뷰라면 애니메이션을 해줍니다
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_left_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
