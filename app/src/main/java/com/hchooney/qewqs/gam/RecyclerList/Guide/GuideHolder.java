package com.hchooney.qewqs.gam.RecyclerList.Guide;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hchooney.qewqs.gam.R;

/**
 * Created by qewqs on 2017-11-21.
 */

public class GuideHolder extends RecyclerView.ViewHolder {
    public ImageView guide_image;
    public TextView guide_title;

    public GuideHolder(View itemView) {
        super(itemView);

        guide_image = (ImageView) itemView.findViewById(R.id.guide_image);
        guide_title = (TextView) itemView.findViewById(R.id.guide_title);
    }
}
