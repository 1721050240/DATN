package com.example.doan.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Interface.ItemClickListener;
import com.example.doan.R;
import com.example.doan.model.EventBus.SuaXoaUserEvent;
import com.example.doan.model.User;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyViewHolder> {


    Context context;
    List<User> list;

    public AdapterUser(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);
        holder.tenuser.setText(user.getUsername());
        holder.emaiuser.setText(user.getEmail());
        holder.sdtuser.setText(user.getMobile());
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                EventBus.getDefault().postSticky(new SuaXoaUserEvent(user));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnLongClickListener, View.OnClickListener {
        TextView tenuser, emaiuser, sdtuser;
        ImageView update, delete;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tenuser = itemView.findViewById(R.id.item_tenuser);
            emaiuser = itemView.findViewById(R.id.item_emailuser);
            sdtuser = itemView.findViewById(R.id.item_sodienthoaiuser);
            update = itemView.findViewById(R.id.img_updateuser);
            delete = itemView.findViewById(R.id.img_delteuser);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, 0, getAdapterPosition(), "Sửa User");
            menu.add(0, 1, getAdapterPosition(), "Xóa User");

        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return false;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
