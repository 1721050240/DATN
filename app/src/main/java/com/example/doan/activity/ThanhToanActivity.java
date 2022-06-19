package com.example.doan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.MainActivity;
import com.example.doan.R;
import com.example.doan.model.DonHang;
import com.example.doan.retrofitClient.ApiBanHang;
import com.example.doan.retrofitClient.RetrofitClient;
import com.example.doan.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    TextView txtTongTien, txtemailTT, txtSDT, txtTenTT, txtSoLuong;
    AppCompatButton btnThanhToan;
    Toolbar toolbarTT;
    EditText edtdiachi;
    int totalItem;
    long tongtien;
    int soluong;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
        totalItem = 0;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }

    }

    private void initControl() {
        setSupportActionBar(toolbarTT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTT.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien", 0);
        txtTongTien.setText(decimalFormat.format(tongtien));

        soluong = getIntent().getIntExtra("soluong", 0);
        txtSoLuong.setText(totalItem + "");
        txtemailTT.setText(Utils.user_current.getEmail());
        txtSDT.setText(Utils.user_current.getMobile());
        txtTenTT.setText(Utils.user_current.getUsername());


        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diachi = edtdiachi.getText().toString().trim();

                if (TextUtils.isEmpty(str_diachi)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ nhận hàng !", Toast.LENGTH_SHORT).show();

                } else {
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();


                    Log.d("ok", new Gson().toJson(Utils.manggiohang));
                    compositeDisposable.add(apiBanHang.createOder(str_email, str_sdt, String.valueOf(tongtien), id, str_diachi, totalItem, new Gson().toJson(Utils.manggiohang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {

                                        Toast.makeText(getApplicationContext(), "Thêm đơn hàng thành công !", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));

                }
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);


        txtSoLuong = findViewById(R.id.txtsoluongTT);
        txtTenTT = findViewById(R.id.txttenTT);
        txtTongTien = findViewById(R.id.txttongtienTT);
        txtemailTT = findViewById(R.id.txtemailTT);
        txtSDT = findViewById(R.id.txtdienthoaiTT);
        toolbarTT = findViewById(R.id.toolbarTT);
        btnThanhToan = findViewById(R.id.btnThanhToan_TT);
        edtdiachi = findViewById(R.id.edt_diachi_tt);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}