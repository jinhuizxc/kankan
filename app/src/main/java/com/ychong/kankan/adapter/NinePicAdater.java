package com.ychong.kankan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.entity.LocalMedia;
import com.ychong.kankan.R;

import java.io.File;
import java.util.List;

/**
 * @author Administrator
 */
public class NinePicAdater extends RecyclerView.Adapter<NinePicAdater.NinePicViewHolder> {

    private  List<LocalMedia> selectList;
    private  Context context;

    public NinePicAdater(Context context, List<LocalMedia> selectList){
        this.context = context;
        this.selectList = selectList;
    }

    @NonNull
    @Override
    public NinePicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NinePicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pic,null));
    }

    @Override
    public void onBindViewHolder(@NonNull NinePicViewHolder holder, int position) {
        String path = selectList.get(position).getPath();
        Log.e("path",path);
        Glide.with(context).load(new File(path)).override(500).into(holder.picIv);

    }

    @Override
    public int getItemCount() {
        return selectList.size();
    }

    class NinePicViewHolder  extends RecyclerView.ViewHolder {
        private ImageView picIv;
        public NinePicViewHolder(@NonNull View itemView) {
            super(itemView);
            picIv = itemView.findViewById(R.id.pic_iv);
        }
    }
}
