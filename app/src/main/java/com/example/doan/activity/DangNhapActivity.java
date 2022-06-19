package com.example.doan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.MainActivity;
import com.example.doan.R;
import com.example.doan.retrofitClient.ApiBanHang;
import com.example.doan.retrofitClient.RetrofitClient;
import com.example.doan.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {

    TextView txtDangKi;
    EditText email_dn, pass_dn;
    ImageView imgfb, imggg, imgmail;
    AppCompatButton btndangnhap;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();
    }

    private void initControl() {
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = email_dn.getText().toString().trim();
                String str_pass = pass_dn.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập email !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập pass !", Toast.LENGTH_SHORT).show();
                } else if (str_email == "Ok") {
                    Toast.makeText(getApplicationContext(), "Sai tài khoản !", Toast.LENGTH_SHORT).show();
                } else {
                    compositeDisposable.add(apiBanHang.dangNhap(str_email, str_pass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if (userModel.isSuccess()) {
                                            Utils.user_current = userModel.getResult().get(0);
                                            Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(main);
                                            finish();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));

                }

            }
        });


        txtDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dangki = new Intent(getApplicationContext(), DangKiActivity.class);
                startActivity(dangki);
            }
        });

        imgfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startFB = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                startActivity(startFB);
            }
        });

        imgmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startFB = new Intent(Intent.ACTION_VIEW, Uri.parse("https://shop.switch.com.my/login"));
                startActivity(startFB);
            }
        });

        imggg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startFB = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.vn/"));
                startActivity(startFB);
            }
        });
    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email_dn = findViewById(R.id.emai_dn);
        pass_dn = findViewById(R.id.pass_dn);
        txtDangKi = findViewById(R.id.txtdangki);
        btndangnhap = findViewById(R.id.btndangnhap);
        imgfb = findViewById(R.id.buttonfb);
        imggg = findViewById(R.id.buttongg);
        imgmail = findViewById(R.id.buttongmail);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getPass() != null) {
            email_dn.setText(Utils.user_current.getEmail());
            pass_dn.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}