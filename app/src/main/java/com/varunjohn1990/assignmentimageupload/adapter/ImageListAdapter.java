package com.varunjohn1990.assignmentimageupload.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varunjohn1990.assignmentimageupload.R;
import com.varunjohn1990.assignmentimageupload.adapter.viewholder.ImageListViewHolder;
import com.varunjohn1990.assignmentimageupload.model.ImageServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class ImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ImageServer> list = new ArrayList<>();
    private Context context;
    private ImageListViewHolder.OnItemClickListener onItemClickListener;

    public ImageListAdapter(Context context, ImageListViewHolder.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public List<ImageServer> getData() {
        return list;
    }

    public void add(List<ImageServer> imageServerList) {
        this.list.addAll(imageServerList);
        notifyDataSetChanged();
    }

    public void delete(ImageServer media) {
        int index = list.indexOf(media);
        list.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_images, null);
        return new ImageListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ImageListViewHolder) holder).bind(list.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
