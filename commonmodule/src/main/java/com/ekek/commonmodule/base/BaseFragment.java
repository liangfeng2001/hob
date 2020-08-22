package com.ekek.commonmodule.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import solid.ren.skinlibrary.base.SkinBaseFragment;


public abstract class BaseFragment extends SkinBaseFragment {

    public static final int LATERAL_MENU_BAR_NOT_DEFINED = -1;
    public static final int LATERAL_MENU_BAR_CLOSE = 0;
    public static final int LATERAL_MENU_BAR_OPEN = 1;

    protected View mRootView;
    protected Context mContext;
    protected OnFragmentListener mListener;
    Unbinder unbinder;
    private int lateralMenuBarOpen = LATERAL_MENU_BAR_NOT_DEFINED;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mListener = (OnFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    protected boolean isAttachedContext(){
        return getActivity() != null;
    }
    /**
     * 检查activity连接情况
     */
    public void checkActivityAttached() {
        if (getActivity() == null) {
            throw new ActivityNotAttachedException();
        }
    }
    public static class ActivityNotAttachedException extends RuntimeException {
        public ActivityNotAttachedException() {
            super("Fragment has disconnected from Activity ! - -.");
        }
    }

    public abstract int initLyaout();
    public abstract void initListener();
    protected abstract void initAllMembersView(Bundle savedInstanceState);

    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public int getLateralMenuBarOpen() {
        return lateralMenuBarOpen;
    }

    public void setLateralMenuBarOpen(int lateralMenuBarOpen) {
        this.lateralMenuBarOpen = lateralMenuBarOpen;
    }

    protected void unbindDrawables(View view)
    {
        if (view == null) {
            return;
        }

        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    public interface OnFragmentListener {

    }
}
