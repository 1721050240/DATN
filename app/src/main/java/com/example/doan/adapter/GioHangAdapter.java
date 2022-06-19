package com.example.doan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.doan.Interface.ImageClickListenner;
import com.example.doan.R;
import com.example.doan.model.EventBus.TinhTongEvent;
import com.example.doan.model.GioHang;
import com.example.doan.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public GioHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(gioHang.getTensp());
        holder.item_giohang_soluong.setText(gioHang.getSoluong() + "");
        Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText("Giá : " + decimalFormat.format(gioHang.getGiasp()) + "đ");
        long gia = gioHang.getSoluong() * gioHang.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
        holder.setListenner(new ImageClickListenner() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1) {
                    if (gioHangList.get(pos).getSoluong() > 1) {
                        int soluongmoi = gioHangList.get(pos).getSoluong() - 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                } else if (giatri == 2) {
                    if (gioHangList.get(pos).getSoluong() < 11) {
                        int soluongmoi = gioHangList.get(pos).getSoluong() + 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                }
                holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong() + "");
                long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                EventBus.getDefault().postSticky(new TinhTongEvent());

                holder.linearLayout_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.manggiohang.remove(pos);
                        notifyDataSetChanged();

                    }
                });
            }
        });



    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView item_giohang_gia, item_giohang_giasp2, item_giohang_soluong, item_giohang_tensp;
        ImageView item_giohang_image, item_giohang_cong, item_giohang_tru;
        ImageClickListenner listenner;
        SwipeRevealLayout swipeRevealLayout;
        LinearLayout linearLayout_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_cong = itemView.findViewById(R.id.item_giohang_cong);
            item_giohang_tru = itemView.findViewById(R.id.item_giohang_tru);

            item_giohang_cong.setOnClickListener(this);
            item_giohang_tru.setOnClickListener(this);

            swipeRevealLayout = itemView.findViewById(R.id.swipereveallayout);
            linearLayout_delete = itemView.findViewById(R.id.layout_delete);
        }

        public void setListenner(ImageClickListenner listenner) {
            this.listenner = listenner;
        }

        @Override
        public void onClick(View v) {
            if (v == item_giohang_tru) {
                listenner.onImageClick(v, getAdapterPosition(), 1);
            } else if (v == item_giohang_cong) {
                listenner.onImageClick(v, getAdapterPosition(), 2);
            }
        }
    }
}
