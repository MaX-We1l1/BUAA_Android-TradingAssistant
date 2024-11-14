package com.example.myapplication.commodity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.Commodity;
import com.example.myapplication.database.DBFunction;
import com.example.myapplication.database.Type;
import com.example.myapplication.minio.MinioUtils;
import com.example.myapplication.square.CommodityListActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.minio.errors.MinioException;

public class AddCommodityActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String base = "";
    // private MinioUtils minioUtils = new MinioUtils();

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
                    type, price, description, base);

//            // 将图片上传到云端
//            try {
//                String filepath = getRealPathFromURI(imageUri);
//                String folderPath = "img/commodity/";
//                String objectName = folderPath + commodity.getId() + "-" + commodity.getCommodityName();
//                if (filepath != null) {
//                    File file = new File(filepath);
//                    minioUtils.uploadFile(file, objectName + ".jpg");
//                }
//                Toast.makeText(this, "图片上传成功", Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(this, "图片上传失败", Toast.LENGTH_SHORT).show();
//            }

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
            ImageView imageView = findViewById(R.id.commodity_image);
            String filepath = getRealPathFromURI(imageUri);
            imageView.setImageURI(imageUri);
            base = encodeImageToBase64(filepath);
            Log.w("from AddCommodityActivity", "url is " + imageUri);
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            Log.w("from AddCommodityActivity", "path is " + imageUri);
            return filePath;
        }
        return null;
    }

    public static String encodeImageToBase64(String imagePath) {
        try {
            FileInputStream imageStream = new FileInputStream(imagePath);
            byte[] imageBytes = new byte[(int) imageStream.available()];
            imageStream.read(imageBytes);
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
