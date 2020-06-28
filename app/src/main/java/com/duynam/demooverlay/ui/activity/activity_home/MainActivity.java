package com.duynam.demooverlay.ui.activity.activity_home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.ActivityMainBinding;
import com.duynam.demooverlay.ui.activity.activity_edit_image.EditImageActivity;
import com.duynam.demooverlay.utils.Constant;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetImageFromDeviceAsynTask.OnLoadImage, ListImageDeviceAdapter.OnClickImageDevice {

    private ActivityMainBinding mainBinding;
    private ListImageDeviceAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private GetImageFromDeviceAsynTask deviceAsynTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initRecycleView();
        initAsyncTask();
        initPermission();
        openStorage();
    }

    private void initAsyncTask() {
        deviceAsynTask = new GetImageFromDeviceAsynTask(getApplicationContext(), this);
    }

    private void initRecycleView() {
        adapter = new ListImageDeviceAdapter(this, this);
        gridLayoutManager = new GridLayoutManager(this, 3);
        mainBinding.rvListImage.setLayoutManager(gridLayoutManager);
        mainBinding.rvListImage.setAdapter(adapter);
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                deviceAsynTask.execute();
            } else if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQUEST_CODE_PERMISSION);
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQUEST_CODE_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.REQUEST_CODE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            deviceAsynTask.execute();
        } else {
            initPermission();
        }
    }

    private void openStorage() {
        mainBinding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(MainActivity.this);
            }
        });
    }

    @Override
    public void onClickImage(String patch) {
//        CropImage.activity(Uri.fromFile(new File(patch)))
//                .start(this);
        UCrop.of(Uri.fromFile(new File(patch)), Uri.fromFile(new File(patch)))
                .start(this);
    }


    @Override
    public void onLoadFinish(List<String> data) {
        adapter.setData(data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            Intent intent = new Intent(MainActivity.this, EditImageActivity.class);
            intent.putExtra(Constant.PATCH_IMAGE, resultUri.toString());
            startActivity(intent);
        }
    }

}