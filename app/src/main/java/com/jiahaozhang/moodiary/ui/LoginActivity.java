package com.jiahaozhang.moodiary.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jiahaozhang.moodiary.R;
import com.jiahaozhang.moodiary.bean.User;
import com.jiahaozhang.moodiary.utils.MD5Util;
import com.jiahaozhang.moodiary.utils.SPUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginAcitivity";
    @BindView(R.id.login_name)
    EditText mLoginName;
    @BindView(R.id.login_pwd)
    EditText mLoginPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_register)
    TextView mBtnRegister;
    @BindView(R.id.cb_remember)
    CheckBox mCbRemember;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         /*set it to be full screen*/
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initDrawableSize();

        //判断文件是否存在
        File file= new File("/data/data/"+getPackageName().toString()+"/shared_prefs","config.xml");

        if(file.exists()){
            sp = this.getSharedPreferences("config", this.MODE_PRIVATE);
            restoreInfo();
            login();
            Log.d(TAG, "config.xml文件存在");
        }else {
            sp = this.getSharedPreferences("config", this.MODE_PRIVATE);
            Log.d(TAG, "config.xml文件不存在");
        }
    }

    /**
     * 从sp文件当中读取信息
     */
    private void restoreInfo() {
        String user = sp.getString("user", "");
        String password = sp.getString("password", "");
        mLoginName.setText(user);
        mLoginPwd.setText(password);
    }


    private void initDrawableSize() {
        Drawable accountDraw = getResources().getDrawable(R.drawable.login_icon_account);
        accountDraw.setBounds(0, 0, 45, 45);
        Drawable passwordDraw = getResources().getDrawable(R.drawable.login_icon_password);
        passwordDraw.setBounds(0, 0, 45, 45);
        mLoginName.setCompoundDrawables(accountDraw, null, null, null);
        mLoginPwd.setCompoundDrawables(passwordDraw, null, null, null);
    }

    private void login() {
        Log.d(TAG, "进入登录方法2！");
        final String name = mLoginName.getText().toString();
        final String pwd = mLoginPwd.getText().toString();
        // 判断是否需要记录用户名和密码
        if (mCbRemember.isChecked()) {
            // 被选中状态，需要记录用户名和密码
            // 3.将数据保存到sp文件中
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("user", name);
            editor.putString("password", pwd);
            editor.commit();// 提交数据，类似关闭流，事务
        }
//        final ProgressDialog progress = new ProgressDialog(
//                LoginActivity.this);
//        progress.setMessage("正在登陆...");
//        progress.setCanceledOnTouchOutside(false);
//        progress.show();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_LONG).show();

            return;
        } else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        } else {
            List<User> userList = DataSupport.where("nickname =  ? and password = ?", name, pwd).find(User.class);

            if (userList.size() == 0) {
                Toast.makeText(this, "请先注册", Toast.LENGTH_SHORT).show();
            } else if (userList.size() > 1) {
                Toast.makeText(this, "注册信息失败，有重复信息.", Toast.LENGTH_SHORT).show();
            } else {
                //保存信息到本地
                SPUtils.put(this, "status", true);
                SPUtils.put(this, "id", userList.get(0).getId());
                SPUtils.put(this, "nickname", name);
                SPUtils.put(this, "pwd", MD5Util.MD5(pwd));
                goToHomeActivity();
                Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            }
            for (User user :
                    userList) {
                Log.d(TAG, "User Id is :" + user.getId());
            }
            return;
        }

//        User user = new User();
//        user.setUsername(name);
//        user.setPassword(MD5Util.MD5(pwd));
//
//        // user.setPassword(pwd);
//        //    Snackbar.make(loginBtn,"登录成功！",Snackbar.LENGTH_SHORT).show();
//        user.login(this, new SaveListener() {
//            @Override
//            public void onSuccess() {
//                progress.dismiss();
//                //  Snackbar.make(loginBtn,"登录成功！",Snackbar.LENGTH_SHORT).show();
//                //将用户信息保存至本地
//                SPUtils.put(LoginActivity.this,"user_name",name);
//                SPUtils.put(LoginActivity.this,"pwd",MD5Util.MD5(pwd));
//                User user2;
//                user2 = BmobUser.getCurrentUser(LoginActivity.this, User.class);
//                //将登陆信息保存本地
//                AccountUtils.saveUserInfos(LoginActivity.this, user2, MD5Util.MD5(pwd));
//                goToHomeActivity();
//
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                progress.dismiss();
//                Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();
//                // Snackbar.make(loginBtn,"登录失败！",Snackbar.LENGTH_SHORT).show();
//            }
//        });


    }

    private void goToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void goToRegisterAcitivity() {
        Intent viewRegister = new Intent(this, RegisterActivity.class);
        startActivity(viewRegister);
    }

    @OnClick(R.id.btn_login)
    public void onMBtnLoginClicked() {
        Log.d(TAG, "进入登录方法！");
        login();
    }

    @OnClick(R.id.btn_register)
    public void onMBtnRegisterClicked() {
        goToRegisterAcitivity();
    }
}
