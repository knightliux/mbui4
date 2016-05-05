package com.bestbaan.moonbox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.moonbox.android.iptv.R;

public class PageIndicatorView extends View {
	private int mCurrentPage = -1;
	private int mTotalPage = 0 ;
	private boolean isHorizon = true;

	public PageIndicatorView(Context context) {
		super(context);
	}

	public PageIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setDirect(boolean isHorizon){
		this.isHorizon = isHorizon;
	}

	public void setTotalPage(int nPageNum) {
		mTotalPage = nPageNum;
		if (mCurrentPage >= mTotalPage)
			mCurrentPage = mTotalPage - 1;
	}

	public int getCurrentPage() {
		return mCurrentPage;
	}

	public void setCurrentPage(int nPageIndex) {
		if (nPageIndex < 0 || nPageIndex >= mTotalPage)
			return;

		if (mCurrentPage != nPageIndex) {
			mCurrentPage = nPageIndex;
			this.invalidate();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);

		Rect r = new Rect();
		this.getDrawingRect(r);

		int iconWidth = 9;
		int iconHeight = 9;
		int space = 12;
		if(isHorizon){
			int x = (r.width() - (iconWidth * mTotalPage + space * (mTotalPage - 1))) / 2;
			int y = (r.height() - iconHeight) / 2;
	
			for (int i = 0; i < mTotalPage; i++) {
	
				int resid = R.drawable.page_dot;
	
				if (i == mCurrentPage) {
					resid = R.drawable.page_dot_focus;
				}
	
				Rect r1 = new Rect();
				r1.left = x;
				r1.top = y;
				r1.right = x + iconWidth;
				r1.bottom = y + iconHeight;
	
				Bitmap bmp = BitmapFactory.decodeResource(getResources(), resid);
				canvas.drawBitmap(bmp, null, r1, paint);
	
				x += iconWidth + space;
			}
		} else {
			int x = (r.width() - (iconWidth * mTotalPage + space * (mTotalPage - 1))) / 2;
			int y = (r.height() - (iconWidth * mTotalPage + space * (mTotalPage - 1))) / 2;
			for (int i = 0; i < mTotalPage; i++) {
				int resid = R.drawable.page_dot;
				if (i == mCurrentPage) {
					resid = R.drawable.page_dot_focus;
				}
				Rect r1 = new Rect();
				r1.left = 5;
				r1.top = y;
				r1.right = 15;
				r1.bottom = y + iconWidth;
				Bitmap bmp = BitmapFactory.decodeResource(getResources(), resid);
				canvas.drawBitmap(bmp, null, r1, paint);
	
				y += iconWidth + space;
			}
		}

	}

	public void DrawImage(Canvas canvas, Bitmap mBitmap, int x, int y, int w, int h, int bx, int by) {
		Rect src = new Rect();
		Rect dst = new Rect();
		src.left = bx;
		src.top = by;
		src.right = bx + w;
		src.bottom = by + h;

		dst.left = x;
		dst.top = y;
		dst.right = x + w;
		dst.bottom = y + h;

		src = null;
		dst = null;
	}

}
