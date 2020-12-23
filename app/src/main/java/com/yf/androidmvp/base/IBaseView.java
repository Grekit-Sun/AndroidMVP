package com.yf.androidmvp.base;

/**
 * @Description: 一个接口，说明了每一个View基本需要的一些操作
 * @Author: ZhengXiang Sun
 * @Data: 2020-12-23
 */
public interface IBaseView {

    /**
     * 显示进度框
     */
    void showProgressDialog();

    /**
     * 关闭进度框
     */
    void hideProgressDialog();

    /**
     * 出错信息的回调
     *
     * @param result
     */
    void onError(String result);
}
