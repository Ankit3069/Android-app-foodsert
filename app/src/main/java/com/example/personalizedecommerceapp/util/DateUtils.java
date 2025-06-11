package com.example.personalizedecommerceapp.util;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

    public static String[] currentDateTime() {

        Date date = new Date();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String currentDateTime = dateTimeFormat.format(date);
        String currentMonth = monthFormat.format(date);
        String currentYear = yearFormat.format(date);
        String currentDate = dateFormat.format(date);
        String currentTime = timeFormat.format(date);

        return new String[]{currentDateTime, currentMonth, currentYear, currentDate, currentTime};
    }

    public static boolean dateValidation(String startDate, String endDate) {

        try {
            String pattern = "yyyy/MM/dd";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date startDateFormat = sdf.parse(startDate);
            Date closeDateFormat = sdf.parse(endDate);
            assert startDateFormat != null;
            return startDateFormat.before(closeDateFormat) || startDateFormat.equals(closeDateFormat);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static boolean dateTimeValidation(String startDate, String startTime, String endDate,
                                             String endTime) {

        try {
            String pattern = "yyyy/MM/dd HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date startDateFormat = sdf.parse(startDate + " " + startTime);
            Date closeDateFormat = sdf.parse(endDate + " " + endTime);
            assert startDateFormat != null;
            if (startDateFormat.before(closeDateFormat)) {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }


    public static void openDatePickerDialog(Context context, EditText editText) {
        try {

            Calendar mCurrentDate = Calendar.getInstance();
            int calendarDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
            int calendarMonth = mCurrentDate.get(Calendar.MONTH);
            int calendarYear = mCurrentDate.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                month = month + 1;
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String monthString = String.valueOf(month);
                        if (monthString.length() == 1) {
                            monthString = "0" + monthString;
                        }
                        String dayString = String.valueOf(dayOfMonth);

                        if (dayString.length() == 1) {
                            dayString = "0" + dayString;
                        }
                        String date = year + "/" + monthString + "/" + dayString;

                        editText.setText(date);
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }, calendarYear, calendarMonth, calendarDay);
//            datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - MILLIS_IN_A_DAY);
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
            datePickerDialog.show();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void openTimePickerDialog(Context context, EditText editText) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(context,
                (timePicker, selectedHour, selectedMinute) -> {
                    String timeString = selectedHour + ":" + selectedMinute;
                    editText.setText(timeString);
                }, hour, minute, true);
        timePickerDialog.setTitle("Select Time 24hr only");
        timePickerDialog.show();
    }
}
