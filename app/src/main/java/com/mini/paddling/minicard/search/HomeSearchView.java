package com.mini.paddling.minicard.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mini.paddling.minicard.R;

import java.util.ArrayList;

/**
 * Created by serenefang on 2018/2/26.
 */

public class HomeSearchView extends RelativeLayout {

	private View viewTop;
	private RelativeLayout rlSearch;
	private TextView tvSearchText;
	private ImageView ivSearchImage;


	public HomeSearchView(Context context) {
		super(context);
		init(context, null);
	}

	public HomeSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(final Context context, AttributeSet attrs) {
		viewTop = LayoutInflater.from(context).inflate(R.layout.home_top_search, this,true);
		rlSearch = viewTop.findViewById(R.id.home_searchLayout);
		tvSearchText = viewTop.findViewById(R.id.home_searchText);
		ivSearchImage = viewTop.findViewById(R.id.home_searchImage);

	}

	public void setSearchVisible(boolean isVisible){
		this.viewTop.setVisibility(isVisible ? VISIBLE: GONE);
	}


	public void setOnSearchIntentListener(OnClickListener l) {
		if(rlSearch != null)
			rlSearch.setOnClickListener(l);
	}

	public void setOnSearchClickListener(OnClickListener l) {
		if(ivSearchImage != null)
			ivSearchImage.setOnClickListener(l);
	}

	public String getHintText(){
		return tvSearchText.getText().toString();
	}


}
