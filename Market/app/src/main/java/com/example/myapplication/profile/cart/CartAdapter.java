package com.example.myapplication.profile.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Tools;
import com.example.myapplication.database.DBFunction;
import com.example.myapplication.square.CommodityDetailActivity;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private Context context;
    private CartManager cartManager;
    //private OnCartUpdateListener listener;

    public CartAdapter(List<CartItem> cartItems, Context context, CartManager cartManager) {
        this.cartItems = cartItems;
        this.context = context;
        this.cartManager = cartManager;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.nameTextView.setText(cartItem.getName());
        holder.priceTextView.setText(String.format("$%.2f", cartItem.getPrice()));
        holder.quantityTextView.setText("x" + cartItem.getQuantity());

//        holder.plusButton.setOnClickListener(v -> {
//            cartItem.setQuantity(cartItem.getQuantity() + 1);
//            notifyItemChanged(position);
//            listener.onCartUpdated();
//        });
//
//        holder.minusButton.setOnClickListener(v -> {
//            if (cartItem.getQuantity() > 1) {
//                cartItem.setQuantity(cartItem.getQuantity() - 1);
//                notifyItemChanged(position);
//                listener.onCartUpdated();
//            }
//        });

        // 移除商品
        holder.removeButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("确认删除")
                    .setMessage("您确定要从购物车删除这个商品吗？")
                    .setPositiveButton("删除", (dialog, which) -> {
                        cartManager.removeItemFromCart(position);
                        // cartItems.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartItems.size());
                    }).setNegativeButton("取消", (dialog, which) -> {
                        // 如果用户取消，什么都不做
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        });

        // 设置选择框
//        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            cartItem.setSelected(isChecked);
//            listener.onCartUpdated();
//        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        ImageButton removeButton;
        Button plusButton;
        Button minusButton;
        CheckBox checkBox;

        public CartViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.commodity_name);
            priceTextView = itemView.findViewById(R.id.commodity_price);
            quantityTextView = itemView.findViewById(R.id.commodity_num);
            removeButton = itemView.findViewById(R.id.cart_item_remove_button);
        }
    }
}

