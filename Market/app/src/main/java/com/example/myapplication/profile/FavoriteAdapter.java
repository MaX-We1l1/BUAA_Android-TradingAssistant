package com.example.myapplication.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R; // 确保导入正确的包
import com.example.myapplication.database.Hobby;
import com.example.myapplication.profile.FavoriteItem; // 模型类
import com.example.myapplication.square.CommodityDetailActivity;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private List<Hobby> favoriteList;

    public FavoriteAdapter(Context context, List<Hobby> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        // 获取当前位置的收藏项
        Hobby favoriteItem = favoriteList.get(position);

        // 设置图片（可以使用图片加载库如Glide或Picasso加载网络图片）
        holder.favoriteImage.setImageResource(favoriteItem.getImageResource());

        // 设置标题
        holder.favoriteTitle.setText(favoriteItem.getTitle());

        // 设置描述
        holder.favoriteDescription.setText(favoriteItem.getDescription());

        // 设置收藏日期
        holder.favoriteDate.setText(favoriteItem.getDate());

        // 可以为每个item设置点击事件
        holder.itemView.setOnClickListener(v -> {
            // 根据你的需要实现点击后的逻辑，比如跳转到详情页
            Log.d("CommodityAdapter", "点击的商品 ID: " + favoriteItem.getCommodityId());
            Intent intent = new Intent(context, CommodityDetailActivity.class);
            intent.putExtra("commodity_id", favoriteItem.getCommodityId());
            //context.startActivity(intent);
            ((Activity) context).startActivityForResult(intent, 1); // 使用传递的请求码
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    // ViewHolder类
    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        ImageView favoriteImage;
        TextView favoriteTitle;
        TextView favoriteDescription;
        TextView favoriteDate;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteImage = itemView.findViewById(R.id.favorite_image);
            favoriteTitle = itemView.findViewById(R.id.favorite_item_title);
            favoriteDescription = itemView.findViewById(R.id.favorite_item_description);
            favoriteDate = itemView.findViewById(R.id.favorite_item_date);
        }
    }
}

