package com.jennifer.jennifer.ui.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jennifer.jennifer.R;

import java.util.List;

/**
 * @author jingjie
 * @date 2019/3/19
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<String> lstData;
    private Context context;
    private OnItemClickListener itemClickListener;

    public MainAdapter(List<String> lstData, Context context) {
        this.lstData = lstData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tvData.setText(lstData.get(i));
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_data:
                        if (itemClickListener != null)
                            itemClickListener.onItemClick(i);
                        break;
                }
            }
        };
        viewHolder.tvData.setOnClickListener(clickListener);

    }

    @Override
    public int getItemCount() {
        return lstData == null ? 0 : lstData.size();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvData = itemView.findViewById(R.id.tv_data);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
