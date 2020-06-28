package com.duynam.demooverlay.ui.fragment.fragment_filter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.FragmentFilterBinding;
import com.duynam.demooverlay.ui.activity.activity_edit_image.EditImageActivity;
import com.filter.base.GPUImage;
import com.filter.base.GPUImageFilter;
import com.filter.base.Rotation;

public class FilterFragment extends Fragment implements FilterAdapter.OnClickFilter {

    private FragmentFilterBinding filterBinding;
    private LinearLayoutManager linearLayoutManager;
    private FilterAdapter filterAdapter;
    private Bitmap bitmap;

    public FilterFragment(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        filterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false);
        initRecycleView();
        return filterBinding.getRoot();
    }

    private void initRecycleView() {
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        filterAdapter = new FilterAdapter(getContext());
        filterAdapter.setOnClickFilter(this);
        filterAdapter.setBitmap(bitmap);
        filterBinding.rvFilter.setLayoutManager(linearLayoutManager);
        filterBinding.rvFilter.setAdapter(filterAdapter);
    }


    @Override
    public void onClickFilter(GPUImageFilter gpuImageFilter) {
        if (getActivity() != null && getActivity() instanceof EditImageActivity) {
            EditImageActivity activity = ((EditImageActivity) getActivity());
            activity.imageBinding.imgFilter.setFilter(gpuImageFilter);
            //activity.imageBinding.imgFilter.getGPUImage().setRotation(Rotation.ROTATION_30);
        }
    }
}
