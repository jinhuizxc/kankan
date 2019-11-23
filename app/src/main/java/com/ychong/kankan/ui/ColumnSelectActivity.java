package com.ychong.kankan.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ychong.kankan.R;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.ui.other.MoreActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class ColumnSelectActivity extends BaseActivity {
    private RecyclerView selectRv;
    private RecyclerView unSelectRv;
    private List<String> selectList = new ArrayList<>();
    private List<String> unSelectList = new ArrayList<>();
    private ColumnSelectAdapter selectAdapter;
    private ColumnSelectAdapter unSelectAdapter;

    public static void startAct(Context context) {
        Intent intent = new Intent(context,ColumnSelectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_column_select;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initListener() {

        selectAdapter.setListener(new ColumnSelectAdapter.OnClickListener() {
            @Override
            public void click(int position) {
                String name = selectList.get(position);
                selectList.remove(name);
                selectAdapter.notifyDataSetChanged();
                unSelectList.add(name);
                unSelectAdapter.notifyDataSetChanged();
            }
        });
        unSelectAdapter.setListener(new ColumnSelectAdapter.OnClickListener() {
            @Override
            public void click(int position) {
                String name = unSelectList.get(position);
                unSelectList.remove(name);
                unSelectAdapter.notifyDataSetChanged();
                selectList.add(name);
                selectAdapter.notifyDataSetChanged();

            }
        });

    }

    private void initData() {

        selectAdapter = new ColumnSelectAdapter(this,selectList);
        selectRv.setLayoutManager(new GridLayoutManager(this,4));
        selectRv.setAdapter(selectAdapter);

        unSelectAdapter = new ColumnSelectAdapter(this,unSelectList);
        unSelectRv.setLayoutManager(new GridLayoutManager(this,4));
        unSelectRv.setAdapter(unSelectAdapter);

        for (int i=0;i<10;i++){
            unSelectList.add("栏目"+i);
        }

    }

    private void initView() {
        selectRv = findViewById(R.id.select_recycler_view);
        unSelectRv = findViewById(R.id.un_select_recycler_view);

    }
}
