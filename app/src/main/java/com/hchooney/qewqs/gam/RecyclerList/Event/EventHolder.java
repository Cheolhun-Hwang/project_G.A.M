package com.hchooney.qewqs.gam.RecyclerList.Event;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hchooney.qewqs.gam.R;

/**
 * Created by qewqs on 2017-11-21.
 */

public class EventHolder extends RecyclerView.ViewHolder {
    public TextView Eid;
    public TextView EName;
    public TextView ELimit;
    public ImageView EImage;

    public EventHolder(View itemView) {
        super(itemView);

        Eid = (TextView) itemView.findViewById(R.id.event_num);
        EName = (TextView) itemView.findViewById(R.id.event_name);
        ELimit = (TextView) itemView.findViewById(R.id.event_limit);
        EImage = (ImageView) itemView.findViewById(R.id.event_photo);
    }
}
