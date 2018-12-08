package com.varunjohn1990.assignmentimageupload.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.varunjohn1990.assignmentimageupload.R;
import com.varunjohn1990.assignmentimageupload.model.ImageServer;
import com.varunjohn1990.assignmentimageupload.model.Media;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class ImageListViewHolder extends RecyclerView.ViewHolder {

    public interface OnItemClickListener {
        void onItemClickImage(View viewRoot, View view, ImageServer model);

        void onItemLongClickImage(View viewRoot, View view, ImageServer model);
    }

    @BindView(R.id.imageViewImage)
    ImageView imageViewImage;

    private Context context;

    public ImageListViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);

        context = view.getContext();
    }

    public void bind(final ImageServer media, final OnItemClickListener onItemClickListener) {
        if (media != null) {
            Glide.with(context)
                    .load(media.getS3MediaUrl())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imageViewImage);

            imageViewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClickImage(itemView, imageViewImage, media);
                }
            });
        }
    }
}
