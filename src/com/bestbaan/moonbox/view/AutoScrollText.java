package com.bestbaan.moonbox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoScrollText extends TextView{

	public AutoScrollText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AutoScrollText(Context context, AttributeSet attrs) {
		super(context, attrs,0);
		// TODO Auto-generated constructor stub
	}

	public AutoScrollText(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}

}
