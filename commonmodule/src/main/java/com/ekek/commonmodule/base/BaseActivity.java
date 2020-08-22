package com.ekek.commonmodule.base;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import solid.ren.skinlibrary.base.SkinBaseActivity;


public abstract class BaseActivity extends SkinBaseActivity {

    protected Context activity;
    protected FragmentManager fragmentManager;
    protected FragmentTransaction transaction;
    protected List<Fragment> fragments;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        doBeforeSetcontentView();
        setContentView(initLyaout());
        getWindow().setBackgroundDrawable(null);
        activity = this;
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        fragments = new ArrayList<>();
        //this.initPresenter();
        this.initView();
        this.initListener();

    }


    /**
     * 设置layout前配置
     */
    private void doBeforeSetcontentView() {

        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);
//        // 无标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void hideFragment(Fragment fragment) {
        transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

    public void removeFragment(Fragment fragment) {
        transaction = fragmentManager.beginTransaction();
        if (fragments.contains(fragment)) {
            fragments.remove(fragment);
            transaction.remove(fragment);
        }
        transaction.commit();
    }

    public void showFragment(Fragment fragment) {
        Logger.getInstance().i("showFragment(" + fragment.getClass().getSimpleName() + ")");
        fragmentManager.executePendingTransactions();
        transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(getContainerID(), fragment);
            transaction.show(fragment);
        }
        transaction.commit();
    }

    public void hideAllFragment(FragmentTransaction transaction) {
        for (int i = 0; i < fragments.size(); i++) {
            transaction.hide(fragments.get(i));
        }
    }


    /*********************子类实现*****************************/
    //获取布局文件
    public abstract int initLyaout();
    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    //public abstract void initPresenter();
    //初始化view
    public abstract void initView();

    protected abstract void initListener();
    //获取装fragment的容器ID
    protected abstract int getContainerID();




    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 初始化控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }


    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 开启浮动加载进度条
     */
    public void startProgressDialog() {
    }

    /**
     * 开启浮动加载进度条
     *
     * @param msg
     */
    public void startProgressDialog(String msg) {
    }

    /**
     * 停止浮动加载进度条
     */
    public void stopProgressDialog() {
    }



    @Override
    protected void onResume() {
        super.onResume();
        hideBottomUIMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("Enter test:: onDestroy");
        fragments.clear();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 通过包名跳转
     * @param context
     * @param activityName
     */
    public  void startActivityForName(Context context, String activityName) {
        try {
            Class clazz = Class.forName(activityName);
            Intent intent = new Intent(this,clazz);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
