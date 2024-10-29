package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.DBFunction;
import com.example.myapplication.database.Type;
import com.example.myapplication.square.CommodityListActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddCommodityActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_commodity);

        EditText nameEditText = findViewById(R.id.editTextCommodityName);
        Spinner typeSpinner = findViewById(R.id.spinnerCommodityType);
        EditText dateEditText = findViewById(R.id.editTextReleaseDate);
        EditText descriptionEditText = findViewById(R.id.editTextCommodityDescription);
        EditText priceEditText = findViewById(R.id.editTextCommodityPrice);
        Button selectImageButton = findViewById(R.id.button_select_image);
        Button addButton = findViewById(R.id.button_add_commodity);

        //设置当天日期
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        dateEditText.setText(currentDate);

        // 设置 Spinner 的适配器,选择Type
        ArrayAdapter<Type> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Type.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST);
        });

        addButton.setOnClickListener(v -> {
            // 获取输入的商品信息
            String name = nameEditText.getText().toString();
            Type type = (Type) typeSpinner.getSelectedItem();
            String description = descriptionEditText.getText().toString();
            Float price = Float.valueOf(priceEditText.getText().toString());

            // 添加商品的逻辑
            // 保存到数据库或其他操作
            DBFunction.addCommodity(name, MainActivity.getCurrentUsername(), currentDate,
                    type, price, description);

            // 返回首页
            Intent intent = new Intent(AddCommodityActivity.this, CommodityListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // 结束当前活动
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // 这里可以显示选中的图片或进行其他处理
        }
    }
}
