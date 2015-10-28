package com.example.flowgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * ���岼��
 * ������������View����ʣ��ռ䲻��ʱ������һ�С�
 * �������View�Ŀ�ȴ���ViewGroup�Ŀ�ȣ��򵥶�ռһ��
 * @author Willing
 *
 */
public class FlowGroup extends ViewGroup
{
 
	public FlowGroup(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public FlowGroup(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public FlowGroup(Context context)
	{
		super(context);
	}
	
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs)
	{
		return new MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		int width = 0;
		int height = 0;
		
	 
		View childView = null;
		// ��ǰ�еĿ��
		int lineWidth = 0;
		// ��ǰ�е���߸߶�
		int lineHeight = 0;
		
		int childWidth = 0;
		int childHeight = 0;
		MarginLayoutParams layoutParams = null;
		
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		
		int count = getChildCount();
		for (int i = 0; i < getChildCount(); ++i)
		{
			childView = getChildAt(i);
			
			layoutParams = (MarginLayoutParams) childView.getLayoutParams();
			childWidth = layoutParams.leftMargin + layoutParams.rightMargin + childView.getMeasuredWidth();
			childHeight = layoutParams.topMargin + layoutParams.bottomMargin + childView.getMeasuredHeight();
			
			// ���ʣ��ռ䲻���Է��¸���View
			if (lineWidth + childWidth > widthSize)
			{
				// �������View������ڲ��ֵĿ��
				if (childWidth > widthSize)
				{
					width = childWidth;
					lineWidth = 0;
					
					// �����View�������һ������߶����Ӹ�View�ĸ߶ȡ���Ϊѭ�������󣬻��������һ�еĸ߶�
					if (i != count - 1)
					{
						height += childHeight;
					}
				}
				else
				{
					width = Math.max(lineWidth, childWidth);
					lineWidth = childWidth;
				}
				// ����һ��ʱ��������һ�е���߸߶�
				height += lineHeight;
				lineHeight = childHeight;
			}
			else
			{
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);
			}
		}	
		
		width = Math.max(width, lineWidth);
		// �������һ�еĸ߶�
		height += lineHeight;
		
		if (widthMode == MeasureSpec.EXACTLY)
		{
			width = widthSize;
		}
		else if (widthMode == MeasureSpec.AT_MOST)
		{
			width = Math.min(width, widthSize);
		}
		
		if (heightMode == MeasureSpec.EXACTLY)
		{
			height = heightSize;
		}
		else if (heightMode == MeasureSpec.AT_MOST)
		{
			height = Math.min(height, heightSize);
		}
		 
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		int x = 0;
		int y = 0;
		int lineHeight = 0;
		int lineWidth = 0;
		int childWidth = 0;
		int childHeight = 0;
		int width = getWidth();
		
		View childView = null;
		MarginLayoutParams layoutParams = null;
		for (int i = 0; i < getChildCount(); ++i)
		{
			childView = getChildAt(i);
			layoutParams = (MarginLayoutParams) childView.getLayoutParams();
			
			childWidth = childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
			childHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
			
			if (lineWidth + childWidth > width)
			{
				// ���ʣ��ռ䲻���Է��¸���View
				if (childWidth > width)
				{
					y = y + lineHeight;
						 
					childView.layout(layoutParams.leftMargin, layoutParams.topMargin + y,
							 Math.max(0, width - layoutParams.rightMargin), 
							 layoutParams.topMargin + y + childView.getMeasuredHeight());
					y += childHeight;
					lineHeight = 0; 
					lineWidth = 0;
				}
				else
				{
					childView.layout(layoutParams.leftMargin, layoutParams.topMargin + y + lineHeight,
							layoutParams.leftMargin + childView.getMeasuredWidth(),
							layoutParams.topMargin + y + lineHeight + childView.getMeasuredHeight());
					lineWidth = childWidth;
					lineHeight = childHeight;
					y += lineHeight;
				}
				
			}
			else
			{
				childView.layout(lineWidth + layoutParams.leftMargin, layoutParams.topMargin + y,
						lineWidth + layoutParams.leftMargin + childView.getMeasuredWidth(), 
						layoutParams.topMargin + y + childView.getMeasuredHeight());
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);
			}
			
		}
		
	}

}
