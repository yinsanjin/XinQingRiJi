package com.jiahaozhang.moodiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jiahaozhang.moodiary.ui.LoginActivity;
import com.jiahaozhang.moodiary.ui.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.register)
    Button mRegister;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    //测试提交master 修改
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register)
    public void clickRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
//        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)
    public void clickLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
