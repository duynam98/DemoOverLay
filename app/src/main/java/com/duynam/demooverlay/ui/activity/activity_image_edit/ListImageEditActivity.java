package com.duynam.demooverlay.ui.activity.activity_image_edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.ActivityListImageEditBinding;
import com.duynam.demooverlay.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class ListImageEditActivity extends AppCompatActivity implements GetImageEditAsynTask.OnLoadImageEdit {

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

    private void initRecycleView(){
        listImageAdapter = new ListImageEditAdapter(this);
        gridLayoutManager = new GridLayoutManager(this, 3);
        binding.rvListImage.setLayoutManager(gridLayoutManager);
        binding.rvListImage.setAdapter(listImageAdapter);
    }

    @Override
    public void onLoadFinish(ArrayList<String> stringListImage) {
        listImageAdapter.setData(stringListImage);
    }

}