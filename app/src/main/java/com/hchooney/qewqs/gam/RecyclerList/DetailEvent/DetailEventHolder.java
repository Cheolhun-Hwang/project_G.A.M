package com.hchooney.qewqs.gam.RecyclerList.DetailEvent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hchooney.qewqs.gam.R;

/**
 * Created by qewqs on 2017-11-22.
 */

public class DetailEventHolder extends RecyclerView.ViewHolder {
    public TextView jelimitdate;
    public TextView jetitle;
    public ImageView jephotoURL;
    public TextView jetag;

    public DetailEventHolder(View itemView) {
        super(itemView);

        jelimitdate = (TextView) itemView.findViewById(R.id.detailevent_limitdate);
        jetitle = (TextView) itemView.findViewById(R.id.detailevent_joinTitle);
        jephotoURL = (ImageView) itemView.findViewById(R.id.detailevent_joinphoto);
        jetag = (TextView) itemView.findViewById(R.id.detailevent_tagUser);
    }
}
