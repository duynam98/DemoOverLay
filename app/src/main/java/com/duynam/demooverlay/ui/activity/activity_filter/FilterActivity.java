package com.duynam.demooverlay.ui.activity.activity_filter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.ActivityFilterBinding;
import com.duynam.demooverlay.ui.activity.activity_home.MainActivity;
import com.filter.base.GPUImageFilter;
import com.filter.helper.FilterManager;

import java.io.ByteArrayOutputStream;

public class FilterActivity extends AppCompatActivity implements FilterAdapter.OnClickFilter {

    private LinearLayoutManager linearLayoutManager;
    private FilterAdapter filterAdapter;
    private ActivityFilterBinding binding;
    private  SaveImage saveImageAsyntask;

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
            Glide.with(this).asBitmap().load(bitmap).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    binding.imgFilter.setImage(bitmap);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
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
        binding.progress.setVisibility(View.VISIBLE);
        Bitmap bitmap = binding.imgFilter.getGPUImage().getBitmapWithFilterApplied();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        saveImageAsyntask = new SaveImage(getApplicationContext());
        SaveImage.OnSaveImage onSaveImage = new SaveImage.OnSaveImage() {
            @Override
            public void onSaveImage(boolean isSave) {
                binding.progress.setVisibility(View.GONE);
                startActivity(new Intent(FilterActivity.this, MainActivity.class));
                finish();
            }
        };
        saveImageAsyntask.setOnSaveImage(onSaveImage);
        saveImageAsyntask.execute(bitmap);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (saveImageAsyntask != null && saveImageAsyntask.isCancelled()){
            saveImageAsyntask.cancel(true);
        }
    }
}
