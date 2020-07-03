package com.duynam.demooverlay.ui.activity.activity_edit_image;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duynam.demooverlay.R;
import com.duynam.demooverlay.utils.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FontTextAdapter extends RecyclerView.Adapter<FontTextAdapter.FontViewHolder> {

    private Context context;
    private List<String> listFontPatch;
    private AssetManager assetManager;
    private OnClickFont onClickFont;

    public FontTextAdapter(Context context) {
        this.context = context;
        assetManager = context.getAssets();
        listFontPatch = new ArrayList<>();
        listFontPatch = getListFontPatchFromAsset();
    }

    public void setOnClickFont(OnClickFont onClickFont) {
        this.onClickFont = onClickFont;
    }

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_font, parent, false);
        return new FontViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        final Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + listFontPatch.get(position));
        holder.tv_apply_font.setTypeface(typeface);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickFont != null){
                    onClickFont.onFontText(typeface);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listFontPatch == null) {
            return 0;
        }
        return listFontPatch.size();
    }

    public class FontViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_apply_font;

        public FontViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_apply_font = itemView.findViewById(R.id.tv_apply_font);
        }
    }

    public List<String> getListFontPatchFromAsset() {
        String[] files = new String[0];
        try {
            files = assetManager.list(Constant.PATCH_FONT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> ims = Arrays.asList(files);
        return ims;
    }

    public interface OnClickFont{
        void onFontText(Typeface typeface);
    }

}
