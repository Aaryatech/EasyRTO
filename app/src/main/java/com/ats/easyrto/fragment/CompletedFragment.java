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
import com.ats.easyrto.interfaces.CompletedInterface;

import static com.ats.easyrto.activity.OrderStatusActivity.staticCompletedWorkList;

public class CompletedFragment extends Fragment implements CompletedInterface {

    private RecyclerView recyclerView;
    WorkStatusAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        adapter = new WorkStatusAdapter(staticCompletedWorkList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void fragmentBecameVisible() {

        Log.e("COMPLETED ARRAY : ", " " + staticCompletedWorkList);
        adapter.notifyDataSetChanged();

    }
}
