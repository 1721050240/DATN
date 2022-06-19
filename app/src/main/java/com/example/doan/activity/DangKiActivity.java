package com.example.doan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.model.User;
import com.example.doan.quanli.QuanLiUserActivity;
import com.example.doan.retrofitClient.ApiBanHang;
import com.example.doan.retrofitClient.RetrofitClient;
import com.example.doan.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    EditText email, pass, repass, mobile, username;
    TextView tv6;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    User userSua;
    Boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        initView();
        initControll();

        Intent intent = getIntent();
        userSua = (User) intent.getSerializableExtra("Sua User");

        if (userSua == null) {
            flag = false;
            tv6.setText("Đăng Kí");
            button.setText("Đăng Kí");
        } else {
            //Sua
            flag = true;
            tv6.setText("Sửa thông tin");
            button.setText("Update");

            username.setText(userSua.getUsername());
            email.setText(userSua.getEmail());
            pass.setText(userSua.getPass());
            repass.setText(userSua.getPass());
            mobile.setText(userSua.getMobile());

        }

    }

    private void initControll() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == false) {
                    dangki();
                    Intent dangnhap1 = new Intent(getApplicationContext(), DangNhapActivity.class);
                    startActivity(dangnhap1);

                } else {
                    updateuser();
                    Intent dangnhap2 = new Intent(getApplicationContext(), QuanLiUserActivity.class);
                    startActivity(dangnhap2);
                }

            }
        });

    }

    private void updateuser() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        String str_user = username.getText().toString().trim();

        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập email !", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_pass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập pass !", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_repass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập lại pass !", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_mobile)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập số điện thoại !", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_user)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên  !", Toast.LENGTH_SHORT).show();
        } else {
            if (str_pass.equals(str_repass)) {

                compositeDisposable.add(apiBanHang.updateUser(str_email, str_pass, str_user, str_mobile, userSua.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                userModel -> {
                                    if (userModel.isSuccess()) {
                                        Utils.user_current.setEmail(str_email);
                                        Utils.user_current.setPass(str_pass);

                                        Intent dangnhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                                        startActivity(dangnhap);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }, throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));


            } else {
                Toast.makeText(getApplicationContext(), "Bạn chưa nhập đúng mật khẩu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dangki() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        String str_user = username.getText().toString().trim();

        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập email !", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_pass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập pass !", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_repass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập lại pass !", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_mobile)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập số điện thoại !", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_user)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên  !", Toast.LENGTH_SHORT).show();
        } else {
            if (str_pass.equals(str_repass)) {

                compositeDisposable.add(apiBanHang.dangKi(str_email, str_pass, str_user, str_mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                userModel -> {
                                    if (userModel.isSuccess()) {
                                        Utils.user_current.setEmail(str_email);
                                        Utils.user_current.setPass(str_pass);

                                        Intent dangnhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                                        startActivity(dangnhap);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }, throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));


            } else {
                Toast.makeText(getApplicationContext(), "Bạn chưa nhập đúng mật khẩu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        repass = findViewById(R.id.repass);
        button = findViewById(R.id.btndangki);
        mobile = findViewById(R.id.mobile);
        username = findViewById(R.id.username);
        tv6 = findViewById(R.id.textView6);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}