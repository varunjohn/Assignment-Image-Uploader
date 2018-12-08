package com.varunjohn1990.assignmentimageupload.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asksira.bsimagepicker.BSImagePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.varunjohn1990.assignmentimageupload.model.ImageServer;
import com.varunjohn1990.assignmentimageupload.adapter.ImageUploaderListAdapter;
import com.varunjohn1990.assignmentimageupload.adapter.viewholder.ImageUploaderViewHolder;
import com.varunjohn1990.assignmentimageupload.logic.ImageUploadingTask;
import com.varunjohn1990.assignmentimageupload.model.Media;
import com.varunjohn1990.assignmentimageupload.util.AppConstants;
import com.varunjohn1990.assignmentimageupload.util.NetworkUtil;
import com.varunjohn1990.assignmentimageupload.model.storage.Prefs;
import com.varunjohn1990.assignmentimageupload.R;
import com.varunjohn1990.assignmentimageupload.util.ToastUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class HomeActivity extends BaseActivity implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener, ImageUploaderViewHolder.OnItemClickListener, ImageUploadingTask.Callback {

    public static void open(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    private BSImagePicker multiSelectionPicker;
    private ImageUploaderListAdapter imageUploaderListAdapter;
    private ImageUploadingTask imageUploadingTask;

    @BindView(R.id.recyclerViewImages)
    RecyclerView recyclerViewImages;
    @BindView(R.id.textViewUpload)
    TextView textViewUpload;
    @BindView(R.id.textViewEmptyData)
    TextView textViewEmptyData;

    @BindView(R.id.textViewProgress)
    TextView textViewProgress;
    @BindView(R.id.textViewProgressDetails)
    TextView textViewProgressDetails;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.layoutAddImages)
    View layoutAddImages;
    @BindView(R.id.layoutProgress)
    View layoutProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(activity);

        setUpAdapter();
    }

    private void setUpAdapter() {
        recyclerViewImages.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewImages.setItemAnimator(new DefaultItemAnimator());

        imageUploaderListAdapter = new ImageUploaderListAdapter(context, this);
        recyclerViewImages.setAdapter(imageUploaderListAdapter);
    }

    @OnClick(R.id.buttonCapture)
    public void buttonCapture(MaterialButton button) {
        CameraActivity.open(activity);
    }

    @OnClick(R.id.buttonGallery)
    public void buttonGallery(MaterialButton button) {

        multiSelectionPicker = new BSImagePicker.Builder("com.yourdomain.yourpackage.fileprovider")
                .isMultiSelect()
                .setMaximumMultiSelectCount(20)
                .setMultiSelectBarBgColor(android.R.color.white)
                .build();

        multiSelectionPicker.show(getSupportFragmentManager(), "picker");
    }

    @OnClick(R.id.textViewUpload)
    public void textViewUpload(TextView view) {

        if (imageUploadingTask != null && imageUploadingTask.isUploadingCompleted()) {
            ImageListActivity.open(context);
            finish();
        } else {
            if (imageUploaderListAdapter.getData().isEmpty()) {
                ToastUtils.show(context, R.string.no_images);
            } else {
                if (NetworkUtil.isNetworkAvailable()) {
                    imageUploadingTask = new ImageUploadingTask(context, imageUploaderListAdapter.getData(), this);
                    imageUploadingTask.start();
                } else {
                    NetworkUtil.handleNoConnection(context);
                }
            }
        }
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        for (Uri uri : uriList) {
            imageUploaderListAdapter.add(new Media(uri));
            checkData();
        }
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        imageUploaderListAdapter.add(new Media(uri));
        checkData();
    }

    public void checkData() {
        if (imageUploaderListAdapter.getData().isEmpty() && textViewEmptyData.getVisibility() == View.GONE) {
            textViewEmptyData.setVisibility(View.VISIBLE);
        } else if (!imageUploaderListAdapter.getData().isEmpty() && textViewEmptyData.getVisibility() == View.VISIBLE) {
            textViewEmptyData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUploadingStart() {
        textViewUpload.setEnabled(false);
        textViewUpload.setText(R.string.uploading);
        imageUploaderListAdapter.setUploadingStarted(true);
        imageUploaderListAdapter.notifyDataSetChanged();

        layoutAddImages.setVisibility(View.INVISIBLE);
        layoutProgress.setVisibility(View.VISIBLE);
        textViewProgressDetails.setText(R.string.uploading);
        textViewProgress.setText("1%");
    }

    @Override
    public void onNextImage(Media media, List<Media> mediaList) {
        media.setUploadStatus(Media.UploadStatus.DOWNLOADING);
        media.setDownloadingProgress(1);
        imageUploaderListAdapter.notifyItemChanged(imageUploaderListAdapter.getData().lastIndexOf(media));
        textViewProgressDetails.setText(context.getString(R.string.uploading) + " (" + (mediaList.indexOf(media) + 1) + "/" + mediaList.size() + ")");
    }

    @Override
    public void onImageUploadingProcess(Media media, int currentProgress, int overallProgress) {
        media.setUploadStatus(Media.UploadStatus.DOWNLOADING);
        media.setDownloadingProgress(currentProgress);
        imageUploaderListAdapter.notifyItemChanged(imageUploaderListAdapter.getData().lastIndexOf(media));
        textViewProgress.setText(overallProgress + "%");
        progressBar.setProgress(overallProgress);
    }

    @Override
    public void onImageUploadingFailed(Media media, String message) {
        media.setUploadStatus(Media.UploadStatus.FAILED);
        imageUploaderListAdapter.notifyItemChanged(imageUploaderListAdapter.getData().lastIndexOf(media));
    }

    @Override
    public void onImageUploadingSuccess(Media media, ImageServer imageServer) {
        media.setUploadStatus(Media.UploadStatus.COMPLETED);
        imageUploaderListAdapter.notifyItemChanged(imageUploaderListAdapter.getData().lastIndexOf(media));

        Prefs.getInstance().addImage(imageServer);
    }

    @Override
    public void onImageUploadingCompressionFailed(Media media, String message) {
    }

    @Override
    public void onImageUploadingCompressionSuccess(Media media) {
    }

    @Override
    public void onUploadingComplete() {
        textViewProgress.setText("100%");
        progressBar.setProgress(100);
        textViewUpload.setEnabled(true);
        textViewUpload.setText(R.string.go_to_sever_images);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == AppConstants.REQUEST_CODE_CAMERA_IMAGE) {
                String path = data.getStringExtra(AppConstants.INTENT_DATA_IMAGE_PATH);

                File file = new File(path);

                CropImage.activity(Uri.fromFile(file))
                        .start(activity);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();

                Media media = new Media(resultUri);
                imageUploaderListAdapter.add(media);
                checkData();
            }
        }
    }

    @Override
    public void onItemClickImageUploader(View viewRoot, View view, Media model) {
    }

    @Override
    public void onItemLongClickImageUploader(View viewRoot, View view, Media model) {
    }
}
