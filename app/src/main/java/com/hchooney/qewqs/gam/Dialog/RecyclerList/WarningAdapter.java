package com.hchooney.qewqs.gam.Dialog.RecyclerList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hchooney.qewqs.gam.Dialog.items.WarningItem;
import com.hchooney.qewqs.gam.R;

import java.util.ArrayList;

/**
 * Created by hooney on 2017. 11. 28..
 */

public class WarningAdapter extends RecyclerView.Adapter {
    private ArrayList<WarningItem> list;
    private Context context;

    private int lastPosition = -1;

    public WarningAdapter(ArrayList<WarningItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_warning,parent,false);
        WarningHolder holder = new WarningHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WarningHolder hold = (WarningHolder) holder;

        hold.wdate.setText(list.get(position).getWWhen());
        hold.wcontext.setText(list.get(position).getWWhy());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // 새로 보여지는 뷰라면 애니메이션을 해줍니다
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
