package com.hchooney.qewqs.gam.RecyclerList.Notify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hchooney.qewqs.gam.R;

import java.util.ArrayList;

/**
 * Created by qewqs on 2017-11-21.
 */

public class NotifyAdapter extends RecyclerView.Adapter{
    private ArrayList<NotifyItem> list;
    private Context context;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public NotifyAdapter(ArrayList<NotifyItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 새로운 뷰를 만든다
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify,parent,false);
        NotifyHolder holder = new NotifyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NotifyHolder hold = (NotifyHolder) holder;
        hold.Nid.setText(list.get(position).getNid());
        hold.NTitle.setText(list.get(position).getTItle());
        hold.NDate.setText(list.get(position).getDate());

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
