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
import com.gghouse.woi.whatsonininput.listener.HomeOnClickListener;
import com.gghouse.woi.whatsonininput.listener.OnLoadMoreListener;
import com.gghouse.woi.whatsonininput.model.Store;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by michaelhalim on 2/10/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = Config.SIZE_PER_PAGE;
    private int lastVisibleItem, totalItemCount;

    private Context mContext;
    private List<Store> mDataSet;
    private HomeOnClickListener mListener;
    private RecyclerView mRecyclerView;

    static class HomeViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView ivImage;
        public TextView tvTitle;
        public TextView tvSubtitle;

        public HomeViewHolder(View v) {
            super(v);
            mView = v;
            ivImage = (ImageView) v.findViewById(R.id.iv_AI_image);
            tvTitle = (TextView) v.findViewById(R.id.tv_AI_title);
            tvSubtitle = (TextView) v.findViewById(R.id.tv_AI_subtitle);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    public HomeAdapter(Context context, List<Store> dataSet, HomeOnClickListener listener, RecyclerView recyclerView) {
        mContext = context;
        mDataSet = dataSet;
        mListener = listener;
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.adater_home_item, parent, false);
            return new HomeViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.screen_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeViewHolder) {
            HomeViewHolder homeViewHolder = (HomeViewHolder) holder;
            final Store store = mDataSet.get(position);
            homeViewHolder.tvTitle.setText(store.getName());
            homeViewHolder.tvSubtitle.setText(store.getDescription());
            switch (Config.runMode) {
                case DUMMY:
                    Picasso.with(mContext)
                            .load(R.mipmap.ic_launcher)
                            .fit()
                            .centerInside()
                            .into(homeViewHolder.ivImage);
                    break;
                default:
                    String url = null;
                    if (store.getPhotos().size() > 0) {
                        url = store.getPhotos().get(0).getUrl();
                    }

                    if (url == null || url.isEmpty()) {
                        Picasso.with(mContext)
                                .load(R.drawable.no_image)
                                .fit()
                                .centerInside()
                                .into(homeViewHolder.ivImage);
                    } else {
                        Picasso.with(mContext)
                                .load(url)
                                .fit()
                                .centerInside()
                                .into(homeViewHolder.ivImage);
                    }
                    break;
            }

            homeViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(store);
                }
            });
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

    public void add(Store store) {
        mDataSet.add(store);
    }

    public void remove(int i) {
        mDataSet.remove(i);
    }

    public void setData(List<Store> dataSet) {
        mDataSet = dataSet;
    }
}