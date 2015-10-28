package com.example.autoscrolltextviewtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * ���ı����ȴ���View����ʾ����ʱ���Զ�������
 * ÿ�����ı���ͷ��x����Ϊ0ʱ��ͣ��stay_time�����ʱ��
 * ���ı����Ȳ�����View����ʾ���ȣ����������
 * 
 * @author Willing
 *
 */
public class AutoScrollTextView extends TextView 
{
	private static final int	DEFAULT_SPEED	= 100;
	private static final float    DEFAULT_STEP_LENGTH = 10;
	private static final float	DEFAULT_DISTANCE	= 50;
	private static final int 	DEFAULT_STAY_TIME = 3000;
	
	// ��ʾ���ٺ������һ��
	private int mSpeed;
	// ��ʾText��drawʱ�����λ��
	private float mLeft = 0;
	// �ڳ�ʼλ��ʱ��ͣ��ʱ��
	private int mStayTime;
	// ÿ�θ���ǰ������
	private float mStepLength;
	// ������ʾ֮��ľ���
	private float mDistance;
	
	private Thread mThread;
	
	public AutoScrollTextView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes)
	{
		this(context, attrs, defStyleAttr);
	}

	public AutoScrollTextView(Context context, AttributeSet attrs,
			int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		
		this.setSingleLine();
		
		TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.auto_scroll_textview, defStyleAttr, 0);
		
		// speed
		mSpeed = arr.getInteger(R.styleable.auto_scroll_textview_speed, DEFAULT_SPEED);
		
		// stepLength
		mStepLength = arr.getDimension(R.styleable.auto_scroll_textview_step_length, pxToDp(DEFAULT_STEP_LENGTH));
		
		// distance
		mDistance = arr.getDimension(R.styleable.auto_scroll_textview_distance, pxToDp(DEFAULT_DISTANCE));
		
		// stayTime
		mStayTime = arr.getInteger(R.styleable.auto_scroll_textview_stay_time, DEFAULT_STAY_TIME);
		
		arr.recycle();
		
	}

	public AutoScrollTextView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public AutoScrollTextView(Context context)
	{
		this(context, null, 0);
	}

	
	
	public int getSpeed()
	{
		return mSpeed;
	}

	public void setSpeed(int speed)
	{
		mSpeed = speed;
	}

	public int getStayTime()
	{
		return mStayTime;
	}

	public void setStayTime(int stayTime)
	{
		mStayTime = stayTime;
	}

	public float getStepLength()
	{
		return mStepLength;
	}

	public void setStepLength(float stepLength)
	{
		mStepLength = stepLength;
	}

	public float getDistance()
	{
		return mDistance;
	}

	public void setDistance(float distance)
	{
		mDistance = distance;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
//		super.onDraw(canvas);
		
		Paint paint = getPaint();
		
		Rect bounds = new Rect();
		paint.getTextBounds(getText().toString(), 0, getText().length(), bounds);		
		
		if (bounds.width() <= getWidth() - getPaddingLeft() - getPaddingRight())
		{
			super.onDraw(canvas);
			
			if (mThread != null)
			{
				mThread.interrupt();
				mThread = null;
			}
			
			return;
		}
		else
		{
			if (mThread == null || !mThread.isAlive())
			{
				mThread = new UpdateTextPosThread(true);
				mThread.start();
			}
		}
		
		Rect rect = new Rect();
		rect.left = getPaddingLeft();
		rect.right = getWidth() - getPaddingRight();
		rect.top = getPaddingTop();
		rect.bottom = getHeight() - getPaddingBottom();
		
		canvas.clipRect(rect);
		
		// 
		
		FontMetrics metrics = paint.getFontMetrics();
		 
		canvas.drawText(getText().toString(), getPaddingLeft() + mLeft , getPaddingTop() - metrics.ascent, paint);
		
		mLeft -= mStepLength;
		

		
		if (bounds.width() + mLeft + mDistance < rect.width()) 
		{
			canvas.drawText(getText().toString(), getPaddingLeft() + bounds.width() + mLeft + mDistance, getPaddingTop() - metrics.ascent, paint);
			
		}
		
		if (bounds.width() + mLeft <= -mDistance)
		{
			mLeft = 0;
		}
	}
	
	@Override
	public void setText(CharSequence text, BufferType type)
	{
		super.setText(text, type);
		
		/**
		 * ���ڵ����ı���setΪ���ı�ʱ��
		 */
		if (mThread != null)
		{
			((UpdateTextPosThread)mThread).setInit(true);
		}
	}
 
	private  float pxToDp(float px)
	{
		float scale = getResources().getDisplayMetrics().density; 
		return px / scale + 0.5f; 
	}
	
	private class UpdateTextPosThread extends Thread
	{
		private volatile boolean init;
		
		public UpdateTextPosThread(boolean init)
		{
			this.init = init;
		}
		
		public void setInit(boolean init)
		{
			this.init = init;
		}
		public boolean getInit()
		{
			return init;
		}
		
		public void run()
		{
			while (true)
			{
				Log.i("test", "thread");
				try
				{
					if (getInit())
					{
						mLeft = 0;
						init = false;
					}
					if (mLeft == 0)
					{
						Thread.sleep(mStayTime);
					}
					else
					{
						Thread.sleep(mSpeed);
					}
					postInvalidate();
					
					
				}
				catch (InterruptedException ex)
				{
					mLeft = 0;
					postInvalidate();
					
					return;
				}
			}
		}
	}
}
