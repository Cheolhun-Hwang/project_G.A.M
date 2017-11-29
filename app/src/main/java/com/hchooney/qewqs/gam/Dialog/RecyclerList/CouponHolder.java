package com.hchooney.qewqs.gam.Dialog.RecyclerList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hchooney.qewqs.gam.R;

/**
 * Created by qewqs on 2017-11-27.
 */

public class CouponHolder extends RecyclerView.ViewHolder {
    public TextView Title;


    public CouponHolder(View itemView) {
        super(itemView);

        Title = (TextView) itemView.findViewById(R.id.item_coupon_title);
    }
}
