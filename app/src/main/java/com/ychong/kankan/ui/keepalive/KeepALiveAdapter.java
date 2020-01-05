package com.ychong.kankan.ui.keepalive;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ychong.kankan.R;

import java.util.List;

public class KeepALiveAdapter extends RecyclerView.Adapter<KeepALiveAdapter.KeepALiveViewHolder> {
    private List<GuideBean> list ;
    private Context context;
    public KeepALiveAdapter(Context context,List<GuideBean> list){
        this.context =context;
        this.list = list;
    }
    @NonNull
    @Override
    public KeepALiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guide,parent,false);
        return new KeepALiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeepALiveViewHolder holder, int position) {
        GuideBean item = list.get(position);
        holder.picIv.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),item.pic));
        holder.descTv.setText(item.desc);

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class KeepALiveViewHolder extends RecyclerView.ViewHolder {
        private ImageView picIv;
        private TextView descTv;
        public KeepALiveViewHolder(@NonNull View itemView) {
            super(itemView);
            picIv = itemView.findViewById(R.id.pic_iv);
            descTv = itemView.findViewById(R.id.desc_tv);
        }
    }
}
