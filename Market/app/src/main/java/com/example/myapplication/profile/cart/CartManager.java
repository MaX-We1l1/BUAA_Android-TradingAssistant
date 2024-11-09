package com.example.myapplication.profile.cart;

import androidx.annotation.NonNull;

import com.example.myapplication.MainActivity;
import com.example.myapplication.database.DBFunction;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private List<CartItem> cartItems;

    public CartManager() {
        cartItems = new ArrayList<>();
        ArrayList<String> carts = DBFunction.getCart(MainActivity.getCurrentUsername());
        for (String s: carts) {
            cartItems.add(CartItem.parseCartItemFromString(s));
        }
    }

    // 添加商品到购物车
    public void addItemToCart(CartItem item) {
        cartItems.add(item);
        DBFunction.addCart(MainActivity.getCurrentUsername(), item.toString());
    }

    // 从购物车中移除商品
    public void removeItemFromCart(CartItem item, int postion) {
        cartItems.remove(item);
        DBFunction.delCart(MainActivity.getCurrentUsername(), postion);
    }

    // 获取购物车中所有商品
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // 计算购物车总价
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}

