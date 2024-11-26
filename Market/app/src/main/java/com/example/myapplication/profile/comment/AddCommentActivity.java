package com.example.myapplication.profile.comment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.Comment;
import com.example.myapplication.database.Commodity;
import com.example.myapplication.database.DBFunction;

import org.litepal.LitePal;

public class AddCommentActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private RatingBar ratingBar;
    private EditText commentInput;
    private Button uploadImageButton;
    private Button submitButton;
    private Commodity commodity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        //从 Intent 中获取商品 ID
        Intent intent = getIntent();
        long commodityId = intent.getLongExtra("commodity_id", -1);
        commodity = LitePal.find(Commodity.class, commodityId);

        // 确保 ID 有效并执行后续逻辑
        if (commodityId != -1) {
            // 根据 ID 加载商品详情

        } else {
            Toast.makeText(this, "商品 ID 无效", Toast.LENGTH_SHORT).show();
        }

        // 初始化视图
        ratingBar = findViewById(R.id.rating_bar);
        commentInput = findViewById(R.id.comment_input);
        uploadImageButton = findViewById(R.id.upload_image_button);
        submitButton = findViewById(R.id.submit_button);

        // 上传图片按钮点击事件
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // 提交按钮点击事件
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment(commodity);
            }
        });
    }

    /**
     * 打开图片选择器
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // 获取用户选择的图片
            Log.d("AddCommentActivity", "Image selected: " + data.getData());
            Toast.makeText(this, "图片上传成功！", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_IMAGE_PICK) {
            Toast.makeText(this, "未选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 提交评论
     */
    private void submitComment(Commodity commodity) {
        float rating = ratingBar.getRating();
        String comment = commentInput.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(this, "请评分", Toast.LENGTH_SHORT).show();
            return;
        }

        if (comment.isEmpty()) {
            Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // 提交评论
        DBFunction.addCommentNoImage(rating, commodity.getId(), MainActivity.getCurrentUsername()
                ,commodity.getCommodityName(), comment, "2024-11-26");
        Toast.makeText(this, "评论提交成功！", Toast.LENGTH_SHORT).show();

        // 提交完成后清空输入
        ratingBar.setRating(0);
        commentInput.setText("");
    }
}
