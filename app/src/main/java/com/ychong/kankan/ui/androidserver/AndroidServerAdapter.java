package com.ychong.kankan.ui.androidserver;

import android.annotation.SuppressLint;
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

public class AndroidServerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ApkInfoBean> list;
    boolean installAllowed = false;
    public AndroidServerAdapter(Context context,List<ApkInfoBean> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.empty_view, parent, false);
            return new EmptyViewHolder(view);
        } else {
            return new AndroidServerViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.layout_book_item, parent,
                    false));
        }
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AndroidServerViewHolder){
            AndroidServerViewHolder viewHolder = (AndroidServerViewHolder) holder;
            ApkInfoBean infoModel = list.get(position);
            viewHolder.mTvAppName.setText(infoModel.name + "(v" + infoModel.version + ")");
            viewHolder.mTvAppSize.setText(infoModel.size);
            viewHolder.mTvAppPath.setText(infoModel.path);
            viewHolder.ivIcon.setImageDrawable(infoModel.icon);
            viewHolder.mTvAppInstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        installAllowed = context.getPackageManager().canRequestPackageInstalls();
                        if (installAllowed) {
                            BaseUtils.installApkFile(context, new File(infoModel.path));
                        } else {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + context.getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            BaseUtils.installApkFile(context, new File(infoModel.path));
                            return;
                        }
                    } else {
                        BaseUtils.installApkFile(context, new File(infoModel.path));
                    }
                }
            });
            viewHolder.mTvAppDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseUtils.unInstall(context, infoModel.packageName);
                }
            });
            if (infoModel.installed) {
                viewHolder.mTvAppDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mTvAppDelete.setVisibility(View.GONE);
            }
        }

    }
    class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public int getItemCount() {
        return list.size() > 0 ? list.size() : 1;
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
