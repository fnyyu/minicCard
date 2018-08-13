package com.mini.paddling.minicard.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mini.paddling.minicard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartUpActivity extends Activity {

    @BindView(R.id.iv_card)
    ImageView ivCard;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        SpannableString word = new SpannableString(getResources().getString(R.string.register_text));
        word.setSpan(new UnderlineSpan(), 8, word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegister.setText(word);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(StartUpActivity.this, LoginActivity.class);
                loginIntent.putExtra("title", getResources().getString(R.string.login));
                startActivity(loginIntent);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(StartUpActivity.this, LoginActivity.class);
                registerIntent.putExtra("title", getResources().getString(R.string.register));
                startActivity(registerIntent);
            }
        });
    }
}
