package com.duynam.demooverlay.ui.fragment.fragment_add_text;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.FragmentAddTextMenuBinding;
import com.duynam.demooverlay.ui.activity.activity_edit_image.EditImageActivity;
import com.duynam.demooverlay.ui.custorm.BubbleTextView;
import com.duynam.demooverlay.ui.fragment.fragment_color.ColorFragment;
import com.duynam.demooverlay.ui.fragment.fragment_opacity.OpacityFragment;

public class AddTextMenuFragment extends Fragment {

    private FragmentAddTextMenuBinding binding;
    private FragmentTransaction fragmentTransaction;

    public static AddTextMenuFragment newInstance(String param1, String param2) {
        AddTextMenuFragment fragment = new AddTextMenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_text_menu, container, false);
        addText();
        editTextTransparency();
        editTextSize();
        setColor();
        binding.ctlColor.setEnabled(false);
        binding.ctlTransparency.setEnabled(false);
        binding.ctlSize.setEnabled(false);
        checkTV();
        return binding.getRoot();
    }

    private void checkTV(){
        if (getActivity() != null && getActivity() instanceof EditImageActivity) {
            EditImageActivity activity = ((EditImageActivity) getActivity());
            if (activity.mViews.size() != 0){
                for (int i = 0; i < activity.mViews.size(); i++) {
                    if (activity.mViews.get(i) instanceof BubbleTextView){
                        binding.ctlColor.setEnabled(true);
                        binding.ctlTransparency.setEnabled(true);
                        binding.ctlSize.setEnabled(true);
                        break;
                    }
                }
            }
        }
    }

    private void editTextTransparency() {
        binding.ctlTransparency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fragmentTransaction = getFragmentManager().beginTransaction();
                //fragmentTransaction.replace(R.id.container, OpacityFragment.newInstance(false, true, false)).addToBackStack(null);
                //fragmentTransaction.commit();
                if (getActivity() != null && getActivity() instanceof EditImageActivity){
                    EditImageActivity activity = ((EditImageActivity) getActivity());
                    activity.isCheckOpacityText = true;
                    activity.isCheckTextSize = false;
                    activity.imageBinding.ctlSeekbarTv.setVisibility(View.VISIBLE);
                    activity.imageBinding.flAddText.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addText() {
        binding.ctlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null && getActivity() instanceof EditImageActivity) {
                    ((EditImageActivity) getActivity()).addText();
                    binding.ctlColor.setEnabled(true);
                    binding.ctlTransparency.setEnabled(true);
                    binding.ctlSize.setEnabled(true);
                }
            }
        });
    }

    private void editTextSize() {
        binding.ctlSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (getActivity() != null && getActivity() instanceof EditImageActivity) {
//                    fragmentTransaction = getFragmentManager().beginTransaction();
//                    fragmentTransaction.add(R.id.container, OpacityFragment.newInstance(false, false, true)).addToBackStack(null);
//                    fragmentTransaction.commit();
//                }
                if (getActivity() != null && getActivity() instanceof EditImageActivity){
                    EditImageActivity activity = ((EditImageActivity) getActivity());
                    activity.isCheckTextSize = true;
                    activity.isCheckOpacityText = false;
                    activity.imageBinding.sbTransparency.setProgress(0);
                    activity.imageBinding.ctlSeekbarTv.setVisibility(View.VISIBLE);
                    activity.imageBinding.flAddText.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setColor() {
        binding.ctlColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.container, ColorFragment.newInstance()).addToBackStack(null);
//                fragmentTransaction.commit();
                if (getActivity() != null && getActivity() instanceof EditImageActivity){
                    EditImageActivity activity = ((EditImageActivity) getActivity());
                    activity.isCheckTextSize = false;
                    activity.isCheckOpacityText = false;
                    activity.imageBinding.flColor.setVisibility(View.VISIBLE);
                    activity.imageBinding.flAddText.setVisibility(View.GONE);
                }
            }
        });
    }

}