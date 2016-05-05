package com.moon.android.iptv;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bestbaan.moonbox.model.UserMsg;
import com.moonbox.android.iptv.R;

public class UserMsgShowActivity extends Activity
					implements OnKeyListener{

	public static final String TAG = "UserMsgShowActivity";
	private TextView mTextTitle;
	private TextView mTextContent;
	private UserMsg mUserMsg;
	private TextView mTextDate;
	private Button mBtbFocusBtn;
	private RelativeLayout mRoot;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_msg_show);
		initData();
		initWidget();
		
		LayoutParams params=getWindow().getAttributes();
        
		params.y=160;
		getWindow().setAttributes(params);
	}

	private void initData() {
		mUserMsg = (UserMsg) getIntent().getSerializableExtra(Configs.PARAM_1);
	}

	private void initWidget() {
		mTextTitle = (TextView) findViewById(R.id.user_msg_show_title);
		mTextContent = (TextView) findViewById(R.id.user_msg_show_content);
		mTextDate  = (TextView) findViewById(R.id.user_msg_show_content_date);
		mBtbFocusBtn = (Button) findViewById(R.id.get_focus_btn);
		mRoot = (RelativeLayout) findViewById(R.id.root);
		mRoot.setOnClickListener(mRootCL);
		mTextTitle.setText(mUserMsg.getTitle());
//		mTextContent.setText("\u3000\u3000"+mUserMsg.getBody());
		mTextContent.setText("\u3000\u3000"+Html.fromHtml(mUserMsg.getBody()));
		mTextDate.setText(mUserMsg.getTime());
		mBtbFocusBtn.requestFocus();
		mBtbFocusBtn.setOnKeyListener(this);
	}
	
	private OnClickListener mRootCL = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		finish();
		return false;
	}

}
