package com.duynam.demooverlay.ui.fragment.fragment_add_text;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.FragmentAddTextMenuBinding;
import com.duynam.demooverlay.ui.activity.activity_edit_image.EditImageActivity;
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
        return binding.getRoot();
    }

    private void editTextTransparency() {
        binding.ctlTransparency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, OpacityFragment.newInstance(false, true, false)).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void addText() {
        binding.ctlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null && getActivity() instanceof EditImageActivity) {
                    ((EditImageActivity) getActivity()).addTextImage();
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
                if (getActivity() != null && getActivity() instanceof EditImageActivity) {
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, OpacityFragment.newInstance(false, false, true)).addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    private void setColor() {
        binding.ctlColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, ColorFragment.newInstance()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

}