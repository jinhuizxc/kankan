package com.ychong.kankan.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.utils.http.ApiService;
import com.ychong.kankan.utils.BaseContract;
import com.ychong.kankan.utils.BaseUtils;
import com.ychong.kankan.utils.ftp.FtpFileHelper;
import com.ychong.kankan.entity.ImageBean;
import com.ychong.kankan.adapter.NinePicAdater;
import com.ychong.kankan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author Administrator
 * 上传图片
 */
@RuntimePermissions
public class AddActivity extends BaseActivity {
    private Button uploadBtn;
    private Button addBtn;
    private EditText titleEt;
    private RecyclerView recyclerView;
    private NinePicAdater adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private ImageView backIv;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        addBtn.setOnClickListener(view -> addPic());

        uploadBtn.setOnClickListener(view -> upload());

        backIv.setOnClickListener(view -> finish());

    }

    @SuppressLint("CheckResult")
    private void upload() {
        if (selectList == null || selectList.size() == 0) {
            Toast.makeText(AddActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressDialog(this,"正在上传",false);
        Observable.create(emitter -> {
            try {
                for (LocalMedia item : selectList) {
                    File file = new File(item.getPath());
                    FileInputStream in = new FileInputStream(file);
                    FtpFileHelper ftpFileHelper = new FtpFileHelper();
                    String currentTime = BaseUtils.gainCurrenTime();
                    //String name = currentTime+".jpg";
                    String name = file.getName();
                    boolean isSuccess = ftpFileHelper.uploadFile(ftpFileHelper.getConnectionFtp(), "ychong", name, in);
                    Log.e("上传文件成功", isSuccess + "--" + selectList.size());
                    if (isSuccess) {
                        //插入数据库
                        ImageBean bean = new ImageBean();
                        bean.name = name;
                        bean.path = BaseContract.FTP_HOST_URL + name;
                        bean.uploadTime = currentTime;
                        bean.title = titleEt.getText().toString();
                        bean.userId = sp.getInt("userId", -1);
                        insertData(bean);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });

    }

    @NeedsPermission(Manifest.permission.CAMERA)
    protected void addPic() {
        PictureSelector.create(AddActivity.this)
                .openGallery(PictureMimeType.ofAll())
                .maxSelectNum(1)
                .previewImage(true)
                .previewVideo(true)
                .isCamera(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);

    }

    private void initData() {
        adapter = new NinePicAdater(this, selectList);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        sp = getSharedPreferences("user", MODE_PRIVATE);
    }

    private void initView() {
        uploadBtn = findViewById(R.id.upload_btn);
        addBtn = findViewById(R.id.add_btn);
        recyclerView = findViewById(R.id.recyclerView);
        backIv = findViewById(R.id.left_iv);
        titleEt = findViewById(R.id.title_et);

    }

    private void initLayout() {
        setContentView(R.layout.activity_add);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    selectList.addAll(PictureSelector.obtainMultipleResult(data));
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    private void insertData(ImageBean bean) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseContract.SERVER_HOST_URL) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        String json = new Gson().toJson(bean);
        ApiService apiService = retrofit.create(ApiService.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        apiService.insertImage(body)
                .subscribeOn(Schedulers.io())
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
                            String msg = jsonObject.getString("msg");
                            boolean sucess = jsonObject.getBoolean("success");
                            Toast.makeText(AddActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if (sucess) {
                                //上传成功
                                finish();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ddddd", e.toString());
                        hideProgressDialog();

                    }

                    @Override
                    public void onComplete() {
                        Log.e("dddddd", "完成");
                        hideProgressDialog();
                    }
                });
    }
}
