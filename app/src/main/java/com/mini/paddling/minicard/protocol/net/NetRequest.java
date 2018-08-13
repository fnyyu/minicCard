package com.mini.paddling.minicard.protocol.net;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mini.paddling.minicard.protocol.bean.BusinessBean;
import com.mini.paddling.minicard.protocol.bean.CardAddBean;
import com.mini.paddling.minicard.protocol.bean.CardBean;
import com.mini.paddling.minicard.protocol.bean.LoginBean;
import com.mini.paddling.minicard.protocol.bean.ResultBean;
import com.mini.paddling.minicard.protocol.bean.UserBean;
import com.mini.paddling.minicard.util.LogUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_ADD_CARD;
import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_ADD_COLLECT;
import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_COLLECT_CARD;
import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_DELETE;
import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_DEL_COLLECT;
import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_EDIT;
import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_FIND;
import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_LOGIN;
import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_REGISTER;
import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_SEARCH;
import static com.mini.paddling.minicard.protocol.bean.LinksBean.LINK_USER_CARD;

public class NetRequest {

    private static final String TAG = "NetRequest";

    private OnRequestListener onRequestListener;

    public static final String REQUEST_RESULT_OK = "200";
    public static final String REQUEST_RESULT_ERROR = "300";

    public NetRequest(OnRequestListener onRequestListener) {
        this.onRequestListener = onRequestListener;
    }

    /**
     * 用户名片列表信息接口请求
     *
     * @param userId
     * @return
     */
    public void businessGetRequest(String userId) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LINK_USER_CARD + userId)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(TAG, e);

                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_USER_CARD, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {

                    BusinessBean jsonBean;
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<BusinessBean>() {
                    }.getType();
                    jsonBean = gson.fromJson(response.body().string(), type);

                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_USER_CARD, jsonBean);
                    }
            }catch (Exception e){
                Log.e(TAG, e.getMessage());
                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_USER_CARD, null);
                }
            }
            }
        });
    }

    /**
     * 用户收藏列表信息接口请求
     *
     * @param userId
     * @return
     */
    public void collectGetRequest(String userId) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LINK_COLLECT_CARD + userId)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(TAG, e);

                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_COLLECT_CARD, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {

                    BusinessBean jsonBean;
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<BusinessBean>() {
                    }.getType();
                    jsonBean = gson.fromJson(response.body().string(), type);

                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_COLLECT_CARD, jsonBean);
                    }
                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_COLLECT_CARD, null);
                    }
                }
            }
        });
    }

    /**
     * 登陆接口调用请求
     *
     * @param bean
     * @return
     */
    public void loginRequest(UserBean bean) {

        RequestBody requestBody = new FormBody.Builder()
                .add(bean.nameToString(), bean.getAname())
                .add(bean.pasToString(), bean.getApwd())
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LINK_LOGIN)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(TAG, e);

                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_LOGIN, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {

                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<LoginBean>() {
                    }.getType();
                    LoginBean jsonBean = gson.fromJson(response.body().string(), type);

                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_LOGIN, jsonBean);
                    }

                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_LOGIN, null);
                    }
                }
            }
        });
    }

    /**
     * 注册接口调用请求
     *
     * @param bean
     * @return
     */
    public void registerRequest(UserBean bean) {

        RequestBody requestBody = new FormBody.Builder()
                .add(bean.nameToString(), bean.getAname())
                .add(bean.pasToString(), bean.getApwd())
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LINK_REGISTER)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(TAG, e);

                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_REGISTER, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {

                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<LoginBean>() {}.getType();
                    LoginBean jsonBean = gson.fromJson(response.body().string(), type);

                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_REGISTER, jsonBean);
                    }
                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_REGISTER, null);
                    }
                }
            }
        });

    }

    /**
     * 添加名片信息接口调用请求
     *
     * @param bean
     * @return
     */
    public void addCardRequest(CardBean bean) {

        RequestBody requestBody = new FormBody.Builder()
                .add("user_id", bean.getUser_id())
                .add("card_business_name", bean.getCard_business_name())
                .add("card_business_trade", bean.getCard_business_trade())
                .add("card_business_service", bean.getCard_business_service())
                .add("card_user_name", bean.getCard_user_name())
                .add("card_user_tel", bean.getCard_user_tel())
                .add("card_user_address", bean.getCard_user_address())
                .add("card_user_slogan", bean.getCard_user_slogan())
                .add("card_user_picture", bean.getCard_user_picture())
                .add("card_click_time", bean.getCard_click_time())
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LINK_ADD_CARD)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(TAG, e);

                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_ADD_CARD, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response){
                try {

                    CardAddBean jsonBean;
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<CardAddBean>() {
                    }.getType();
                    jsonBean = gson.fromJson(response.body().string(), type);

                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_ADD_CARD, jsonBean);
                    }
                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_ADD_CARD, null);
                    }
                }
            }
        });
    }

    /**
     * 编辑名片信息接口调用
     *
     * @param bean
     */
    public void editCardRequest(CardBean bean) {

        RequestBody requestBody = new FormBody.Builder()
                .add("card_id", bean.getCard_id())
                .add("card_business_name", bean.getCard_business_name())
                .add("card_business_trade", bean.getCard_business_trade())
                .add("card_business_service", bean.getCard_business_service())
                .add("card_user_name", bean.getCard_user_name())
                .add("card_user_tel", bean.getCard_user_tel())
                .add("card_user_address", bean.getCard_user_address())
                .add("card_user_slogan", bean.getCard_user_slogan())
                .add("card_user_picture", bean.getCard_user_picture())
                .add("card_user_video", bean.getCard_user_video())
                .add("card_click_time", bean.getCard_click_time())
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LINK_EDIT)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(TAG, e);

                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_EDIT, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResultBean jsonBean;
                    Gson gson = new Gson();

                    java.lang.reflect.Type type = new TypeToken<ResultBean>() {}.getType();
                    jsonBean = gson.fromJson(response.body().string(), type);

                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_EDIT, jsonBean);
                    }
                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_EDIT, null);
                    }
                }

            }
        });
    }


    /**
     * 添加收藏信息接口调用请求
     * @param cardId
     * @param userId
     */
    public void addCollectRequest(String cardId, String userId) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LINK_ADD_COLLECT + "collect_user_id=" + userId + "&collect_card_id=" +cardId)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(TAG, e);

                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_ADD_COLLECT, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResultBean jsonBean;
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<ResultBean>() {
                    }.getType();
                    jsonBean = gson.fromJson(response.body().string(), type);

                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_ADD_COLLECT, jsonBean);
                    }
                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_ADD_COLLECT, null);
                    }
                }
            }
        });
    }

    /**
     * 删除收藏信息接口调用请求
     * @param cardId
     * @param userId
     */
    public void delCollectRequest(String cardId, String userId) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LINK_DEL_COLLECT + "collect_card_id=" +cardId + "&collect_user_id=" + userId)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(TAG, e);

                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_DEL_COLLECT, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResultBean jsonBean;
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<ResultBean>() {
                    }.getType();
                    jsonBean = gson.fromJson(response.body().string(), type);

                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_DEL_COLLECT, jsonBean);
                    }

                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_DEL_COLLECT, null);
                    }
                }
            }
        });
    }

    /**
     *获取某名片信息接口调用请求
     * @param cardId
     */
    public void findRequest(String cardId) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LINK_FIND + cardId)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(TAG, e);

                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_FIND, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    BusinessBean jsonBean;
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<BusinessBean>() {
                    }.getType();
                    jsonBean = gson.fromJson(response.body().string(), type);

                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_FIND, jsonBean);
                    }

                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_FIND, null);
                    }
                }
            }
        });
    }


    /**
     * 删除名片信息接口调用请求
     *
     * @param cardId
     * @return
     */
    public void delCardRequest(String cardId) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LINK_DELETE + cardId)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(TAG, e);

                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_DELETE, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResultBean jsonBean;
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<ResultBean>() {
                    }.getType();
                    jsonBean = gson.fromJson(response.body().string(), type);

                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_DELETE, jsonBean);
                    }

                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_DELETE, null);
                    }
                }
            }
        });
    }


    /**
     * 模糊搜索名片信息接口请求
     *
     * @param input
     * @return
     */
    public void searchRequest(String input) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(LINK_SEARCH + input)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i(TAG, e);

                if (onRequestListener != null) {
                    onRequestListener.onLoadFinish(LINK_SEARCH, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {

                    BusinessBean jsonBean;
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<BusinessBean>() {
                    }.getType();
                    jsonBean = gson.fromJson(response.body().string(), type);

                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_SEARCH, jsonBean);
                    }
                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    if (onRequestListener != null) {
                        onRequestListener.onLoadFinish(LINK_SEARCH, null);
                    }
                }
            }
        });
    }


    public interface OnRequestListener {
        void onLoadFinish(String operationType, ResultBean resultBean);
    }

}
