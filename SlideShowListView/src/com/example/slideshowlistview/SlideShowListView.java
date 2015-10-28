package com.example.slideshowlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * Ϊ��ʹ�ø��࣬�ṩ����Դ�ļ��б������һ��idΪdelete��View��
 * @author Willing
 *
 */
public class SlideShowListView extends ListView
{
	private View mView; // ����ɾ����View
	private View mDeleteView; 
	private int mDeleteViewWidth;
	private boolean mDeleteShow;
	private int mPosition; // ����ɾ��View��position
	
	// ���ʱ��xyֵ
	private int mDownX;
	private int mDownY; 
	
	private Scroller mScroller;
	
	// ��������С���룬�����˾���ű�ʾ����
	public int mTouchSlop;
	
	public SlideShowListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		init(context);
	}

	public SlideShowListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		init(context);
	}

	public SlideShowListView(Context context)
	{
		super(context);
		
		init(context);
	}
	
	private void init(Context context)
	{
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mScroller = new Scroller(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		if (!mScroller.isFinished())
		{
			return true;
		}
		
		int action = ev.getAction();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			
			int pos = pointToPosition(x, y);	
			
			if (pos != mPosition && mDeleteShow)
			{
				// �ջ�
				mScroller.startScroll(mView.getScrollX(), 0, -mView.getScrollX(), 0);
				postInvalidate();
				collapseDelete();
				return true;
			}
			
			mDownX = x;
			mDownY = y;
			
			mPosition = pos;
			
			if (mPosition != AdapterView.INVALID_POSITION)
			{
				mView = getChildAt(mPosition - getFirstVisiblePosition());
	
				mDeleteView = mView.findViewById(R.id.delete);
				
			}
			break;
		case MotionEvent.ACTION_MOVE:
			
			if (Math.abs(ev.getX() - mDownX) > mTouchSlop && Math.abs(ev.getY() - mDownY) < mTouchSlop)
			{
				if (mDeleteView != null && !mDeleteShow)
				{
					expandDelete();
				}
			}
 
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	
	private void expandDelete()
	{
		mDeleteViewWidth = mDeleteView.getWidth();
		mDeleteView.offsetLeftAndRight(mDeleteViewWidth);
		mDeleteView.setVisibility(View.VISIBLE);
		
		mDeleteShow = true;
		
	}

	private void collapseDelete()
	{
		mDeleteView.offsetLeftAndRight(-mDeleteViewWidth);
		 
		mDeleteView.setVisibility(View.INVISIBLE);
		mDeleteShow = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (mDeleteShow && mPosition != AdapterView.INVALID_POSITION)
		{
 
			int action = ev.getAction();
			int x = (int) ev.getX();
			switch (action)
			{
			case MotionEvent.ACTION_MOVE:
				int detalX = mDownX - x;
				mDownX = x;
				
				int scrollX = Math.min(mDeleteViewWidth, mView.getScrollX() + detalX);
				scrollX = Math.max(0, scrollX);
				
				if (scrollX == 0)
				{
					if (mDeleteView != null && mDeleteShow)
					{	
						collapseDelete();
					 
					}
				}
			
				mView.scrollTo(scrollX, 0); // detalXΪ������ʾ�������
				
 
				break;
			case MotionEvent.ACTION_UP:
				
				break;
			}
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void computeScroll()
	{
		if (mScroller.computeScrollOffset())
		{
			mView.scrollTo(mScroller.getCurrX(), 0);
			postInvalidate();
			
			if (mScroller.getCurrX() == 0)
			{
				mScroller.forceFinished(true);
			}
		}
	}
}
