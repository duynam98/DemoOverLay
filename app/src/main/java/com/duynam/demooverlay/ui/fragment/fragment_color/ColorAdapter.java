package com.duynam.demooverlay.ui.fragment.fragment_color;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duynam.demooverlay.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {

    private Context context;
    private List<String> listColor;
    private OnClickColor onClickColor;
    private String[] arr_color;

    public void setOnClickColor(OnClickColor onClickColor) {
        this.onClickColor = onClickColor;
    }

    public ColorAdapter(Context context) {
        this.context = context;
        listColor = new ArrayList<>();
        setData();
    }

    public void setData() {
        arr_color = context.getResources().getStringArray(R.array.color);
        for (int i = 0; i < arr_color.length; i++) {
            listColor.add(arr_color[i]);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_change_color, parent, false);
        return new ColorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, final int position) {
        String colorString = listColor.get(position);
        int color = Color.parseColor(colorString);
        ColorDrawable cd = new ColorDrawable(color);
        holder.img_color.setImageDrawable(cd);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickColor != null) {
                    onClickColor.onSetColor(listColor.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listColor == null) {
            return 0;
        }
        return listColor.size();
    }

    public class ColorHolder extends RecyclerView.ViewHolder {
        public CircleImageView img_color;

        public ColorHolder(@NonNull View itemView) {
            super(itemView);
            img_color = itemView.findViewById(R.id.img_color);
        }
    }

    public interface OnClickColor {
        void onSetColor(String color);
    }

}
