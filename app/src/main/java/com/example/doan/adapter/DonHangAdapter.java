package com.example.doan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Interface.ItemClickListener;
import com.example.doan.R;
import com.example.doan.model.DonHang;
import com.example.doan.model.EventBus.DonHangEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> donHangList;

    public DonHangAdapter(Context context, List<DonHang> donHangList) {
        this.context = context;
        this.donHangList = donHangList;
    }

    @NonNull
    @Override
    public DonHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangAdapter.MyViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        holder.txtdonhang.setText("Đơn hàng số : " + donHang.getId());
        holder.txttrangthai.setText(trangThaiDon(donHang.getTrangthai()));

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txttonggg.setText(donHang.getTongtien());

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rvChitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());

        AdapterChitiet adapterChitiet = new AdapterChitiet(context, donHang.getItem());
        holder.rvChitiet.setLayoutManager(layoutManager);
        holder.rvChitiet.setAdapter(adapterChitiet);
        holder.rvChitiet.setRecycledViewPool(viewPool);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (isLongClick){
                    EventBus.getDefault().postSticky(new DonHangEvent(donHang));
                }
            }
        });


    }

    private String trangThaiDon(int status) {
        String result = "";
        switch (status) {
            case 0:
                result = "Đơn hàng đang được xử lí";
                break;
            case 1:
                result = "Thành công";
                break;
            case 2:
                result = "Hủy Đơn Hàng";
                break;
        }

        return result;
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView txtdonhang, txttrangthai,txttonggg;
        RecyclerView rvChitiet;
        ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            txttrangthai = itemView.findViewById(R.id.trangthai);
            rvChitiet = itemView.findViewById(R.id.rv_chitiett);
            txttonggg = itemView.findViewById(R.id.tonggg);

            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return false;
        }
    }
}
