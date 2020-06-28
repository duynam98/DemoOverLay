package com.duynam.demooverlay.ui.fragment.fragment_filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.duynam.demooverlay.R;
import com.filter.base.GPUImage;
import com.filter.base.GPUImageFilter;
import com.filter.base.GPUImageView;
import com.filter.helper.FilterManager;
import com.filter.helper.MagicFilterType;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {

    private Context context;
    private List<MagicFilterType> filterTypeList;
    private Bitmap bitmap;
    private OnClickFilter onClickFilter;

    public FilterAdapter(Context context) {
        this.context = context;
        filterTypeList = new ArrayList<>();
        setData();
    }

    public void setOnClickFilter(OnClickFilter onClickFilter) {
        this.onClickFilter = onClickFilter;
    }

    public void setData() {
        for (int i = 0; i < FilterManager.getInstance().types.length; i++) {
            filterTypeList.add(FilterManager.getInstance().types[i]);
        }
        notifyDataSetChanged();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @NonNull
    @Override
    public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_filter, parent, false);
        return new FilterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterHolder holder, final int position) {
//        Glide.with(context).asBitmap().load(bitmap).into(new CustomTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//
//            }
//
//            @Override
//            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//            }
//        });
        holder.gpuImageView.setImage(bitmap);
        holder.gpuImageView.setFilter(FilterManager.getInstance().getFilter(filterTypeList.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickFilter != null){
                    onClickFilter.onClickFilter(FilterManager.getInstance().getFilter(filterTypeList.get(position)));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (filterTypeList == null) {
            return 0;
        }
        return filterTypeList.size();
    }

    public class FilterHolder extends RecyclerView.ViewHolder {
        public GPUImageView gpuImageView;

        public FilterHolder(@NonNull View itemView) {
            super(itemView);
            gpuImageView = itemView.findViewById(R.id.imgFilter);
        }
    }

    public interface OnClickFilter{
        void onClickFilter(GPUImageFilter gpuImageFilter);
    }

}
