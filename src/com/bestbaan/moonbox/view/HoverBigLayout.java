package com.bestbaan.moonbox.view;



import com.moonbox.android.iptv.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;




public class HoverBigLayout extends RelativeLayout{  
      
    private Rect mBound;  
    private Drawable mDrawable;  
    private Rect mRect;  
      
    private Animation scaleSmallAnimation;  
    private Animation scaleBigAnimation;  
    private Context con;
    public HoverBigLayout(Context context) {  
        super(context);  
        init(context);  
    }  
    public void test(){
    	Log.d("----","----");
    }
    public HoverBigLayout(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init(context);  
    }  
  
    public HoverBigLayout(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init(context);  
    }  
      
    protected void init(Context context) {  
      
        View view = LayoutInflater.from(context).inflate(R.layout.home_page_channel_item,this);
        setWillNotDraw(false); 
        mRect = new Rect();  
        mBound = new Rect();  
        mDrawable = getResources().getDrawable(R.drawable.index_selector);//nav_focused_2,poster_shadow_4  
        setChildrenDrawingOrderEnabled(true);  
    }  
  
    @Override  
    protected void onAttachedToWindow() {  
        super.onAttachedToWindow();  
    }  
      
    @Override  
    public void draw(Canvas canvas) {  
        super.draw(canvas);  
    }  
      
    @Override  
    protected void onDraw(Canvas canvas) {  
        if (hasFocus()) {  
//            System.out.println("HomeItemContainer focus : true ");  
//            super.getDrawingRect(mRect);  
//            mBound.set(-39+mRect.left, -39+mRect.top, 39+mRect.right, 39+mRect.bottom);  
//            mDrawable.setBounds(mBound);  
//            canvas.save();  
//            mDrawable.draw(canvas);  
//            canvas.restore();  
        }  
        super.onDraw(canvas);  
    }  
      
    @Override  
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {  
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);  
        if (gainFocus) {  
            bringToFront();  
            getRootView().requestLayout();  
            getRootView().invalidate();  
            zoomOut();  
        } else {  
            zoomIn();  
        }  
    }  
      
    private void zoomIn() {  
        if (scaleSmallAnimation == null) {  
            scaleSmallAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_small);  
        }  
    
        startAnimation(scaleSmallAnimation);  
    }  
      
    private void zoomOut() {  
//        if (scaleBigAnimation == null) {  
//            scaleBigAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_big);  
//        }  
//        System.out.println("----Out----"); 
//        startAnimation(scaleBigAnimation);
    	
    	scaleBigAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,  
                   Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,  
                   0.5f);  
    	scaleBigAnimation.setDuration(200); 
    
    	this.setAnimation(scaleBigAnimation);
    	scaleBigAnimation.startNow();
    	scaleBigAnimation.setFillAfter(true);
//    	this.setEnabled(false);
//    	scaleBigAnimation.setInterpolator(new AccelerateInterpolator());  
    	
//           AnimationSet aa = new AnimationSet(true);  
//           aa.addAnimation(scaleBigAnimation);  
//          
//          
//           scaleBigAnimation.startNow();
//           startAnimation(aa); 
    	
    }

      
}  