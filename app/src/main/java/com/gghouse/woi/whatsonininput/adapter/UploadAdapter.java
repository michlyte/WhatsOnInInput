package com.gghouse.woi.whatsonininput.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gghouse.woi.whatsonininput.R;
import com.gghouse.woi.whatsonininput.common.Config;
import com.gghouse.woi.whatsonininput.listener.OnLoadMoreListener;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by michael on 3/29/2017.
 */

public class UploadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = Config.SIZE_PER_PAGE;
    private int lastVisibleItem, totalItemCount;

    private Context mContext;
    private List<StoreFileLocation> mDataSet;
    private RecyclerView mRecyclerView;

    static class UploadViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView ivImage;
        public TextView tvTitle;
        public TextView tvSubtitle;
        public ProgressBar pbProgressBar;

        public UploadViewHolder(View v) {
            super(v);
            mView = v;
            ivImage = (ImageView) v.findViewById(R.id.iv_AUI_image);
            tvTitle = (TextView) v.findViewById(R.id.tv_AUI_title);
            tvSubtitle = (TextView) v.findViewById(R.id.tv_AUI_subtitle);
            pbProgressBar = (ProgressBar) v.findViewById(R.id.pb_AUI_progressBar);

            DoubleBounce doubleBounce = new DoubleBounce();
            doubleBounce.setBounds(0, 0, 100, 100);
            pbProgressBar.setIndeterminateDrawable(doubleBounce);

            pbProgressBar.setVisibility(View.GONE);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    public UploadAdapter(Context context, List<StoreFileLocation> dataSet, RecyclerView recyclerView) {
        mContext = context;
        mDataSet = dataSet;
        mRecyclerView = recyclerView;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adater_upload_item, parent, false);
            return new UploadViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.screen_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UploadViewHolder) {
            UploadViewHolder uploadViewHolder = (UploadViewHolder) holder;
            final StoreFileLocation storeFileLocation = mDataSet.get(position);
            uploadViewHolder.tvTitle.setText(storeFileLocation.getFileName());
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
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void removeOnLoadMoreListener() {
        this.mOnLoadMoreListener = null;
    }

    public void add(StoreFileLocation storeFileLocation) {
        mDataSet.add(storeFileLocation);
    }

    public void remove(int i) {
        mDataSet.remove(i);
    }

    public void setData(List<StoreFileLocation> dataSet) {
        mDataSet = dataSet;
    }
}