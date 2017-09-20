package com.neopi.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Calendar;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;

/**
 * @Author NeoPi
 * @Date 2017/09/19
 * @Description 请用一句话描述该类的左右
 */

public class SimpleMonthView extends ViewGroup {

  private Calendar mCalendar;
  private int dayCount;
  //private int firstIndex = 1; // 本月份中1号是第一周的第几天
  private int weekCount; // 本月有几周

  private int screenWidth ;
  private int screenHeight ;
  private int cellHeight ;
  private int cellWidth ;
  private float density ;

  private final int DEFAULT_WIDTH = 50 ; // cell默认宽高大小

  private Context mContext;
  private ArrayList<Cell> mCells;
  private OnCellClickListener mListener ;

  public SimpleMonthView(Context context) {
    this(context, null);
  }

  public SimpleMonthView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SimpleMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init(context, attrs, defStyleAttr);
  }

  private void init(Context context, AttributeSet attrs, int defStyleAttr) {
    mCalendar = Calendar.getInstance();
    mCells = new ArrayList<>();
    mContext = context;

    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    screenWidth = displayMetrics.widthPixels;
    screenHeight = displayMetrics.heightPixels;
    density = displayMetrics.density ;
    cellWidth = screenWidth / 7;
    cellHeight = (int) (density * DEFAULT_WIDTH + 0.5f);

    dayCount = CalendarUtils.getDayCount(mCalendar);
    weekCount = CalendarUtils.getWeekCount(mCalendar);

  }

  private void buildCell() {
    mCells.clear();
    for (int i = 0; i < dayCount; i++) {
      final Cell cell = new Cell(getContext());
      cell.setLayoutParams(new LayoutParams(cellWidth,cellHeight));
      mCalendar.set(Calendar.DAY_OF_MONTH, 1 + i);
      cell.setCalendar(mCalendar);
      addView(cell);
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int width = MeasureSpec.getSize(widthMeasureSpec);
    switch (widthMode) {
      case EXACTLY: // match_parent 或者 固定值
        cellWidth = width / 7 ;
        break;
      case AT_MOST:
        cellWidth = (int)(density * DEFAULT_WIDTH + 0.5f);
        break;
    }

    int heightMode = MeasureSpec.getMode(heightMeasureSpec) ;
    int height = MeasureSpec.getSize(heightMeasureSpec) ;
    switch (heightMode) {
      case EXACTLY:
        cellHeight = height / 7;
        break;
      case AT_MOST:
        cellHeight = (int)(density * DEFAULT_WIDTH + 0.5f);
        break;
    }

    int count = getChildCount();
    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);
      child.setLayoutParams(new LayoutParams(cellWidth,cellHeight));
      measureChild(child,cellWidth * 7,cellHeight * weekCount);
    }

    setMeasuredDimension(widthMeasureSpec, cellHeight * weekCount);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int childCount = getChildCount();
    for (int i = 1; i <= childCount; i++) {
      mCalendar.set(Calendar.DAY_OF_MONTH,i);
      int indexOfWeek = CalendarUtils.indexOfWeek(mCalendar);
      int indexWeek = CalendarUtils.weekOfMonth(mCalendar);
      View childAt = getChildAt(i - 1);
      childAt.layout((indexOfWeek - 1) * childAt.getMeasuredWidth() ,(indexWeek - 1) * childAt.getMeasuredHeight() ,
          indexOfWeek * childAt.getMeasuredWidth(),indexWeek * childAt.getMeasuredHeight());
    }
  }

  private Cell lastView ;
  @Override public boolean onTouchEvent(MotionEvent event) {

    Log.e("111",event.getX()+"...."+event.getY()+"    "+ cellWidth+ "   "+cellHeight);
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        float downX = event.getX();
        float downY = event.getY();
        int dayOfWeek = (int)(downX / cellWidth + 1);
        int weekOfMonth = (int)(downY / cellHeight + 1);
        mCalendar.set(Calendar.DAY_OF_WEEK,dayOfWeek);
        mCalendar.set(Calendar.WEEK_OF_MONTH,weekOfMonth);
        int position = mCalendar.get(Calendar.DAY_OF_MONTH);
        Cell childCell = (Cell) getChildAt(position - 1);
        if (childCell != null) {
          childCell.setSelected(true);
          if (mListener != null) {
            mListener.onCellClick(childCell,position -1,mCalendar);
          }
          if (lastView != null) {
            invalidateRang(lastView,childCell);
          }
          lastView = childCell ;
        }
        break ;
    }
    return super.onTouchEvent(event);
  }

  /**
   *
   * @param lastView
   * @param clickView
   *
   */
  private void invalidateRang(Cell lastView, Cell clickView) {

    int childCount = getChildCount() ;
    for (int i = 0; i < childCount; i++) {
      Cell child = (Cell) getChildAt(i);
      long childTimeMillis = child.getCalendar().getTimeInMillis();
      if ((childTimeMillis > lastView.getCalendar().getTimeInMillis() && childTimeMillis < clickView.getCalendar().getTimeInMillis())
          || (childTimeMillis < lastView.getCalendar().getTimeInMillis() && childTimeMillis > clickView.getCalendar().getTimeInMillis())
          ) {
        child.setMode(Cell.MODE.MID);
      } else {
        child.setMode(Cell.MODE.DEFAULT);
      }
    }

    int offset = (lastView.getCalendar().getTime()).compareTo(clickView.getCalendar().getTime()) ;

    if (offset == 0) {
      lastView.setSelected(true);
    } else if (offset > 0) {
      lastView.setMode(Cell.MODE.END);
      clickView.setMode(Cell.MODE.START);
    } else {
      lastView.setMode(Cell.MODE.START);
      clickView.setMode(Cell.MODE.END);
    }

  }

  public void setCalendar(Calendar calendar) {
    mCalendar = calendar;
    dayCount = CalendarUtils.getDayCount(calendar);
    weekCount = CalendarUtils.getWeekCount(mCalendar);
    buildCell();
    requestLayout();
  }

  public void setListener(OnCellClickListener listener) {
    mListener = listener;
  }

  public interface OnCellClickListener {
    void onCellClick (View view,int position,Calendar calendar);
    void onRangSelect (Calendar calendar,Calendar endCalendar);
  }
}
