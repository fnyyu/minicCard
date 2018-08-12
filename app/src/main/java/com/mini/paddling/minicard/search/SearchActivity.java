package com.mini.paddling.minicard.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.mini.paddling.minicard.R;
import com.mini.paddling.minicard.main.adapter.CardAdapter;
import com.mini.paddling.minicard.protocol.bean.BusinessBean;
import com.mini.paddling.minicard.protocol.bean.ResultBean;
import com.mini.paddling.minicard.protocol.net.NetRequest;
import com.mini.paddling.minicard.view.CommonEmptyView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends Activity  implements SearchEditText.SearchClickListener, NetRequest.OnRequestListener{

    @BindView(R.id.stv_search)
    SearchTopView stvSearch;
    @BindView(R.id.rlv_card)
    RecyclerView rlvCard;
    @BindView(R.id.cev_empty)
    CommonEmptyView cevEmpty;

    private CardAdapter cardAdapter;

    private String strKeyWord = "";

    private SearchTextWatcher textWatcher;

    private ShowInputBoardRunnable mShowInputBoardRunnable;

    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    private NetRequest netRequest;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initView();
        initSearch();
    }

    private void initSearch() {
      stvSearch.setCancelClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              finish();
          }
      });

        stvSearch.setEditOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stvSearch.getEtSearchEdit().setCursorVisible(true);
                hideInputMethod(false);
            }
        });

        stvSearch.setSearchEditListener(this);
        textWatcher = new SearchTextWatcher();
        stvSearch.setTextWatch(textWatcher);

        mShowInputBoardRunnable = new ShowInputBoardRunnable();

        stvSearch.setOnEditorListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_UNSPECIFIED){
                    String keyWord = stvSearch.getEtSearchEdit().getText().toString();
                    if (!TextUtils.isEmpty(keyWord)){
                        stvSearch.removeTextWatch();
                        stvSearch.getEtSearchEdit().setText(keyWord);
                        stvSearch.setClearIcon(!keyWord.isEmpty());
                        stvSearch.getEtSearchEdit().setSelection(keyWord.length());
                        stvSearch.setTextWatch(textWatcher);
                        searchClick();
                    }
                }
                return false;
            }
        });
    }

    private void initView() {

        stvSearch.setCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cardAdapter = new CardAdapter(this);
        rlvCard.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rlvCard.setAdapter(cardAdapter);

        cevEmpty.setTvEmptyString("很抱歉，未能找到相关内容");

        netRequest = new NetRequest(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideInputMethod(false);
    }

    @Override
    public void clearClick() {

    }

    @Override
    public void searchClick() {
        strKeyWord = stvSearch.getEditText();
        hideInputMethod(true);
        stvSearch.requestFocus();
        if (!TextUtils.isEmpty(strKeyWord)){
            netRequest.searchRequest(strKeyWord);
        }
    }

    @Override
    public void onLoadFinish(String operationType, ResultBean resultBean) {
        if (resultBean != null && resultBean instanceof BusinessBean){
            cardAdapter.setBusinessBean((BusinessBean) resultBean);
            cardAdapter.notifyDataSetChanged();
            cevEmpty.setVisibility(View.GONE);
        }else {
            cevEmpty.setVisibility(View.VISIBLE);
        }
    }

    private class SearchTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            stvSearch.setClearIcon(charSequence.length() > 0);
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (!TextUtils.isEmpty(editable.toString())) {

                strKeyWord = editable.toString();
            }
        }
    }

    private void hideInputMethod(boolean isHide) {
        if (stvSearch != null) {

            if (isHide) {
                stvSearch.getEtSearchEdit().setCursorVisible(false);
                stvSearch.getEtSearchEdit().clearFocus();
            } else {
                stvSearch.getEtSearchEdit().requestFocus();
                stvSearch.getEtSearchEdit().setCursorVisible(true);
            }
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            final View tempView = getCurrentFocus();
            if (tempView != null) {
                mUiHandler.removeCallbacks(mShowInputBoardRunnable);
                if (!isFinishing() && !isDestroyed()) {
                    if (isHide) {
                        imm.hideSoftInputFromWindow(tempView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    } else {
                        mUiHandler.postDelayed(mShowInputBoardRunnable, 200);
                    }
                }
            }
        }
    }


    final class ShowInputBoardRunnable implements Runnable {

        @Override
        public void run() {
            if (!isFinishing() && !isDestroyed()) {
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                final View tempView = getCurrentFocus();
                if (tempView != null){
                    imm.showSoftInput(tempView, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (stvSearch != null) {
            //解决系统EditText泄漏问题
            stvSearch.removeTextWatch();
        }

    }
}
