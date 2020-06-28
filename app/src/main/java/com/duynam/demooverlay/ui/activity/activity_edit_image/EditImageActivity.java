package com.duynam.demooverlay.ui.activity.activity_edit_image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.duynam.demooverlay.ui.activity.activity_image_edit.ListImageEditActivity;
import com.duynam.demooverlay.ui.custorm.BubbleTextView;
import com.duynam.demooverlay.ui.custorm.StickerView;
import com.duynam.demooverlay.ui.fragment.fragment_add_text.AddTextMenuFragment;
import com.duynam.demooverlay.ui.fragment.fragment_filter.FilterFragment;
import com.duynam.demooverlay.ui.fragment.fragment_menu_sticker.MenuStickerFragment;
import com.duynam.demooverlay.ui.fragment.fragment_opacity.OpacityFragment;
import com.duynam.demooverlay.ui.fragment.fragment_rotate.RotateImageFragment;
import com.duynam.demooverlay.utils.Constant;
import com.filter.advanced.JSToneCurved;
import com.filter.base.GPUImage;
import com.filter.base.GPUImageFilter;
import com.filter.helper.FilterManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class EditImageActivity extends AppCompatActivity implements MenuAdapter.OnClick {

    public ActivityEditImageBinding imageBinding;
    public Matrix matrix = new Matrix();
    public int w, h;
    private FragmentTransaction fragmentTransaction;
    private LinearLayoutManager linearLayoutManager;
    private MenuAdapter menuAdapter;

    private MenuStickerFragment menuStickerFragment;
    private AddTextMenuFragment addTextMenuFragment;

    public StickerView stickerView;
    private ArrayList<View> mViews;
    public StickerView mCurrentTView;
    public BubbleTextView mCurrentEditTextView;
    public BubbleTextView bubbleTextView;

    public Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_image);
        init();
        initImage();
        rotateImage();
        initRecycleViewMenu();
        doneAddText();
        menuTop();
        FilterManager.init(this);
        int q = imageBinding.rootView.getWidth();
        int q1 = imageBinding.rootView.getHeight();
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
                Glide.with(this).asBitmap().load(bitmap).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        //imageBinding.imgFilter.setImage(resource);
                        imageBinding.imgFilter.setImage(resource);
                        //setSizeRllSave(bitmap.getWidth(), bitmap.getHeight());
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
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(w, 0);
        params.topToBottom = R.id.toolbarView;
        params.bottomToTop = R.id.frame_menu;
        params.leftToLeft = R.id.parent;
        params.rightToRight = R.id.parent;
        imageBinding.rootView.setLayoutParams(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        h = imageBinding.imgContainer.getHeight();
        w = imageBinding.imgContainer.getWidth();
    }

//    public void setImageCenterAfterCrop(int w, int h) {
//        matrix = new Matrix();
//        Drawable d = imageBinding.imgContainer.getDrawable();
//        RectF imageRectF = new RectF(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//        RectF viewRectF = new RectF(0, 0, w, h);
//        matrix.setRectToRect(imageRectF, viewRectF, Matrix.ScaleToFit.CENTER);
//        imageBinding.imgContainer.setImageMatrix(matrix);
//    }

    public void rotateImage() {
        imageBinding.imgRotateDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_menu, new RotateImageFragment()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    public void init() {
        addTextMenuFragment = new AddTextMenuFragment();
        menuStickerFragment = new MenuStickerFragment();
        mViews = new ArrayList<>();
    }

    @Override
    public void OnClickMenu(int position) {
        switch (position) {
            case 0:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_menu, addTextMenuFragment).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_menu, menuStickerFragment).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_menu, new FilterFragment(bitmap)).addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }

    public void addStickerImage(String patch) {
        stickerView = new StickerView(this);
        Bitmap bitmap = getBitmapFromAsset(patch);
        stickerView.setBitmap(bitmap);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(mCurrentTView);
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
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
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
        mViews.add(stickerView);
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
                if (mCurrentTView != null) {
                    mCurrentTView.setInEdit(false);
                }
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                saveImage();
                Intent intent = new Intent(EditImageActivity.this, ListImageEditActivity.class);
                startActivity(intent);
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

    public void saveImage() {
        Bitmap bitmap = Bitmap.createBitmap(imageBinding.rootView.getWidth(), imageBinding.rootView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        imageBinding.rootView.draw(canvas);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        File folder = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "edit_image1");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String file_name = UUID.randomUUID().toString() + ".jpg";
        File file = new File(folder, file_name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}