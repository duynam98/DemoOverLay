package com.duynam.demooverlay.ui.activity.activity_image_edit;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.ActivityListImageEditBinding;

import java.util.ArrayList;

public class ListImageEditActivity extends AppCompatActivity implements GetImageEditAsynTask.OnLoadImageEdit, ListImageEditAdapter.ChooseImage {

    private GetImageEditAsynTask getImageEdit;
    private ListImageEditAdapter listImageAdapter;
    private GridLayoutManager gridLayoutManager;
    private ActivityListImageEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_image_edit);
        getImageEdit = new GetImageEditAsynTask();
        getImageEdit.setOnLoadImageEdit(this);
        getImageEdit.execute();
        initRecycleView();
    }

    private void initRecycleView() {
        listImageAdapter = new ListImageEditAdapter(this);
        listImageAdapter.setChooseImage(this);
        gridLayoutManager = new GridLayoutManager(this, 3);
        binding.rvListImage.setLayoutManager(gridLayoutManager);
        binding.rvListImage.setAdapter(listImageAdapter);
    }

    @Override
    public void onLoadFinish(ArrayList<String> stringListImage) {
        listImageAdapter.setData(stringListImage);
    }

    @Override
    public void getImageFinish(String patch) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_view_image);
        ImageView imageView = dialog.findViewById(R.id.img_view);
        Glide.with(this).load(patch).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
        dialog.show();
    }
}