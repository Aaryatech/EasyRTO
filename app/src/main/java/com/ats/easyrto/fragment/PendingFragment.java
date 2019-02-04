package com.ats.easyrto.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ats.easyrto.R;
import com.ats.easyrto.adapter.WorkStatusAdapter;
import com.ats.easyrto.interfaces.PendingInterface;
import com.ats.easyrto.model.WorkHeader;

import java.util.ArrayList;

import static com.ats.easyrto.activity.OrderStatusActivity.staticPendingWorkList;
import static com.ats.easyrto.activity.OrderStatusActivity.staticWorkHeaderList;

public class PendingFragment extends Fragment implements PendingInterface {

    WorkStatusAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        adapter = new WorkStatusAdapter(staticPendingWorkList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void fragmentBecameVisible() {

        Log.e("PENDING ARRAY : ", " " + staticPendingWorkList);
        adapter.notifyDataSetChanged();

    }
}
