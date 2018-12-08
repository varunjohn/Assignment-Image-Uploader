package com.varunjohn1990.assignmentimageupload.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.cameraview.CameraView;
import com.varunjohn1990.assignmentimageupload.R;
import com.varunjohn1990.assignmentimageupload.util.AppConstants;
import com.varunjohn1990.assignmentimageupload.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Varun John on 8/12/2018
 * <br/>Email : varunjohn1990@gmail.com
 */
public class CameraActivity extends BaseActivity {


    public static void open(Activity activity) {
        activity.startActivityForResult(new Intent(activity, CameraActivity.class), AppConstants.REQUEST_CODE_CAMERA_IMAGE);
    }

    @BindView(R.id.cameraView)
    public CameraView cameraView;

    private Animation animSqueeze;
    private Handler backgroundHandler;

    private static final int REQUEST_CAMERA_STORAGE_PERMISSION = 101;

    private int currentFlash;

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ButterKnife.bind(activity);

        animSqueeze = AnimationUtils.loadAnimation(context,
                R.anim.camera_capture);

        if (cameraView != null) {
            cameraView.addCallback(callback);
        }
    }

    @OnClick(R.id.imageViewCapture)
    public void imageViewCapture(ImageView view) {
        if (cameraView != null) {
            cameraView.takePicture();
            view.setEnabled(false);
            view.setAlpha(0.6f);
            view.startAnimation(animSqueeze);
        }
    }

    @OnClick(R.id.imageViewFlash)
    public void imageViewFlash(ImageView view) {
        if (cameraView != null) {
            currentFlash = (currentFlash + 1) % FLASH_OPTIONS.length;
            view.setImageResource(FLASH_ICONS[currentFlash]);
            cameraView.setFlash(FLASH_OPTIONS[currentFlash]);
        }
    }

    @OnClick(R.id.imageViewSwitch)
    public void imageViewSwitch(ImageView view) {
        if (cameraView != null) {
            int facing = cameraView.getFacing();
            cameraView.setFacing(facing == CameraView.FACING_FRONT ? CameraView.FACING_BACK : CameraView.FACING_FRONT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            cameraView.start();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_STORAGE_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundHandler != null) {
            backgroundHandler.getLooper().quit();
            backgroundHandler = null;
        }
    }

    private void debug(String s, Exception e) {
    }

    private Handler getBackgroundHandler() {
        if (backgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            backgroundHandler = new Handler(thread.getLooper());
        }
        return backgroundHandler;
    }

    private CameraView.Callback callback = new CameraView.Callback() {
        @Override
        public void onCameraOpened(CameraView cameraView) {
            super.onCameraOpened(cameraView);
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            super.onCameraClosed(cameraView);
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            super.onPictureTaken(cameraView, data);
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                            "picture" + System.currentTimeMillis() + ".jpg");

                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        os.write(data);
                        os.close();

                        setResult(RESULT_OK, getIntent()
                                .putExtra(AppConstants.INTENT_DATA_IMAGE_PATH, file.getAbsolutePath()));
                        finish();

                        LogUtils.debug("Image saved " + file.getAbsolutePath());

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.debug("Cannot write to " + file + " " + e.getMessage());
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtils.debug("Cannot close " + file + " " + e.getMessage());
                            }
                        }
                    }
                }
            });
        }
    };
}
