package com.hchooney.qewqs.gam.CustomItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hchooney.qewqs.gam.R;

import java.util.ArrayList;

/**
 * Created by qewqs on 2017-11-22.
 */

public class SpinnerAdapter01 extends BaseAdapter {

    private Context context;
    ArrayList<String> list;
    LayoutInflater inflater;

    public SpinnerAdapter01(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        String text = list.get(position);
        ((TextView)convertView.findViewById(R.id.Map_SpinnerItemText)).setText(text);
        return convertView;
    }
}
