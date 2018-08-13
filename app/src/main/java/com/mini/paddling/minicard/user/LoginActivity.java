package com.mini.paddling.minicard.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.paddling.minicard.R;
import com.mini.paddling.minicard.main.MainActivity;
import com.mini.paddling.minicard.protocol.bean.LoginBean;
import com.mini.paddling.minicard.protocol.bean.ResultBean;
import com.mini.paddling.minicard.protocol.bean.UserBean;
import com.mini.paddling.minicard.protocol.net.NetRequest;
import com.mini.paddling.minicard.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_LOGIN;
import static com.mini.paddling.minicard.protocol.net.NetRequest.REQUEST_RESULT_OK;

public class LoginActivity extends Activity implements NetRequest.OnRequestListener{

    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.et_conform)
    EditText etConform;
    @BindView(R.id.text_input_name)
    TextInputLayout textInputName;
    @BindView(R.id.text_input_password)
    TextInputLayout textInputPassword;
    @BindView(R.id.text_input_confirm)
    TextInputLayout textInputConfirm;

    private String typeName = "";

    private NetRequest netRequest;

    private boolean isLogin;

    private UserBean userBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        initView();
        initEditView();
    }

    private void initView() {
        tbvTitle.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tbvTitle.setTvRightBarVisible(false);

        typeName = getIntent().getStringExtra("title");

        tbvTitle.setTvTitleBar(typeName);
        tbvTitle.setTvRightBar(getResources().getString(R.string.cancel));

        isLogin = typeName.equals(getResources().getString(R.string.login));
        tvEdit.setText(typeName);
        textInputConfirm.setVisibility(isLogin ? View.INVISIBLE : View.VISIBLE);

        netRequest = new NetRequest(this);
        userBean = new UserBean();
    }

    private void initEditView(){
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence content, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence content, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable content) {
                if (content == null || content.length() < 5 || content.length() > 18) {
                    textInputPassword.setError("密码格式错误，需5～18位");
                    textInputPassword.setErrorEnabled(true);

                } else {
                    //不满足条件需要设置为false
                    textInputPassword.setErrorEnabled(false);
                }
            }

        });

        etConform.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence content, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence content, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable content) {
                if (content == null || !content.toString().equals(getPassword())) {
                    textInputConfirm.setError("密码确认不符合，请重新输入");
                    textInputConfirm.setErrorEnabled(true);

                } else {
                    //不满足条件需要设置为false
                    textInputConfirm.setErrorEnabled(false);
                }
            }

        });
    }

    private String getName() {
        return TextUtils.isEmpty(etName.getText()) ? "" : etName.getText().toString();
    }

    private String getPassword() {
        return TextUtils.isEmpty(etPassword.getText()) ? "" : etPassword.getText().toString();
    }

    private String getConfirmPassword() {
        return TextUtils.isEmpty(etConform.getText()) ? "" : etConform.getText().toString();
    }

    @OnClick(R.id.tv_edit)
    public void onViewClicked() {

        userBean.setAname(getName());
        userBean.setApwd(getPassword());

        if (isLogin){
            if (TextUtils.isEmpty(getName()) || TextUtils.isEmpty(getPassword())){
                Toast.makeText(LoginActivity.this, "请输入完整的用户名和密码", Toast.LENGTH_SHORT).show();
            }else {
                netRequest.loginRequest(userBean);
            }
        }else {
            if (TextUtils.isEmpty(getName()) || TextUtils.isEmpty(getPassword()) || TextUtils.isEmpty(getConfirmPassword())){
                Toast.makeText(LoginActivity.this, "请输入完整的用户名和密码", Toast.LENGTH_SHORT).show();

            }else {
                netRequest.registerRequest(userBean);
            }
        }
    }

    private void startHome(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoadFinish(final String operationType, final ResultBean resultBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (resultBean != null && resultBean instanceof LoginBean){
                    LoginBean loginBean = (LoginBean) resultBean;
                    Toast.makeText(LoginActivity.this, loginBean.getRet_message(), Toast.LENGTH_SHORT).show();

                    if (loginBean.getRet_code().equals(REQUEST_RESULT_OK)){

                        if (operationType.equals(LINK_LOGIN) && loginBean.getData()!= null){
                            userBean.setUserId(String.valueOf(loginBean.getData().getUser_id()));
                            LoginUserManager.getInstance().doLogin(userBean);
                            startHome();
                        }else {
                            netRequest.loginRequest(userBean);
                        }

                    }else {
                        etName.setText("");
                        etConform.setText("");
                        etPassword.setText("");
                    }


                }else {
                    Toast.makeText(LoginActivity.this, "系统错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}
