package com.duynam.demooverlay.ui.activity.activity_edit_image;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.ActivityEditImageBinding;
import com.duynam.demooverlay.ui.activity.activity_filter.FilterAdapter;
import com.duynam.demooverlay.ui.activity.activity_filter.SaveImage;
import com.duynam.demooverlay.ui.activity.activity_home.MainActivity;
import com.duynam.demooverlay.ui.custorm.BlurImage;
import com.duynam.demooverlay.ui.custorm.BubbleTextView;
import com.duynam.demooverlay.ui.custorm.StartPointSeekBar;
import com.duynam.demooverlay.ui.custorm.StickerView;
import com.duynam.demooverlay.ui.fragment.fragment_add_text.AddTextMenuFragment;
import com.duynam.demooverlay.ui.fragment.fragment_color.ColorAdapter;
import com.duynam.demooverlay.ui.fragment.fragment_menu_sticker.MenuStickerAdapter;
import com.duynam.demooverlay.ui.fragment.fragment_menu_sticker.MenuStickerFragment;
import com.duynam.demooverlay.utils.Constant;
import com.filter.base.GPUImageFilter;
import com.filter.helper.FilterManager;
import com.xiaopo.flying.sticker.DialogDrawable;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class EditImageActivity extends AppCompatActivity implements MenuAdapter.OnClick, ColorAdapter.OnClickColor, FilterAdapter.OnClickFilter,
        MenuStickerAdapter.OnClickSticker, com.xiaopo.flying.sticker.StickerView.OnStickerClickListener, FontTextAdapter.OnClickFont {

    public ActivityEditImageBinding imageBinding;
    public Matrix matrix = new Matrix();
    public int w, h;
    private FragmentTransaction fragmentTransaction;
    private LinearLayoutManager linearLayoutManager;
    private MenuAdapter menuAdapter;

    //fragment
    private MenuStickerFragment menuStickerFragment;
    private AddTextMenuFragment addTextMenuFragment;

    //adapter
    private ColorAdapter colorAdapter;
    private FilterAdapter filterAdapter;
    private MenuStickerAdapter menuStickerAdapter;
    private FontTextAdapter fontTextAdapter;

    public ArrayList<View> mViews;
    public ArrayList<View> mStickerViewAdd;
    public StickerView mCurrentTView;
    public BubbleTextView mCurrentEditTextView;

    private Sticker mCurrentSticker;
    public long lastTimeClick = 0;

    public Bitmap bitmap;

    public boolean isCheckOpacityText, isCheckStickerView, isBlur,
            isCheckTextSize, isCheckStroke, isCheckShadow, isBrightness, isContrast;

    private SaveImage saveImageAsyntask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_image);
        initImage();
        init();
        initRecycleViewMenu();
        initRecycleViewColor();
        initRecycleViewSticker();
        initRecycleViewFont();
        doneAddText();
        menuTop();
        finishAddSticker();
        finishAddText();
        finishFilter();
        setTransparency();
        doneTransparency();
        onChangeTwoSeekBar();
        clickMenuAdjust();
        finishAdjust();
        clickMenuTwoSeekBar();
        FilterManager.init(this);
    }

    private void initRecycleViewMenu() {
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        menuAdapter = new MenuAdapter(this);
        menuAdapter.setOnClick(this);
        imageBinding.rvmenuEdit.setLayoutManager(linearLayoutManager);
        imageBinding.rvmenuEdit.setAdapter(menuAdapter);
    }

    private void initImage() {
        imageBinding.imgSticker.setOnStickerClickListener(this);
        if (getIntent() != null) {
            String path = getIntent().getStringExtra(Constant.PATCH_IMAGE);
            Glide.with(this).asBitmap().load(Uri.parse(path)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    imageBinding.imgSticker.setImageBitmap(resource);
                    reSizeBitmap(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });


        }
    }

    private void reSizeBitmap(Bitmap bitmap) {
        int wb = bitmap.getWidth();
        int hb = bitmap.getHeight();
        if (wb > hb) {
            wb = imageBinding.rootView.getWidth();
            hb = imageBinding.rootView.getWidth() * bitmap.getHeight() / bitmap.getWidth();
            setNewWithHeight(wb, hb);
        } else if (hb > wb) {
            hb = imageBinding.rootView.getHeight();
            wb = imageBinding.rootView.getHeight() * bitmap.getWidth() / bitmap.getHeight();
            setNewWithHeight(wb, hb);
        } else {
            wb = imageBinding.rootView.getWidth();
            hb = imageBinding.rootView.getWidth() * bitmap.getHeight() / bitmap.getWidth();
            setNewWithHeight(wb, hb);
        }
    }

    private void setNewWithHeight(int wb, int hb) {
        int new_wC, new_hC;
        int max_cW = imageBinding.rootView.getWidth();
        int max_cH = imageBinding.rootView.getHeight();
        float a = max_cW * hb - max_cH * wb;
        if (a > 0) {
            new_wC = wb;
            new_hC = (max_cW * hb) / wb;
        } else {
            new_wC = max_cW;
            new_hC = (max_cW * hb) / wb;
        }
        setSizeRllSave(new_wC, new_hC);
    }

    public void setSizeRllSave(int w, int h) {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(w, h);
        params.topToTop = R.id.parent_ctl;
        params.bottomToBottom = R.id.parent_ctl;
        params.startToStart = R.id.parent_ctl;
        params.endToEnd = R.id.parent_ctl;
        imageBinding.rootView.setLayoutParams(params);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }


    public void init() {
        imageBinding.sbBrightness.setProgress(0);
        imageBinding.imgFilter.setAlpha(0);
        imageBinding.imgSticker.setAlpha((float) 1.0);
        imageBinding.ctlStickerBar.setVisibility(View.GONE);
        imageBinding.flSticker.setVisibility(View.GONE);
        imageBinding.ctlFilterBar.setVisibility(View.GONE);
        imageBinding.ctlAddTextBar.setVisibility(View.GONE);
        imageBinding.flAddText.setVisibility(View.GONE);
        imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
        imageBinding.flColor.setVisibility(View.GONE);
        imageBinding.ctlFont.setVisibility(View.GONE);
        imageBinding.ctlAdjustBar.setVisibility(View.GONE);
        addTextMenuFragment = new AddTextMenuFragment();
        menuStickerFragment = new MenuStickerFragment();
        mViews = new ArrayList<>();
        mStickerViewAdd = new ArrayList<>();
        colorAdapter = new ColorAdapter(this);
        fontTextAdapter = new FontTextAdapter(this);
        menuStickerAdapter = new MenuStickerAdapter(this);
    }

    @Override
    public void OnClickMenu(int position) {
        switch (position) {
            case 0:
                imageBinding.ctlAddTextBar.setVisibility(View.VISIBLE);
                imageBinding.toolbarView.setVisibility(View.GONE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fl_addText, addTextMenuFragment).addToBackStack(null);
                fragmentTransaction.commit();
                imageBinding.flAddText.setVisibility(View.VISIBLE);
                break;
            case 1:
                imageBinding.ctlStickerBar.setVisibility(View.VISIBLE);
                imageBinding.toolbarView.setVisibility(View.GONE);
                imageBinding.flSticker.setVisibility(View.VISIBLE);
                imageBinding.sbTransparency.setProgress(0);
                break;
            case 2:
                imageBinding.ctlFilterBar.setVisibility(View.VISIBLE);
                imageBinding.toolbarView.setVisibility(View.GONE);
                bitmapToGpuImage(getCurrentBitmap());
                initRecycleViewFilter();
                imageBinding.flFilter.setVisibility(View.VISIBLE);
                imageBinding.imgFilter.setFilter(FilterManager.getInstance().getFilter(FilterManager.getInstance().types[0]));
                break;
            case 3:
                setImageBitmapGlide(getCurrentBitmap(), imageBinding.imgBrightness);
                imageBinding.ctlAdjustBar.setVisibility(View.VISIBLE);
                imageBinding.flAdjust.setVisibility(View.VISIBLE);
                imageBinding.toolbarView.setVisibility(View.GONE);
                imageBinding.frameMenu.setVisibility(View.GONE);
                break;
        }
    }

    private void clickMenuAdjust() {
        imageBinding.includeAdjust.ctlBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBrightness = true;
                imageBinding.flAdjust.setVisibility(View.GONE);
                imageBinding.ctlTwoSeekBar.setVisibility(View.VISIBLE);
                imageBinding.imgBrightness.setVisibility(View.VISIBLE);
                imageBinding.imgBrightness.setBright(0);
                imageBinding.imgBrightness.setContrast(0);
            }
        });

        imageBinding.includeAdjust.ctlContrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isContrast = true;
                imageBinding.flAdjust.setVisibility(View.GONE);
                imageBinding.ctlTwoSeekBar.setVisibility(View.VISIBLE);
                imageBinding.imgBrightness.setVisibility(View.VISIBLE);
                imageBinding.imgBrightness.setBright(0);
                imageBinding.imgBrightness.setContrast(0);
            }
        });

        imageBinding.includeAdjust.ctlBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBlur = true;
                bitmap = getCurrentBitmap();
                imageBinding.flAdjust.setVisibility(View.GONE);
                imageBinding.ctlSeekbarTv.setVisibility(View.VISIBLE);
                imageBinding.imgBrightness.setVisibility(View.GONE);
                imageBinding.imgFilter.setVisibility(View.GONE);
                imageBinding.imgSticker.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onChangeTwoSeekBar() {
        imageBinding.sbBrightness.setOnSeekBarChangeListener(new StartPointSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onOnSeekBarValueChange(StartPointSeekBar bar, double value) {
                if (isBrightness == true) {
                    imageBinding.imgBrightness.setBright((float) value);
                } else {
                    imageBinding.imgBrightness.setContrast((float) value);
                }
            }
        });
    }

    private void bitmapToGpuImage(Bitmap bitmap) {
        Glide.with(this).asBitmap().load(bitmap).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageBinding.imgFilter.setImage(resource);
                imageBinding.imgFilter.setAlpha(1);
                imageBinding.imgFilter.setVisibility(View.VISIBLE);
                imageBinding.imgFilter.getGPUImage().setFilter(FilterManager.getInstance().getFilter(FilterManager.getInstance().types[0]));
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    private Bitmap getBitmapFromAsset(String patch) {
        Bitmap bitmap = null;
        try {
            InputStream ims = getAssets().open(patch);
            bitmap = BitmapFactory.decodeStream(ims);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void menuTop() {
        imageBinding.imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentTView != null) {
                    mCurrentTView.setInEdit(false);
                }
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                initDialog();
            }
        });
        imageBinding.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void clickMenuTwoSeekBar() {
        imageBinding.imgDoneBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImageBitmapGlide(getCurrentBitmap(), imageBinding.imgSticker);
                imageBinding.ctlTwoSeekBar.setVisibility(View.GONE);
                imageBinding.flAdjust.setVisibility(View.GONE);
                imageBinding.frameMenu.setVisibility(View.VISIBLE);
                imageBinding.imgBrightness.setVisibility(View.GONE);
                imageBinding.ctlAdjustBar.setVisibility(View.GONE);
                imageBinding.toolbarView.setVisibility(View.VISIBLE);
                imageBinding.sbBrightness.setProgress(0);
                isContrast = false;
                isBrightness = false;
            }
        });
        imageBinding.imgCancelBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBinding.sbBrightness.setProgress(0);
                if (isBrightness) {
                    imageBinding.imgBrightness.setBright(0);
                }
                if (isContrast) {
                    imageBinding.imgBrightness.setContrast(0);
                }
                imageBinding.ctlTwoSeekBar.setVisibility(View.GONE);
                imageBinding.flAdjust.setVisibility(View.VISIBLE);
                isContrast = false;
                isBrightness = false;
            }
        });
    }

    private void doneAddText() {
        imageBinding.imgAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentSticker != null && mCurrentSticker instanceof DrawableSticker) {
                    ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable()).setText(imageBinding.edtAddText.getText().toString());
                    imageBinding.ctlIputEdt.setVisibility(View.GONE);
                    imageBinding.imgSticker.invalidate();
                    imageBinding.flAddText.setVisibility(View.VISIBLE);
                    imageBinding.ctlAddTextBar.setVisibility(View.VISIBLE);
                    addMenuAddText();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    public void addText() {
        DialogDrawable dialogDrawable = new DialogDrawable();
        imageBinding.imgSticker.addSticker(dialogDrawable);
        mCurrentSticker = new DrawableSticker(dialogDrawable);
    }

    private void clearTextView() {
        imageBinding.imgSticker.setHandling();
        imageBinding.imgSticker.clearSticker();
    }

    private void clearSticker() {
        imageBinding.imgSticker.setHandling();
        imageBinding.imgSticker.clearSticker();
    }

    private Bitmap getCurrentBitmap() {
        Bitmap bitmap_image = Bitmap.createBitmap(imageBinding.rootView.getWidth(), imageBinding.rootView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap_image);
        imageBinding.rootView.draw(canvas);
        return bitmap_image;
    }

    private void finishAdjust() {
        imageBinding.imgDoneAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImageBitmapGlide(getCurrentBitmap(), imageBinding.imgSticker);
                imageBinding.ctlAdjustBar.setVisibility(View.GONE);
                imageBinding.toolbarView.setVisibility(View.VISIBLE);
                imageBinding.ctlTwoSeekBar.setVisibility(View.GONE);
                imageBinding.flAdjust.setVisibility(View.GONE);
                imageBinding.imgBrightness.setVisibility(View.GONE);
                imageBinding.sbBrightness.setProgress(0);
                imageBinding.frameMenu.setVisibility(View.VISIBLE);
            }
        });
    }

    private void finishAddText() {
        imageBinding.imgDoneAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBinding.imgSticker.setHandling();
                imageBinding.ctlAddTextBar.setVisibility(View.GONE);
                setImageBitmapGlide(getCurrentBitmap(), imageBinding.imgSticker);
                clearTextView();
                imageBinding.flColor.setVisibility(View.GONE);
                imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                imageBinding.flAddText.setVisibility(View.GONE);
                imageBinding.toolbarView.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().remove(addTextMenuFragment);
                getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void finishAddSticker() {
        imageBinding.imgDoneSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBinding.imgSticker.setHandling();
                imageBinding.ctlStickerBar.setVisibility(View.GONE);
                imageBinding.flSticker.setVisibility(View.GONE);
                imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                imageBinding.toolbarView.setVisibility(View.VISIBLE);
                setImageBitmapGlide(getCurrentBitmap(), imageBinding.imgSticker);
                getSupportFragmentManager().beginTransaction().remove(menuStickerFragment).commit();
                getSupportFragmentManager().popBackStack();
                isCheckStickerView = false;
                clearSticker();
            }
        });
    }

    private void finishFilter() {
        imageBinding.imgDoneFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bitmap bitmap = imageBinding.imgFilter.getGPUImage().getBitmapWithFilterApplied();
                imageBinding.ctlFilterBar.setVisibility(View.GONE);
                setImageBitmapGlide(bitmap, imageBinding.imgSticker);
                imageBinding.flFilter.setVisibility(View.GONE);
                imageBinding.imgFilter.setVisibility(View.GONE);
                imageBinding.toolbarView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setImageBitmapGlide(final Bitmap bitmap, final ImageView imageView) {
        Glide.with(this).asBitmap().load(bitmap).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (imageBinding.flAddText.getVisibility() == View.VISIBLE) {
            clearTextView();
            imageBinding.ctlAddTextBar.setVisibility(View.GONE);
            imageBinding.flAddText.setVisibility(View.GONE);
            imageBinding.toolbarView.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().remove(addTextMenuFragment).commit();
            getSupportFragmentManager().popBackStack();
        } else if (imageBinding.ctlIputEdt.getVisibility() == View.VISIBLE) {
            imageBinding.ctlIputEdt.setVisibility(View.GONE);
            imageBinding.flAddText.setVisibility(View.VISIBLE);
            imageBinding.ctlAddTextBar.setVisibility(View.VISIBLE);
            addMenuAddText();
        } else if (imageBinding.flColor.getVisibility() == View.VISIBLE) {
            imageBinding.flColor.setVisibility(View.GONE);
            imageBinding.flAddText.setVisibility(View.VISIBLE);
            addMenuAddText();
        } else if (imageBinding.ctlSeekbarTv.getVisibility() == View.VISIBLE) {
            imageBinding.tvTransparency.setText("");
            imageBinding.sbTransparency.setProgress(0);
            if (isCheckStickerView) {
                isCheckStickerView = false;
                imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                imageBinding.flSticker.setVisibility(View.VISIBLE);
            } else if (isBlur) {
                setImageBitmapGlide(bitmap, imageBinding.imgSticker);
                isBlur = false;
                imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                imageBinding.flAdjust.setVisibility(View.VISIBLE);
            } else {
                isCheckShadow = false;
                isCheckStroke = false;
                isCheckTextSize = false;
                isCheckOpacityText = false;
                imageBinding.sbTransparency.setProgress(0);
                imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                imageBinding.flAddText.setVisibility(View.VISIBLE);
                addMenuAddText();
            }
        } else if (imageBinding.ctlFont.getVisibility() == View.VISIBLE) {
            imageBinding.ctlFont.setVisibility(View.GONE);
            imageBinding.flAddText.setVisibility(View.VISIBLE);
        } else if (imageBinding.ctlTwoSeekBar.getVisibility() == View.VISIBLE) {
            if (isBrightness) {
                imageBinding.imgBrightness.setBright(0);
            }
            if (isContrast) {
                imageBinding.imgBrightness.setContrast(0);
            }
            imageBinding.ctlTwoSeekBar.setVisibility(View.GONE);
            imageBinding.flAdjust.setVisibility(View.VISIBLE);
            imageBinding.sbBrightness.setProgress(0);
        } else if (imageBinding.flAdjust.getVisibility() == View.VISIBLE) {
            imageBinding.flAdjust.setVisibility(View.GONE);
            imageBinding.ctlAdjustBar.setVisibility(View.GONE);
            imageBinding.toolbarView.setVisibility(View.VISIBLE);
            imageBinding.imgBrightness.setVisibility(View.GONE);
            imageBinding.frameMenu.setVisibility(View.VISIBLE);
        } else if (imageBinding.flFilter.getVisibility() == View.VISIBLE) {
            imageBinding.flFilter.setVisibility(View.GONE);
            imageBinding.imgFilter.setAlpha(0);
            imageBinding.ctlFilterBar.setVisibility(View.GONE);
            imageBinding.toolbarView.setVisibility(View.VISIBLE);
        } else if (imageBinding.flSticker.getVisibility() == View.VISIBLE) {
            isCheckStickerView = false;
            clearSticker();
            imageBinding.flSticker.setVisibility(View.GONE);
            imageBinding.ctlStickerBar.setVisibility(View.GONE);
            imageBinding.toolbarView.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    private void addMenuAddText() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_addText, addTextMenuFragment);
        fragmentTransaction.commit();
    }

    private void initDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_save);
        ImageView img_yes = dialog.findViewById(R.id.img_yes);
        ImageView img_cancel = dialog.findViewById(R.id.img_no);

        img_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                saveImage();
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void saveImage() {
        imageBinding.progress.setVisibility(View.VISIBLE);
        Bitmap bitmap = getCurrentBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        saveImageAsyntask = new SaveImage(getApplicationContext());
        SaveImage.OnSaveImage onSaveImage = new SaveImage.OnSaveImage() {
            @Override
            public void onSaveImage(boolean isSave) {
                imageBinding.progress.setVisibility(View.GONE);
                startActivity(new Intent(EditImageActivity.this, MainActivity.class));
                finish();
            }
        };
        saveImageAsyntask.setOnSaveImage(onSaveImage);
        saveImageAsyntask.execute(bitmap);
    }

    private void setTransparency() {
        imageBinding.sbTransparency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int i, boolean b) {
                if (isCheckStickerView) {
                    if (imageBinding.imgSticker.mStickers.size() > 0 && mCurrentSticker != null) {
                        imageBinding.tvTransparency.setText(i + "%");
                        imageBinding.imgSticker.setAlphaLogo((int) (255 - Math.round(seekBar.getProgress() * 2.55)));
                    }
                }
                if (isCheckStroke) {
                    if (mCurrentSticker != null && mCurrentSticker instanceof DrawableSticker) {
                        if ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable() != null) {
                            imageBinding.tvTransparency.setText(i + "%");
                            //int color = ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable()).getColor();
                            ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable()).setStroke((Math.round(seekBar.getProgress() * 0.1)), "141414");
                            imageBinding.imgSticker.invalidate();
                        }
                    }
                }
                if (isCheckShadow) {
                    if (mCurrentSticker != null && mCurrentSticker instanceof DrawableSticker) {
                        if ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable() != null) {
                            imageBinding.tvTransparency.setText(i + "%");
                            ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable()).setShadow(Math.round(seekBar.getProgress() * 0.1),
                                    Math.round(seekBar.getProgress() * 0.1), Math.round(seekBar.getProgress() * 0.1), "141414");
                            imageBinding.imgSticker.invalidate();
                        }
                    }
                }
                if (isCheckOpacityText) {
                    if (mCurrentSticker != null && mCurrentSticker instanceof DrawableSticker) {
                        if ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable() != null) {
                            imageBinding.tvTransparency.setText(i + "%");
                            ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable()).setOpacityText((int) (255 - Math.round(seekBar.getProgress() * 2.55)));
                            imageBinding.imgSticker.invalidate();
                        }
                    }
                }
                if (isCheckTextSize) {
                    if (mCurrentSticker != null && mCurrentSticker instanceof DrawableSticker) {
                        if ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable() != null) {
                            imageBinding.tvTransparency.setText(i + "%");
                            ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable()).setTextSize(16 + (Math.round(seekBar.getProgress() * 0.4)));
                            imageBinding.imgSticker.invalidate();
                        }
                    }
                }
                if (isBlur) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int blur = 1;
                            if (seekBar.getProgress() / 4 > 0) {
                                blur = seekBar.getProgress() / 4;
                                BlurImage.withContext(getApplicationContext())
                                        .setBlurRadius(blur)
                                        .blurFromBitmap(bitmap).into(imageBinding.imgSticker);
                            }
                        }
                    });
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

    private void doneTransparency() {
        imageBinding.imgDoneTransparency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTransparency();
                if (isCheckStickerView) {
                    imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                    imageBinding.flSticker.setVisibility(View.VISIBLE);
                    imageBinding.tvTransparency.setText("");
                } else if (isBlur) {
                    isBlur = false;
                    imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                    imageBinding.flAdjust.setVisibility(View.VISIBLE);
                    setImageBitmapGlide(getCurrentBitmap(), imageBinding.imgSticker);
                    imageBinding.flAdjust.setVisibility(View.GONE);
                    imageBinding.frameMenu.setVisibility(View.VISIBLE);
                    imageBinding.imgBrightness.setVisibility(View.GONE);
                    imageBinding.ctlAdjustBar.setVisibility(View.GONE);
                    imageBinding.toolbarView.setVisibility(View.VISIBLE);
                } else {
                    isCheckShadow = false;
                    isCheckStroke = false;
                    isCheckTextSize = false;
                    isCheckOpacityText = false;
                    imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                    imageBinding.flAddText.setVisibility(View.VISIBLE);
                    imageBinding.sbTransparency.setProgress(0);
                    imageBinding.tvTransparency.setText("");
                }
            }
        });

        imageBinding.imgCancleTransparency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheckStickerView) {
                    imageBinding.sbTransparency.setProgress(0);
                    imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                    imageBinding.flSticker.setVisibility(View.VISIBLE);
                    imageBinding.sbTransparency.setProgress(0);
                    imageBinding.tvTransparency.setText("");
                } else if (isBlur) {
                    imageBinding.sbTransparency.setProgress(1);
                    setImageBitmapGlide(bitmap, imageBinding.imgSticker);
                    isBlur = false;
                    isCheckOpacityText = false;
                    isCheckTextSize = false;
                    imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                    imageBinding.flAdjust.setVisibility(View.VISIBLE);
                    imageBinding.tvTransparency.setText("");
                } else if (isCheckOpacityText) {
                    imageBinding.sbTransparency.setProgress(0);
                    imageBinding.tvTransparency.setText("");
                    isCheckOpacityText = false;
                    imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                    imageBinding.flAddText.setVisibility(View.VISIBLE);
                } else {
                    imageBinding.sbTransparency.setProgress(0);
                    imageBinding.tvTransparency.setText("");
                    isCheckShadow = false;
                    isCheckStroke = false;
                    isCheckTextSize = false;
                    isCheckOpacityText = false;
                    imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                    imageBinding.flAddText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initRecycleViewColor() {
        colorAdapter.setOnClickColor(this);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        imageBinding.rvColor.setLayoutManager(linearLayoutManager);
        imageBinding.rvColor.setAdapter(colorAdapter);
    }

    private void initRecycleViewFont() {
        fontTextAdapter.setOnClickFont(this);
        linearLayoutManager = new LinearLayoutManager(this);
        imageBinding.rvFont.setLayoutManager(linearLayoutManager);
        imageBinding.rvFont.setAdapter(fontTextAdapter);
    }

    private void initRecycleViewSticker() {
        menuStickerAdapter.setOnClickSticker(this);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        imageBinding.rvSticker.setLayoutManager(linearLayoutManager);
        imageBinding.rvSticker.setAdapter(menuStickerAdapter);
    }

    private void initRecycleViewFilter() {
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        filterAdapter = new FilterAdapter(this);
        filterAdapter.setOnClickFilter(this);
        filterAdapter.setBitmap(getCurrentBitmap());
        imageBinding.rvFilter.setLayoutManager(linearLayoutManager);
        imageBinding.rvFilter.setAdapter(filterAdapter);
    }

    @Override
    public void onSetColor(String color) {
        if (((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable()) != null) {
            ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable()).setColorText(Color.parseColor(color));
            imageBinding.imgSticker.invalidate();
        }
    }

    @Override
    public void onClickFilter(GPUImageFilter gpuImageFilter) {
        imageBinding.imgFilter.setFilter(gpuImageFilter);
    }

    @Override
    public void chooseStickerFinish(String name) {
        Bitmap bitmap = getBitmapFromAsset(Constant.PATCH_STICKER + "/" + name);
        imageBinding.imgSticker.addSticker(bitmap);
        mCurrentSticker = imageBinding.imgSticker.getSticker();
        isCheckStickerView = true;
    }


    @Override
    public void onStickerClick(Sticker sticker) {
        if (sticker != null) {
            if (((DrawableSticker) sticker).getDrawable() instanceof DialogDrawable) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastTimeClick < 300) {
                    imageBinding.ctlIputEdt.setVisibility(View.VISIBLE);
                    imageBinding.flAddText.setVisibility(View.GONE);
                    imageBinding.ctlAddTextBar.setVisibility(View.GONE);
                    imageBinding.toolbarView.setVisibility(View.GONE);
                    imageBinding.ctlSeekbarTv.setVisibility(View.GONE);
                    imageBinding.flColor.setVisibility(View.GONE);
                }
                lastTimeClick = clickTime;
            }
            mCurrentSticker = sticker;
        }
    }

    @Override
    public void onOpacity() {
        imageBinding.ctlSeekbarTv.setVisibility(View.VISIBLE);
        imageBinding.flSticker.setVisibility(View.GONE);
        setTransparency();
    }

    public void setAlphaProgressTextView() {
        if (mCurrentSticker != null && mCurrentSticker instanceof DrawableSticker) {
            imageBinding.sbTransparency.setProgress(0);
        }
    }

    @Override
    public void onFontText(Typeface typeface) {
        if (mCurrentSticker != null && mCurrentSticker instanceof DrawableSticker) {
            if ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable() != null) {
                ((DialogDrawable) ((DrawableSticker) mCurrentSticker).getDrawable()).setFontText(typeface);
                imageBinding.imgSticker.invalidate();
            }
        }
        imageBinding.ctlFont.setVisibility(View.GONE);
        imageBinding.flAddText.setVisibility(View.VISIBLE);
    }
}