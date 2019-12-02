package com.ychong.kankan.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ychong.kankan.R;
import com.ychong.baselib.base.BaseActivity;
import com.ychong.baselib.utils.http.RetrofitUtils;
import com.ychong.kankan.utils.ApiService;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {
    private ImageView backIv;
    private TextView titleTv;
    private EditText accountEt;
    private EditText passwordEt;
    private EditText rePasswordEt;
    private TextView registerTv;

    private String account;
    private String password;
    private String rePassword;

    public static void startAct(Context context) {
        Intent intent = new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    private void initListener() {
        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()){
                    register();
                }
            }
        });


    }

    private void initData() {

    }

    private void initView() {
        backIv = (ImageView) findViewById(R.id.left_iv);
        titleTv = (TextView) findViewById(R.id.title_tv);
        accountEt = (EditText) findViewById(R.id.account_et);
        passwordEt = (EditText) findViewById(R.id.password_et);
        rePasswordEt = (EditText) findViewById(R.id.re_password_et);
        registerTv = (TextView) findViewById(R.id.register_tv);
    }

    private void initLayout() {
        //setContentView(R.layout.activity_register);
    }

    private boolean checkData() {
        account = accountEt.getText().toString();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        password = passwordEt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        rePassword = rePasswordEt.getText().toString();
        if (password.equals(rePassword)) {
            Toast.makeText(this, "两次密码输入不同", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void register(){
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
        RetrofitUtils.getInstance(this).getRetrofit().create(ApiService.class)
                .register(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody body) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}
