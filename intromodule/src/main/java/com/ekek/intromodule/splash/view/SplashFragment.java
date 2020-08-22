package com.ekek.intromodule.splash.view;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.VideoView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.intromodule.R;
import com.ekek.intromodule.R2;
import com.ekek.intromodule.splash.contract.SplashContract;
import com.ekek.intromodule.splash.presenter.SplashPresenter;
import com.ekek.settingmodule.utils.SoundUtil;
import com.ekek.viewmodule.product.ProductManager;

import butterknife.BindView;

public class SplashFragment extends BaseFragment implements SplashContract.View {
    public static final int SPLASH_MODE_INTRO = 0;
    public static final int SPLASH_MODE_ENDING_IDLE = 1;
    public static final int SPLASH_MODE_ENDING_SERIAL = 2;
    public static final int SPLASH_MODE_ENDING_PANNEL_HIGH_TEMP = 3;
    private static final int HANDLER_MEDIA_PLAY_FINISH = 0;
    private static final int DELELAY_TIME_MILLIS = 0;
    private int mode = SPLASH_MODE_INTRO;

    private int originalSystemVolume;

    @BindView(R2.id.vv_media)
    VideoView vvMedia;
    private SplashContract.Presenter mPresenter;
    public SplashFragment() {
        mPresenter = new SplashPresenter(this);
    }

    @SuppressLint("ValidFragment")
    public SplashFragment(int mode) {
        this.mode = mode;
        mPresenter = new SplashPresenter(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_MEDIA_PLAY_FINISH:
                    ((OnSplashFragmentListener)mListener).onSplashFragmentFinish(mode);
                    break;
            }
        }
    };

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public int initLyaout() {
        return R.layout.intromodule_fragment_splash;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        vvMedia.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Logger.getInstance().i("startSplash onCompletion");
                SoundUtil.setSystemVolume(getActivity(), originalSystemVolume);
                handler.sendEmptyMessageDelayed(HANDLER_MEDIA_PLAY_FINISH,DELELAY_TIME_MILLIS);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
       // mPresenter.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.start();
        } else {
            GlobalVars.getInstance().setPlaySplashVideo(false);
        }
    }

    @Override
    public void startSplash() {
        Uri uri;
        LogUtil.d("Enter:: startSplash");
        LogUtil.d("getActivity().getPackageName()---------->" + getActivity().getPackageName());
        //com.ekek.intromodule
        if (mode == SPLASH_MODE_INTRO) {
            if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
                uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.intro_haier);
            }else {
                uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.intro);
            }
           // uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.cata);
        }else {

            if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
                uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.ending_haier);
            }else {
                uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.ending);
            }
            //uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.cata);

            //uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.intro);
        }

        vvMedia.setVideoURI(uri);
        originalSystemVolume = SoundUtil.getSystemCurrentVolume(getActivity());
        // 客户嫌音量过大，所以，播放视频时，用 70% 的音量进行播放
        int newVolume = (int)Math.round(originalSystemVolume * 0.7);
        SoundUtil.setSystemVolume(getActivity(), newVolume);
        Logger.getInstance().i("startSplash");
        vvMedia.start();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {

    }

    public interface OnSplashFragmentListener extends OnFragmentListener {
        void onSplashFragmentFinish(int mode);

    }

}
