package com.mini.paddling.minicard.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mini.paddling.minicard.R;
import com.mini.paddling.minicard.main.adapter.CardAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineFragment extends Fragment {

    @BindView(R.id.tv_user)
    TextView tvUser;

    private View viewRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (isAdded() && getActivity() != null) {
            if (viewRoot == null) {
                viewRoot = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine, container, false);
            }
        }
        ButterKnife.bind(this, viewRoot);

        initView();

        return viewRoot;

    }

    private void initView() {

    }

}
