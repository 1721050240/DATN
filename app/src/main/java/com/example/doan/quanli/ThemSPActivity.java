package com.example.doan.quanli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.model.SanPhamMoi;
import com.example.doan.retrofitClient.ApiBanHang;
import com.example.doan.retrofitClient.RetrofitClient;
import com.example.doan.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThemSPActivity extends AppCompatActivity {
    Toolbar toolbarThemSP;
    AppCompatButton btn_add;
    Spinner spinnerThemSP;
    int loai = 0;
    EditText ten, gia, mota, hinhanh;
    ImageView img_camera;

    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    SanPhamMoi sanPhamSua;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_spactivity);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        initView();
        initToolBarThem();
        initData();

        Intent intent = getIntent();
        sanPhamSua = (SanPhamMoi) intent.getSerializableExtra("sua");
        if (sanPhamSua == null) {
            flag = false;
        } else {
            flag = true;
            btn_add.setText("Sửa sản phẩm");

            mota.setText(sanPhamSua.getMota());
            gia.setText(sanPhamSua.getGiasp());
            ten.setText(sanPhamSua.getTensanpham());
            hinhanh.setText(sanPhamSua.getHinhanh());
            spinnerThemSP.setSelection(sanPhamSua.getLoai());
        }


    }

    private void initData() {
        List<String> strings = new ArrayList<>();
        strings.add("Vui lòng chọn loại sản phẩm : ");
        strings.add("Loại 1");
        strings.add("Loại 2");
        strings.add("Loại 3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strings);
        spinnerThemSP.setAdapter(adapter);
        spinnerThemSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                loai = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == false) {
                    themsanpham();
                } else {
                    suaSanPham();
                }

            }
        });

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ThemSPActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


    }

    private void suaSanPham() {
        String str_ten = ten.getText().toString().trim();
        String str_gia = gia.getText().toString().trim();
        String str_mota = mota.getText().toString().trim();
        String str_hinhanh = hinhanh.getText().toString().trim();

        if (TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) || loai == 0) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập đủ thông tin !", Toast.LENGTH_SHORT).show();
        } else {
            compositeDisposable.add(apiBanHang.updatesp(str_ten, str_gia, str_hinhanh, str_mota, loai,sanPhamSua.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()) {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));
        }
    }

    private void themsanpham() {
        String str_ten = ten.getText().toString().trim();
        String str_gia = gia.getText().toString().trim();
        String str_mota = mota.getText().toString().trim();
        String str_hinhanh = hinhanh.getText().toString().trim();

        if (TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) || loai == 0) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập đủ thông tin !", Toast.LENGTH_SHORT).show();
        } else {
            compositeDisposable.add(apiBanHang.insertSp(str_ten, str_gia, str_hinhanh, str_mota, loai)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()) {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));
        }
    }

    private void initToolBarThem() {
        setSupportActionBar(toolbarThemSP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThemSP.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        toolbarThemSP = findViewById(R.id.toolbarThemSP);
        btn_add = findViewById(R.id.btn_add);
        spinnerThemSP = findViewById(R.id.spinner_loai);
        ten = findViewById(R.id.ten_add);
        gia = findViewById(R.id.gia_add);
        mota = findViewById(R.id.mota_add);
        hinhanh = findViewById(R.id.hinhanh_add);
        img_camera = findViewById(R.id.img_camera);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}