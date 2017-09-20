package com.neopi.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author NeoPi
 * @Date 2017/09/20
 * @Description 请用一句话描述该类的左右
 */

public class CellData {

  public int year;
  public int month;
  public int day;

  public Cell.MODE selectMode = Cell.MODE.DEFAULT;

  public CellData() {}

  public CellData(Calendar calendar) {
    if (calendar != null) {
      this.year = calendar.get(Calendar.YEAR);
      this.month = calendar.get(Calendar.MONTH);
      this.day = calendar.get(Calendar.DAY_OF_MONTH);
    } else {
      throw new NullPointerException("param calendar can not be null");
    }
  }

  public CellData(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
    this.selectMode = Cell.MODE.DEFAULT;
  }

  public CellData(int year, int month, int day, Cell.MODE mode) {
    this.year = year;
    this.month = month;
    this.day = day;
    this.selectMode = mode;
  }

  public Calendar toCalendar() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);
    return calendar;
  }

  public long getTimeInMills() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);
    return calendar.getTimeInMillis();
  }

  public Date getDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);
    return calendar.getTime();
  }

  @Override public String toString() {
    return "CellData{"
        + "year="
        + year
        + ", month="
        + month
        + ", day="
        + day
        + ", selectMode="
        + selectMode
        + '}';
  }
}
