package com.example.doan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.adapter.GioHangAdapter;
import com.example.doan.model.EventBus.TinhTongEvent;
import com.example.doan.model.GioHang;
import com.example.doan.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView tongtien;
    Toolbar toolbarGH;
    RecyclerView rv_giohang;
    Button btnmuahang;
    GioHangAdapter adapter;
    List<GioHang> gioHangList;
    long tongtiensp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initeControl();
        tinhTongTien();
    }

    private void tinhTongTien() {
        tongtiensp = 0;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            tongtiensp = tongtiensp + (Utils.manggiohang.get(i).getGiasp() * Utils.manggiohang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongtiensp));

    }

    private void initeControl() {
        setSupportActionBar(toolbarGH);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGH.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_giohang.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_giohang.setLayoutManager(layoutManager);
        if (Utils.manggiohang.size() == 0) {
            Toast.makeText(getApplicationContext(), "Bạn chưa mua hàng", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new GioHangAdapter(getApplicationContext(), Utils.manggiohang);
            rv_giohang.setAdapter(adapter);

        }


        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.manggiohang.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa mua hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Intent thanhtoan = new Intent(GioHangActivity.this, ThanhToanActivity.class);
                    thanhtoan.putExtra("tongtien", tongtiensp);
                    thanhtoan.putExtra("soluong",Utils.manggiohang.size());
                    startActivity(thanhtoan);
                }

            }
        });

    }

    private void initView() {
        toolbarGH = findViewById(R.id.toolbarGH);
        tongtien = findViewById(R.id.txttongtien);
        btnmuahang = findViewById(R.id.btnmuahang);
        rv_giohang = findViewById(R.id.rv_giohang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event) {
        if (event != null) {
            tinhTongTien();
        }
    }
}