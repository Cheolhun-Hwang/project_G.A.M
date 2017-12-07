package com.hchooney.qewqs.gam.RecyclerList.Event;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hchooney.qewqs.gam.Database.Account;
import com.hchooney.qewqs.gam.DetailEventActivity;
import com.hchooney.qewqs.gam.R;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideHolder;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by qewqs on 2017-11-21.
 */

public class EventAdapter extends RecyclerView.Adapter {
    private ArrayList<EventItem> list;
    private Context context;
    private Account account;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public EventAdapter(ArrayList<EventItem> list, Context context, Account ac) {
        this.list = list;
        this.context = context;
        this.account = ac;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event,parent,false);
        EventHolder holder = new EventHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        EventHolder hold = (EventHolder) holder;
        hold.Eid.setText((position+1)+".");
        hold.EName.setText(list.get(position).geteName());
        hold.ELimit.setText("~ " + list.get(position).geteLimitDate());
        //hold.EImage.setImageBitmap();

        Log.d("EVENT URL", "http://203.249.127.32:64001/mobile/search/event/images?spot="+list.get(position).geteSpot());
        Picasso.with(context).load("http://203.249.127.32:64001/mobile/search/event/images?eid="+list.get(position).getEid()).into(hold.EImage);


        hold.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailEventActivity.class);
                Log.d("Guide Adapter", "VIew ID : " + view.getId());
                intent.putExtra("Event", list.get(position));
                intent.putExtra("user", account);
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
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
