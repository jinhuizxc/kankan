package com.ychong.kankan.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ychong.kankan.R;

import java.util.List;

/**
 * @author Administrator
 */
public class ColumnSelectAdapter extends RecyclerView.Adapter<ColumnSelectAdapter.ColumnSelectViewHolder> {

    private Context context;
    private List<String> list;
    private OnClickListener listener;
    public void setListener(OnClickListener listener){
        this.listener = listener;
    }
    public ColumnSelectAdapter(Context context,List<String> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ColumnSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ColumnSelectViewHolder(LayoutInflater.from(context).inflate(R.layout.item_column,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColumnSelectViewHolder holder, int position) {
        String name = list.get(position);
        holder.columnNameTv.setText(name);
        holder.columnNameTv.setOnClickListener(view -> {
            listener.click(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ColumnSelectViewHolder extends RecyclerView.ViewHolder {
        private TextView columnNameTv;
        public ColumnSelectViewHolder(@NonNull View itemView) {
            super(itemView);
            columnNameTv = itemView.findViewById(R.id.column_name_tv);
        }
    }

    public interface OnClickListener{
        void click(int position);
    }
}
