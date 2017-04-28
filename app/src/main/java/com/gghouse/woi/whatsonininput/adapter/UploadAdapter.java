package com.gghouse.woi.whatsonininput.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by michael on 3/29/2017.
 */

public class UploadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<StoreFileLocation> mDataSet;

    static class UploadViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView ivImage;
        public TextView tvTitle;
        public TextView tvSubtitle;

        public UploadViewHolder(View v) {
            super(v);
            mView = v;
            ivImage = (ImageView) v.findViewById(R.id.iv_AUI_image);
            tvTitle = (TextView) v.findViewById(R.id.tv_AUI_title);
            tvSubtitle = (TextView) v.findViewById(R.id.tv_AUI_subtitle);
        }
    }

    public UploadAdapter(Context context, List<StoreFileLocation> dataSet, RecyclerView recyclerView) {
        mContext = context;
        mDataSet = dataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adater_upload_item, parent, false);
        return new UploadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UploadViewHolder uploadViewHolder = (UploadViewHolder) holder;
        final StoreFileLocation storeFileLocation = mDataSet.get(position);
        uploadViewHolder.tvTitle.setText(storeFileLocation.getFileName() == null ? "Null" : storeFileLocation.getFileName());
        uploadViewHolder.tvSubtitle.setText(storeFileLocation.getLocation());
        File file = new File(storeFileLocation.getLocation());
        if (file.exists()) {
            Picasso.with(mContext)
                    .load(file)
                    .fit()
                    .centerInside()
                    .into(uploadViewHolder.ivImage);
        } else {
            Picasso.with(mContext)
                    .load(R.mipmap.ic_launcher)
                    .fit()
                    .centerInside()
                    .into(uploadViewHolder.ivImage);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public void add(StoreFileLocation storeFileLocation) {
        mDataSet.add(storeFileLocation);
    }

    public void setData(List<StoreFileLocation> dataSet) {
        mDataSet = dataSet;
    }
}