package com.duynam.demooverlay.ui.activity.activity_filter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.ActivityFilterBinding;
import com.duynam.demooverlay.ui.activity.activity_image_edit.ListImageEditActivity;
import com.filter.base.GPUImageFilter;
import com.filter.helper.FilterManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class FilterActivity extends AppCompatActivity implements FilterAdapter.OnClickFilter {

    private LinearLayoutManager linearLayoutManager;
    private FilterAdapter filterAdapter;
    private Bitmap bitmap;
    private ActivityFilterBinding binding;
    private String timesave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        FilterManager.init(this);
        initRecycleView();
        initImage();
        menuTop();
    }

    private void initRecycleView() {
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        filterAdapter = new FilterAdapter(this);
        filterAdapter.setOnClickFilter(this);
        binding.rvmenuEdit.setLayoutManager(linearLayoutManager);
        binding.rvmenuEdit.setAdapter(filterAdapter);
    }

    private void initImage() {
        String timesave;
        if (getIntent() != null) {
            timesave = getIntent().getStringExtra("time");
            final Bitmap bitmap = BitmapFactory.decodeFile("/data/data/com.duynam.demooverlay/cache/cacheImageCrop/" + timesave + ".jpg");
            binding.imgFilter.setImage(bitmap);
            filterAdapter.setBitmap(bitmap);
        }
    }

    @Override
    public void onClickFilter(GPUImageFilter gpuImageFilter) {
        binding.imgFilter.setFilter(gpuImageFilter);
    }

    private void menuTop() {
        binding.imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialog();
            }
        });
        binding.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void saveImage() {
        Bitmap bitmap = binding.imgFilter.getGPUImage().getBitmapWithFilterApplied();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        File folder = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "input_image");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String file_name = Calendar.getInstance().getTimeInMillis() + ".jpg";
        File file = new File(folder, file_name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_save);
        ImageView img_yes = dialog.findViewById(R.id.img_yes);
        ImageView img_cancel = dialog.findViewById(R.id.img_no);

        img_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                saveImage();
                startActivity(new Intent(FilterActivity.this, ListImageEditActivity.class));
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
