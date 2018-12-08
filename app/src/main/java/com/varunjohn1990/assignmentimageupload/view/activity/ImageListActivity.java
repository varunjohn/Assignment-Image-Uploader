package com.varunjohn1990.assignmentimageupload.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.varunjohn1990.assignmentimageupload.R;
import com.varunjohn1990.assignmentimageupload.adapter.ImageListAdapter;
import com.varunjohn1990.assignmentimageupload.adapter.viewholder.ImageListViewHolder;
import com.varunjohn1990.assignmentimageupload.model.ImageServer;
import com.varunjohn1990.assignmentimageupload.model.storage.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class ImageListActivity extends BaseActivity implements ImageListViewHolder.OnItemClickListener {

    public static void open(Context context) {
        context.startActivity(new Intent(context, ImageListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @BindView(R.id.recyclerViewImages)
    RecyclerView recyclerViewImages;

    private ImageListAdapter imageListAdapter;
    private List<ImageServer> imageServerList;
    private List<String> imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        ButterKnife.bind(activity);

        getImages();

        setupAdapter();
    }

    private void setupAdapter() {
        int spanCount = 3;
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 5;
        } else {
            spanCount = 3;
        }

        recyclerViewImages.setLayoutManager(new GridLayoutManager(context, spanCount));
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewImages.setItemAnimator(new DefaultItemAnimator());

        imageListAdapter = new ImageListAdapter(context, this);
        recyclerViewImages.setAdapter(imageListAdapter);

        imageListAdapter.add(imageServerList);
    }

    private void getImages() {
        imageServerList = Prefs.getInstance().getImages();

        if (imageServerList != null) {
            imagesList = new ArrayList<>(imageServerList.size());

            for (ImageServer imageServer : imageServerList) {
                imagesList.add(imageServer.getS3MediaUrl());
            }
        }
    }

    @OnClick(R.id.imageViewAddImages)
    public void imageViewAddImages(View View) {
        HomeActivity.open(context);
    }

    @Override
    public void onItemClickImage(View viewRoot, View view, ImageServer model) {
        GalleryActivity.openActivity(context, imageServerList.indexOf(model), imagesList);
    }

    @Override
    public void onItemLongClickImage(View viewRoot, View view, ImageServer model) {
    }
}
