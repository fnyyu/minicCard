package com.mini.paddling.minicard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mini.paddling.minicard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonEmptyView extends RelativeLayout {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    public CommonEmptyView(Context context) {
        super(context);
        initView(context);
    }

    public CommonEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_common_empty, this);
        ButterKnife.bind(this);

    }

    public void setTvEmptyString(String tvEmptyString){
        tvEmpty.setText(tvEmptyString);
    }
}
