package com.rachitgoyal.smartsms.module.main.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.abdularis.civ.AvatarImageView;
import com.rachitgoyal.smartsms.R;
import com.rachitgoyal.smartsms.model.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rachit Goyal on 15/01/19.
 */
class MessagesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_aiv)
    AvatarImageView mImageAIV;

    @BindView(R.id.unread_dot)
    View mUnreadDot;

    @BindView(R.id.name_number_tv)
    TextView mNameNumberTV;

    @BindView(R.id.content_tv)
    TextView mContentTV;

    @BindView(R.id.time_tv)
    TextView mTimeTV;

    MessagesViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bind(Message message) {
        if (message.getPerson() != null && !message.getPerson().isEmpty()) {
            mNameNumberTV.setText(message.getPerson());
            mImageAIV.setText(message.getPerson());
        } else {
            mNameNumberTV.setText(message.getAddress());
            mImageAIV.setText(message.getAddress());
        }
        mImageAIV.setTextColor(ContextCompat.getColor(mImageAIV.getContext(), R.color.white));
        mContentTV.setText(message.getBody());
        mUnreadDot.setVisibility(message.getRead().equals("0") ? View.VISIBLE : View.GONE);

        Long timestamp = Long.parseLong(message.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("h:mm aa", Locale.getDefault());
        mTimeTV.setText(format.format(calendar.getTime()));
    }
}
