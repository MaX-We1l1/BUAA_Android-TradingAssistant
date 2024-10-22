package com.example.myapplication.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.profile.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // 绑定订单标题
        holder.orderTitle.setText(order.getTitle());

        // 绑定订单状态
        holder.orderStatus.setText(order.getStatus());

        // 绑定订单价格
        holder.orderPrice.setText(String.format("¥%.2f", order.getPrice()));

        // 绑定订单图片
        holder.orderImage.setImageResource(order.getImageResourceId());

        // 动态设置状态颜色（如“待发货”是黄色，“已完成”是绿色）
        switch (order.getStatus()) {
            case "待发货":
                holder.orderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
                break;
            case "已完成":
                holder.orderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "已取消":
                holder.orderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                break;
            default:
                holder.orderStatus.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        ImageView orderImage;
        TextView orderTitle, orderStatus, orderPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.order_image);
            orderTitle = itemView.findViewById(R.id.order_title);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderPrice = itemView.findViewById(R.id.order_price);
        }
    }
}