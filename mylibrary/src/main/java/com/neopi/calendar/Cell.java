package com.neopi.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import java.util.Calendar;

/**
 * @Author NeoPi
 * @Date 2017/09/18
 * @Description 请用一句话描述该类的左右
 */

public class Cell extends View {

  private int dayTextColor;
  private int descTextColor;
  private int bgColor;
  private int bgSelectColor;
  private int bgPadding ; // 这个padding值是用于回执选中时背景画笔与边框的距离，不是drawText时的距离，本lib每个cell不设置任何padding处理

  private Paint dayPaint;
  private Paint descPaint;
  private Paint bgPaint;

  private Calendar mCalendar;
  private CellData mCellData;

  public Cell(Context context) {
    this(context, null);
  }

  public Cell(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public Cell(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    readValue(context, attrs, defStyleAttr);
    init(context, attrs, defStyleAttr);
  }

  private void readValue(Context context, AttributeSet attrs, int defStyleAttr) {

    TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Cell);

    dayTextColor = array.getColor(R.styleable.Cell_text_color,
        ContextCompat.getColor(context, R.color.default_text_color));
    descTextColor = array.getColor(R.styleable.Cell_desc_text_color,
        ContextCompat.getColor(context, R.color.default_text_color));
    bgColor = array.getColor(R.styleable.Cell_bg_color,
        ContextCompat.getColor(context, R.color.bg_color));
    bgSelectColor = array.getColor(R.styleable.Cell_bg_select_color,
        ContextCompat.getColor(context, R.color.bg_select_color));

    array.recycle();
  }

  private void init(Context context, AttributeSet attrs, int defStyleAttr) {

    dayPaint = new Paint();
    dayPaint.setAntiAlias(true);
    dayPaint.setColor(dayTextColor);
    dayPaint.setTextSize(30);

    descPaint = new Paint();
    descPaint.setAntiAlias(true);
    descPaint.setColor(descTextColor);
    descPaint.setTextSize(25);

    bgPaint = new Paint();
    bgPaint.setAntiAlias(true);
    bgPaint.setColor(bgColor);
    bgPaint.setStyle(Paint.Style.FILL);

    mCalendar = Calendar.getInstance();
    mCellData = new CellData(mCalendar);

  }

  public void setCalendar(Calendar calendar) {
    mCalendar = calendar;
    mCellData = new CellData(calendar) ;
    invalidate();
  }

  public void setCalendar(Calendar calendar,MODE mode) {
    mCalendar = calendar;
    mCellData = new CellData(calendar) ;
    mCellData.selectMode = mode ;
    invalidate();
  }

  public long getTimeInMillis () {
    return mCalendar.getTimeInMillis() ;
  }

  public CellData getCellData() {
    return mCellData;
  }

  @Override public void setSelected(boolean selected) {
    super.setSelected(selected);
    mCellData.selectMode = (selected ? MODE.SELECT : MODE.DEFAULT);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int height = MeasureSpec.getSize(heightMeasureSpec);

    bgPadding = Math.min(width,height) / 8 ;
    setMeasuredDimension(width, height) ;
  }

  @Override protected void onDraw(Canvas canvas) {

    if (mCellData == null) {
      return;
    }
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
    int radius = Math.min((width - 2 * bgPadding) / 2, (height - 2 * bgPadding) / 2) ;

    dayPaint.setColor(dayTextColor);
    if (mCellData.selectMode == MODE.START) {
      RectF startRectF = new RectF(bgPadding,bgPadding,width,height - bgPadding);
      bgPaint.setColor(bgSelectColor);
      bgPaint.setStyle(Paint.Style.FILL);
      canvas.drawArc(startRectF,90,180,true,bgPaint);
      canvas.drawRect(width / 2 + 1,bgPadding,width,height - bgPadding,bgPaint);
    } else if (mCellData.selectMode == MODE.MID) {
      bgPaint.setColor(bgSelectColor);
      bgPaint.setStyle(Paint.Style.FILL);
      canvas.drawRect(0,bgPadding,width,height - bgPadding,bgPaint);
    } else if (mCellData.selectMode == MODE.END) {
      RectF startRectF = new RectF(bgPadding,bgPadding,width - bgPadding,height - bgPadding);
      bgPaint.setColor(bgSelectColor);
      bgPaint.setStyle(Paint.Style.FILL);
      canvas.drawArc(startRectF,270,180,true,bgPaint);
      canvas.drawRect(0,bgPadding,width / 2 + 1,height - bgPadding,bgPaint);
    } else if (mCellData.selectMode == MODE.SELECT) {
      bgPaint.setStyle(Paint.Style.FILL);
      bgPaint.setColor(bgSelectColor);
      bgPaint.setStrokeWidth(1);

      dayPaint.setColor(Color.WHITE);
      canvas.drawCircle(width / 2, height / 2, radius, bgPaint);
    } else {
      Calendar instance = Calendar.getInstance();
      if (instance.get(Calendar.YEAR) == mCellData.year
          && instance.get(Calendar.DAY_OF_MONTH) == mCellData.day
          && instance.get(Calendar.MONTH) == mCellData.month) {
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(bgSelectColor);
        bgPaint.setStrokeWidth(1);
        canvas.drawCircle(width / 2, height / 2, radius, bgPaint);
      }
    }

    String day = mCellData.day + "";
    Rect bound = new Rect();
    dayPaint.getTextBounds(day, 0, day.length(), bound);
    canvas.drawText(day, (width - bound.width()) / 2, (height + bound.height()) / 2, dayPaint);
  }

  public void setCellData (CellData cellData) {
    mCellData = cellData;
    mCalendar = mCellData.toCalendar();
    invalidate();
  }

  public Calendar getCalendar() {
    return mCellData.toCalendar();
  }

  public void setMode(MODE mode) {
    mCellData.selectMode = mode ;
    invalidate();
  }

  public enum MODE {
    SELECT,START, END, MID, DEFAULT
  }
}
