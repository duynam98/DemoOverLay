package com.duynam.demooverlay.ui.fragment.fragment_opacity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.FragmentOpacityBinding;
import com.duynam.demooverlay.ui.activity.activity_edit_image.EditImageActivity;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpacityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpacityFragment extends Fragment {

    private FragmentOpacityBinding binding;
    private static final String ISCHECKTVIEW = "isCheckTView";
    private static final String ISCHECKEDITTEXTVIEW = "isCheckRditTextView";
    private static final String ISCHECKTEXTSIZE = "isCheckTextSize";
    private boolean isCheckTView, isCheckEditText, isCheckTextSize;

    public static OpacityFragment newInstance(boolean isCheckTView, boolean isCheckEditTextView, boolean isCheckTextSize) {
        OpacityFragment fragment = new OpacityFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ISCHECKTVIEW, isCheckTView);
        bundle.putBoolean(ISCHECKEDITTEXTVIEW, isCheckEditTextView);
        bundle.putBoolean(ISCHECKTEXTSIZE, isCheckTextSize);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_opacity, container, false);
        Bundle bundle = getArguments();
        if (bundle != null){
            isCheckTView = bundle.getBoolean(ISCHECKTVIEW);
            isCheckEditText = bundle.getBoolean(ISCHECKEDITTEXTVIEW);
            isCheckTextSize = bundle.getBoolean(ISCHECKTEXTSIZE);
        }
        setTransparncyStickerView();
        cancelTransparency();
        return binding.getRoot();
    }

    private void setTransparncyStickerView() {
        binding.sbTransparency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (isCheckTView){
                    if (getActivity() != null && getActivity() instanceof EditImageActivity){
                        binding.tvTransparency.setText(i + "%");
                        if (((EditImageActivity) getActivity()).mCurrentTView != null){
                            ((EditImageActivity) getActivity()).mCurrentTView.setOpacity((int) (255 - Math.round(seekBar.getProgress() * 2.55)));
                            ((EditImageActivity) getActivity()).mCurrentTView.invalidate();
                        }
                    }
                }
                if (isCheckEditText){
                    if (getActivity() != null && getActivity() instanceof EditImageActivity){
                        binding.tvTransparency.setText(i + "%");
                        if (((EditImageActivity) getActivity()).mCurrentEditTextView != null){
                            ((EditImageActivity) getActivity()).mCurrentEditTextView.setOpacity((int) (255 - Math.round(seekBar.getProgress() * 2.55)));
                            ((EditImageActivity) getActivity()).mCurrentEditTextView.invalidate();
                        }
                    }
                }
                if (isCheckTextSize){
                    if (getActivity() != null && getActivity() instanceof EditImageActivity ){
                        if (((EditImageActivity) getActivity()).mCurrentEditTextView != null){
                            ((EditImageActivity) getActivity()).mCurrentEditTextView.setSize(16 + ((float) (seekBar.getProgress()*0.3)));
                            ((EditImageActivity) getActivity()).mCurrentEditTextView.invalidate();
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void cancelTransparency() {
        binding.imgCancleTransparency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheckTView){
                    if (getActivity() != null && getActivity() instanceof EditImageActivity ){
                        if (((EditImageActivity) getActivity()).mCurrentTView != null){
                            ((EditImageActivity) getActivity()).mCurrentTView.setAlpha((float) 1.0);
                            binding.sbTransparency.setProgress(0);
                        }
                    }
                }
                if (isCheckEditText){
                    if (getActivity() != null && getActivity() instanceof EditImageActivity ){
                        if (((EditImageActivity) getActivity()).mCurrentEditTextView != null){
                            ((EditImageActivity) getActivity()).mCurrentEditTextView.setAlpha((float) 1.0);
                            binding.sbTransparency.setProgress(0);
                        }
                    }
                }
                if (isCheckTextSize){

                }
                getActivity().getSupportFragmentManager().beginTransaction().remove(OpacityFragment.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        binding.imgDoneTransparency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheckTView){
                    if (getActivity() != null && getActivity() instanceof EditImageActivity){
                        setTransparncyStickerView();
                    }
                }
                if (isCheckEditText){

                }
                getActivity().getSupportFragmentManager().beginTransaction().remove(OpacityFragment.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

}