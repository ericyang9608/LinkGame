package com.example.linkgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewDataAdapter extends RecyclerView.Adapter<ViewDataAdapter.ItemViewHolder> {
    private ArrayList<LinkItem> dataList;

    public ViewDataAdapter(ArrayList<LinkItem> datas) {
        this.dataList = datas;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        final LinkItem linkItem = dataList.get(position);
        if (linkItem.getKey() < 0) {
            holder.itemRoot.setVisibility(View.GONE);
            return;
        }
        holder.reverseImg.setImageResource(linkItem.getResId());
        holder.frontRoot.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout itemRoot;
        ConstraintLayout reverseRoot;
        ConstraintLayout frontRoot;
        ImageView reverseImg;
        ImageView frontImg;

        private ItemViewHolder(View itemView) {
            super(itemView);
            itemRoot = (ConstraintLayout) itemView.findViewById(R.id.itemRoot);
            reverseRoot = (ConstraintLayout) itemView.findViewById(R.id.reverseRoot);
            frontRoot = (ConstraintLayout) itemView.findViewById(R.id.frontRoot);
            reverseImg = (ImageView) itemView.findViewById(R.id.reverseImg);
            frontImg = (ImageView) itemView.findViewById(R.id.frontImg);
        }
    }
}