package com.mini.paddling.minicard.card.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mini.paddling.minicard.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{

    private Context context;

    public ImageAdapter(Context context){
        this.context = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_pic, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView ivCard;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ivCard = itemView.findViewById(R.id.iv_picture);
        }
    }
}
