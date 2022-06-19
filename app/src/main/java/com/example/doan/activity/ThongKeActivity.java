package com.example.doan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.adapter.AdapterUser;
import com.example.doan.adapter.DonHangAdapter;
import com.example.doan.adapter.SanPhamMoiAdapter;
import com.example.doan.quanli.HangDaHetActivity;
import com.example.doan.retrofitClient.ApiBanHang;
import com.example.doan.retrofitClient.RetrofitClient;
import com.example.doan.utils.Utils;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThongKeActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    Toolbar toolbarTK;
    TextView tongdoanhthu, tongdonhang, xemhangdahet, sizeUser, hangtonkho, donhangthanhcong, dahethang;
    int dahet = 0, dhthanhcong = 0, tongdh = 0, tongtien = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        initView();
        getData();
        initTollBar();
        getSizeUser();
        getSoLuongTonKho();
        getMatHangDaHet();
        getDataDonHangThanhCong();

    }

    private void getDataDonHangThanhCong() {
        compositeDisposable.add(apiBanHang.xemDonHang(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            tongdonhang.setText(donHangModel.getResult().size() + " Đơn hàng");
                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                            for (int i = 0; i < donHangModel.getResult().size(); i++) {
                                if (donHangModel.getResult().get(i).getTrangthai() == 1) {
                                    dhthanhcong = dhthanhcong + 1;
                                }

                            }

//                            tongdoanhthu.setText(decimalFormat.format(Double.parseDouble(donHangModel.getResult().get(0).getTongtien())) +" vnđ");
                            donhangthanhcong.setText(dhthanhcong + " Đơn hàng đã giao dịch");

                        },
                        throwable -> {

                        }
                ));
    }

    private void getMatHangDaHet() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            for (int i = 0; i < sanPhamMoiModel.getResult().size(); i++) {
                                if (sanPhamMoiModel.getResult().get(i).getSoluong().equals("0")) {
                                    dahet = dahet + 1;
                                }

                            }
                            dahethang.setText(dahet + " sản phẩm ok");

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với sever", Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    private void getSoLuongTonKho() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            for (int i = 0; i < sanPhamMoiModel.getResult().size(); i++) {
                                tongdh = Integer.parseInt(sanPhamMoiModel.getResult().get(i).getSoluong()) + tongdh;

                            }
                            hangtonkho.setText("Hàn tồn kho : " + tongdh + " sản phẩm");

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với sever", Toast.LENGTH_SHORT).show();
                        }
                ));
    }


    private void getSizeUser() {
        compositeDisposable.add(apiBanHang.getuser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                sizeUser.setText("Hiện có :" + (userModel.getResult().size() - 1) + " tài khoản đăng kí");
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với sever", Toast.LENGTH_SHORT).show();
                        }
                ));
    }


    private void getData() {
        compositeDisposable.add(apiBanHang.xemDonHang(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            tongdonhang.setText(donHangModel.getResult().size() + " Đơn hàng");
                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                            for (int i = 0; i < donHangModel.getResult().size(); i++) {
                                if (donHangModel.getResult().get(i).getTrangthai() == 1) {
                                    tongtien = Integer.parseInt(donHangModel.getResult().get(i).getTongtien()) + tongtien;
                                }

                            }

//                            tongdoanhthu.setText(decimalFormat.format(Double.parseDouble(donHangModel.getResult().get(0).getTongtien())) +" vnđ");
                            tongdoanhthu.setText(decimalFormat.format(tongtien) + " vnđ");

                        },
                        throwable -> {

                        }
                ));

    }

    private void initTollBar() {
        setSupportActionBar(toolbarTK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTK.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbarTK = findViewById(R.id.toolbarTK);
        tongdoanhthu = findViewById(R.id.tv_tongdoanhthu);
        tongdonhang = findViewById(R.id.tv_tongdonhang);
        sizeUser = findViewById(R.id.soluonguser);
        hangtonkho = findViewById(R.id.hangtonkho);
        donhangthanhcong = findViewById(R.id.tv_donhangthanhcong);
        dahethang = findViewById(R.id.hangdahet);
        xemhangdahet = findViewById(R.id.xemhangdahet);

        xemhangdahet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongKeActivity.this, HangDaHetActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}