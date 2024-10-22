package com.example.myapplication.square;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.database.Commodity;

import java.util.List;

public class CommodityAdapter extends RecyclerView.Adapter<CommodityAdapter.CommodityViewHolder> {
    private List<Commodity> commodityList;
    private Context context;

    public CommodityAdapter(Context context, List<Commodity> commodityList) {
        this.context = context;
        this.commodityList = commodityList;
    }

    @NonNull
    @Override
    public CommodityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_commodity, parent, false);
        return new CommodityViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CommodityViewHolder holder, int position) {
        Commodity commodity = commodityList.get(position);
        holder.commodityName.setText(commodity.getCommodityName());
        holder.commodityPrice.setText("¥ " + commodity.getPrice());
//        Glide.with(context)
//                .load(commodity.getImageUrl())
//                .into(holder.commodityImage);
        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            Log.d("CommodityAdapter", "点击的商品 ID: " + commodity.getId());
            Intent intent = new Intent(context, CommodityDetailActivity.class);
            intent.putExtra("commodity_id", commodity.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return commodityList.size();
    }

    public static class CommodityViewHolder extends RecyclerView.ViewHolder {
        ImageView commodityImage;
        TextView commodityName, commodityPrice;

        public CommodityViewHolder(@NonNull View itemView) {
            super(itemView);
            commodityImage = itemView.findViewById(R.id.commodity_image);
            commodityName = itemView.findViewById(R.id.commodity_name);
            commodityPrice = itemView.findViewById(R.id.commodity_price);
        }
    }
}
