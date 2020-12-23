package com.yf.androidmvp.base;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import butterknife.ButterKnife;

/**
 * @Description:
 * @Author: ZhengXiang Sun
 * @Data: 2020-12-23
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Bundle mBundle;

    /**
     * 恢复数据
     *
     * @param outState bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBundle != null) {
            outState.putBundle("bundle", mBundle);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle("bundle");
        }
        setContentView(getLayoutId());
        initPresenter();
        initViews();
        ButterKnife.bind(this);
    }

    /**
     * 实例化Presenter
     */
    protected abstract void initPresenter();

    /**
     * 实例化Views
     */
    protected abstract void initViews();

    /**
     * 得到布局ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 启动Fragment
     *
     * @param id
     * @param fragment
     */
    protected void startFragment(int id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(id, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 得到bundle
     *
     * @return bundle
     */
    public Bundle getBundle() {
        return mBundle;
    }

}
