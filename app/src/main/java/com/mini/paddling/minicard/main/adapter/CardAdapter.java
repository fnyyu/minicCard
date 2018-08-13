package com.mini.paddling.minicard.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mini.paddling.minicard.R;
import com.mini.paddling.minicard.card.CardActivity;
import com.mini.paddling.minicard.protocol.bean.BusinessBean;
import com.mini.paddling.minicard.protocol.bean.CardBean;
import com.mini.paddling.minicard.protocol.bean.ResultBean;
import com.mini.paddling.minicard.protocol.net.NetRequest;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;

import static com.mini.paddling.minicard.protocol.net.NetRequest.REQUEST_RESULT_OK;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> implements NetRequest.OnRequestListener{

    private Context context;

    private BusinessBean businessBean;

    private OnItemLongClickListener onItemLongClickListener;

    private NetRequest netRequest;

    private int delItemPos;

    public CardAdapter(Context context){
        this.context = context;
        netRequest = new NetRequest(this);
    }

    public void setBusinessBean(BusinessBean businessBean) {
        this.businessBean = businessBean;
        if (this.businessBean == null){
            this.businessBean = new BusinessBean();
        }

        if (this.businessBean.getData() == null){
            this.businessBean.setData(new ArrayList<CardBean>());
        }
        notifyDataSetChanged();
    }

    public void addItem(CardBean cardBean){
        businessBean.getData().add(cardBean);
        notifyItemChanged(businessBean.getData().size() - 1);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {
        if (businessBean.getData() != null && businessBean.getData().size() > position){
            final CardBean card = businessBean.getData().get(position);
            holder.ivCard.setImageURI(card.getCard_user_picture());
            holder.tvSpecia.setText(String.format(context.getResources().getString(R.string.special_text),
                    card.getCard_business_service(), card.getCard_business_trade()));
            holder.tvAddress.setText(card.getCard_user_address());
            holder.tvTitle.setText(card.getCard_business_name());

            holder.ivCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CardActivity.class);
                    intent.putExtra("bean",  card);
                    context.startActivity(intent);
                }
            });

            if (onItemLongClickListener != null){
                holder.ivCard.setLongClickable(true);
                holder.ivCard.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int position = holder.getLayoutPosition();
                        onItemLongClickListener.onItemLongClick(holder.ivCard, position);
                        return true;
                    }
                });
            }
        }
    }

    public void removeItem(int pos){

        if (businessBean != null && businessBean.getData() != null && businessBean.getData().size() > pos){

            delItemPos = pos;
            netRequest.delCardRequest(businessBean.getData().get(pos).getCard_id());
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public void onLoadFinish(String operationType, final ResultBean resultBean) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                setBean(resultBean);
            }
        });
    }

    private void setBean(ResultBean resultBean){

        if (resultBean != null && resultBean.getRet_code().equals(REQUEST_RESULT_OK)){
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            businessBean.getData().remove(delItemPos);
            notifyItemRemoved(delItemPos);
            if(delItemPos != businessBean.getData().size()){
                notifyItemRangeChanged(delItemPos, businessBean.getData().size() - delItemPos);
            }
        }else {
            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }


    @Override
    public int getItemCount() {
        return businessBean == null || businessBean.getData() == null ? 0 : businessBean.getData().size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView ivCard;
        private TextView tvTitle;
        private TextView tvSpecia;
        private TextView tvAddress;

        public CardViewHolder(View itemView) {
            super(itemView);
            ivCard = itemView.findViewById(R.id.iv_card);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAddress = itemView.findViewById(R.id.tv_operating);
            tvSpecia = itemView.findViewById(R.id.tv_address);
        }
    }
}
