package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*
    用TODO ok来标注需要和队友工作进行对接的地方
*/

public class MainActivity extends AppCompatActivity {
    // 全局变量, 用于存当前登录着的用户名
    private static String currentUsername;
    private Button loginButton;
    private TextView clickToRegister;
    private EditText inputUsername;
    private EditText inputPassword;

    public static String getCurrentUsername() {
        return currentUsername;
    }

    private enum LoginCheckResult {
        // 优先级自上至下
        NO_USERNAME,
        NO_PASSWORD,
        USER_PASSWORD_NOT_MATCH,
        SUCCESS
    }

    public LoginCheckResult checkLoginUser(String username, String password) {
        if (username.equals("")) {
            return LoginCheckResult.NO_USERNAME;
        } else if (password.equals("")) {
            return LoginCheckResult.NO_PASSWORD;
        }
        // TODO 用户名和密码合法性检查函数
        // isAllowLogin()
        boolean isAllow = true;
        if (isAllow) // 不管怎么样，先登录进去
            return LoginCheckResult.SUCCESS;
        else
            return LoginCheckResult.USER_PASSWORD_NOT_MATCH;
    }

    public void tryLogin(String username, String password) {
        LoginCheckResult res = checkLoginUser(username, password);
        switch (res) {
            case NO_USERNAME: Tools.toastMessageShort(MainActivity.this, "请输入用户名"); break;
            case NO_PASSWORD: Tools.toastMessageShort(MainActivity.this, "请输入密码"); break;
            case USER_PASSWORD_NOT_MATCH: Tools.toastMessageShort(MainActivity.this, "用户名和密码不匹配"); break;
            default: {
                // 登录成功, 跳转到目标界面
                // TODO 跳到homePage
                currentUsername = username;
                startActivity(new Intent(MainActivity.this, HomepageActivity.class));
                Tools.toastMessageShort(MainActivity.this, "登录成功!");
                finish();
            }
        }
    }

    private void initAttribute() {
        this.loginButton = findViewById(R.id.login);
        this.clickToRegister = findViewById(R.id.text_register);
        this.inputUsername = findViewById(R.id.inputUserName);
        this.inputPassword = findViewById(R.id.inputPassword);
    }

    public void setOnClickListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                tryLogin(username, password);
            }
        });

        clickToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAttribute();
        setOnClickListeners();
    }

}