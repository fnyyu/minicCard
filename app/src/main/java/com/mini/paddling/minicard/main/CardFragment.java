package com.mini.paddling.minicard.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mini.paddling.minicard.R;
import com.mini.paddling.minicard.card.CardAddOrEditActivity;
import com.mini.paddling.minicard.event.CardEvent;
import com.mini.paddling.minicard.event.CollectEvent;
import com.mini.paddling.minicard.main.adapter.CardAdapter;
import com.mini.paddling.minicard.protocol.bean.BusinessBean;
import com.mini.paddling.minicard.protocol.bean.ResultBean;
import com.mini.paddling.minicard.protocol.net.NetRequest;
import com.mini.paddling.minicard.search.HomeSearchView;
import com.mini.paddling.minicard.search.SearchActivity;
import com.mini.paddling.minicard.user.LoginUserManager;
import com.mini.paddling.minicard.util.LogUtils;
import com.mini.paddling.minicard.view.CommonEmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.CountDownLatch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mini.paddling.minicard.protocol.net.NetRequest.REQUEST_RESULT_OK;
import static com.mini.paddling.minicard.util.CommonUtils.dip2px;
import static com.mini.paddling.minicard.util.CommonUtils.getScreenWidth;

public class CardFragment extends Fragment implements NetRequest.OnRequestListener {

    @BindView(R.id.hsv_search)
    HomeSearchView hsvSearch;
    @BindView(R.id.rlv_card)
    RecyclerView rlvCard;
    @BindView(R.id.cev_empty)
    CommonEmptyView cevEmpty;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    private View viewRoot;

    private CardAdapter cardAdapter;

    private NetRequest request;

    private boolean isCardPage;
    private String userId;

    private BusinessBean businessBean;

    private int popupWindowX;

    public static final String TAG = "CardFragment";

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
            userId = LoginUserManager.getInstance().getUserUid();
        }

        EventBus.getDefault().register(this);

        popupWindowX = Math.abs((getScreenWidth(getActivity()) - dip2px(getActivity(), 100)) / 2);

        initView();

        return viewRoot;

    }

    private void initView() {

        hsvSearch.setOnSearchIntentListener(new View.OnClickListener() {
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
            request.businessGetRequest(LoginUserManager.getInstance().getUserUid());
        } else {
            fabAdd.setVisibility(View.INVISIBLE);
            cevEmpty.setTvEmptyString(getResources().getString(R.string.add_collect));
            request.collectGetRequest(LoginUserManager.getInstance().getUserUid());
        }

        cardAdapter.setOnItemLongClickListener(new CardAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                showPopupWindow(view, position);
            }
        });

        srlRefresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        srlRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srlRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isCardPage) {
                    request.businessGetRequest(LoginUserManager.getInstance().getUserUid());
                } else {
                    request.collectGetRequest(LoginUserManager.getInstance().getUserUid());
                }
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LogUtils.i(TAG, e);
        }
    }

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public void onLoadFinish(String operationType, ResultBean resultBean) {
        if (resultBean != null && resultBean instanceof BusinessBean) {

            srlRefresh.post(new Runnable() {
                @Override
                public void run() {
                    if (srlRefresh.isRefreshing()){
                        srlRefresh.setRefreshing(false);
                    }
                }
            });


            businessBean = (BusinessBean) resultBean;
            rlvCard.post(new Runnable() {
                @Override
                public void run() {
                    cardAdapter.setBusinessBean(businessBean);
                }
            });

            cevEmpty.post(new Runnable() {
                @Override
                public void run() {
                    if (businessBean.getRet_code().equals(REQUEST_RESULT_OK)) {
                        cevEmpty.setVisibility(View.GONE);

                    } else {
                        cevEmpty.setVisibility(View.VISIBLE);
                    }
                }
            });


        } else {
            cevEmpty.post(new Runnable() {
                @Override
                public void run() {
                    cevEmpty.setVisibility(View.VISIBLE);
                }
            });

        }
        countDownLatch.countDown();
    }

    private void showPopupWindow(View view, final int pos) {
        //设置contentView
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_delete, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentView);
        TextView tvDelete = contentView.findViewById(R.id.tv_delete);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardAdapter.removeItem(pos);
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }

            }
        });
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAsDropDown(view, popupWindowX, 0);
        popupWindow.update();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCardEvent(CardEvent messageEvent) {
        if (isCardPage && messageEvent != null && messageEvent.getCardBean() != null) {
            request.businessGetRequest(LoginUserManager.getInstance().getUserUid());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectEvent(CollectEvent messageEvent) {
        if (!isCardPage && messageEvent != null) {
            request.collectGetRequest(LoginUserManager.getInstance().getUserUid());
        }
    }


    @OnClick(R.id.fab_add)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), CardAddOrEditActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
