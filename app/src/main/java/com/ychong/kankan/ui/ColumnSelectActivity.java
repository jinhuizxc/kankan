package com.ychong.kankan.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ychong.kankan.R;
import com.ychong.baselib.base.BaseActivity;
import com.ychong.kankan.utils.animator.FlyAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class ColumnSelectActivity extends BaseActivity {
    private ImageView backIv;
    private TextView titleTv;
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
        backIv.setOnClickListener(v -> onBackPressed());
    }

    private void initData() {
        titleTv.setText("栏目选择");

        selectAdapter = new ColumnSelectAdapter(this,selectList);
        selectRv.setItemAnimator(new DefaultItemAnimator());
        selectRv.setLayoutManager(new GridLayoutManager(this,4));
        selectRv.setAdapter(selectAdapter);

        unSelectAdapter = new ColumnSelectAdapter(this,unSelectList);
        unSelectRv.setItemAnimator(new FlyAnimator());
        unSelectRv.setLayoutManager(new GridLayoutManager(this,4));
        unSelectRv.setAdapter(unSelectAdapter);

        for (int i=0;i<20;i++){
            unSelectList.add("栏目"+i);
        }

    }

    private void initView() {
        backIv = (ImageView) findViewById(R.id.right_iv);
        titleTv = (TextView) findViewById(R.id.title_tv);
        selectRv = (RecyclerView) findViewById(R.id.select_recycler_view);
        unSelectRv = (RecyclerView) findViewById(R.id.un_select_recycler_view);

    }
}
