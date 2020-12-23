package com.yf.androidmvp.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Constructor;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.ContentValues.TAG;

/**
 * @Description:
 * @Author: ZhengXiang Sun
 * @Data: 2020-12-23
 */
public abstract class BaseFragment<V extends IBaseView, P extends BasePresenter> extends Fragment implements IBaseView {
    protected Context mContext;
    protected Bundle mBundle;
    protected Unbinder mUnbinder;
    protected View mView;

    protected P mPresenter;

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

    /**
     * 绑定activity
     *
     * @param context context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * 运行在onAttach之后，可以接收别人传递过来的参数，实例化对象
     * 可以解决返回的时候页面空白的bug
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle("bundle");
        } else {
            mBundle = getArguments() == null ? new Bundle() : getArguments();
        }
        initPresenter();
        if (mContext != null && getActivity() != null) {
            ButterKnife.bind(getActivity());
        }
    }

    /**
     * 运行在onCreate之后，生成View视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = initView(inflater, container, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    /**
     * 运行在onCreateView之后
     * 加载数据
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 跳转Fragment
     *
     * @param toFragment 跳转去的fragment
     */
    public void startFragment(Fragment toFragment) {
        Log.d(TAG, "toFragment:" + toFragment.getId());
        startFragment(toFragment, null);
    }

    /**
     * 跳转Fragment
     *
     * @param toFragment 跳转到的fragment
     * @param tag        fragment的标签
     */
    public void startFragment(Fragment toFragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.hide(this).add(android.R.id.content, toFragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * fragment进行回退
     * 类似于activity的OnBackPress
     */
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDetach() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDetach();
    }

    /**
     * 初始化Presenter
     */
    public BasePresenter initPresenter() {
        try {
            Constructor constructor = getPresenterClass().getConstructor(getViewClass());
            mPresenter = (P) constructor.newInstance(this);
        } catch (Exception e) {
//            Logger.e("Init presenter throw an error : [" + e.getMessage() + "]");
        }
        return mPresenter;
    }

    /**
     * 返回逻辑处理的具体类型.
     */
    protected abstract Class<P> getPresenterClass();

    /**
     * 返回View层的接口类.
     */
    protected abstract Class<V> getViewClass();

    /**
     * 初始化Fragment应有的视图
     *
     * @return view
     */
    public abstract View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected P getPresenter() {
        return mPresenter;
    }

    /**
     * 得到context
     *
     * @return context
     */
    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * 得到bundle
     *
     * @return bundle
     */
    public Bundle getBundle() {
        return mBundle;
    }

    /**
     * 得到fragment
     *
     * @return fragment
     */
    public Fragment getFragment() {
        return this;
    }
}
