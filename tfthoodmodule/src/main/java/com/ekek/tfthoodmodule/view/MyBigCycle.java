package com.ekek.tfthoodmodule.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ekek.tfthoodmodule.R;
import com.ekek.tfthoodmodule.utils.MiscUtil;

public class MyBigCycle extends View {

    private int progressBarBackgroundID;
    private Paint mPaintprogress,mPaitText,mPaitbackground;//
    boolean readyGoToWork=true ;
    private int progress_Width=15; // 进度条宽度 393:31 宽 499 :

    private int progress_color=Color.rgb(98, 161, 215);  // 进度条颜色
    private int progress_Max=174-20;  // 进度条最大值 默认 100；
    private int progress_Max_real=100;  // 进度条实际的最大值，根据用户应用场景设置
    private float progress_value=0.0f;  // 进度条当前值
    //默认大小
    private int mDefaultSize;
    public MyBigCycle(Context context) {
        super(context);
    }
    public MyBigCycle(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        init(context);
    }

    public MyBigCycle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HoobProgressBar);
        progressBarBackgroundID = a.getResourceId(R.styleable.HoobProgressBar_progressbar_background, R.mipmap.my_big_cycle_new_2);
        a.recycle();
        init(context);
    }

    private void init(Context context){
        mPaintprogress = new Paint( );
        mPaitbackground =new Paint() ;

        mPaintprogress.setStyle(Paint.Style.STROKE);//充满
        mPaintprogress.setAntiAlias(true);// 设置画笔的锯齿效果

        mPaintprogress .setStrokeWidth(progress_Width) ;
        mPaintprogress.setStrokeCap(Paint.Cap.ROUND);
      //  mDefaultSize = MiscUtil.dipToPx(context, 400); // 393 的圈圈
        mDefaultSize = MiscUtil.dipToPx(context, 510);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (readyGoToWork ) {
            final int width = getMeasuredWidth()+393;
            final int height = getMeasuredHeight()+393;

            // make the view the original height + indicator height size
            setMeasuredDimension(MiscUtil.measure(widthMeasureSpec, mDefaultSize),
                    MiscUtil.measure(heightMeasureSpec, mDefaultSize));
        }
    }

    @Override
    protected  void  onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //--------------背景------------------
        //Bitmap icon= BitmapFactory .decodeResource(getResources(),progerss_background) ;
        Bitmap icon = BitmapFactory.decodeResource(getResources(), progressBarBackgroundID);
        mPaitbackground=new Paint(Paint .ANTI_ALIAS_FLAG |Paint .FILTER_BITMAP_FLAG ) ;
        mPaitbackground.setColor(Color .WHITE ) ;
        canvas .drawBitmap(icon ,0,0,mPaitbackground ) ;

        //--------------画环, 即进度--------------------
        mPaintprogress.setColor(progress_color);
     //   RectF rect =new RectF(20,20,372,372); // 393 的圈圈
        RectF rect =new RectF(9,9,500,500); // 499 的圈圈
        canvas.drawArc(rect,270,360-progress_value,false,mPaintprogress);
    }
    public void setProgress(float v){
        progress_value=v;
        readyGoToWork=true ;
        //   Log.d("the progress_value=",Integer .toString(progress_value) ) ;
        invalidate();  // 此处要加这个函数，是更新onDraw

    }
    public void setProgress_color(int c){
        progress_color=c;
    }
    public void setProgress_Max (int m){
        progress_Max_real=m;
    }



}
