package com.ychong.kankan.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ychong.kankan.R;
import com.ychong.kankan.utils.http.RetrofitUtils;

import org.w3c.dom.Text;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
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
        backIv = findViewById(R.id.left_iv);
        titleTv = findViewById(R.id.title_tv);
        accountEt = findViewById(R.id.account_et);
        passwordEt = findViewById(R.id.password_et);
        rePasswordEt = findViewById(R.id.re_password_et);
        registerTv = findViewById(R.id.register_tv);
    }

    private void initLayout() {
        setContentView(R.layout.activity_register);
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
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        RetrofitUtils.getInstance()
                .getApiService()
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
