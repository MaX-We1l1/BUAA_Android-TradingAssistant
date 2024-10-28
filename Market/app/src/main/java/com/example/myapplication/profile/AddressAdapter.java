package com.example.myapplication.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R; // 确保导入正确的包

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private List<Address> addressList;
    private Context context;

    public AddressAdapter(List<Address> addressList, Context context) {
        this.addressList = addressList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Address address = addressList.get(position);
        holder.addressName.setText(address.getName());
        holder.addressDetail.setText(address.getDetail());

        // 删除按钮点击事件 TODO
        holder.deleteButton.setOnClickListener(v -> {
            addressList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, addressList.size());
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView addressName, addressDetail;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addressName = itemView.findViewById(R.id.address_name);
            addressDetail = itemView.findViewById(R.id.address_detail);
            deleteButton = itemView.findViewById(R.id.button_delete_address);
        }
    }
}

