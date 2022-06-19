package com.example.doan.quanli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.activity.DangKiActivity;
import com.example.doan.adapter.AdapterUser;
import com.example.doan.adapter.SanPhamMoiAdapter;
import com.example.doan.model.EventBus.SuaXoaUserEvent;
import com.example.doan.model.User;
import com.example.doan.retrofitClient.ApiBanHang;
import com.example.doan.retrofitClient.RetrofitClient;
import com.example.doan.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanLiUserActivity extends AppCompatActivity {
    Toolbar toolbarQuanLiUser;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    RecyclerView rv_user;
    List<User> userArrayList;
    AdapterUser adapterUser;

    User userSuaXoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li_user);
        initView();
        initToolBarUser();
        getDatauser();
    }

    private void getDatauser() {
        compositeDisposable.add(apiBanHang.getuser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                userArrayList = userModel.getResult();
                                adapterUser = new AdapterUser(getApplicationContext(), userArrayList);
                                rv_user.setAdapter(adapterUser);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với sever", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void initToolBarUser() {
        setSupportActionBar(toolbarQuanLiUser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarQuanLiUser.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbarQuanLiUser = findViewById(R.id.toolbarQuanLiUser);
        rv_user = findViewById(R.id.rv_user);
        userArrayList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        rv_user.setLayoutManager(layoutManager);
        rv_user.setHasFixedSize(true);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Sửa User")) {
            SuaUser();
        } else if (item.getTitle().equals("Xóa User")) {
            XoaUser();
        }
        return super.onContextItemSelected(item);
    }

    private void SuaUser() {
        Intent intent = new Intent(getApplicationContext(), DangKiActivity.class);
        intent.putExtra("Sua User", userSuaXoa);
        startActivity(intent);
    }

    private void XoaUser() {
        compositeDisposable.add(apiBanHang.xoaUser(userSuaXoa.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()) {
                                Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                getDatauser();
                            } else {
                                Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                getDatauser();
                            }
                        },
                        throwable -> {
                            Log.d("Log", throwable.getMessage());
                        }
                ));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventSuaXoaUser(SuaXoaUserEvent event) {
        if (event != null) {
            userSuaXoa = event.getUser();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}