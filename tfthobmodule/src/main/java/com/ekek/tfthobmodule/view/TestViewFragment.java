package com.ekek.tfthobmodule.view;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.utils.GenericToast;
import com.ekek.viewmodule.listener.OnPasswordListener;
import com.ekek.viewmodule.listener.PatternLockViewListener;
import com.ekek.viewmodule.passwordview.PatternLockView;
import com.ekek.viewmodule.passwordview.PinPasswordView;
import com.ekek.viewmodule.passwordview.UnlockView;
import com.ekek.viewmodule.utils.PatternLockUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TestViewFragment extends BaseFragment implements OnPasswordListener {
    @BindView(R.id.ppv)
    PinPasswordView ppv;
    @BindView(R.id.ib_down_info)
    ImageButton ibDownInfo;
    @BindView(R.id.ulv)
    UnlockView ulv;
    @BindView(R.id.pattern_lock_view)
    PatternLockView patternLockView;
    private Toast toast;
    private TextView textView;


    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_test_view;
    }

    @Override
    public void initListener() {
        ppv.setOnPasswordListener(this);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();//调用Activity的getLayoutInflater()
        View view = inflater.inflate(R.layout.tfthobmodule_layout_up_tips_toast, null); //加載layout下的布局
        textView = view.findViewById(R.id.tv_toast);

        toast = new Toast(getActivity());
        int[] position = new int[2];
        ibDownInfo.getLocationInWindow(position);
        position[0] = position[0] - ibDownInfo.getMeasuredWidth() / 2;
        position[1] = position[1] - ibDownInfo.getMeasuredHeight();
        System.out.println("getLocationOnScreen:" + position[0] + "," + position[1]);
        LogUtil.d("ibDownInfo.getWidth() / 2---->" + ibDownInfo.getMeasuredWidth() / 2);
        LogUtil.d("ibDownInfo.getHeight()----->" + ibDownInfo.getMeasuredHeight());
        toast.setGravity(Gravity.TOP | Gravity.RIGHT, ibDownInfo.getWidth() / 2, ibDownInfo.getHeight());//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
        toast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件

        ulv.setMode(UnlockView.CREATE_MODE);


        init();

    }

    private void init() {
        patternLockView.setAspectRatioEnabled(true);
        patternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_WIDTH_BIAS);
        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
        patternLockView.setDotAnimationDuration(150);
        patternLockView.setPathEndAnimationDuration(100);
        patternLockView.setInStealthMode(false);
        patternLockView.setTactileFeedbackEnabled(true);
        patternLockView.setWrongStateColor(Color.RED);
        patternLockView.setInputEnabled(true);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
                Log.d(getClass().getName(), "Pattern drawing started");
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                Log.d(getClass().getName(), "Pattern progress: " + PatternLockUtils.patternToString(patternLockView, progressPattern));
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                Log.d(getClass().getName(), "Pattern complete: " + PatternLockUtils.patternToString(patternLockView, pattern)
                        );

                if (!PatternLockUtils.patternToString(patternLockView, pattern).equals("048")) {
                    LogUtil.d("Enter:: pattern is wrong-------->" + PatternLockUtils.patternToString(patternLockView, pattern));
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);

                }
            }

            @Override
            public void onCleared() {
                Log.d(getClass().getName(), "Pattern has been cleared");
            }
        });
    }


    @Override
    public boolean onCheckPwd(String pwd) {
        return false;
    }

    @Override
    public void onWarning(String msg) {
        LogUtil.d("Enter:: onWarning--->" + msg);
        showToast(msg);
    }

    private void showToast(String msg) {
        //toast.cancel();
        int[] position = new int[2];
        ibDownInfo.getLocationOnScreen(position);
        System.out.println("getLocationOnScreen:" + position[0] + "," + position[1]);
        ibDownInfo.getLocationInWindow(position);
        System.out.println("getLocationInWindow:" + position[0] + "," + position[1]);
        LogUtil.d("ibDownInfo.getWidth() / 2---->" + ibDownInfo.getMeasuredWidth() / 2);
        LogUtil.d("ibDownInfo.getHeight()----->" + ibDownInfo.getMeasuredHeight());
        int x = 1280 - position[0] - ibDownInfo.getMeasuredWidth() / 2 - 50;
        toast.setGravity(Gravity.TOP | Gravity.RIGHT, x, ibDownInfo.getMeasuredHeight());
        textView.setText(msg);
        toast.show();
    }


    @OnClick(R.id.ib_down_info)
    public void onViewClicked() {
        test();
    }

    private void test() {
       /* final Toast toast =  Toast.makeText(getActivity(), "土司内容", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();*/
        //延长土司时间
  /*      new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("Enter:: toast cancel");
                toast.cancel();
            }
        },1000 * 60);*/

        GenericToast mGToast = GenericToast.makeText(getActivity(), "I am generic toast", 10 * 1000);
        mGToast.show();

    }

}
