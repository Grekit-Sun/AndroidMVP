package com.yf.androidmvp.base;

import java.lang.reflect.Constructor;

/**
 * @Description:
 * @Author: ZhengXiang Sun
 * @Data: 2020-12-23
 */
public abstract class BaseAppActivity<V extends IBaseView, P extends BasePresenter> extends BaseActivity{

    private P mPresenter;

    /**
     * init P
     */
    @Override
    protected void initPresenter() {
        try {
            Constructor constructor = getPresenterClass().getConstructor(getViewClass());
            mPresenter = (P) constructor.newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回View层的接口类.
     */
    protected abstract Class<V> getViewClass();

    /**
     * 返回逻辑处理的具体类型.
     */
    protected abstract Class<P> getPresenterClass();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
