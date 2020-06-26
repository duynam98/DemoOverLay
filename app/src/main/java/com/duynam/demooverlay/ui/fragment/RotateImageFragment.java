package com.duynam.demooverlay.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.FragmentRotateImageBinding;
import com.duynam.demooverlay.ui.activity.activity_edit_image.EditImageActivity;
import com.duynam.demooverlay.ui.custorm.HorizontalProgressWheelView;

public class RotateImageFragment extends Fragment {

    private FragmentRotateImageBinding rotateImageBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rotateImageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_rotate_image, container, false);
        rotate();
        return rotateImageBinding.getRoot();
    }

    private void rotate(){
        rotateImageBinding.progressRotate.setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
            @Override
            public void onScrollStart() {

            }

            @Override
            public void onScroll(float delta, float totalDistance) {
                if (getActivity() != null && getActivity() instanceof EditImageActivity){
                    EditImageActivity activity = ((EditImageActivity) getActivity());
                    activity.matrix.postRotate(delta / 42, activity.w / 2f, activity.h / 2f);
                    activity.imageBinding.imgContainer.setImageMatrix(activity.matrix);
                }
            }

            @Override
            public void onScrollEnd() {

            }
        });
    }

}