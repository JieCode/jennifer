package com.jennifer.jennifer.ui.radio.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jennifer.jennifer.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/23.
 */

public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> firstList;
    private ArrayList<String> secondList;

    public FirstAdapter(Context context, ArrayList<String> firstList, ArrayList<String> secondList) {
        this.context = context;
        this.firstList = firstList;
        this.secondList = secondList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_radio, null);
        MyViewHolder myViewHolder = new MyViewHolder(inflate);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_question_radio.setText(firstList.get(position));
        //RecyclerView嵌套
        holder.rv_question_radio.setLayoutManager(new GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false));
        holder.rv_question_radio.setAdapter(new SecondAdapter(context,secondList));
        holder.rv_question_radio.getRecycledViewPool().setMaxRecycledViews(holder.getItemViewType(),10);
    }

    @Override
    public int getItemCount() {
        return firstList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_question_radio;
        RecyclerView rv_question_radio;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_question_radio = itemView.findViewById(R.id.tv_question_radio);
            rv_question_radio = itemView.findViewById(R.id.rv_question_radio);
        }
    }

}