package com.ychong.kankan.ui.androidserver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ychong.kankan.R;
import com.ychong.kankan.entity.ApkInfoBean;
import com.ychong.kankan.utils.BaseUtils;

import java.io.File;
import java.util.List;

public class AndroidServerAdapter extends RecyclerView.Adapter<AndroidServerAdapter.AndroidServerViewHolder> {
    private Context context;
    private List<ApkInfoBean> list;
    public AndroidServerAdapter(Context context,List<ApkInfoBean> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public AndroidServerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AndroidServerViewHolder viewHolder = new AndroidServerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_apk,parent,false));
        return viewHolder;
    }
    boolean installAllowed = false;
    @Override
    public void onBindViewHolder(@NonNull AndroidServerViewHolder holder, int position) {
            ApkInfoBean item = list.get(position);
            holder.mTvAppName.setText(item.name + "(v" + item.version + ")");
            holder.mTvAppSize.setText(item.size);
            holder.mTvAppPath.setText(item.path);
            holder.ivIcon.setImageDrawable(item.icon);

            holder.mTvAppInstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        installAllowed = context.getPackageManager().canRequestPackageInstalls();
                        if (installAllowed) {
                            BaseUtils.installApkFile(context, new File(item.path));
                        } else {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + context.getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            BaseUtils.installApkFile(context, new File(item.path));
                            return;
                        }
                    } else {
                        BaseUtils.installApkFile(context, new File(item.path));
                    }
                }
            });
            holder.mTvAppDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseUtils.delete(context, item.packageName);
                }
            });

            if (item.installed) {
                holder.mTvAppDelete.setVisibility(View.VISIBLE);
            } else {
                holder.mTvAppDelete.setVisibility(View.GONE);
            }
    }

    @Override
    public int getItemCount() {
        return list.size()>0?list.size():1;
    }

    public class AndroidServerViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAppName;
        TextView mTvAppSize;
        TextView mTvAppInstall;
        TextView mTvAppDelete;
        TextView mTvAppPath;
        ImageView ivIcon;

        public AndroidServerViewHolder(View view) {
            super(view);
            mTvAppName =  view.findViewById(R.id.tv_name);
            mTvAppSize =  view.findViewById(R.id.tv_size);
            mTvAppInstall =  view.findViewById(R.id.tv_install);
            mTvAppPath =  view.findViewById(R.id.tv_path);
            mTvAppDelete =  view.findViewById(R.id.tv_delete);
            ivIcon =  view.findViewById(R.id.iv_icon);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0) {
            return 1;
        }
        return super.getItemViewType(position);
    }
}
