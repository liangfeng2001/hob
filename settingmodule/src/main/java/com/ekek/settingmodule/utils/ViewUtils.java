package com.ekek.settingmodule.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekek.settingmodule.R;

import java.util.ArrayList;
import java.util.List;

public class ViewUtils {

    public static final int STRING_RESOURCE_ID_NONE = -1;

    public static List<View> getAllChildrenViews(View view) {
        List<View> allChildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup)view;
            for (int i = 0; i < vp.getChildCount(); i++) {

                View child = vp.getChildAt(i);
                allChildren.add(child);
                allChildren.addAll(getAllChildrenViews(child));
            }
        }
        return allChildren;
    }

    public static void setText(TextView textView, String value) {
        textView.setText(value);
        textView.setTag(R.id.settingmodule_tag_string_resource, STRING_RESOURCE_ID_NONE);
    }
    public static void setText(TextView textView, int value) {
        textView.setText(value);
        textView.setTag(R.id.settingmodule_tag_string_resource, value);
    }
    public static void refreshText(TextView textView) {
        Object tag = textView.getTag(R.id.settingmodule_tag_string_resource);
        if (tag == null) return;
        int stringResource = (int)tag;
        if (stringResource == STRING_RESOURCE_ID_NONE) return;
        textView.setText(stringResource);
    }
}
