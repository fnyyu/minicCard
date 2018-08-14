package com.mini.paddling.minicard.card;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mini.paddling.minicard.R;
import com.mini.paddling.minicard.event.CardEvent;
import com.mini.paddling.minicard.protocol.bean.CardBean;
import com.mini.paddling.minicard.protocol.bean.ResultBean;
import com.mini.paddling.minicard.protocol.net.NetRequest;
import com.mini.paddling.minicard.user.LoginUserManager;
import com.mini.paddling.minicard.util.CommonUtils;
import com.mini.paddling.minicard.util.FileUtils;
import com.mini.paddling.minicard.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CardAddOrEditActivity extends Activity implements NetRequest.OnRequestListener {

    private static final Uri URI_IMAGE = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final Uri URI_VIDEO = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private static final String TYPE_IMAGE = "image/*";
    private static final String TYPE_VIDEO = "video/*";

    private static final int REQUEST_IMAGE_FROM_NATIVE = 0;
    private static final int REQUEST_IMAGE_FROM_CAMERA = 1;
    public static final int REQUEST_CROP_IMAGE = 2;
    private static final int REQUEST_VIDEO_FROM_NATIVE = 3;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 4;

    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_manager)
    EditText etManager;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_gexing)
    EditText etGexing;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.iv_src)
    SimpleDraweeView ivSrc;
    @BindView(R.id.iv_video_picture)
    ImageView ivVideo;
    @BindView(R.id.iv_video_src)
    SimpleDraweeView ivVideoSrc;
    @BindView(R.id.et_special)
    EditText etSpecial;

    private NetRequest netRequest;

    private CardBean cardBean;

    private int failedMessageResId;
    private Runnable requestMethod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);

        initTitleView();

        ivSrc.setVisibility(View.INVISIBLE);
        ivPicture.setVisibility(View.VISIBLE);
        ivVideoSrc.setVisibility(View.INVISIBLE);
        ivVideo.setVisibility(View.VISIBLE);

        cardBean = (CardBean) getIntent().getSerializableExtra("card");

        if (cardBean != null) {
            initView();
            failedMessageResId = R.string.edit_failed;
            requestMethod = new Runnable() {
                @Override
                public void run() {
                    netRequest.editCardRequest(cardBean);
                }
            };
        } else {
            cardBean = new CardBean();
            cardBean.setUser_id(LoginUserManager.getInstance().getUserUid());
            failedMessageResId = R.string.add_failed;
            requestMethod = new Runnable() {
                @Override
                public void run() {
                    netRequest.addCardRequest(cardBean);
                }
            };
        }

        netRequest = new NetRequest(this);

        EventBus.getDefault().register(this);
    }

    private void initView() {
        etTitle.setText(cardBean.getCard_business_name());
        etGexing.setText(cardBean.getCard_user_slogan());
        etAddress.setText(cardBean.getCard_user_address());
        etPhone.setText(cardBean.getCard_user_tel());
        etManager.setText(cardBean.getCard_business_service());
        etSpecial.setText(cardBean.getCard_business_trade());

        if (!TextUtils.isEmpty(cardBean.getCard_user_picture())) {
            ivSrc.setImageURI(cardBean.getCard_user_picture());
            ivPicture.setVisibility(View.INVISIBLE);
            ivSrc.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(cardBean.getCard_user_video())) {
//            ivVideo.setImageURI(cardBean.getCard_user_video());
//            ivVideo.setVisibility(View.INVISIBLE);
//            ivVideoSrc.setVisibility(View.VISIBLE);
        }
    }

    private void initTitleView() {

        tbvTitle.setTvRightBar(getResources().getString(R.string.cancel));
        tbvTitle.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tbvTitle.setTvTitleBar(getResources().getString(R.string.edit));
        tbvTitle.setRightBarClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onLoadFinish(String operationType, final ResultBean resultBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (resultBean != null) {

                    Toast.makeText(CardAddOrEditActivity.this, resultBean.getRet_message(), Toast.LENGTH_SHORT).show();
                    CardEvent cardEvent = new CardEvent();
                    cardEvent.setType(1);
                    cardEvent.setCardBean(cardBean);
                    EventBus.getDefault().post(cardEvent);
                    finish();

                } else {
                    Toast.makeText(CardAddOrEditActivity.this, failedMessageResId, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @OnClick({R.id.iv_picture, R.id.tv_commit, R.id.iv_src, R.id.iv_video_picture, R.id.iv_video_src})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_src:
            case R.id.iv_picture:
                showSelectImageDialog();
                break;
            case R.id.iv_video_src:
            case R.id.iv_video_picture:
                String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                int permissionStatus = ActivityCompat.checkSelfPermission(this, permission);

                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    showSelectVideoDialog();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_WRITE_STORAGE_PERMISSION);
                }
                break;
            case R.id.tv_commit:

                if (TextUtils.isEmpty(etTitle.getText())) {
                    Toast.makeText(CardAddOrEditActivity.this, R.string.name_required, Toast.LENGTH_SHORT).show();
                    return;
                }
                cardBean.setCard_business_name(etTitle.getText().toString());
                cardBean.setCard_business_service(etManager.getText().toString());
                cardBean.setCard_user_tel(etPhone.getText().toString());
                cardBean.setCard_user_address(etAddress.getText().toString());
                cardBean.setCard_user_slogan(etGexing.getText().toString());
                cardBean.setCard_business_trade(etSpecial.getText().toString());

                requestMethod.run();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //相册获取结果
            case REQUEST_IMAGE_FROM_NATIVE:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());
                }
                break;
            //相机拍照结果
            case REQUEST_IMAGE_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/businessBitmap.jpg");
                    cropPhoto(Uri.fromFile(temp));
                }
                break;
            //裁剪图片后
            case REQUEST_CROP_IMAGE:
                if (resultCode == RESULT_OK && data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap businessBitmap = extras.getParcelable("data");
                    if (businessBitmap != null) {
                        cardBean.setCard_user_picture("data:image/jpeg;base64," + CommonUtils.Bitmap2Base(businessBitmap));
                        try {
                            ivSrc.setVisibility(View.INVISIBLE);
                            ivPicture.setVisibility(View.VISIBLE);
                            ivPicture.setImageBitmap(businessBitmap);
                        } catch (Exception e) {

                        }
                    }
                }
                break;
            case REQUEST_VIDEO_FROM_NATIVE:
                if (resultCode == RESULT_OK) {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        Uri uri = Uri.parse(dataString);
                        File file = new File(FileUtils.getRealFilePath(this, uri));
                        byte[] bytes = FileUtils.getBytesFromFile(this, file);
                        String base = CommonUtils.byteArray2Base(bytes);

                        String fileFormat = file.getName().substring(file.getName().indexOf('.') + 1);

                        cardBean.setCard_user_video("data:video/" + fileFormat + ";base64," + base);
//                    vvPicture.setImageURI(Uri.parse(dataString));
                        Log.v("", "");
                    }
                }
                break;
        }
    }

    private void showSelectImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CardAddOrEditActivity.this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(CardAddOrEditActivity.this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(URI_IMAGE, TYPE_IMAGE);
                startActivityForResult(intent1, REQUEST_IMAGE_FROM_NATIVE);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "businessBitmap.jpg")));
                startActivityForResult(intent2, REQUEST_IMAGE_FROM_CAMERA);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void showSelectVideoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CardAddOrEditActivity.this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(CardAddOrEditActivity.this, R.layout.dialog_select_video, null);
        TextView tv_select_gallery = view.findViewById(R.id.tv_select_gallery);
//        TextView tv_select_camera = view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在视频库中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(URI_VIDEO, TYPE_VIDEO);
                startActivityForResult(intent1, REQUEST_VIDEO_FROM_NATIVE);
                dialog.dismiss();
            }
        });
//        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
//            @Override
//            public void onClick(View v) {
//                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "businessBitmap.jpg")));
//                startActivityForResult(intent2, 1);// 采用ForResult打开
//                dialog.dismiss();
//            }
//        });
        dialog.setView(view);
        dialog.show();
    }

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //找到指定URI对应的资源图片
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 3);
        intent.putExtra("aspectY", 2);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 330);
        intent.putExtra("outputY", 220);
        intent.putExtra("return-data", true);
        //进入系统裁剪图片的界面
        startActivityForResult(intent, REQUEST_CROP_IMAGE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCardEvent(CardEvent messageEvent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showSelectVideoDialog();
                }
                break;
        }
    }
}
