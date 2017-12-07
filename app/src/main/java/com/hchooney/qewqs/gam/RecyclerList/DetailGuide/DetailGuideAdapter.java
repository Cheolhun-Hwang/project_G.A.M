package com.hchooney.qewqs.gam.RecyclerList.DetailGuide;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hchooney.qewqs.gam.R;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by qewqs on 2017-11-22.
 */

public class DetailGuideAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<String> imagelist;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public DetailGuideAdapter(Context context, ArrayList<String> imagelist) {
        this.context = context;
        this.imagelist = imagelist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detailguide,parent,false);
        DetailGuideHolder holder = new DetailGuideHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DetailGuideHolder hold = (DetailGuideHolder) holder;

        Picasso.with(context).load("http://203.249.127.32:64001/mobile/search/dguide/images?"+imagelist.get(position)).into(hold.detailguideImage);
        Log.d("IMAGE URL", "http://203.249.127.32:64001/mobile/search/dguide/images?"+imagelist.get(position));
        //이미지 파트
        //hold.detailguideImage.setImageBitmap();

        setAnimation(hold.itemView, position);
    }

    @Override
    public int getItemCount() {
        return this.imagelist.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // 새로 보여지는 뷰라면 애니메이션을 해줍니다
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
