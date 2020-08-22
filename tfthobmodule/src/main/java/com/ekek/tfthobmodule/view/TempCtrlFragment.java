package com.ekek.tfthobmodule.view;

import android.os.Bundle;

import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.tfthobmodule.R;

public class TempCtrlFragment extends BaseFragment {
    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_temp_ctrl;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

    }

    public interface OnTempCtrlFragmentListener extends OnFragmentListener {
        void onTempCtrlFragmentFinish();

    }
}
