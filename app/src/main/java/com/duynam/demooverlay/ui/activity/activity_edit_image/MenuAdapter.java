package com.duynam.demooverlay.ui.activity.activity_edit_image;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duynam.demooverlay.R;
import com.duynam.demooverlay.databinding.ItemMenuBinding;
import com.duynam.demooverlay.model.MenuHome;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private List<MenuHome> menuHomeList;
    private Context context;
    private OnClick onClick;
    private Animation animationImage, animationText;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public MenuAdapter(Context context) {
        this.context = context;
        menuHomeList = new ArrayList<>();
        setData();
        animationImage = AnimationUtils.loadAnimation(context, R.anim.slide_top_tobottom);
        animationText = AnimationUtils.loadAnimation(context, R.anim.slide_left_toright);
    }

    public void setData() {
        menuHomeList.add(new MenuHome(R.drawable.ic_text, context.getResources().getString(R.string.menu_edit_text)));
        menuHomeList.add(new MenuHome(R.drawable.ic_stickers, context.getResources().getString(R.string.menu_edit_stickers)));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemMenuBinding itemMenuBinding = DataBindingUtil.inflate(inflater, R.layout.item_menu, parent, false);
        return new ViewHolder(itemMenuBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, final int position) {
        holder.itemView.startAnimation(animationImage);
        holder.setData(menuHomeList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClick != null) {
                    onClick.OnClickMenu(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (menuHomeList == null) {
            return 0;
        }
        return menuHomeList.size();
    }

    public interface OnClick {
        void OnClickMenu(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ObservableField<String> title;
        public ItemMenuBinding itemMenuBinding;

        public ViewHolder(@NonNull ItemMenuBinding itemView) {
            super(itemView.getRoot());
            title = new ObservableField<>();
            itemMenuBinding = itemView;
        }

        public void setData(MenuHome menuHome) {
            if (itemMenuBinding.getMenuHolder() == null){
                itemMenuBinding.setMenuHolder(this);
            }
            title.set(menuHome.getTitle());
            Glide.with(itemMenuBinding.imgMenu.getContext()).load(menuHome.getImage()).into(itemMenuBinding.imgMenu);
        }
    }
}
