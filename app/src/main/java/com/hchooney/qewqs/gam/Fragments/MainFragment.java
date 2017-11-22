package com.hchooney.qewqs.gam.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hchooney.qewqs.gam.MainActivity;
import com.hchooney.qewqs.gam.R;
import com.hchooney.qewqs.gam.RecyclerList.Event.EventAdapter;
import com.hchooney.qewqs.gam.RecyclerList.Event.EventItem;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideAdapter;
import com.hchooney.qewqs.gam.RecyclerList.Guide.GuideItem;
import com.hchooney.qewqs.gam.RecyclerList.Notify.NotifyAdapter;
import com.hchooney.qewqs.gam.RecyclerList.Notify.NotifyItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private View v;

    private static ArrayList<NotifyItem> notifylist;
    private static ArrayList<GuideItem> guidelist;
    private static ArrayList<EventItem> eventlist;

    private RecyclerView notify;
    private RecyclerView guide;
    private RecyclerView event;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_main, container, false);

        init();
        ListSetup();

        return v;
    }

    private void init(){

        notifylist = ((MainActivity)getActivity()).getNotifylist();
        guidelist = ((MainActivity)getActivity()).getGuidelist();
        eventlist = ((MainActivity)getActivity()).getEventlist();

        notify = (RecyclerView) v.findViewById(R.id.Main_Notify_ListView);
        notify.setHasFixedSize(true);
        guide = (RecyclerView) v.findViewById(R.id.Main_Guide_ListView);
        guide.setHasFixedSize(true);
        event = (RecyclerView) v.findViewById(R.id.Main_Event_ListView);
        event.setHasFixedSize(true);
    }

    private void ListSetup(){
        notify.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        notify.setAdapter(new NotifyAdapter(notifylist, getContext()));

        guide.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        guide.setAdapter(new GuideAdapter(guidelist, getContext()));

        event.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        event.setAdapter(new EventAdapter(eventlist, getContext()));
    }

}
