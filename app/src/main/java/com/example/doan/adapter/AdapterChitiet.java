package com.example.doan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.R;
import com.example.doan.model.Item;

import java.util.List;

public class AdapterChitiet extends RecyclerView.Adapter<AdapterChitiet.MyViewHolder> {

    Context context;
    List<Item> itemlist;

    public AdapterChitiet(Context context, List<Item> itemlist) {
        this.context = context;
        this.itemlist = itemlist;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemlist.get(position);
        holder.txtTen.setText(item.getTensanpham() + "");
        holder.txtSoluong.setText("Số lượng :" + item.getSoluong() + "");
        Glide.with(context).load(item.getHinhanh()).into(holder.imagechitiet);
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagechitiet;
        TextView txtTen, txtSoluong;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagechitiet = itemView.findViewById(R.id.item_imgchitiet);
            txtTen = itemView.findViewById(R.id.item_tenspchitiet);
            txtSoluong = itemView.findViewById(R.id.item_soluongchitiet);
        }
    }
}
