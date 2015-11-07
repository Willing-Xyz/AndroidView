package com.example.startstopbutton;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class StartStopButton extends View
{
	private static final int	DEF_START_COLOR	= 0x00ff00;
	private static final int	DEF_STOP_COLOR	= 0xff0000;
	private static final int	DEF_COLOR	= 0xdddddd;
	private static final float	DEF_TEXT_SIZE	= 30;
	private static final float	DEF_CIRCLE_WIDTH	= 3;
	
	private Paint mPaint;
	private boolean mStarted;
	private ArrayList<StateChangeListener> listeners;
	
	// �뾶
	private float mRadius;
	private String mStartText;
	private String mStopText;
	// start text��Բ����ɫ
	private int mStartColor;
	private int mStopColor;
	private float mTextSize;
	// ԰�ڲ�������԰���ı�����ɫ
	private int mColor;
	// Բ�����ߵĿ��
	private float mCircleWidth;
	
	public StartStopButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.start_stop_button, defStyle, 0);
		
		mRadius = arr.getDimension(R.styleable.start_stop_button_radius, 20);
		mStartText = arr.getString(R.styleable.start_stop_button_startText);
		mStopText = arr.getString(R.styleable.start_stop_button_stopText);
		mStartColor = arr.getColor(R.styleable.start_stop_button_startColor, DEF_START_COLOR);
		mStopColor = arr.getColor(R.styleable.start_stop_button_stopColor, DEF_STOP_COLOR);
		mColor = arr.getColor(R.styleable.start_stop_button_color, DEF_COLOR);
		mTextSize = arr.getDimension(R.styleable.start_stop_button_textSize, DEF_TEXT_SIZE);
		mCircleWidth = arr.getDimension(R.styleable.start_stop_button_circleWidth, DEF_CIRCLE_WIDTH);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Style.STROKE);
		mPaint.setTextSize(mTextSize);
		mPaint.setStrokeWidth(10);
		
		listeners = new ArrayList<StateChangeListener>();
	}

	public StartStopButton(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public StartStopButton(Context context)
	{
		this(context, null, 0);
	}
	
	

	public float getTextSize()
	{
		return mTextSize;
	}

	public void setTextSize(float textSize)
	{
		mTextSize = textSize;
		mPaint.setTextSize(textSize);
		invalidate();
	}

	public float getCircleWidth()
	{
		return mCircleWidth;
	}

	public void setCircleWidth(float circleWidth)
	{
		mCircleWidth = circleWidth;
		mPaint.setStrokeWidth(circleWidth);
		invalidate();
	}

	public String getStartText()
	{
		return mStartText;
	}

	public void setStartText(String startText)
	{
		mStartText = startText;
		invalidate();
	}

	public String getStopText()
	{
		return mStopText;
	}

	public void setStopText(String stopText)
	{
		mStopText = stopText;
		invalidate();
	}

	public int getStartColor()
	{
		return mStartColor;
	}

	public void setStartColor(int startColor)
	{
		mStartColor = startColor;
		invalidate();
	}

	public int getStopColor()
	{
		return mStopColor;
	}

	public void setStopColor(int stopColor)
	{
		mStopColor = stopColor;
		invalidate();
	}

	public int getColor()
	{
		return mColor;
	}

	public void setColor(int color)
	{
		mColor = color;
		
		invalidate();
	}

	public float getRadius()
	{
		return mRadius;
	}

	public void setRadius(float radius)
	{
		mRadius = radius;
		invalidate();
	}

	public boolean isStarted()
	{
		return mStarted;
	}

	public void setStarted(boolean started)
	{
		if (this.mStarted != started)
		{
			this.mStarted = started;
			fireStateChangeListener(mStarted);
			invalidate();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int width = 0;
		int height = 0;
		
		width = (int) (2 * mRadius + getPaddingLeft() + getPaddingRight() + mCircleWidth * 2);
		height = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom() + mCircleWidth * 2);
		
		switch (widthMode)
		{
		case MeasureSpec.AT_MOST:
			width = Math.min(width, widthSize);
			break;
		case MeasureSpec.EXACTLY:
			width = widthSize;
			break;
		}
		switch (heightMode)
		{
		case MeasureSpec.AT_MOST:
			height = Math.min(height, heightSize);
			break;
		case MeasureSpec.EXACTLY:
			height = heightSize;
			break;
		}
		
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		// ������
		int x = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
		int y = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
		mPaint.setStrokeWidth(mCircleWidth);
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(mColor);
		canvas.drawCircle(x, y, mRadius, mPaint);
		
		// �������Բ
		mPaint.setStyle(Style.STROKE);
		
		canvas.drawCircle(x, y, mRadius, mPaint);
		
		String text = null;
		if (mStarted)
		{
			text = mStopText;
			mPaint.setColor(mStopColor);
		}
		else
		{
			text = mStartText;
			mPaint.setColor(mStartColor);
		}
		
		// ������
		mPaint.setStrokeWidth(1);
		mPaint.setStyle(Style.FILL);
		Rect bounds = new Rect();
		mPaint.getTextBounds(text, 0, 2, bounds);
		
		x = (int) ((getWidth() - getPaddingLeft() - getPaddingRight() - bounds.width())/ 2) ;
		y = (int) ((int) ((getHeight() - getPaddingTop() - getPaddingBottom() - bounds.height()) / 2) + bounds.height() - mPaint.descent());


		if (mStarted)
		{
			canvas.drawText(mStopText, x, y, mPaint);
		}
		else
		{
			canvas.drawText(mStartText, x, y, mPaint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		switch (action)
		{
		case MotionEvent.ACTION_UP:
			mStarted = !mStarted;
			fireStateChangeListener(mStarted);
			invalidate();
			break;
		}
		 
		
		return true;
	}
	
	public void registerStateChangeListener(StateChangeListener listener)
	{
		if (!listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}
	public void unregisterStateChangeListener(StateChangeListener listener)
	{
		listeners.remove(listener);
	}
	private void fireStateChangeListener(boolean nowState)
	{
		for (int i = 0; i < listeners.size(); ++i)
		{
			listeners.get(i).stateChanged(nowState);
		}
	}
	
	public static interface StateChangeListener
	{
		void stateChanged(boolean started);
	}
}
