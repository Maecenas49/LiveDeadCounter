package com.computing.millenium.springers.livedeadcounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Mike on 7/7/2015.
 */
public class DateTimePickerFragment extends DialogFragment {
    public static final String DATE_EXTRA =
            "com.computing.millenium.springers.livedeadcounter.date";
    private Date mDate;

    //Pass through results instead of object
    public static DateTimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(DATE_EXTRA, date);
        DateTimePickerFragment fragment = new DateTimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date_time, null);
        mDate = (Date)getArguments().getSerializable(DATE_EXTRA);

        //Create a Calendar to get the year, month, day, and time
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        final DatePicker datePicker = (DatePicker)v.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker)v.findViewById(R.id.time_picker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth,
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute()).getTime();

                getArguments().putSerializable(DATE_EXTRA, mDate);
            }
        });

        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mDate = new GregorianCalendar(
                        datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                        hourOfDay, minute).getTime();
                getArguments().putSerializable(DATE_EXTRA, mDate);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_time_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .setView(v)
                .create();

    }
}
