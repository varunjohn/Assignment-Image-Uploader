package com.varunjohn1990.assignmentimageupload.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varunjohn1990.assignmentimageupload.adapter.viewholder.ImageUploaderViewHolder;
import com.varunjohn1990.assignmentimageupload.model.Media;
import com.varunjohn1990.assignmentimageupload.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class ImageUploaderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Media> list = new ArrayList<>();
    private Context context;
    private ImageUploaderViewHolder.OnItemClickListener onItemClickListener;

    private boolean isUploadingStarted;

    public ImageUploaderListAdapter(Context context, ImageUploaderViewHolder.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public List<Media> getData() {
        return list;
    }

    public boolean isUploadingStarted() {
        return isUploadingStarted;
    }

    public void setUploadingStarted(boolean uploadingStarted) {
        isUploadingStarted = uploadingStarted;
    }

    public void add(Media media) {
        list.add(0, media);
        notifyItemInserted(0);
    }

    public void delete(Media media) {
        int index = list.indexOf(media);
        list.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image_uploader, null);
        return new ImageUploaderViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ImageUploaderViewHolder) holder).bind(list.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
