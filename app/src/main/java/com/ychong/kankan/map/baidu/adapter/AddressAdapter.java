package com.ychong.kankan.map.baidu.adapter;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ychong.kankan.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<Address> addressList;
    private Context context;
    private OnClickListener onClickListener;
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    public AddressAdapter(Context context,List<Address> addressList){
        this.context = context;
        this.addressList = addressList;
    }
    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.addressNameTv.setText(addressList.get(position).getAddressLine(0));
        holder.addressLl.setOnClickListener(view -> onClickListener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return addressList==null?0:addressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout addressLl;
        private TextView addressNameTv;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            addressNameTv  = itemView.findViewById(R.id.address_name_tv);
            addressLl = itemView.findViewById(R.id.address_ll);
        }
    }
    public interface OnClickListener{
        void onClick(int position);
    }
}
