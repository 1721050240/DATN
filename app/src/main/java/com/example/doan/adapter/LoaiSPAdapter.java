package com.example.doan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doan.R;
import com.example.doan.model.LoaiSanPham;

import java.util.List;

public class LoaiSPAdapter extends BaseAdapter {

    List<LoaiSanPham> arrLoaiSP;
    Context context;

    public LoaiSPAdapter( Context context,List<LoaiSanPham> arrLoaiSP) {
        this.arrLoaiSP = arrLoaiSP;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrLoaiSP.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class ViewHolder{
        TextView texttensp;
        ImageView imghinhanh;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_sanpham,null);
        viewHolder.texttensp = view.findViewById(R.id.item_tensp);
        viewHolder.imghinhanh = view.findViewById(R.id.item_image);
        view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();

        }
        viewHolder.texttensp.setText(arrLoaiSP.get(i).getTensanpham());
        Glide.with(context).load(arrLoaiSP.get(i).getHinhanh()).into(viewHolder.imghinhanh);

        return view;
    }
}
