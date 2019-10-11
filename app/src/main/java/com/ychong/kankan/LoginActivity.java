package com.ychong.kankan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity {
    private EditText accountEt;
    private EditText passwordEt;
    private Button loginBtn;
    private String account;
    private String password;
    private SharedPreferences sp;

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
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    showLoadingDialog(LoginActivity.this);
                    UserBean bean = new UserBean();
                    bean.account = account;
                    bean.password = password;
                    login(bean);
                }
            }
        });

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseContract.SERVER_HOST_URL) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        String json = new Gson().toJson(userBean);
        ApiService apiService = retrofit.create(ApiService.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        apiService.login(body).subscribeOn(Schedulers.io())
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
                        hideLoadingDialog();
                        showText("登录失败");
                    }

                    @Override
                    public void onComplete() {
                        hideLoadingDialog();
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
        sp = getSharedPreferences("user", MODE_PRIVATE);
        accountEt.setText(sp.getString("account", ""));
        passwordEt.setText(sp.getString("password", ""));

    }

    private void initView() {
        accountEt = findViewById(R.id.account_et);
        passwordEt = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);

    }

    private void initLayout() {
        setContentView(R.layout.activity_login);
    }
}
