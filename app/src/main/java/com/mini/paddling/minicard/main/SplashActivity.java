package com.mini.paddling.minicard.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.mini.paddling.minicard.user.LoginUserManager;
import com.mini.paddling.minicard.user.StartUpActivity;
import com.mini.paddling.minicard.util.FileUtils;

import java.io.File;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FileUtils.sharePicturesPath = new File(getApplicationContext().getExternalCacheDir(), "/SharePictures/").getAbsolutePath();
        FileUtils.clearDir(FileUtils.sharePicturesPath);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intentHome();
    }

    private void intentHome() {
        Intent intent;
        if (LoginUserManager.getInstance().isLogin()){
             intent = new Intent(SplashActivity.this, MainActivity.class);
        }else {
            intent = new Intent(SplashActivity.this, StartUpActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
