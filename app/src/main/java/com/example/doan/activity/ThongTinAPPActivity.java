package com.example.doan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.doan.R;

public class ThongTinAPPActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbarTTApp;
    LinearLayout lienheFB,lienheWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_appactivity);
        initView();
        initControl();
    }

    private void initControl() {
        setSupportActionBar(toolbarTTApp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTTApp.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        toolbarTTApp = findViewById(R.id.toolbarTTApp);
        lienheFB = findViewById(R.id.lienheFB);
        lienheFB.setOnClickListener(this);
        lienheWeb = findViewById(R.id.lienheWeb);
        lienheWeb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lienheFB:
                Intent startfb = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/nguyenthe.hieu.5209/"));
                startActivity(startfb);
                break;
            case R.id.lienheWeb:
                Intent lienheWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.techone.vn/"));
                startActivity(lienheWeb);
                break;
        }
    }
}