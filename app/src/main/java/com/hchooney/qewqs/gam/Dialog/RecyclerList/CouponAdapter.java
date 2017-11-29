package com.hchooney.qewqs.gam.Dialog.RecyclerList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hchooney.qewqs.gam.CouponActivity;
import com.hchooney.qewqs.gam.R;

import java.util.ArrayList;

/**
 * Created by hooney on 2017. 11. 28..
 */

public class CouponAdapter extends RecyclerView.Adapter {
    private ArrayList<CouponItem> list;
    private Context context;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;


    public CouponAdapter(ArrayList<CouponItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon,parent,false);
        CouponHolder holder = new CouponHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CouponHolder hold = (CouponHolder) holder;

        hold.Title.setText("이벤트 : \"" + list.get(position).getEName()+"\"");

        setAnimation(hold.itemView, position);

        hold.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CouponActivity.class);
                intent.putExtra("url", list.get(position).getCPhotourl());
                context.startActivity(intent);
            }
        });
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
