package com.example.myapplication.profile;;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.io.FileInputStream;
import java.io.IOException;

public class PersonalInfoActivity extends AppCompatActivity {
    private ImageView profileImage;
    private Button changeAvatarButton;
    private Uri imageUri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        String userImageUrl = MainActivity.getCurrentImageUrl();
        profileImage = findViewById(R.id.profile_image);
        if (userImageUrl != null && !userImageUrl.isEmpty()) {
            byte[] decodedString = Base64.decode(userImageUrl, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0 , decodedString.length);
            profileImage.setImageBitmap(decodedByte);
        }
        changeAvatarButton = findViewById(R.id.change_avatar_button);
        changeAvatarButton.setOnClickListener(view -> openImagePicker());

        TextView nameTextView = findViewById(R.id.username_text);
        nameTextView.setText(MainActivity.getCurrentUsername());

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> {
            finish();
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择图片"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            String filepath = getRealPathFromURI(imageUri);
            profileImage.setImageURI(imageUri);
            String base = encodeImageToBase64(filepath);
            MainActivity.setCurrentImageUrl(base);
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