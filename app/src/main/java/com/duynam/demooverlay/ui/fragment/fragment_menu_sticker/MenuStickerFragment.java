package com.duynam.demooverlay.ui.fragment.fragment_menu_sticker;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.FragmentMenuStickerBinding;
import com.duynam.demooverlay.ui.activity.activity_edit_image.EditImageActivity;
import com.duynam.demooverlay.utils.Constant;

public class MenuStickerFragment extends Fragment implements MenuStickerAdapter.OnClickSticker{

    private FragmentMenuStickerBinding binding;
    private MenuStickerAdapter menuStickerAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu_sticker, container, false);
        initRecycleview();
        return binding.getRoot();
    }

    private void initRecycleview(){
        menuStickerAdapter = new MenuStickerAdapter(getContext());
        menuStickerAdapter.setOnClickSticker(this);
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        binding.rvSticker.setLayoutManager(linearLayoutManager);
        binding.rvSticker.setAdapter(menuStickerAdapter);
    }

    @Override
    public void chooseStickerFinish(String name) {
        if (getActivity() != null && getActivity() instanceof EditImageActivity){
            ((EditImageActivity) getActivity()).addStickerImage(Constant.PATCH_STICKER + "/" + name);
        }
    }


}