package com.hchooney.qewqs.gam.Dialog.RecyclerList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hchooney.qewqs.gam.R;

/**
 * Created by hooney on 2017. 11. 28..
 */

public class WarningHolder extends RecyclerView.ViewHolder {
    public TextView wdate;
    public TextView wcontext;


    public WarningHolder(View itemView) {
        super(itemView);

        wdate = (TextView) itemView.findViewById(R.id.warning_dialog_date);
        wcontext = (TextView) itemView.findViewById(R.id.warning_dialog_context);


    }
}
