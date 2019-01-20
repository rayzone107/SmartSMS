package com.rachitgoyal.smartsms.module.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rachitgoyal.smartsms.R;
import com.rachitgoyal.smartsms.model.Message;
import com.rachitgoyal.smartsms.model.Model;
import com.rachitgoyal.smartsms.model.TimeHeader;
import com.rachitgoyal.smartsms.util.StickyHeaderItemDecoration;

import java.util.List;

/**
 * Created by Rachit Goyal on 15/01/19.
 */
public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderItemDecoration.StickyHeaderInterface {

    private static final int HEADER_TYPE = 0;
    private static final int MESSAGE_TYPE = 1;

    private List<Model> mModels;

    public MessagesAdapter(List<Model> models) {
        mModels = models;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER_TYPE:
                View headerLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
                return new HeaderViewHolder(headerLayout);
            default:
                View messageLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
                return new MessagesViewHolder(messageLayout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (mModels.get(position) instanceof TimeHeader) {
            ((HeaderViewHolder) viewHolder).bind((TimeHeader) mModels.get(position));
        } else {
            ((MessagesViewHolder) viewHolder).bind((Message) mModels.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mModels == null || mModels.isEmpty() ? 0 : mModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mModels.get(position) instanceof TimeHeader) {
            return HEADER_TYPE;
        } else {
            return MESSAGE_TYPE;
        }
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        return R.layout.item_header;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {
        if (mModels.get(headerPosition) instanceof TimeHeader) {
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(header);
            headerViewHolder.bind((TimeHeader) mModels.get(headerPosition));
        }
    }

    @Override
    public boolean isHeader(int itemPosition) {
        return mModels.get(itemPosition) instanceof TimeHeader;
    }

    public void addMessage(Message message) {
        mModels.add(0, message);
        notifyDataSetChanged();
    }
}
