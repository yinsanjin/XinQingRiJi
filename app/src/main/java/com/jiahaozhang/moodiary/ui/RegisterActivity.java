package com.jiahaozhang.moodiary.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jiahaozhang.moodiary.R;
import com.jiahaozhang.moodiary.bean.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "RegisterActivity";
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_twicepassword)
    EditText mEtTwicepassword;
    @BindView(R.id.img_back)
    ImageButton mImgBack;
    @BindView(R.id.btn_register)
    Button mBtnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.img_back)
    public void back() {
        finish();
    }


    @OnClick(R.id.btn_register)
    public void register(){
        String name =  mEtUsername.getText().toString().trim();
        String password =  mEtPassword.getText().toString().trim();
        String twicePwd =  mEtTwicepassword.getText().toString().trim();
//        String name =accountEt.getText().toString();
//        String password = pwdEt.getText().toString();
//        String pwd_again = twicePwdEt.getText().toString();
//
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_LONG).show();

            return;
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }else if (!twicePwd.equals(password)) {
            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }else{
//            SQLiteDatabase mDiary = LitePal.getDatabase();
            User user = new User();
            user.setNickname(name);
            user.setPassword(password);
            boolean res = user.save();
            if(res){
                Toast.makeText(this, "存储数据成功", Toast.LENGTH_SHORT).show();
                this.finish();
            }else {
                Toast.makeText(this, "存储数据失败", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "User save is:" + user.getId());
        }
//
//        boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
//        if(!isNetConnected){
//            Snackbar.make(registerBtn,"网络连接出错",Snackbar.LENGTH_LONG).show();
//            return;
//        }
//
//        final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
//        progress.setMessage("正在注册...");
//        progress.setCanceledOnTouchOutside(false);
//        progress.show();
//        //由于每个应用的注册所需的资料都不一样，故IM sdk未提供注册方法，用户可按照bmod SDK的注册方式进行注册。
//        //注册的时候需要注意两点：1、User表中绑定设备id和type，2、设备表中绑定username字段
//        final User bu = new User();
//        bu.setUsername(name);
//        bu.setPassword(MD5Util.MD5(password));
//        //将user和设备id进行绑定aa
//        //       bu.setUserSex(true);
////        bu.setDeviceType("android");
////        bu.setInstallId(BmobInstallation.getInstallationId(this));
//        bu.signUp(RegisterActivity.this, new SaveListener() {
//
//            @Override
//            public void onSuccess() {
//                // TODO Auto-generated method stub
//                progress.dismiss();
//                Snackbar.make(registerBtn,"注册成功",Snackbar.LENGTH_LONG).show();
//                // 将设备与username进行绑定
//                //  userManager.bindInstallationForRegister(bu.getUsername());
//                // 启动主页
//                //  Log.i("test","enter mainActivity");
//                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
//
//            }
//            @Override
//            public void onFailure(int arg0, String arg1) {
//                // TODO Auto-generated method stub
//                Snackbar.make(registerBtn,"注册失败:",Snackbar.LENGTH_LONG).show();
//                progress.dismiss();
//            }
//        });
    }
}
