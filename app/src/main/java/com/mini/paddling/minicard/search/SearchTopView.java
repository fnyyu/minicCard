package com.mini.paddling.minicard.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini.paddling.minicard.R;

/**
 * Created by serenefang on 2018/3/5.
 * 搜索页面顶部搜索框
 */

public class SearchTopView extends LinearLayout {

	private View viewTop;
	private TextView tvCancel;
	private SearchEditText etSearchEdit;

	private TextWatcher textWatcher;

	public SearchTopView(Context context) {
		super(context);
		initView(context);
	}

	public SearchTopView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {

		viewTop = LayoutInflater.from(context).inflate(R.layout.view_search_top, this,true);
		tvCancel = viewTop.findViewById(R.id.search_cancelView);
		etSearchEdit = viewTop.findViewById(R.id.search_editText);

	}

	/**
	 * 取消EditText的焦点，避免控制软键盘隐藏失败
	 * 主要为转移焦点至其他view上
	 */
	public void clearEditTextFoucus(){
		tvCancel.setFocusable(true);
		tvCancel.setFocusableInTouchMode(true);
		tvCancel.requestFocus();
	}

	public void setClearIcon(boolean isShow){
		etSearchEdit.setClearIconVisible(isShow);
	}

	public void setOnEditorListener(TextView.OnEditorActionListener l){
		etSearchEdit.setOnEditorActionListener(l);
	}

	public void setEditOnClickListener(OnClickListener l){
		etSearchEdit.setOnClickListener(l);
	}

	public void setCancelClickListener(OnClickListener l){
		tvCancel.setOnClickListener(l);
	}

	public void setSearchEditListener(SearchEditText.SearchClickListener l){
		etSearchEdit.setSearchClickListener(l);
	}

	public String getEditText(){
		return etSearchEdit.getText().toString();
	}

	public SearchEditText getEtSearchEdit(){
		return etSearchEdit;
	}

	public void setTextWatch(TextWatcher textWatch){
		this.textWatcher = textWatch;
		etSearchEdit.addTextChangedListener(textWatch);
	}

	public void removeTextWatch(){
		if (textWatcher != null){
			etSearchEdit.removeTextChangedListener(textWatcher);
		}
	}

}
