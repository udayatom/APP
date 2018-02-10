package com.uday.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uday.R;

import java.util.ArrayList;

/**
 * Created by uday on 10/2/18.
 */

public class ParallaxAdapter extends
        RecyclerView.Adapter<ParallaxAdapter.DemoViewHolder> {
    private ArrayList<String> arrayList;
    private Context context;


    public ParallaxAdapter(Context context,
                                ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    public class DemoViewHolder  extends RecyclerView.ViewHolder {


        public TextView title;


        public DemoViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.cardTitle);
        }
    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(DemoViewHolder holder,
                                 int position) {


        final DemoViewHolder mainHolder = (DemoViewHolder) holder;
        //Setting text over textview
        mainHolder.title.setText(arrayList.get(position));

    }

    @Override
    public DemoViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_row, viewGroup, false);
        DemoViewHolder mainHolder = new DemoViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };


        return mainHolder;

    }


}