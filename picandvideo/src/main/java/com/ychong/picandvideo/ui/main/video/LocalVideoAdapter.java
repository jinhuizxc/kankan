package com.ychong.picandvideo.ui.main.video;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ychong.picandvideo.R;
import com.ychong.picandvideo.entity.LocalVideoBean;
import com.ychong.picandvideo.ui.main.common.PreViewActivity;
import com.ychong.picandvideo.utils.BaseContract;

import java.io.File;
import java.util.List;

public class LocalVideoAdapter extends RecyclerView.Adapter<LocalVideoAdapter.LocalVideoViewHolder> {
    private Context context;
    private List<LocalVideoBean> list;

    public LocalVideoAdapter(Context context, List<LocalVideoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LocalVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocalVideoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_local_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocalVideoViewHolder holder, int position) {
        LocalVideoBean bean = list.get(position);
        holder.localVideoTv.setText(bean.path);
        Glide
                .with(context.getApplicationContext())
                .load(Uri.fromFile(new File(bean.path)))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.localVideoIv);
        holder.localVideoLayout.setOnClickListener(v -> PreViewActivity.startAct(context, BaseContract.VIDEO_TYPE, bean.path));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LocalVideoViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout localVideoLayout;
        private TextView localVideoTv;
        private ImageView localVideoIv;

        public LocalVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            localVideoLayout = itemView.findViewById(R.id.local_video_layout);
            localVideoTv = itemView.findViewById(R.id.local_video_tv);
            localVideoIv = itemView.findViewById(R.id.local_video_iv);
        }
    }
}
