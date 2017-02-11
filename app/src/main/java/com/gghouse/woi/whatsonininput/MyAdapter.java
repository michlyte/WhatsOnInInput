package com.gghouse.woi.whatsonininput;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gghouse.woi.whatsonininput.model.Dummy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by michaelhalim on 2/10/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context mContext;
    private List<Dummy> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView ivImage;
        public TextView tvTitle;
        public TextView tvSubtitle;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            ivImage = (ImageView) v.findViewById(R.id.iv_AI_image);
            tvTitle = (TextView) v.findViewById(R.id.tv_AI_title);
            tvSubtitle = (TextView) v.findViewById(R.id.tv_AI_subtitle);
        }
    }

    public MyAdapter(Context context, List<Dummy> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Dummy dummy = mDataset.get(position);
        holder.tvTitle.setText(dummy.getTitle());
        holder.tvSubtitle.setText(dummy.getSubtitle());
        Picasso.with(mContext)
                .load(dummy.getDrawable())
                .fit()
                .centerInside()
                .into(holder.ivImage);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}