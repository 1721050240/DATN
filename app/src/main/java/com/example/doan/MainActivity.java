package com.example.doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.doan.activity.DangNhapActivity;
import com.example.doan.activity.LoaiSanPhamActivity;
import com.example.doan.activity.GioHangActivity;
import com.example.doan.activity.ThongKeActivity;
import com.example.doan.quanli.QuanLiActivity;
import com.example.doan.activity.ThongTinAPPActivity;
import com.example.doan.activity.XemDonActivity;
import com.example.doan.adapter.DienThoaiAdapter;
import com.example.doan.adapter.LoaiSPAdapter;
import com.example.doan.adapter.SanPhamMoiAdapter;
import com.example.doan.model.LoaiSanPham;
import com.example.doan.model.SanPhamMoi;
import com.example.doan.quanli.QuanLiUserActivity;
import com.example.doan.retrofitClient.ApiBanHang;
import com.example.doan.retrofitClient.RetrofitClient;
import com.example.doan.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    RecyclerView rv_manhinhchinh;
    NavigationView navigationView;
    GridView listView_manhinhchinh;

    LoaiSPAdapter loaiSPAdapter;
    List<LoaiSanPham> mangloaisp;

    DrawerLayout drawerLayout;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    List<SanPhamMoi> mangSpmoi;
    SanPhamMoiAdapter spAdapter;

    NotificationBadge bage;
    FrameLayout frameLayout;

    EditText mai;
    RecyclerView rv_timKiem2;
    DienThoaiAdapter adapterSearch2;
    List<SanPhamMoi> listSearch2;
    ApiBanHang apiBanHang2;
    CompositeDisposable compositeDisposable2 = new CompositeDisposable();
    EditText editText_timkiem;
    BottomNavigationView bottomNavigationView;

    ViewFlipper viewFlipper;
    Animation in, out;

    TextView name_user, email_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        initView();
        viewFlipper();
        iniViewSearch();
        ActionBar();
//        BottomNavigationClick();

        if (isConnected(this)) {
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        } else {
            Toast.makeText(getApplicationContext(), "Không có internet, vui lòng kiểm tra kết nối!", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewFlipper() {
        in = AnimationUtils.loadAnimation(this, R.anim.slide_int_right);
        out = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

    }

//    private void BottomNavigationClick() {
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.navigation_zalo5:
//                        Toast.makeText(getApplicationContext(), "Zalo 5 ", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.navigation_mess5:
//                        Toast.makeText(getApplicationContext(), "Mess 5 ", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.navigation_shopping5:
//                        Intent intent = new Intent(MainActivity.this, GioHangActivity.class);
//                        startActivity(intent);
//                        break;
//                    case R.id.navigation_call5:
//                        Toast.makeText(getApplicationContext(), "Call 5 ", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.navigation_map5:
//                        Intent xemdon = new Intent(MainActivity.this, XemDonActivity.class);
//                        startActivity(xemdon);
//                        break;
//                }
//                return true;
//            }
//
//        });
//    }

    private void iniViewSearch() {
        editText_timkiem = findViewById(R.id.edt_timkiem);
        listSearch2 = new ArrayList<>();
        apiBanHang2 = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        rv_timKiem2 = findViewById(R.id.rv_timkiem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_timKiem2.setHasFixedSize(true);
        rv_timKiem2.setLayoutManager(layoutManager);
        editText_timkiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() == 0) {
                    listSearch2.clear();
                    adapterSearch2 = new DienThoaiAdapter(getApplicationContext(), listSearch2);
                    rv_timKiem2.setAdapter(adapterSearch2);
                } else {
                    getDataSearch(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getDataSearch(String s) {
        listSearch2.clear();
        compositeDisposable2.add(apiBanHang2.search(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                listSearch2 = sanPhamMoiModel.getResult();
                                adapterSearch2 = new DienThoaiAdapter(getApplicationContext(), listSearch2);
                                rv_timKiem2.setAdapter(adapterSearch2);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void getEventClick() {
        listView_manhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;

                    case 1:
                        Intent IPhone = new Intent(getApplicationContext(), LoaiSanPhamActivity.class);
                        IPhone.putExtra("loai", 1);
                        startActivity(IPhone);
                        break;

                    case 2:
                        Intent SamSung = new Intent(getApplicationContext(), LoaiSanPhamActivity.class);
                        SamSung.putExtra("loai", 2);
                        startActivity(SamSung);
                        break;
                    case 3:
                        Intent PhuKien = new Intent(getApplicationContext(), LoaiSanPhamActivity.class);
                        PhuKien.putExtra("loai", 3);
                        startActivity(PhuKien);
                        break;

                    case 4:
                        Intent LienHe = new Intent(getApplicationContext(), ThongTinAPPActivity.class);
                        startActivity(LienHe);
                        break;

                    case 5:
                        Intent hoadon = new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(hoadon);
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                mangSpmoi = sanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpmoi);
                                rv_manhinhchinh.setAdapter(spAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với sever", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()) {
                                mangloaisp = loaiSpModel.getResult();
                                loaiSPAdapter = new LoaiSPAdapter(getApplicationContext(), mangloaisp);
                                listView_manhinhchinh.setAdapter(loaiSPAdapter);
                            }
                        }
                ));
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;

        } else {
            return false;
        }
    }


    private void initView() {
        name_user = findViewById(R.id.name_user);
        email_user = findViewById(R.id.gmail_user);
        name_user.setText(Utils.user_current.getUsername());
        email_user.setText(Utils.user_current.getEmail());

        viewFlipper = findViewById(R.id.view_flipper);


//        bottomNavigationView = findViewById(R.id.bt_navigation);
        toolbar = findViewById(R.id.tollbar);
        rv_manhinhchinh = findViewById(R.id.rv_spmn);
        listView_manhinhchinh = findViewById(R.id.lv_manhinhchinh);
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        mangSpmoi = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        rv_manhinhchinh.setLayoutManager(layoutManager);
        rv_manhinhchinh.setHasFixedSize(true);

        mangloaisp = new ArrayList<>();

        drawerLayout = findViewById(R.id.drawerlayout);
        frameLayout = findViewById(R.id.framegiohang);
        bage = findViewById(R.id.menu_sl);

        if (Utils.manggiohang == null) {
            Utils.manggiohang = new ArrayList<>();
        } else {
            int totalItem = 0;
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            bage.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        bage.setText(String.valueOf(totalItem));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable2.clear();
        compositeDisposable.clear();
        super.onDestroy();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_trangchu:
                Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(trangchu);
                break;
            case R.id.navigation_iphone:
                Intent IPhone1 = new Intent(getApplicationContext(), LoaiSanPhamActivity.class);
//                IPhone.putExtra("loai", 1);
                startActivity(IPhone1);
                break;
            case R.id.navigation_samsung:
                Intent SamSung = new Intent(getApplicationContext(), LoaiSanPhamActivity.class);
                SamSung.putExtra("loai", 2);
                startActivity(SamSung);
                break;
            case R.id.navigation_phukien:
                Intent PhuKien = new Intent(getApplicationContext(), LoaiSanPhamActivity.class);
                PhuKien.putExtra("loai", 3);
                startActivity(PhuKien);
                break;
            case R.id.navigation_thongtin:
                Intent ThonTinApp = new Intent(getApplicationContext(), ThongTinAPPActivity.class);
                startActivity(ThonTinApp);
                break;

            case R.id.navigation_quanli:

                if (name_user.getText().equals("Admin")) {
                    Intent quanli = new Intent(MainActivity.this, QuanLiActivity.class);
                    startActivity(quanli);
                } else {
                    Toast.makeText(getApplicationContext(), "Dành cho Admin", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.navigation_quanliuser:
                if (name_user.getText().equals("Admin")) {
                    Intent quanliuser = new Intent(MainActivity.this, QuanLiUserActivity.class);
                    startActivity(quanliuser);
                } else {
                    Toast.makeText(getApplicationContext(), "Dành cho Admin", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.navigation_quanlidonhang:
                Intent quanlidonhang = new Intent(MainActivity.this, XemDonActivity.class);
                startActivity(quanlidonhang);
                break;
            case R.id.navigation_thongke:
                if (name_user.getText().equals("Admin")) {
                    Intent thongke = new Intent(MainActivity.this, ThongKeActivity.class);
                    startActivity(thongke);
                } else {
                    Toast.makeText(getApplicationContext(), "Dành cho Admin", Toast.LENGTH_SHORT).show();
                }

                break;


            case R.id.navigation_dangxuat:
                Toast.makeText(getApplicationContext(), "Đăng xuất", Toast.LENGTH_SHORT).show();
                Intent dangxuat = new Intent(MainActivity.this, DangNhapActivity.class);
                startActivity(dangxuat);
                break;

        }
        return true;
    }
}