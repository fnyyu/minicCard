package com.mini.paddling.minicard.search;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by serenefang on 2018/2/27.
 */

public class SearchEditText extends EditText{

	private SearchClickListener searchClickListener;
	private Drawable clearIcon;

	private OnFocusChangeListener mOnFocusChangeListener;

	public SearchEditText(Context context) {
		super(context);
		init(context);
	}

	public SearchEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		Drawable drawable = this.getCompoundDrawables()[2];
		clearIcon = drawable;
		setClearIconVisible(false);
	}

	public void setSearchClickListener(SearchClickListener searchClickListener){
		this.searchClickListener = searchClickListener;
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Drawable drawable = this.getCompoundDrawables()[2];
		if (drawable != null && event.getX() > this.getWidth()
				- this.getPaddingRight()
				- drawable.getIntrinsicWidth()){
			this.setText("");
			if (searchClickListener != null){
				searchClickListener.clearClick();
			}
		}
		return super.onTouchEvent(event);
	}

	public void setClearIconVisible(final boolean visible) {
		clearIcon.setVisible(visible, false);
		final Drawable[] compoundDrawables = getCompoundDrawables();
		setCompoundDrawables(
				compoundDrawables[0],
				compoundDrawables[1],
				visible ? clearIcon : null,
				compoundDrawables[3]);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_ENTER && searchClickListener != null){
//			searchClickListener.searchClick();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean showContextMenu() {
		return false;
	}

	public interface SearchClickListener{
		void clearClick();
		void searchClick();
	}

}
