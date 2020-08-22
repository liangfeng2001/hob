package com.ekek.viewmodule.passwordview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.viewmodule.R;
import com.ekek.viewmodule.R2;
import com.ekek.viewmodule.listener.OnPasswordListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PinPasswordView extends LinearLayout {
    private static final int LENGTH_PIN_PASSWORD = 4;
    public static final int MODE_SET_PASSWORD = 0;
    public static final int MODE_CHECK_PASSWORD = 1;
    Unbinder unbinder;
    @BindView(R2.id.ib_pin_one)
    ImageButton ibPinOne;
    @BindView(R2.id.ib_pin_two)
    ImageButton ibPinTwo;
    @BindView(R2.id.ib_pin_three)
    ImageButton ibPinThree;
    @BindView(R2.id.ib_pin_four)
    ImageButton ibPinFour;
    @BindView(R2.id.ib_pin_five)
    ImageButton ibPinFive;
    @BindView(R2.id.ib_pin_six)
    ImageButton ibPinSix;
    @BindView(R2.id.ib_pin_seven)
    ImageButton ibPinSeven;
    @BindView(R2.id.ib_pin_eight)
    ImageButton ibPinEight;
    @BindView(R2.id.ib_pin_nine)
    ImageButton ibPinNine;
    @BindView(R2.id.ib_pin_delete)
    ImageButton ibPinDelete;
    @BindView(R2.id.ib_pin_zero)
    ImageButton ibPinZero;
    @BindView(R2.id.ib_pin_confirm)
    ImageButton ibPinConfirm;
    @BindView(R2.id.et_password)
    EditText etPassword;
    private OnPasswordListener mListener;
    private TranslateAnimation animation;
    private int mode = MODE_CHECK_PASSWORD;
    private String password = "";

    public PinPasswordView(Context context) {
        this(context, null);
    }

    public PinPasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View mRootView = LayoutInflater.from(context).inflate(R.layout.viewmodule_layout_pin_password_view, this);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
    }

    private void init() {
        animation = new TranslateAnimation(0, -10, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(80);
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                etPassword.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @OnClick({R2.id.ib_pin_one, R2.id.ib_pin_two, R2.id.ib_pin_three, R2.id.ib_pin_four, R2.id.ib_pin_five, R2.id.ib_pin_six, R2.id.ib_pin_seven, R2.id.ib_pin_eight, R2.id.ib_pin_nine, R2.id.ib_pin_delete, R2.id.ib_pin_zero, R2.id.ib_pin_confirm})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.ib_pin_one) {
            doEnterPwd("1");
        } else if (i == R.id.ib_pin_two) {
            doEnterPwd("2");
        } else if (i == R.id.ib_pin_three) {
            doEnterPwd("3");
        } else if (i == R.id.ib_pin_four) {
            doEnterPwd("4");
        } else if (i == R.id.ib_pin_five) {
            doEnterPwd("5");
        } else if (i == R.id.ib_pin_six) {
            doEnterPwd("6");
        } else if (i == R.id.ib_pin_seven) {
            doEnterPwd("7");
        } else if (i == R.id.ib_pin_eight) {
            doEnterPwd("8");
        } else if (i == R.id.ib_pin_nine) {
            doEnterPwd("9");
        } else if (i == R.id.ib_pin_delete) {
            doDeletePwd();
        } else if (i == R.id.ib_pin_zero) {
            doEnterPwd("0");
        } else if (i == R.id.ib_pin_confirm) {
            doConfirmPwd();
        }
    }

    public void clearEditText() {
        etPassword.setText("");
    }

    private void doConfirmPwd() {
        String pwdStr = etPassword.getText().toString();
        if (mListener != null) {
            if (pwdStr.length() == LENGTH_PIN_PASSWORD) {
                if (mode == MODE_CHECK_PASSWORD) {
                    boolean result = mListener.onCheckPwd(etPassword.getText().toString());
                    if (!result) warnForPwdError();
                    else {

                    }
                }else {
                    LogUtil.d("Enter::-------------1-----------------");
                    boolean result = mListener.onCheckPwd(etPassword.getText().toString());
                    if (!result) {
                        LogUtil.d("Enter::-------------2-----------------");
                        warnForPwdError();
                    }
                    else {
                        LogUtil.d("Enter::-------------3-----------------");
                        etPassword.setText("");
                    }
                    //password = etPassword.getText().toString();

                }



            }else {
                warnForPwdError();
                //mListener.onWarning(getResources().getString(R.string.viewmodule_warning_message_pwd_length_error));
            }


        }
    }

    private void warnForPwdError() {
        etPassword.startAnimation(animation);
        mListener.onWarning(getResources().getString(R.string.viewmodule_warning_message_pwd_error));

    }

    private void doDeletePwd() {
        String pwdStr = etPassword.getText().toString();
        if (pwdStr.length() > 0) {
            pwdStr = pwdStr.substring(0,pwdStr.length() - 1);
            etPassword.setText(pwdStr);
            etPassword.setTextColor(Color.WHITE);
        }


    }

    private void doEnterPwd(String inputStr) {
        String pwdStr = etPassword.getText().toString();
        if (pwdStr.length() < LENGTH_PIN_PASSWORD) {
            pwdStr = pwdStr + inputStr;
            etPassword.setText(pwdStr);
            etPassword.setTextColor(Color.WHITE);
        }else {

        }
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setOnPasswordListener(OnPasswordListener listener) {
        mListener = listener;
    }

}
