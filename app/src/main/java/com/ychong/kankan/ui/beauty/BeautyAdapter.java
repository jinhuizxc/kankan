package com.ychong.kankan.ui.beauty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ychong.kankan.R;
import com.ychong.kankan.entity.BeautyBean;

import java.util.ArrayList;
import java.util.List;

public class BeautyAdapter extends RecyclerView.Adapter<BeautyAdapter.ViewHolder> {

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack{
        void onItemClick(int pos);
    }

    public List<BeautyBean> list = null;
    private ItemClickCallBack clickCallBack;
    private Context context;

    public BeautyAdapter(Context context,List<BeautyBean> list) {
        this.context = context;
        this.list = list;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_beauty,viewGroup,false);
        return new ViewHolder(view);
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder,final int position) {
        Glide.with(context.getApplicationContext())
                .load(list.get(position).url)
                .into(viewHolder.beautyIv);
        viewHolder.beautyIv.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(clickCallBack != null){
                            clickCallBack.onItemClick(position);
                        }
                    }
                }
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return list.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView beautyIv;
        public ViewHolder(View view){
            super(view);
            beautyIv = (ImageView) view.findViewById(R.id.beauty_iv);
        }
    }
}
