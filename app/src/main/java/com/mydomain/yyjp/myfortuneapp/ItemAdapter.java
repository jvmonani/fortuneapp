package com.mydomain.yyjp.myfortuneapp;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private static final String TAG = ItemAdapter.class.getSimpleName();

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout;
        private ViewHolder(View itemView) {
            super(itemView);
            this.layout = (ConstraintLayout) itemView;
        }

        private void bind(String row) {
            if(layout != null && row != null) {
                TextView textView = layout.findViewById(R.id.item_name);
                textView.setText(row);
            }
        }
    }

    private final List<String> items = new ArrayList<>();

    public ItemAdapter() {
    }

    @NonNull @Override public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public void update(List<String> newItems) {
       items.clear();
       items.addAll(newItems);
       notifyDataSetChanged();
    }
}
