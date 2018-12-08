package com.varunjohn1990.assignmentimageupload.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.varunjohn1990.assignmentimageupload.model.Media;
import com.varunjohn1990.assignmentimageupload.R;
import com.varunjohn1990.assignmentimageupload.adapter.ImageUploaderListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class ImageUploaderViewHolder extends RecyclerView.ViewHolder {

    public interface OnItemClickListener {
        void onItemClickImageUploader(View viewRoot, View view, Media model);

        void onItemLongClickImageUploader(View viewRoot, View view, Media model);
    }

    @BindView(R.id.textViewProgress)
    TextView textViewProgress;
    @BindView(R.id.imageViewImage)
    ImageView imageViewImage;
    @BindView(R.id.circularProgressBar)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.layoutDownloading)
    View layoutDownloading;

    ImageUploaderListAdapter imageUploaderListAdapter;

    private Context context;

    public ImageUploaderViewHolder(View view, ImageUploaderListAdapter imageUploaderListAdapter) {
        super(view);
        ButterKnife.bind(this, view);

        this.imageUploaderListAdapter = imageUploaderListAdapter;
        context = view.getContext();
    }

    public void bind(final Media media, final OnItemClickListener onItemClickListener) {
        if (media != null) {
            Glide.with(context)
                    .load(media.getUri())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imageViewImage);

            if (imageUploaderListAdapter.isUploadingStarted()) {
                layoutDownloading.setVisibility(View.VISIBLE);
                switch (media.getUploadStatus()) {
                    case WAITING:
                        circularProgressBar.setVisibility(View.INVISIBLE);
                        textViewProgress.setText(context.getString(R.string.waiting));
                        break;
                    case DOWNLOADING:
                        circularProgressBar.setVisibility(View.VISIBLE);
                        textViewProgress.setText(media.getDownloadingProgress() + "%");
                        circularProgressBar.setProgress(media.getDownloadingProgress());
                        break;
                    case FAILED:
                        circularProgressBar.setVisibility(View.INVISIBLE);
                        textViewProgress.setText(context.getString(R.string.retry));
                        break;
                    case COMPLETED:
                        circularProgressBar.setVisibility(View.INVISIBLE);
                        textViewProgress.setText(context.getString(R.string.completed));
                        break;
                }
            } else {
                layoutDownloading.setVisibility(View.GONE);
            }
        }
    }
}
