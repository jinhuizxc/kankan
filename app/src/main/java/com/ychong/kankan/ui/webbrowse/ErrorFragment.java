package com.ychong.kankan.ui.webbrowse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ychong.kankan.R;

/**
 * webview加载错误界面
 */
public class ErrorFragment extends Fragment {
    private Button refreshBtn;
    private RefreshListener refreshListener;

    public static ErrorFragment newInstance(){
        return new ErrorFragment();
    }
    public ErrorFragment(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_err,container,false);
        refreshBtn = view.findViewById(R.id.net_error_refresh);
        initListener();
        return view;
    }

    public void setRefreshListener(RefreshListener refreshListener){
        this.refreshListener = refreshListener;
    }


    private void initListener() {
        refreshBtn.setOnClickListener(v -> refreshListener.refresh());
    }

    public interface RefreshListener{
        void refresh();
    }
}
