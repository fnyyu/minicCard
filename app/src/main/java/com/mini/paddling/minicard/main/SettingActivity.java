package com.mini.paddling.minicard.main;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini.paddling.minicard.R;
import com.mini.paddling.minicard.util.DataCleanManager;
import com.mini.paddling.minicard.util.GetFileSize;
import com.mini.paddling.minicard.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends Activity {

    @BindView(R.id.tsv_title)
    TitleBarView tsvTitle;
    @BindView(R.id.ll_pas)
    LinearLayout llPas;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_about)
    LinearLayout llAbout;

    private String cacheSize = "";

    private static final String TAG = "SettingActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        tsvTitle.setTvTitleBar(getResources().getString(R.string.mine_setting));
        tsvTitle.setTvRightBarVisible(false);
        tsvTitle.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                versionName = "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        tvVersion.setText(versionName);

        try {
            cacheSize = GetFileSize.FormetFileSize(GetFileSize.getFileSize(getCacheDir()));
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
        tvCache.setText(cacheSize);
    }

    @OnClick(R.id.ll_clear)
    public void onViewClicked() {
        DataCleanManager.cleanInternalCache(SettingActivity.this);
        try {
            cacheSize = GetFileSize.FormetFileSize(GetFileSize.getFileSize(getCacheDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvCache.setText(cacheSize);
    }
}
