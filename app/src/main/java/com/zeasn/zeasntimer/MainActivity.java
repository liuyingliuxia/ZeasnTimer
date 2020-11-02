package com.zeasn.zeasntimer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import butterknife.BindView;
import butterknife.ButterKnife;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tpTime)
    TimePicker mTimePicker;

    @BindView(R.id.tvTime)
    TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setHour(9);
        mTimePicker.setMinute(0);
        mTimePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            int[] timeArray = offDutyTime(hourOfDay, minute);

            if (timeArray[0] == -1) tvTime.setText(getResources().getString(R.string.late));
            else
                tvTime.setText(String.format(getResources().getString(R.string.time), timeArray[0], addZero(timeArray[1])));
        });

        Resources systemResources = Resources.getSystem();
        int hourNumberPickedId = systemResources.getIdentifier("hour", "id", "android");
        int minuteNumberPickedId = systemResources.getIdentifier("minute", "id", "android");

        NumberPicker hourNumberPicker = (NumberPicker) mTimePicker.findViewById(hourNumberPickedId);
        NumberPicker minNumberPicker = (NumberPicker) mTimePicker.findViewById(minuteNumberPickedId);

        hourNumberPicker.setMinValue(8);
        hourNumberPicker.setMaxValue(10);

    }

    private int[] offDutyTime(int hour, int min) {
        int[] hourAndMin = {18, 0};
        if (hour == 8) {
            return hourAndMin;
        }
        if (hour == 9) {
            if (min == 0) hourAndMin[1] = 0;
            if (min > 0 && min < 30)
                hourAndMin[1] = min + 30;
            else if(min >=30 ) {
                hourAndMin[0] = 19;
                hourAndMin[1] = min - 30;
            }
            return hourAndMin;
        }
        if (hour == 10) {
            if (min == 0) {
                hourAndMin[0] = 19;
                hourAndMin[1] = 30;
            } else {
                hourAndMin[0] = -1;
            }
        }
        return hourAndMin;
    }

    private String addZero(int min) {
        if (min < 10)
            return "0" + min;
        else return min + "";
    }
}