package com.jennifer.jennifer.ui.ceiling;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.entity.TermsEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author jingjie
 * @date :2020/10/19 15:54
 * TODO:
 */
public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.ViewHolder> {
    private Context context;
    private List<TermsEntity> dataList = new ArrayList<>();
    private OnItemClickListener listener;

    public TermsAdapter(Context context, List<TermsEntity> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_term, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        if (position < dataList.size()) {
            TermsEntity termEntity = dataList.get(position);
            viewHolder.tvTitleName.setText(termEntity.getTerms());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitleName;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitleName = itemView.findViewById(R.id.tv_title_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
