package com.computing.millenium.springers.livedeadcounter;

import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.zip.Inflater;

public class LiveDeadCounterActivityFragment extends Fragment {

    private int mDilution;
    private int mQ1LiveCount;
    private int mQ1DeadCount;

    private float mViableCellDensity;
    private float mViability;

    private final String TAG = "LiveDeadCounterFragment";

    public LiveDeadCounterActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: Maintain variables on savedInstanceState
        if (savedInstanceState == null){
            mQ1DeadCount = 0;
            mQ1LiveCount = 0;
        }
        //TODO: Save dilution in settings and maintain on rotation
        mDilution = 10;
        View v = inflater.inflate(R.layout.fragment_live_dead_counter,container, false);

        final Vibrator vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        //Get Textviews associated with counts and set to initial count
        final TextView liveCountTextView = (TextView)v.findViewById(R.id.liveTextView);
        final TextView deadCountTextView = (TextView)v.findViewById(R.id.deadTextView);
        liveCountTextView.setText(Integer.toString(mQ1LiveCount));
        deadCountTextView.setText(Integer.toString(mQ1DeadCount));

        //Set calculations results text view and update to current counts
        final TextView calculationTextView = (TextView)v.findViewById(R.id.calculation_text_view);
        calculateAndUpdate(calculationTextView);

        //Set live counter button with haptic feedback and calculation update on each click
        Button liveCounter = (Button)v.findViewById(R.id.liveButton);
        liveCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(20);
                if (mQ1LiveCount == 999) mQ1LiveCount = 0;
                else mQ1LiveCount++;
                liveCountTextView.setText(Integer.toString(mQ1LiveCount));
                calculateAndUpdate(calculationTextView);
            }
        });

        //Set dead counter button with haptic feedback and calculation update on each click
        Button deadCounter = (Button)v.findViewById(R.id.deadButton);
        deadCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(20);
                if (mQ1DeadCount == 999) mQ1DeadCount = 0;
                else mQ1DeadCount++;
                deadCountTextView.setText(Integer.toString(mQ1DeadCount));
                calculateAndUpdate(calculationTextView);
            }
        });

        View.OnClickListener quadClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(20);
            }
        };

        RadioGroup quadrantGroup = new RadioGroup(getActivity());
        //TODO: Add all buttons to a radiogroup
        RadioButton Q1Button = (RadioButton)v.findViewById(R.id.quadrant_one_button);
        RadioButton Q2Button = (RadioButton)v.findViewById(R.id.quadrant_two_button);
        RadioButton Q3Button = (RadioButton)v.findViewById(R.id.quadrant_three_button);
        RadioButton Q4Button = (RadioButton)v.findViewById(R.id.quadrant_four_button);

        return v;
    }

    private void calculateAndUpdate(TextView calcView){
        calculate();
        String calcText = getString(R.string.vcd_text);
        NumberFormat formatter = new DecimalFormat("0.##E0");

        calcView.setText(String.format(calcText, formatter.format(mViableCellDensity), mViability));
    }

    private void calculate(){
        if (mQ1DeadCount == 0 & mQ1LiveCount == 0){
            mViability = 0;
            mViableCellDensity = 0;
        } else{
        mViability = (float)mQ1LiveCount/(mQ1LiveCount + mQ1DeadCount)*100;
        mViableCellDensity = (float)mQ1LiveCount*10000*(1+(mDilution/100f));
        }
    }
}
