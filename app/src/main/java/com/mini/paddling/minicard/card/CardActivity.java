package com.mini.paddling.minicard.card;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mini.paddling.minicard.R;
import com.mini.paddling.minicard.event.CardEvent;
import com.mini.paddling.minicard.event.CollectEvent;
import com.mini.paddling.minicard.protocol.bean.CardBean;
import com.mini.paddling.minicard.protocol.bean.ResultBean;
import com.mini.paddling.minicard.protocol.net.NetRequest;
import com.mini.paddling.minicard.util.CommonUtils;
import com.mini.paddling.minicard.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_ADD_COLLECT;
import static com.mini.paddling.minicard.protocol.net.NetRequest.REQUEST_RESULT_OK;

public class CardActivity extends Activity implements NetRequest.OnRequestListener{

    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.iv_picture)
    SimpleDraweeView ivPicture;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_special)
    TextView tvSpecial;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_gexing)
    TextView tvGexing;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.rl_card)
    RelativeLayout rlCard;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;

    private CardBean cardBean;

    private NetRequest netRequest;

    private static final int RES[] = new int[]{R.drawable.un_collect, R.drawable.collect};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);

        cardBean = getIntent().getParcelableExtra("bean");

        initTitleView();
        initView();

        netRequest = new NetRequest(this);
        EventBus.getDefault().register(this);
    }

    private void initTitleView() {
        tbvTitle.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tbvTitle.setTvTitleBar(cardBean.getCard_business_name());
    }

    private void initView() {
        ivPicture.setImageURI(cardBean.getCard_user_picture());
        tvTitle.setText(cardBean.getCard_business_name());
        tvAddress.setText(cardBean.getCard_user_address());
        tvGexing.setText(cardBean.getCard_user_slogan());
        tvPhone.setText(cardBean.getCard_user_tel());
        tvSpecial.setText(String.format(getResources().getString(R.string.special_text),
                cardBean.getCard_business_service(), cardBean.getCard_business_trade()));

        ivCollect.setImageResource(RES[Integer.parseInt(cardBean.getIs_collect())]);
    }

    @OnClick({R.id.tv_share, R.id.tv_edit, R.id.iv_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_share:
                Bitmap bitmap = CommonUtils.getViewBitmap(rlCard);
                String imageBase = CommonUtils.Bitmap2Base(bitmap);
                break;
            case R.id.tv_edit:

                Intent intent = new Intent(CardActivity.this, CardEditActivity.class);
                intent.putExtra("card", (Parcelable) cardBean);
                startActivity(intent);

                break;
            case R.id.iv_collect:

                if (cardBean.getIs_collect().equals("1")){
                    netRequest.addCollectRequest(cardBean);
                }else {
                    netRequest.delCollectRequest(cardBean);
                }
                break;
        }
    }


    @Override
    public void onLoadFinish(String operationType, ResultBean resultBean) {
        if (resultBean != null && resultBean.getRet_code().equals(REQUEST_RESULT_OK)){

            CollectEvent collectEvent = new CollectEvent();

            if (operationType.equals(LINK_ADD_COLLECT)){
                ivCollect.setImageResource(RES[1]);

                collectEvent.setCardId(cardBean.getCard_id());
                collectEvent.setUserId(cardBean.getUser_id());
                collectEvent.setType(1);

            }else {
                ivCollect.setImageResource(RES[0]);
                collectEvent.setCardId(cardBean.getCard_id());
                collectEvent.setUserId(cardBean.getUser_id());
                collectEvent.setType(0);
            }

            EventBus.getDefault().post(collectEvent);

        }else {
            Toast.makeText(CardActivity.this, "收藏相关操作失败请重试", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentEvent(CardEvent messageEvent) {
        if (messageEvent != null && messageEvent.getCardBean() != null && messageEvent.getType() == 1){
            this.cardBean = messageEvent.getCardBean();
            initView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
