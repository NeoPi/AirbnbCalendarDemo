package com.neopi.airbnbcalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.neopi.calendar.SimpleMonthView;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


  SimpleMonthView mMonthView ;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mMonthView = (SimpleMonthView) findViewById(R.id.simple_month_view);

    Calendar calendar = Calendar.getInstance() ;
    calendar.roll(Calendar.MONTH,2);
    mMonthView.setCalendar(calendar);

  }

}
