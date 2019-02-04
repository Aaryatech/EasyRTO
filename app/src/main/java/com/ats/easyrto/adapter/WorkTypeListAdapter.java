package com.ats.easyrto.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.easyrto.R;
import com.ats.easyrto.activity.AttachmentActivity;
import com.ats.easyrto.model.WorkTypeModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkTypeListAdapter extends BaseAdapter {

    ArrayList<WorkTypeModel> displayedValues;
    ArrayList<WorkTypeModel> originalValues;
    Context context;
    private static LayoutInflater inflater = null;

    public WorkTypeListAdapter(ArrayList<WorkTypeModel> displayedValues, Context context) {
        this.displayedValues = displayedValues;
        this.originalValues = displayedValues;
        this.context = context;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return displayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return displayedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class Holder {
        TextView tvName;
        ImageView imageView;
        CardView cardView;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.adapter_work_type_list, null);

            holder.imageView = rowView.findViewById(R.id.imageView);
            holder.tvName = rowView.findViewById(R.id.tvName);
            holder.cardView = rowView.findViewById(R.id.cardView);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

        if (position == 0) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
        } else if (position == 1) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color2));
        } else if (position == 2) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color3));
        } else if (position == 3) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color4));
        } else if (position == 4) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color2));
        } else if (position == 5) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
        }


        holder.tvName.setText("" + displayedValues.get(position).getWorkTypeName());

        try {
            Picasso.get().load("" + displayedValues.get(position).getExStr1())
                    .into(holder.imageView);
        } catch (Exception e) {
            Log.e("IMAGE : ", "------------------ " + e.getMessage());
            e.printStackTrace();
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                String json = gson.toJson(displayedValues.get(position));

                Intent intent = new Intent(context, AttachmentActivity.class);
                intent.putExtra("json", json);
                context.startActivity(intent);
            }
        });


        return rowView;
    }


}
