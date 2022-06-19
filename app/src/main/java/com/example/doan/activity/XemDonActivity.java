package com.example.doan.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doan.R;
import com.example.doan.adapter.DonHangAdapter;
import com.example.doan.model.DonHang;
import com.example.doan.model.EventBus.DonHangEvent;
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

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView rv_donhang;
    Toolbar toolbarDH;
    int tinhtrang;
    AlertDialog dialog;
    int soluongdonhang, tongdoanhthu,dagiao,dahuy,dangxuli;


    DonHang donHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);
        initView();
        initToolbar();
        getDonHang();
        thongke();

    }

    private void thongke() {

    }

    private void getDonHang() {
        if (Utils.user_current.getUsername().equals("Admin")) {
            compositeDisposable.add(apiBanHang.xemDonHang(0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            donHangModel -> {
                                DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(), donHangModel.getResult());
                                rv_donhang.setAdapter(adapter);
                            },
                            throwable -> {

                            }
                    ));
        } else {
            compositeDisposable.add(apiBanHang.xemDonHang(Utils.user_current.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            donHangModel -> {
//                                Toast.makeText(getApplicationContext(), donHangModel.getResult().get(0).getItem().get(0).getTensanpham(), Toast.LENGTH_SHORT).show();
                                DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(), donHangModel.getResult());
                                rv_donhang.setAdapter(adapter);
                            },
                            throwable -> {

                            }
                    ));
        }

    }


    private void initToolbar() {
        setSupportActionBar(toolbarDH);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDH.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbarDH = findViewById(R.id.toobarxemdonhang);

        rv_donhang = findViewById(R.id.rv_xemdonhang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_donhang.setLayoutManager(layoutManager);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void evenDonHang(DonHangEvent event) {
        if (event != null) {
            donHang = event.getDonHang();
            if (Utils.user_current.getUsername().equals("Admin")) {
                showCustumDiaLog();
            } else {
                Toast.makeText(getApplicationContext(), "K thể thay đổi", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void showCustumDiaLog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_donhang, null);
        Spinner spinner = view.findViewById(R.id.spinner_dialog);
        AppCompatButton btndongy = view.findViewById(R.id.dongy_dialog);
        List<String> list = new ArrayList<>();
        list.add("Đơn hàng đang được xử lí");
        list.add("Thành Công");
        list.add("Hủy đơn hàng");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
        spinner.setSelection(donHang.getTrangthai());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tinhtrang = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btndongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDonHang();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void updateDonHang() {
        compositeDisposable.add(apiBanHang.updateDonHang(donHang.getId(), tinhtrang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            getDonHang();
                            dialog.dismiss();
                        },
                        throwable -> {

                        }
                ));
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