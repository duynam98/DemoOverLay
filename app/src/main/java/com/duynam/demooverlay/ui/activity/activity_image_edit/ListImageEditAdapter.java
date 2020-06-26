package com.duynam.demooverlay.ui.activity.activity_image_edit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duynam.demooverlay.R;

import java.util.ArrayList;

public class ListImageEditAdapter extends RecyclerView.Adapter<ListImageEditAdapter.ImageHolder> {

    private Context context;
    private ArrayList<String> listImage;
    private ChooseImage chooseImage;

    public void setChooseImage(ChooseImage chooseImage){
        this.chooseImage = chooseImage;
    }

    public ListImageEditAdapter(Context context) {
        this.context = context;
        listImage = new ArrayList<>();
    }

    public void setData(ArrayList<String> data) {
        if (data != null) {
            listImage.addAll(data);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_image_home, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, final int position) {
        Glide.with(context).load(listImage.get(position)).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.img_view);
        holder.img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chooseImage != null){
                    chooseImage.getImageFinish(listImage.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listImage == null) {
            return 0;
        }
        return listImage.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView img_view;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            img_view = itemView.findViewById(R.id.img_of_device);
        }
    }

    public interface ChooseImage {
        void getImageFinish(String patch);
    }

}

