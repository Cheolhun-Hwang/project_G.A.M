package com.hchooney.qewqs.gam.RecyclerList.DetailGuide;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hchooney.qewqs.gam.R;

/**
 * Created by qewqs on 2017-11-22.
 */

public class DetailGuideHolder extends RecyclerView.ViewHolder {
    public ImageView detailguideImage;

    public DetailGuideHolder(View itemView) {
        super(itemView);

        detailguideImage = (ImageView) itemView.findViewById(R.id.detailguide_listitem_imageview);
    }
}
