package com.duynam.demooverlay.ui.fragment.fragment_filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.FragmentFilterBinding;

public class FilterFragment extends Fragment {

    private FragmentFilterBinding filterBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        filterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false);
        return filterBinding.getRoot();
    }
}
