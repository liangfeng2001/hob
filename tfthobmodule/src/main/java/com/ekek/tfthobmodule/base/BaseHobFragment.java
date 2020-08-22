package com.ekek.tfthobmodule.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.event.SoundEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import solid.ren.skinlibrary.base.SkinBaseFragment;

public abstract class BaseHobFragment extends SkinBaseFragment {

    public static final int LATERAL_MENU_BAR_NOT_DEFINED = -1;
    public static final int LATERAL_MENU_BAR_CLOSE = 0;
    public static final int LATERAL_MENU_BAR_OPEN = 1;

    Unbinder unbinder;
    protected View mRootView;
    protected Context mContext;
    protected BaseFragment.OnFragmentListener mListener;
    private int lateralMenuBarOpen = LATERAL_MENU_BAR_NOT_DEFINED;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseFragment.OnFragmentListener) {
            mListener = (BaseFragment.OnFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(initLyaout(), container, false);
        this.mContext = getActivity();
        unbinder = ButterKnife.bind(this, mRootView);
        initAllMembersView(savedInstanceState);
        initListener();
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    protected boolean isAttachedContext(){
        return getActivity() != null;
    }

    /**
     * 检查activity连接情况
     */
    public void checkActivityAttached() {
        if (getActivity() == null) {
            throw new BaseFragment.ActivityNotAttachedException();
        }
    }
    public static class ActivityNotAttachedException extends RuntimeException {
        public ActivityNotAttachedException() {
            super("Fragment has disconnected from Activity ! - -.");
        }
    }

    public abstract int initLyaout();
    public abstract void initListener();
    public abstract void setMode(int mode);
    public abstract void setRecipesSettingData(CookerSettingData data);
    protected abstract void initAllMembersView(Bundle savedInstanceState);

    public void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

    public int getLateralMenuBarOpen() {
        return lateralMenuBarOpen;
    }

    public void setLateralMenuBarOpen(int lateralMenuBarOpen) {
        this.lateralMenuBarOpen = lateralMenuBarOpen;
    }

    public interface OnFragmentListener {

    }
}
