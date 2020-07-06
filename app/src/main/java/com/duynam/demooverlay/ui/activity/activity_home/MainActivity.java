package com.duynam.demooverlay.ui.activity.activity_home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.ActivityMainBinding;
import com.duynam.demooverlay.ui.activity.activity_edit_image.EditImageActivity;
import com.duynam.demooverlay.utils.Constant;
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
                deviceAsynTask = new GetImageFromDeviceAsynTask(this, this);
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
            deviceAsynTask = new GetImageFromDeviceAsynTask(MainActivity.this, this);
            deviceAsynTask.execute();
        } else {
            initPermission();
        }
    }

    private void openStorage() {
        mainBinding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constant.PICK_IMAGE);
            }
        });
    }

    @Override
    public void onClickImage(String patch) {
//        CropImage.activity(Uri.fromFile(new File(patch)))
//                .start(this);
        UCrop.of(Uri.fromFile(new File(patch)), Uri.fromFile(new File(getCacheDir(), Constant.IMAGE_CROP_NAME)))
                .useSourceImageAspectRatio()
                .start(this);
    }


    @Override
    public void onLoadFinish(List<String> data) {
        adapter.setData(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PICK_IMAGE) {
            if (data != null) {
                Uri patch = data.getData();
                UCrop.of(Uri.fromFile(new File(getRealPathFromURI_API19(this, patch))),
                        Uri.fromFile(new File(getCacheDir(), Constant.IMAGE_CROP_NAME)))
                        .useSourceImageAspectRatio().start(MainActivity.this);
            }
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            Intent intent = new Intent(MainActivity.this, EditImageActivity.class);
            intent.putExtra(Constant.PATCH_IMAGE, resultUri.toString());
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initPermission();
            }
        }, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (deviceAsynTask != null && deviceAsynTask.isCancelled()) {
            deviceAsynTask.cancel(true);
        }
    }
}