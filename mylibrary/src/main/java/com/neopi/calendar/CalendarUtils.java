package com.neopi.calendar;

import java.util.Calendar;

/**
 * @Author NeoPi
 * @Date 2017/09/19
 * @Description 请用一句话描述该类的左右
 */

public class CalendarUtils {

  /**
   * 获取月份是天数
   * @param calendar
   * @return
   */
  public static int getDayCount (Calendar calendar) {
    int count = 30;
    switch (calendar.get(Calendar.MONTH)) {
      case Calendar.JANUARY:
      case Calendar.MARCH:
      case Calendar.MAY:
      case Calendar.JULY:
      case Calendar.AUGUST:
      case Calendar.OCTOBER:
      case Calendar.DECEMBER:
        count = 31;
        break ;
      case  Calendar.APRIL:
      case  Calendar.JUNE:
      case  Calendar.SEPTEMBER:
      case  Calendar.NOVEMBER:
        count = 30;
        break;
      case Calendar.FEBRUARY:
        if (isLeapYear(calendar.get(Calendar.YEAR))) {
          count = 29;
        } else {
          count = 28;
        }
        break;
    }

    return count ;
  }

  /**
   * 是否是闰年
   * @param year
   * @return
   */
  public static boolean isLeapYear(int year) {
    return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ;
  }

  /**
   * 获取当月中一号是第一周的第几天
   *
   * @param calendar
   * @return
   */
  public static int getFirstDayOfWeek(Calendar calendar) {
    calendar.set(Calendar.DAY_OF_MONTH,1);
    return calendar.get(Calendar.DAY_OF_WEEK) ;
  }

  /**
   * 获取本月有多少周
   *
   * @param calendar
   * @return
   */
  public static int getWeekCount(Calendar calendar) {
    return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
  }

  /**
   * 获取某一天在一周中是第几天
   * @param calendar
   * @return
   */
  public static int indexOfWeek (Calendar calendar) {
    return calendar.get(Calendar.DAY_OF_WEEK);
  }

  public static int weekOfMonth (Calendar calendar) {
    return calendar.get(Calendar.WEEK_OF_MONTH);
  }
}
