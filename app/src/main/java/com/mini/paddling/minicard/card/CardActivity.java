package com.mini.paddling.minicard.card;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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
import com.mini.paddling.minicard.util.FileUtils;
import com.mini.paddling.minicard.util.LogUtils;
import com.mini.paddling.minicard.view.TitleBarView;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_ADD_COLLECT;
import static com.mini.paddling.minicard.protocol.net.NetRequest.REQUEST_RESULT_OK;

public class CardActivity extends Activity implements NetRequest.OnRequestListener{

    public static final int REQUEST_WRITE_STORAGE_PERMISSION = 1;
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

    private static final String APP_ID = "101496222";

    private static Tencent qqApi;

    private static final int RES[] = new int[]{R.drawable.un_collect, R.drawable.collect};

    public static final String TAG = "CardActivity";

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

        qqApi = Tencent.createInstance(APP_ID, this);
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
                String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                int permissionStatus = ActivityCompat.checkSelfPermission(this, permission);

                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    shareViewBitmapToQQ();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_WRITE_STORAGE_PERMISSION);
                }
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

    /**
     * bitmap缓存到本地，分享完后再删掉
     */
    private void shareViewBitmapToQQ() {
        try {
            Bitmap bitmap = CommonUtils.getViewBitmap(rlCard);
//            String imageBase = CommonUtils.Bitmap2Base(bitmap);
            String path = FileUtils.sharePicturesPath;
            String filename = FileUtils.generateFilenameByTimeStamp(FileUtils.JPG);

            FileUtils.saveBitmap(bitmap, path, filename);
            shareToQQ(path, filename);

            //分享是异步，直接删除会导致分享图片失效。改成应用启动时统一删除
//            FileUtils.deleteFile(path, filename);
        } catch (IOException e) {
            LogUtils.i(TAG, e);
        }
    }

    /**
     * 分享本地文件到qq
     * @param path
     * @param filename
     */
    private void shareToQQ(String path, String filename) {
        String file = new File(path, filename).getAbsolutePath();

        Bundle params = new Bundle();
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, file);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "mini");
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        qqApi.shareToQQ(CardActivity.this, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Log.i(TAG, "QQ分享成功: " + o.toString());
            }

            @Override
            public void onError(UiError uiError) {
                Log.i(TAG, String.format("QQ分享错误: code = %d, detail = %s, message = %s\n",
                        uiError.errorCode, uiError.errorDetail, uiError.errorMessage));
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "QQ分享取消");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    shareViewBitmapToQQ();
                }
                break;
        }
    }
}
