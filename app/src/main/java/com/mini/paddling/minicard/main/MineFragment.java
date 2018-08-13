package com.mini.paddling.minicard.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mini.paddling.minicard.R;
import com.mini.paddling.minicard.user.LoginUserManager;
import com.mini.paddling.minicard.user.StartUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineFragment extends Fragment {

    @BindView(R.id.iv_avatar)
    SimpleDraweeView ivAvatar;
    @BindView(R.id.tv_avatar)
    TextView tvAvatar;
    @BindView(R.id.mine_top)
    RelativeLayout mineTop;
    @BindView(R.id.rl_setting)
    TextView rlSetting;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.rl_logout)
    TextView rlLogout;

    private View viewRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (isAdded() && getActivity() != null) {
            if (viewRoot == null) {
                viewRoot = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me, container, false);
            }
        }
        ButterKnife.bind(this, viewRoot);

        initView();

        return viewRoot;

    }

    private void initView() {
        tvAvatar.setText(LoginUserManager.getInstance().getUserName());

        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUserManager.getInstance().logOut();
                getActivity().startActivity(new Intent(getActivity(), StartUpActivity.class));
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
