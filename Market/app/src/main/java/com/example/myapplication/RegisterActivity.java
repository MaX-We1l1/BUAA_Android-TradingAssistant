package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private EditText inputUsername;
    private EditText inputPassword1;
    private EditText inputPassword2;
    private final String NO_USERNAME_TOAST = "请输入用户名";
    private final String NO_PASSWORD_TOAST = "请输入密码";
    private final String NO_AGAIN_PASSWORD_TOAST = "请确认密码";
    private final String PASSWORD_NOT_SAME_TOAST = "两次输入的密码不一致, 请重新输入";
    private final String PASSWORD_TOO_SHORT_TOAST = "密码长度应至少为8个字符";

    private enum RegisterCheckResult {
        NO_USERNAME,
        NO_PASSWORD,
        NO_AGAIN_PASSWORD,
        PASSWORD_NOT_SAME,
        PASSWORD_TOO_SHORT, // 密码长度至少为8
        USERNAME_EXIST,
        SUCCESS
    }

    public void setOnClickListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = inputUsername.getText().toString();
                String password1 = inputPassword1.getText().toString();
                String password2 = inputPassword2.getText().toString();
                tryRegister(username, password1, password2);
            }
        });
    }

    public RegisterCheckResult checkRegisterUser(String username, String password1, String password2) {
        if (username.equals("")) {
            return RegisterCheckResult.NO_USERNAME;
        } else if (password1.equals("")) {
            return RegisterCheckResult.NO_PASSWORD;
        } else if (password2.equals("")) {
            return RegisterCheckResult.NO_AGAIN_PASSWORD;
        } else if (!password1.equals(password2)) {
            return RegisterCheckResult.PASSWORD_NOT_SAME;
        } //TODO 检查用户名是否已经存在
        return RegisterCheckResult.SUCCESS;
    }

    public void toastThis(String mes) {
        Tools.toastMessageShort(RegisterActivity.this, mes);
    }

    public void tryRegister(String username, String password1, String password2) {
        RegisterCheckResult res = checkRegisterUser(username, password1, password2);
        switch (res) {
            case NO_USERNAME: toastThis(NO_USERNAME_TOAST); break;
            case NO_PASSWORD: toastThis(NO_PASSWORD_TOAST); break;
            case NO_AGAIN_PASSWORD: toastThis(NO_AGAIN_PASSWORD_TOAST); break;
            case PASSWORD_NOT_SAME: toastThis(PASSWORD_NOT_SAME_TOAST); break;
            case PASSWORD_TOO_SHORT: toastThis(PASSWORD_TOO_SHORT_TOAST); break;
            default: {
                // 跳转到登录界面
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String date = String.format("%04d-%02d-%02d", year, month, day);
                //TODO 加入进去
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                toastThis("注册成功!");
                finish();
            }
        }
    }

    public void initAttribute() {
        this.registerButton = findViewById(R.id.register);
        this.inputUsername = findViewById(R.id.reg_inputUserName);
        this.inputPassword1 = findViewById(R.id.reg_inputPassword);
        this.inputPassword2 = findViewById(R.id.reg_inputPassword_again);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initAttribute();
        setOnClickListeners();
    }

}
