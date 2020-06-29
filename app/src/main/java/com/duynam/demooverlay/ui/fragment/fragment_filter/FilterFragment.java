package com.duynam.demooverlay.ui.fragment.fragment_filter;

import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.duynam.demooverlay.ui.activity.activity_filter.FilterAdapter;
import com.filter.base.GPUImageFilter;

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
    public void onClickFilter(final GPUImageFilter gpuImageFilter) {
        Bitmap bitmap = null;
        if (getActivity() != null && getActivity() instanceof EditImageActivity) {
            final EditImageActivity activity = ((EditImageActivity) getActivity());
            if (activity.mCurrentTView != null) {
                activity.mCurrentTView.setInEdit(false);
            }
            if (activity.mCurrentEditTextView != null) {
                activity.mCurrentTView.setInEdit(false);
            }
            activity.imageBinding.rootView.setDrawingCacheEnabled(false);
            activity.imageBinding.rootView.buildDrawingCache();
            bitmap = activity.imageBinding.rootView.getDrawingCache();
            //activity.imageBinding.imgFilter.setImage(bitmap);
            //activity.imageBinding.imgFilter.setFilter(gpuImageFilter);

            //activity.imageBinding.imgFilter.setImage(bitmap);
            //activity.imageBinding.imgFilter.setFilter(gpuImageFilter);

            activity.imageBinding.imgContainer.setVisibility(View.GONE);
            if (activity.mCurrentTView != null) {
                activity.mViews.remove(activity.mCurrentTView);
                activity.imageBinding.rootView.removeView(activity.mCurrentTView);
            }
            if (activity.mCurrentEditTextView != null) {
                activity.mViews.remove(activity.mCurrentEditTextView);
                activity.mViews.remove(activity.mCurrentEditTextView);
            }
        }
    }


}
