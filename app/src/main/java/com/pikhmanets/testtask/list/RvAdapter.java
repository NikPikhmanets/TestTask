package com.pikhmanets.testtask.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pikhmanets.testtask.list.model.News;
import com.pikhmanets.testtask.R;

import java.util.ArrayList;
import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> {

    private OnItemClickListener listener;
    private List<News> mTitleList = new ArrayList<>();

    public RvAdapter(List<News> titleList) {
        mTitleList = titleList;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
        return new RvViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        holder.bind(mTitleList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTitleList == null ? 0 : mTitleList.size();
    }


    class RvViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleText;
        private final TextView timeText;

        RvViewHolder(View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.textItemTitle);
            timeText = itemView.findViewById(R.id.textItemPostTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemTitleClick(mTitleList.get(getAdapterPosition()));
                    }
                }
            });
        }

        void bind(News mTitle) {
            titleText.setText(mTitle.getTitle());
            timeText.setText(String.format("Posted: %s", mTitle.getPostTime()));
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
