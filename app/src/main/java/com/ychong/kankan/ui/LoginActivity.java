package com.ychong.kankan.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.ychong.kankan.test.TestActivity;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.R;
import com.ychong.kankan.entity.UserBean;
import com.ychong.kankan.ui.other.MoreActivity;
import com.ychong.kankan.utils.http.RetrofitUtils;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {
    private EditText accountEt;
    private EditText passwordEt;
    private TextView loginTv;
    private TextView moreTv;
    private TextView registerTv;
    private String account;
    private String password;
    private SharedPreferences sp;
    private ImageView iconIv;
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null &&                       action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        initLayout();
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        loginTv.setOnClickListener(view -> {
            if (checkData()) {
                showProgressDialog(LoginActivity.this,"正在登录",false);
                UserBean bean = new UserBean();
                bean.account = account;
                bean.password = password;
                login(bean);
            }
        });
        registerTv.setOnClickListener(view -> {

        });
        moreTv.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, MoreActivity.class)));


    }

    private boolean checkData() {
        account = accountEt.getText().toString();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        password = passwordEt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login(UserBean userBean) {
        String json = new Gson().toJson(userBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        RetrofitUtils.getInstance().getApiService().login(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String json = responseBody.string();
                            JSONObject jsonObject = new JSONObject(json);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                showText("登录成功");
                                saveData(jsonObject.getString("data"));
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.toString());
                        hideProgressDialog();
                        showText("登录失败");
                    }

                    @Override
                    public void onComplete() {
                        hideProgressDialog();
                    }
                });
    }

    private void saveData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("account", jsonObject.getString("account"));
            editor.putString("password", jsonObject.getString("password"));
            editor.putInt("userId", jsonObject.getInt("id"));
            editor.apply();
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initData() {
        handler.postDelayed(runnable,500);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        accountEt.setText(sp.getString("account", ""));
        passwordEt.setText(sp.getString("password", ""));

    }

    private void initIcon() {
        //缩放
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iconIv,"ScaleX",1f,0.8f,0.6f,0.4f,0.2f,0.1f,0.2f,0.4f,0.6f,0.8f,1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iconIv,"ScaleY",1f,0.8f,0.6f,0.4f,0.2f,0.1f,0.2f,0.4f,0.6f,0.8f,1f);
         animatorSet = new AnimatorSet();
        animatorSet.setDuration(5000);
        animatorSet.play(scaleX);
        animatorSet.play(scaleY);
        animatorSet.start();
    }

    private void initView() {
        accountEt = findViewById(R.id.account_et);
        passwordEt = findViewById(R.id.password_et);
        loginTv = findViewById(R.id.login_tv);
        moreTv = findViewById(R.id.more_tv);
        registerTv = findViewById(R.id.register_tv);
        iconIv = findViewById(R.id.icon_iv);

    }
    private void initLayout() {
        setContentView(R.layout.activity_login);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            initIcon();
            handler.postDelayed(this, 5000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (animatorSet!=null){
            animatorSet.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animatorSet!=null){
            animatorSet.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animatorSet!=null){
            animatorSet.cancel();
            animatorSet=null;
        }
    }
}
