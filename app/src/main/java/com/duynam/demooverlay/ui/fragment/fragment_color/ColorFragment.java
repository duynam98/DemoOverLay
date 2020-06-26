package com.duynam.demooverlay.ui.fragment.fragment_color;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.FragmentColorBinding;
import com.duynam.demooverlay.ui.activity.activity_edit_image.EditImageActivity;

import java.util.ArrayList;
import java.util.List;

public class ColorFragment extends Fragment implements ColorAdapter.OnClickColor {

    private FragmentColorBinding binding;
    private ColorAdapter colorAdapter;
    private List<String> listColor ;
    private String[] arr_color;
    private LinearLayoutManager linearLayoutManager;

    public static ColorFragment newInstance() {
        ColorFragment fragment = new ColorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_color, container, false);
        initAdapter();
        getAllImage();
        initRecycleViewColor();
        return binding.getRoot();
    }

    private void getAllImage(){
        listColor = new ArrayList<>();
        arr_color = getResources().getStringArray(R.array.color);
        for (int i = 0; i < arr_color.length; i++) {
            listColor.add(arr_color[i]);
        }
        colorAdapter.setData(listColor);
    }

    private void initAdapter(){
        colorAdapter = new ColorAdapter(getContext());
        colorAdapter.setOnClickColor(this);
    }

    private void initRecycleViewColor(){
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        binding.rvListColor.setLayoutManager(linearLayoutManager);
        binding.rvListColor.setAdapter(colorAdapter);
    }

    @Override
    public void onSetColor(String color) {
        if (getActivity() != null && getActivity() instanceof EditImageActivity){
            if (((EditImageActivity) getActivity()).mCurrentEditTextView != null){
                ((EditImageActivity) getActivity()).mCurrentEditTextView.setColor(Color.parseColor(color));
            }
        }
    }
}