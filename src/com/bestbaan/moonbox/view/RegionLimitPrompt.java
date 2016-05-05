package com.bestbaan.moonbox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bestbaan.moonbox.util.MACUtils;
import com.moonbox.android.iptv.R;

/**
 * 当机顶盒在不同的区域使用时，会弹出此框来阻止用户使用
 * @author houhualiang
 *
 */
public class RegionLimitPrompt extends LinearLayout{


	public RegionLimitPrompt(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWidget(context);
	}

	public RegionLimitPrompt(Context context) {
		super(context);
		initWidget(context);
	}

	
	private TextView mPromptText;
	private TextView mTextMac;
	private void initWidget(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.region_limit_prompt, this);
		mPromptText = (TextView) findViewById(R.id.prompt_text);
		mTextMac = (TextView) findViewById(R.id.mac);
		String mac = MACUtils.getMac();
		if(null != mac)
			mTextMac.setText("MAC:"+mac);
	}
	
	
	public void setPromptText(int strID){
		mPromptText.setText(strID);
	}
	
	public void setPromptText(String promptText){
		mPromptText.setText(promptText);
	}
}
