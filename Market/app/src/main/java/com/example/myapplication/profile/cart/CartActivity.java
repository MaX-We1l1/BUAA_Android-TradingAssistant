package com.example.myapplication.profile.cart;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnChangeListener{

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private CartManager cartManager;
    private TextView totalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartManager = CartManager.getInstance();

        recyclerView = findViewById(R.id.recycler_view_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalPriceTextView = findViewById(R.id.text_view_total_price);

        // 添加示例商品到购物车
//        CartItem cartItem = new CartItem("商品1", 1L, 20.00F, 2);
//        cartManager.addItemToCart(cartItem);

        // 更新 RecyclerView
        cartAdapter = new CartAdapter(cartManager.getCartItems(), this, cartManager);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.setOnChangeListener(this);

        // 显示总价
        updateTotalPrice();

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void updateTotalPrice() {
        double totalPrice = cartManager.getTotalPrice();
        totalPriceTextView.setText(String.format("总价: $%.2f", totalPrice));
    }

    @Override
    public void onChange(String message) {
        if (message.equals("change")) {
            updateTotalPrice();
        }
    }
}
