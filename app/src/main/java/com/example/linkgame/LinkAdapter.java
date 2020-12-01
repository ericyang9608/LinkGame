package com.example.linkgame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ItemViewHolder> {
    private static final String TAG = "LinkAdapter";
    private List<LinkItem> dataList;
    private Context context;

    private AnimatorSet rightOutSet;
    private AnimatorSet leftInSet;
    private AnimatorSet rightOutSetPlace;
    private AnimatorSet leftInSetPlace;

    private final String REVERSE_TAG = "reverse";
    private final String FRONT_TAG = "front";

    public LinkAdapter(List<LinkItem> datas) {
        this.dataList = datas;
    }

    private OnItemClickListener<LinkItem> onItemClickListener;

    private Pair<Integer, LinkItem> prePairAnimation;

    public void setOnItemClickListener(OnItemClickListener<LinkItem> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 刷新数据
     *
     * @param datas
     */
    public void refreshData(List<LinkItem> datas) {
        if (datas == null) datas = new ArrayList<>();
        if (dataList.size() > 0) dataList.clear();
        this.dataList.addAll(datas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        //设置动画特效
        // 右出动画
        rightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.anim_out);
        // 左入动画
        leftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.anim_in);
        rightOutSetPlace = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.anim_out_place);
        leftInSetPlace = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.anim_in_place);
        ItemViewHolder itemViewHolder = new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv, parent, false));
        itemViewHolder.setIsRecyclable(false);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        final LinkItem linkItem = dataList.get(position);
        if (linkItem.getKey() < 0) {
            holder.itemRoot.setVisibility(View.GONE);
            return;
        }
        //设置翻转动画效果
        holder.frontImg.setImageResource(R.mipmap.front);
        holder.frontRoot.clearAnimation();
        holder.reverseRoot.clearAnimation();
        String tag = holder.itemRoot.getTag() == null ? REVERSE_TAG : holder.itemRoot.getTag().toString();
        if (!TextUtils.isEmpty(tag) && FRONT_TAG.equals(tag)) {
            rightOutSetPlace.setTarget(holder.reverseRoot);
            leftInSetPlace.setTarget(holder.frontRoot);
            rightOutSetPlace.start();
            leftInSetPlace.start();
            holder.itemRoot.setTag(REVERSE_TAG);
        }
        holder.itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rightOutSet.isRunning() && !leftInSet.isRunning()) {
                    String tag = REVERSE_TAG;
                    if (null == holder.itemRoot.getTag()) {
                        //设置视角距离  贴近屏幕
                        int distance = 16000;
                        float scale = context.getResources().getDisplayMetrics().density * distance;
                        holder.frontRoot.setCameraDistance(scale);
                        holder.reverseRoot.setCameraDistance(scale);
                    } else {
                        tag = holder.itemRoot.getTag().toString();
                    }
                    if (REVERSE_TAG.equals(tag)) {
                        holder.reverseImg.setImageResource(linkItem.getResId());
                        rightOutSet.setTarget(holder.frontRoot);
                        leftInSet.setTarget(holder.reverseRoot);
                        prePairAnimation = new Pair<>(position, linkItem);
                        leftInSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationCancel(Animator animation) {
                                super.onAnimationCancel(animation);
                                Log.i(TAG, "leftInSet  onAnimationCancel: ");
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if (onItemClickListener != null && prePairAnimation != null) {
                                    onItemClickListener.onItemClick(prePairAnimation.first, prePairAnimation.second);
                                    prePairAnimation = null;
                                    holder.frontRoot.clearAnimation();
                                    holder.reverseRoot.clearAnimation();
                                }
                            }
                        });
                        rightOutSet.start();
                        leftInSet.start();
                        holder.itemRoot.setTag(FRONT_TAG);
                    }
                }
            }
        });
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

    interface OnItemClickListener<T> {
        /**
         * item点击事件
         *
         * @param position
         * @param t
         */
        void onItemClick(int position, T t);
    }
}