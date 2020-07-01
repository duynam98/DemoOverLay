package com.duynam.demooverlay.ui.activity.activity_filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duynam.demooverlay.R;
import com.filter.base.GPUImage;
import com.filter.base.GPUImageFilter;
import com.filter.helper.FilterManager;
import com.filter.helper.MagicFilterType;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {

    private Context context;
    private List<MagicFilterType> filterTypeList;
    private Bitmap bitmap;
    private OnClickFilter onClickFilter;
    private ArrayList<Bitmap> bitmapArrayList;

    public FilterAdapter(Context context) {
        this.context = context;
        filterTypeList = new ArrayList<>();
        bitmapArrayList = new ArrayList<>();
        setData();
    }

    public void clearBitmap(){
        if (bitmapArrayList!=null){
            bitmapArrayList.clear();
            notifyDataSetChanged();
        }
    }

    public void setOnClickFilter(OnClickFilter onClickFilter) {
        this.onClickFilter = onClickFilter;
    }

    public void setData() {
        for (int i = 0; i < FilterManager.getInstance().types.length; i++) {
            filterTypeList.add(FilterManager.getInstance().types[i]);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        int wB = bitmap.getWidth();
        int hB = bitmap.getHeight();
        if (wB > hB) {
            hB = 200 * hB / wB;
            wB = 200;
        } else {
            wB = 200 * wB / hB;
            hB = 200;
        }
        this.bitmap = Bitmap.createScaledBitmap(bitmap, wB, hB, false);
        Log.e("Namtd", "setBitmap: ");
    }

    @NonNull
    @Override
    public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_filter, parent, false);
        return new FilterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterHolder holder, final int position) {
        if (bitmapArrayList.size() < position) {
            holder.imageView.setImageBitmap(bitmapArrayList.get(position));
        } else {
            GPUImage gpuImage = new GPUImage(holder.itemView.getContext());
            gpuImage.setImage(bitmap);
            gpuImage.setFilter(FilterManager.getInstance().getFilter(filterTypeList.get(position)));
            Bitmap bitmap = gpuImage.getBitmapWithFilterApplied();
            Glide.with(context).load(bitmap).into(holder.imageView);
            bitmapArrayList.add(bitmap);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickFilter != null) {
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
        public ImageView imageView;

        public FilterHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgFilter);
        }
    }

    public interface OnClickFilter {
        void onClickFilter(GPUImageFilter gpuImageFilter);
    }

}
