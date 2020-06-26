package com.duynam.demooverlay.ui.activity.activity_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duynam.demooverlay.R;

import java.util.ArrayList;
import java.util.List;

public class ListImageDeviceAdapter extends RecyclerView.Adapter<ListImageDeviceAdapter.ListImageHomeHolder> {

    private Context context;
    private List<String> list_patch_Image;
    private OnClickImageDevice onClickImageDevice;

    public ListImageDeviceAdapter(Context context, OnClickImageDevice onClickImageDevice) {
        this.context = context;
        this.onClickImageDevice = onClickImageDevice;
        list_patch_Image = new ArrayList<>();
    }

    public void setData(List<String> data) {
        if (data != null) {
            list_patch_Image.addAll(data);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ListImageHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_home, parent, false);
        return new ListImageHomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListImageHomeHolder holder, final int position) {
        Glide.with(context).load(list_patch_Image.get(position)).into(holder.img_of_device);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickImageDevice != null) {
                    onClickImageDevice.onClickImage(list_patch_Image.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list_patch_Image == null) {
            return 0;
        }
        return list_patch_Image.size();
    }

    public class ListImageHomeHolder extends RecyclerView.ViewHolder {
        public ImageView img_of_device;

        public ListImageHomeHolder(@NonNull View itemView) {
            super(itemView);
            img_of_device = itemView.findViewById(R.id.img_of_device);
        }
    }

    public interface OnClickImageDevice {
        void onClickImage(String patch);
    }

}
