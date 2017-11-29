package com.hchooney.qewqs.gam.Dialog.RecyclerList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hchooney.qewqs.gam.Dialog.items.JoinEventItem;
import com.hchooney.qewqs.gam.R;

import java.util.ArrayList;

/**
 * Created by hooney on 2017. 11. 28..
 */

public class JoinEventAdapter extends RecyclerView.Adapter {

    private ArrayList<JoinEventItem> list;
    private Context context;

    private int lastPosition = -1;

    public JoinEventAdapter(ArrayList<JoinEventItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joinevent,parent,false);
        JoinEventHolder holder = new JoinEventHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        JoinEventHolder hold = (JoinEventHolder) holder;

        hold.etitle.setText("이벤트 : " + list.get(position).getETitle());
        hold.eprofit.setText("혜택 : "+list.get(position).getEProfit());
        hold.edeadline.setText("기간 : " + list.get(position).getEDeadline());
        hold.ecordination.setText("조건 : "+ list.get(position).getECordination());
        hold.enumber.setText("인원 : "+list.get(position).getENum());

        if(list.get(position).isResult()){
            hold.eisResult.setImageResource(R.drawable.ic_warning_ok);
        }else{
            hold.eisResult.setImageResource(R.drawable.ic_warning_x);
        }


        setAnimation(hold.itemView, position);
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
