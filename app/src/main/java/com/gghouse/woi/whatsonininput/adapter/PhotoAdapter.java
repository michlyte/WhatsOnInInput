package com.gghouse.woi.whatsonininput.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by michaelhalim on 2/10/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Context mContext;
    private List<StoreFileLocation> mDataset;

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public StoreFileLocation mItem;

        public PhotoViewHolder(View v) {
            super(v);
            mView = v;
            mImageView = (ImageView) v.findViewById(R.id.iv_SHI_image);
        }
    }

    public PhotoAdapter(Context context, List<StoreFileLocation> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.screen_photo_item, parent, false);
        PhotoViewHolder vh = new PhotoViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.mItem = mDataset.get(position);
        Picasso.with(mContext)
                .load(new File(holder.mItem.getLocation()))
                .fit()
                .centerCrop()
                .into(holder.mImageView);
//        Picasso.with(mContext)
//                .load(R.mipmap.ic_launcher)
//                .fit()
//                .centerCrop()
//                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}