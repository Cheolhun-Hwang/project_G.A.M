package com.hchooney.qewqs.gam.Dialog.RecyclerList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hchooney.qewqs.gam.R;

/**
 * Created by hooney on 2017. 11. 28..
 */

public class JoinEventHolder extends RecyclerView.ViewHolder {
    public TextView etitle;
    public TextView eprofit;
    public TextView ecordination;
    public TextView edeadline;
    public TextView enumber;
    public ImageView eisResult;

    public JoinEventHolder(View itemView) {
        super(itemView);

        etitle = (TextView) itemView.findViewById(R.id.je_dialog_ename);
        eprofit = (TextView) itemView.findViewById(R.id.je_dialog_eprofit);
        ecordination = (TextView) itemView.findViewById(R.id.je_dialog_ecordination);
        edeadline = (TextView) itemView.findViewById(R.id.je_dialog_edeadline);
        enumber = (TextView) itemView.findViewById(R.id.je_dialog_num);
        eisResult = (ImageView) itemView.findViewById(R.id.je_dialog_isResult);
    }
}
