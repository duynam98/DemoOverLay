package com.duynam.demooverlay.ui.fragment.fragment_menu_sticker;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duynam.demooverlay.R;
import com.duynam.demooverlay.utils.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuStickerAdapter extends RecyclerView.Adapter<MenuStickerAdapter.StickerHolder> {

    private Context context;
    private List<String> nenuStickerList;
    private OnClickSticker onClickSticker;

    public MenuStickerAdapter(Context context) {
        this.context = context;
        nenuStickerList = new ArrayList<>();
        nenuStickerList = getImageFromAsset();
    }

    public void setOnClickSticker(OnClickSticker onClickSticker){
        this.onClickSticker = onClickSticker;
    }

    @NonNull
    @Override
    public StickerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_menu_sticker, parent, false);
        return new StickerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerHolder holder, final int position) {
        try {
            InputStream ims = context.getAssets().open(Constant.PATCH_STICKER +"/"+nenuStickerList.get(position));
            Bitmap bitmap = BitmapFactory.decodeStream(ims);
            Glide.with(context).load(bitmap).into(holder.imageView);
        } catch (IOException ex) {
            return;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickSticker != null){
                    onClickSticker.chooseStickerFinish(nenuStickerList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (nenuStickerList == null) {
            return 0;
        }
        return nenuStickerList.size();
    }

    //get list Image From Folder Asset
    public List<String> getImageFromAsset() {
        AssetManager assetManager = context.getAssets();
        String[] files = new String[0];
        try {
            files = assetManager.list(Constant.PATCH_STICKER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> ims = Arrays.asList(files);
        return ims;
    }

    public interface OnClickSticker{
        void chooseStickerFinish(String name);
    }

    public class StickerHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public StickerHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_sticker_menu);
        }
    }
}
