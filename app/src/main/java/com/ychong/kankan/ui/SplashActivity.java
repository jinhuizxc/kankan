package com.ychong.kankan.ui;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ychong.kankan.R;
import com.ychong.kankan.ui.base.BaseActivity;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 开屏页
 * @author ychong
 */
public class SplashActivity extends Activity {
    private CompositeDisposable disposable = new CompositeDisposable();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initData();
    }

    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    private void initData() {
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()){
                            disposable.add(d);
                        }
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LoginActivity.startAct(SplashActivity.this);
                        SplashActivity.this.finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initLayout() {
        setContentView(getLayoutId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //判断是否解除订阅
        boolean disposed = disposable.isDisposed();
        if (!disposed){
            //消除订阅
            disposable.clear();
            //解除订阅
            disposable.dispose();
        }
    }
}
