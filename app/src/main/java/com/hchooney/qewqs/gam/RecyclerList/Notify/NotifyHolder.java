package com.hchooney.qewqs.gam.RecyclerList.Notify;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hchooney.qewqs.gam.R;

import org.w3c.dom.Text;

/**
 * Created by qewqs on 2017-11-21.
 */

public class NotifyHolder extends RecyclerView.ViewHolder {
    public TextView Nid;
    public TextView NTitle;
    public TextView NDate;


    public NotifyHolder(View itemView) {
        super(itemView);

        Nid = (TextView) itemView.findViewById(R.id.notify_num);
        NTitle = (TextView) itemView.findViewById(R.id.notify_title);
        NDate = (TextView) itemView.findViewById(R.id.notify_date);
    }
}
