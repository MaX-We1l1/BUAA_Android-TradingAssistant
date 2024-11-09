package com.example.myapplication.profile.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private Context context;
    private CartManager cartManager;

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

        // 移除商品
        holder.removeButton.setOnClickListener(v -> {
            cartManager.removeItemFromCart(cartItem, position);
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
        });
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

        public CartViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.commodity_name);
            priceTextView = itemView.findViewById(R.id.commodity_price);
            quantityTextView = itemView.findViewById(R.id.commodity_num);
            removeButton = itemView.findViewById(R.id.cart_item_remove_button);
        }
    }
}

