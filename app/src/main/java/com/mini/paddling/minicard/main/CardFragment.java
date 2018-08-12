package com.mini.paddling.minicard.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mini.paddling.minicard.R;
import com.mini.paddling.minicard.card.CardAddActivity;
import com.mini.paddling.minicard.main.adapter.CardAdapter;
import com.mini.paddling.minicard.protocol.bean.BusinessBean;
import com.mini.paddling.minicard.protocol.bean.ResultBean;
import com.mini.paddling.minicard.protocol.net.NetRequest;
import com.mini.paddling.minicard.search.HomeSearchView;
import com.mini.paddling.minicard.search.SearchActivity;
import com.mini.paddling.minicard.view.CommonEmptyView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mini.paddling.minicard.protocol.net.NetRequest.REQUEST_RESULT_OK;

public class CardFragment extends Fragment implements NetRequest.OnRequestListener {

    @BindView(R.id.hsv_search)
    HomeSearchView hsvSearch;
    @BindView(R.id.rlv_card)
    RecyclerView rlvCard;
    @BindView(R.id.cev_empty)
    CommonEmptyView cevEmpty;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    private View viewRoot;

    private CardAdapter cardAdapter;

    private NetRequest request;

    private boolean isCardPage;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (isAdded() && getActivity() != null) {
            if (viewRoot == null) {
                viewRoot = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_card, container, false);
            }
        }
        ButterKnife.bind(this, viewRoot);

        if (isAdded()) {
            isCardPage = getArguments().getString("portId").equals("mine");
            userId = getArguments().getString("userId");
        }

        initView();

        return viewRoot;

    }

    private void initView() {

        hsvSearch.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        cardAdapter = new CardAdapter(getActivity());
        rlvCard.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rlvCard.setAdapter(cardAdapter);

        request = new NetRequest(this);

        if (isCardPage) {
            fabAdd.setVisibility(View.VISIBLE);
            cevEmpty.setTvEmptyString(getResources().getString(R.string.add_card));
            request.businessGetRequest(getActivity().getIntent().getStringExtra("userId"));
        } else {
            fabAdd.setVisibility(View.INVISIBLE);
            cevEmpty.setTvEmptyString(getResources().getString(R.string.add_collect));
            request.collectGetRequest(getActivity().getIntent().getStringExtra("userId"));
        }

        cardAdapter.setOnItemLongClickListener(new CardAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                showPopMenu(view, position);
            }
        });

    }

    @Override
    public void onLoadFinish(String operationType, ResultBean resultBean) {
        if (resultBean != null && resultBean instanceof BusinessBean) {
            BusinessBean businessBean = (BusinessBean) resultBean;

            if (businessBean.getRet_code().equals(REQUEST_RESULT_OK)) {
                cevEmpty.setVisibility(View.GONE);
                cardAdapter.setBusinessBean(businessBean);
            } else {
                cevEmpty.setVisibility(View.VISIBLE);
            }
        }else {
            cevEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void showPopMenu(View view,final int pos){
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                cardAdapter.removeItem(pos);
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();
    }


    @OnClick(R.id.fab_add)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), CardAddActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
}
