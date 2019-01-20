package com.rachitgoyal.smartsms.module.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rachitgoyal.smartsms.R;
import com.rachitgoyal.smartsms.model.TimeHeader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rachit Goyal on 15/01/19.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.time_duration_tv)
    TextView mTitleTV;

    @BindView(R.id.date_tv)
    TextView mDateTV;

    @BindView(R.id.count_tv)
    TextView mCountTV;

    HeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bind(TimeHeader timeHeader) {
        mTitleTV.setText(timeHeader.getTimeSegment());
        mDateTV.setText(timeHeader.getDate());
        mCountTV.setText(String.valueOf(timeHeader.getItemCount()));
    }
}
