package com.ychong.kankan.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ychong.kankan.R;
import com.ychong.kankan.entity.ImageBean;

import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainRecyclerViewHolder> {
    private Context context;
    private List<ImageBean> list;
    private OnClickListener onClickListener;
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public MainRecyclerAdapter(Context context,List<ImageBean> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MainRecyclerAdapter.MainRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_pic, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerAdapter.MainRecyclerViewHolder holder, int position) {
        String path = list.get(position).path;
        if (TextUtils.isEmpty(path)){
            return;
        }
        String time = list.get(position).uploadTime;
        if (!TextUtils.isEmpty(time)){
            holder.uploadTimeTv.setText(time);
        }
        String title = list.get(position).title;
        if (!TextUtils.isEmpty(title)){
            holder.titleTv.setText(title);
        }
        Glide.with(context)
                .load(path)
                .placeholder(R.drawable.kankan)
                .override(400,500)
                .into(holder.picIv);
        holder.picIv.setOnClickListener(view -> onClickListener.onClick(path));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MainRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView authorTv;
        private TextView uploadTimeTv;
        private TextView titleTv;
        private ImageView picIv;

        MainRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            authorTv = itemView.findViewById(R.id.author_tv);
            uploadTimeTv = itemView.findViewById(R.id.time_tv);
            titleTv = itemView.findViewById(R.id.title_tv);
            picIv = itemView.findViewById(R.id.main_pic_iv);
        }
    }

    public interface OnClickListener{
        void onClick(String path);
    }

}
