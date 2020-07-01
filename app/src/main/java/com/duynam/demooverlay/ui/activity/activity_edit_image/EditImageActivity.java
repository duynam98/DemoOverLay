package com.duynam.demooverlay.ui.activity.activity_edit_image;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.ActivityEditImageBinding;
import com.duynam.demooverlay.ui.activity.activity_filter.SaveImage;
import com.duynam.demooverlay.ui.activity.activity_home.MainActivity;
import com.duynam.demooverlay.ui.custorm.BubbleTextView;
import com.duynam.demooverlay.ui.custorm.StickerView;
import com.duynam.demooverlay.ui.fragment.fragment_add_text.AddTextMenuFragment;
import com.duynam.demooverlay.ui.fragment.fragment_filter.FilterFragment;
import com.duynam.demooverlay.ui.fragment.fragment_menu_sticker.MenuStickerFragment;
import com.duynam.demooverlay.ui.fragment.fragment_opacity.OpacityFragment;
import com.duynam.demooverlay.utils.Constant;
import com.filter.helper.FilterManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class EditImageActivity extends AppCompatActivity implements MenuAdapter.OnClick {

    public ActivityEditImageBinding imageBinding;
    public Matrix matrix = new Matrix();
    public int w, h;
    private FragmentTransaction fragmentTransaction;
    private LinearLayoutManager linearLayoutManager;
    private MenuAdapter menuAdapter;

    //fragment
    private MenuStickerFragment menuStickerFragment;
    private AddTextMenuFragment addTextMenuFragment;
    private FilterFragment filterFragment;

    public StickerView stickerView;
    public ArrayList<View> mViews;
    public ArrayList<View> mStickerViewAdd;
    public StickerView mCurrentTView;
    public BubbleTextView mCurrentEditTextView;
    public BubbleTextView bubbleTextView;

    public Bitmap bitmap;
    private String timesave;

    private boolean isAddText, isAddSticker, isAddFilter;

    private SaveImage saveImageAsyntask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_image);
        initImage();
        init();
        initRecycleViewMenu();
        doneAddText();
        menuTop();
        finishAddSticker();
        finishAddText();
        finishFilter();
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
        if (getIntent() != null) {
            String path = getIntent().getStringExtra(Constant.PATCH_IMAGE);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(path));
                if (bitmap.getHeight() >= getResources().getDisplayMetrics().heightPixels) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) Math.round(bitmap.getWidth() / 1.5), (int) Math.round(bitmap.getHeight() / 1.5), false);
                }
                Glide.with(this).asBitmap().load(bitmap).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageBinding.imgContainer.setImageBitmap(bitmap);
                        setSizeRllSave(bitmap.getWidth(), bitmap.getHeight());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void setSizeRllSave(int w, int h) {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(w, h);
        params.topToBottom = R.id.toolbarView;
        params.bottomToTop = R.id.frame_menu;
        params.endToEnd = R.id.toolbarView;
        params.startToStart = R.id.toolbarView;
        imageBinding.rootView.setLayoutParams(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        h = imageBinding.imgContainer.getHeight();
        w = imageBinding.imgContainer.getWidth();
    }


    public void init() {
        imageBinding.imgFilter.setAlpha(0);
        imageBinding.imgContainer.setAlpha((float) 1.0);
        imageBinding.ctlStickerBar.setVisibility(View.GONE);
        imageBinding.ctlFilterBar.setVisibility(View.GONE);
        imageBinding.ctlAddTextBar.setVisibility(View.GONE);
        addTextMenuFragment = new AddTextMenuFragment();
        menuStickerFragment = new MenuStickerFragment();
        filterFragment = new FilterFragment(bitmap);
        mViews = new ArrayList<>();
        mStickerViewAdd = new ArrayList<>();
    }

    @Override
    public void OnClickMenu(int position) {
        switch (position) {
            case 0:
                isAddText = true;
                imageBinding.ctlAddTextBar.setVisibility(View.VISIBLE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_menu, addTextMenuFragment, Constant.FRAGMENT_ADD_TEXT).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 1:
                isAddSticker = true;
                imageBinding.ctlStickerBar.setVisibility(View.VISIBLE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_menu, menuStickerFragment, Constant.FRAGMENT_ADD_STICKER).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 2:
                isAddFilter = true;
                imageBinding.ctlFilterBar.setVisibility(View.VISIBLE);
                bitmapToGpuImage(getCurrentBitmap());
                filterFragment.setBitMap(getCurrentBitmap());
                if (filterFragment.filterAdapter!=null){
                    filterFragment.filterAdapter.clearBitmap();
                }
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_menu, filterFragment, Constant.FRAGMENT_ADD_FILTER).addToBackStack(null);
                fragmentTransaction.commit();
        }
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


//    private void setImageAfterFilter() {
//        if (imageBinding.imgDoneFilter.getVisibility() == View.VISIBLE) {
//            imageBinding.imgDoneFilter.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    imageBinding.imgDoneFilter.setVisibility(View.GONE);
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            super.run();
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    final Bitmap bitmap = imageBinding.imgFilter.getGPUImage().getBitmapWithFilterApplied();
//                                    Glide.with(EditImageActivity.this).asBitmap().load(bitmap).into(new CustomTarget<Bitmap>() {
//                                        @Override
//                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                            imageBinding.imgContainer.setImageBitmap(bitmap);
//                                            imageBinding.imgFilter.setAlpha(0);
//                                        }
//
//                                        @Override
//                                        public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    }.start();
//                }
//            });
//        }
//    }

    public void addStickerImage(String patch) {
        stickerView = new StickerView(this);
        Bitmap bitmap = getBitmapFromAsset(patch);
        stickerView.setBitmap(bitmap);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mStickerViewAdd.remove(mCurrentTView);
                imageBinding.rootView.removeView(mCurrentTView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                if (mCurrentTView != null) {
                    mCurrentTView.setInEdit(false);
                }
                mCurrentTView.setInEdit(false);
                mCurrentTView = stickerView;
                mCurrentTView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mStickerViewAdd.indexOf(stickerView);
                if (position == mStickerViewAdd.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mStickerViewAdd.remove(position);
                mStickerViewAdd.add(mStickerViewAdd.size(), stickerTemp);
            }

            @Override
            public void onTransparency() {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_menu, OpacityFragment.newInstance(true, false, false)).addToBackStack(null);
                fragmentTransaction.commit();
            }

        });
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        imageBinding.rootView.addView(stickerView, lp);
        mStickerViewAdd.add(stickerView);
        setCurrentTView(stickerView);
    }

    public void addTextImage() {
        bubbleTextView = new BubbleTextView(this,
                Color.WHITE, 0);
        bubbleTextView.setImageResource(R.mipmap.none);
        bubbleTextView.setOperationListener(new BubbleTextView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(mCurrentEditTextView);
                imageBinding.rootView.removeView(mCurrentEditTextView);
            }

            @Override
            public void onEdit(BubbleTextView bubbleTextView) {
                if (mCurrentTView != null) {
                    mCurrentTView.setInEdit(false);
                }
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                mCurrentEditTextView.setInEdit(false);
                mCurrentEditTextView = bubbleTextView;
                mCurrentEditTextView.setInEdit(true);
            }

            @Override
            public void onClick(BubbleTextView bubbleTextView) {
                imageBinding.ctlIputEdt.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTop(BubbleTextView bubbleTextView) {
                int position = mViews.indexOf(bubbleTextView);
                if (position == mViews.size() - 1) {
                    return;
                }
                BubbleTextView textView = (BubbleTextView) mViews.remove(position);
                mViews.add(mViews.size(), textView);
            }

            @Override
            public void onSetSize() {

            }
        });
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        imageBinding.rootView.addView(bubbleTextView, lp);
        mViews.add(bubbleTextView);
        setCurrentEdit(bubbleTextView);
    }

    public void setCurrentEdit(BubbleTextView bubbleTextView) {
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        if (mCurrentTView != null) {
            mCurrentTView.setInEdit(false);
        }
        mCurrentEditTextView = bubbleTextView;
        mCurrentEditTextView.setInEdit(true);
    }

    public void setCurrentTView(StickerView stickerView) {
        if (mCurrentTView != null) {
            mCurrentTView.setInEdit(false);
        }
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        mCurrentTView = stickerView;
        mCurrentTView.setInEdit(true);
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
                timesave = String.valueOf(Calendar.getInstance().getTimeInMillis());
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

    private void doneAddText() {
        imageBinding.imgDoneAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setText(imageBinding.edtAddText.getText().toString());
                    imageBinding.ctlIputEdt.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    public void createFolder(final Context context) {
        Bitmap bitmap = Bitmap.createBitmap(imageBinding.rootView.getWidth(), imageBinding.rootView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        imageBinding.rootView.draw(canvas);
        timesave = String.valueOf(Calendar.getInstance().getTimeInMillis());
        File folderCacheDir = context.getCacheDir();
        String file_name = timesave + ".jpg";
        File file = new File(folderCacheDir + File.separator + "cacheImageCrop");
        if (!file.exists()) {
            file.mkdirs();
        }
        File fileCache = new File(file, file_name);
        try {
            FileOutputStream out = new FileOutputStream(fileCache.getAbsoluteFile());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.close();
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearSticker() {
        if (mStickerViewAdd != null && mStickerViewAdd.size() > 0) {
            for (int i = 0; i < mStickerViewAdd.size(); i++) {
                imageBinding.rootView.removeView(mStickerViewAdd.get(i));
            }
        }
        mStickerViewAdd.clear();
    }

    private void clearTextView() {
        if (mViews != null && mViews.size() > 0) {
            for (int i = 0; i < mViews.size(); i++) {
                imageBinding.rootView.removeView(mViews.get(i));
            }
        }
        mViews.clear();
    }

    private Bitmap getCurrentBitmap() {
        Bitmap bitmap_image = Bitmap.createBitmap(imageBinding.rootView.getWidth(), imageBinding.rootView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap_image);
        imageBinding.rootView.draw(canvas);
        return bitmap_image;
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

    private void finishAddText() {
        imageBinding.imgDoneAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBinding.ctlAddTextBar.setVisibility(View.GONE);
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                if (mCurrentTView != null) {
                    mCurrentTView.setInEdit(false);
                }
                setImageBitmapGlide(getCurrentBitmap(), imageBinding.imgContainer);
                clearTextView();
                getSupportFragmentManager().beginTransaction().remove(addTextMenuFragment).commit();
                getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void finishAddSticker() {
        imageBinding.imgDoneSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBinding.ctlStickerBar.setVisibility(View.GONE);
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                if (mCurrentTView != null) {
                    mCurrentTView.setInEdit(false);
                }
                setImageBitmapGlide(getCurrentBitmap(), imageBinding.imgContainer);
                clearSticker();
                getSupportFragmentManager().beginTransaction().remove(menuStickerFragment).commit();
                getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void finishFilter() {
        imageBinding.imgDoneFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bitmap bitmap = imageBinding.imgFilter.getGPUImage().getBitmapWithFilterApplied();
                imageBinding.ctlFilterBar.setVisibility(View.GONE);
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                if (mCurrentTView != null) {
                    mCurrentTView.setInEdit(false);
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setImageBitmapGlide(bitmap, imageBinding.imgContainer);
                                clearSticker();
                            }
                        });
                    }
                }.start();
                imageBinding.imgFilter.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (imageBinding.ctlIputEdt != null && imageBinding.ctlIputEdt.getVisibility() == View.VISIBLE) {
            imageBinding.ctlIputEdt.setVisibility(View.GONE);
        }

        if (imageBinding.imgFilter.getAlpha() == 1) {
            imageBinding.imgFilter.setAlpha(0);
            imageBinding.ctlFilterBar.setVisibility(View.GONE);
            imageBinding.imgContainer.setAlpha((float) 1.0);
        }

        if (imageBinding.ctlStickerBar.getVisibility() == View.VISIBLE) {
            clearSticker();
            imageBinding.ctlStickerBar.setVisibility(View.GONE);
        }

        if (imageBinding.ctlFilterBar.getVisibility() == View.VISIBLE) {
            clearSticker();
            imageBinding.ctlStickerBar.setVisibility(View.GONE);
            imageBinding.imgFilter.setVisibility(View.GONE);
        }

        if (imageBinding.ctlAddTextBar.getVisibility() == View.VISIBLE) {
            if (isAddText) {
                clearTextView();
                imageBinding.ctlAddTextBar.setVisibility(View.GONE);
            }
        }

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
        //binding.progress.setVisibility(View.VISIBLE);
        Bitmap bitmap = getCurrentBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        saveImageAsyntask = new SaveImage(getApplicationContext());
        SaveImage.OnSaveImage onSaveImage = new SaveImage.OnSaveImage() {
            @Override
            public void onSaveImage(boolean isSave) {
                //binding.progress.setVisibility(View.GONE);
                startActivity(new Intent(EditImageActivity.this, MainActivity.class));
                finish();
            }
        };
        saveImageAsyntask.setOnSaveImage(onSaveImage);
        saveImageAsyntask.execute(bitmap);
    }

}