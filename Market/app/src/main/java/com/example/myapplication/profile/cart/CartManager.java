package com.example.myapplication.profile.cart;

import androidx.annotation.NonNull;

import com.example.myapplication.MainActivity;
import com.example.myapplication.Tools;
import com.example.myapplication.database.DBFunction;
import com.example.myapplication.square.CommodityDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private List<CartItem> cartItems;
    private static CartManager cartManager = new CartManager(); // 单例模式
    private static String username = MainActivity.getCurrentUsername();

    public static CartManager getInstance() {
        return cartManager;
    }

    private CartManager() {
        cartItems = new ArrayList<>();
        ArrayList<String> carts = DBFunction.getCart(MainActivity.getCurrentUsername());
        for (String s: carts) {
            cartItems.add(CartItem.parseCartItemFromString(s));
        }
    }

    // 添加商品到购物车
    public void addItemToCart(CartItem item) {
        boolean needupdate = false;
        int index = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getId() == item.getId()) {
                int q = cartItems.get(i).getQuantity();
                item.setQuantity(q + item.getQuantity());
                needupdate = true;
                index = i;
                //cartItems.get(i).setQuantity(q + item.getQuantity());
                break;
            }
        }
        if (needupdate) {
            removeItemFromCart(index);
        }
        cartItems.add(item);
        DBFunction.addCart(MainActivity.getCurrentUsername(), item.toString());
    }

    // 从购物车中移除商品
    public void removeItemFromCart(int postion) {
        cartItems.remove(postion);
        DBFunction.delCart(MainActivity.getCurrentUsername(), postion);
    }

    // 获取购物车中所有商品
    public List<CartItem> getCartItems() {
        if (!username.equals(MainActivity.getCurrentUsername())) {
            return updateCartItems();
        }
        return cartItems;
    }

    public List<CartItem> updateCartItems() {
        cartItems.clear();
        ArrayList<String> carts = DBFunction.getCart(MainActivity.getCurrentUsername());
        for (String s: carts) {
            cartItems.add(CartItem.parseCartItemFromString(s));
        }
        return cartItems;
    }

    // 获取某一商品数量
    public int getQuantity(CartItem item) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getId() == item.getId()) {
                return cartItems.get(i).getQuantity();
            }
        }
        return 0;
    }

    // 计算购物车总价
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            if (item.isSelected()) {  // 只计算选中的商品
                total += item.getPrice() * item.getQuantity();
            }
        }
        return total;
    }
}

