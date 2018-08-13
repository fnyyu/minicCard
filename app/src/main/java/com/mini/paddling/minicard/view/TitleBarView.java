package com.mini.paddling.minicard.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mini.paddling.minicard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitleBarView extends RelativeLayout {


    @BindView(R.id.iv_back_bar)
    ImageView ivBackBar;
    @BindView(R.id.tv_title_bar)
    TextView tvTitleBar;
    @BindView(R.id.tv_right_bar)
    TextView tvRightBar;

    public TitleBarView(Context context) {
        super(context);
        initView(context);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_common_title, this);
        ButterKnife.bind(this);
    }

    public void setBackListener(OnClickListener listener) {
        if (listener != null) {
            ivBackBar.setOnClickListener(listener);
        }
    }

    public void setTvTitleBar(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitleBar.setText(title);
        }
    }

    public void setTvRightBar(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvRightBar.setText(title);
        }
    }

    public void setTvRightBarVisible(boolean isVisible){
        tvRightBar.setVisibility(isVisible? VISIBLE : GONE);
    }

    public void setRightBarClick(OnClickListener listener){
        tvRightBar.setOnClickListener(listener);
    }

}
