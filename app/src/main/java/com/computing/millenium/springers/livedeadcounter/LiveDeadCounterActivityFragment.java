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
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class LiveDeadCounterActivityFragment extends Fragment {

    //TODO:Implement total counts class
    private int mDilution;
    private int mQ1LiveCount;
    private int mQ1DeadCount;

    private RadioButton mQ1Button;
    private RadioButton mQ2Button;
    private RadioButton mQ3Button;
    private RadioButton mQ4Button;

    private TotalCount mTotalCount;
    private float mViableCellDensity;
    private float mViability;

    private final String TAG = "LiveDeadCounterFragment";

    public LiveDeadCounterActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mQ1DeadCount = 0;
            mQ1LiveCount = 0;
            mTotalCount = new TotalCount();
            mTotalCount.getQ1Count().setActivated(true);
        }
        //TODO: Save dilution in settings
        mDilution = 10;
        View v = inflater.inflate(R.layout.fragment_live_dead_counter, container, false);

        final Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        //Get Textviews associated with counts and set to initial count
        final TextView liveCountTextView = (TextView) v.findViewById(R.id.liveTextView);
        final TextView deadCountTextView = (TextView) v.findViewById(R.id.deadTextView);
        liveCountTextView.setText(Integer.toString(mQ1LiveCount));
        deadCountTextView.setText(Integer.toString(mQ1DeadCount));

        //Set calculations results text view and update to current counts
        final TextView calculationTextView = (TextView) v.findViewById(R.id.calculation_text_view);
        calculateAndUpdate(calculationTextView);

        //Set live counter button with haptic feedback and calculation update on each click
        Button liveCounter = (Button) v.findViewById(R.id.liveButton);
        liveCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(20);
                QuadrantCount activeCount = getActiveQuadrant();
                if (activeCount != null){
                    activeCount.incrementLiveCount();
                }

                liveCountTextView.setText(Integer.toString(activeCount.getLiveCount()));
                calculateAndUpdate(calculationTextView);
            }
        });

        //Set dead counter button with haptic feedback and calculation update on each click
        Button deadCounter = (Button) v.findViewById(R.id.deadButton);
        deadCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(20);
                QuadrantCount activeCount = getActiveQuadrant();
                if (activeCount != null){
                    activeCount.incrementDeadCount();
                }
                deadCountTextView.setText(Integer.toString(activeCount.getDeadCount()));
                calculateAndUpdate(calculationTextView);
            }
        });

        //Onclicklistener for all quadrant buttons that changes the text to activate each quadrant
        //and activates the specified quadrant in total count
        View.OnClickListener quadClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(20);
                Button b = (Button)v;
                b.setTextColor(getResources().getColor(R.color.quadButtonActiveText));
                switch (v.getId()){
                    case R.id.quadrant_one_button:
                        QuadrantCount Q1Count = mTotalCount.getQ1Count();
                        Q1Count.setActivated(true);
                    case R.id.quadrant_two_button:
                        QuadrantCount Q2Count = mTotalCount.getQ2Count();
                        Q2Count.setActivated(true);
                    case R.id.quadrant_three_button:
                        QuadrantCount Q3Count = mTotalCount.getQ3Count();
                        Q3Count.setActivated(true);
                    case R.id.quadrant_four_button:
                        QuadrantCount Q4Count = mTotalCount.getQ4Count();
                        Q4Count.setActivated(true);
                }
                QuadrantCount activeCount = getActiveQuadrant();
                deadCountTextView.setText(Integer.toString(activeCount.getDeadCount()));
                liveCountTextView.setText(Integer.toString(activeCount.getLiveCount()));
            }
        };

        //Add click listener and update active quadrant
        mQ1Button = (RadioButton) v.findViewById(R.id.quadrant_one_button);
        mQ1Button.setTextColor(getResources().getColor(R.color.quadButtonActiveText));
        mQ1Button.setOnClickListener(quadClickListener);
        mQ2Button = (RadioButton) v.findViewById(R.id.quadrant_two_button);
        mQ2Button.setOnClickListener(quadClickListener);
        mQ3Button = (RadioButton) v.findViewById(R.id.quadrant_three_button);
        mQ3Button.setOnClickListener(quadClickListener);
        mQ4Button = (RadioButton) v.findViewById(R.id.quadrant_four_button);
        mQ4Button.setOnClickListener(quadClickListener);

        return v;
    }


    private void calculateAndUpdate(TextView calcView) {
        calculate();
        String calcText = getString(R.string.vcd_text);
        NumberFormat formatter = new DecimalFormat("0.##E0");

        calcView.setText(String.format(calcText, formatter.format(mViableCellDensity), mViability));
    }

    private void calculate() {
        if (mQ1DeadCount == 0 & mQ1LiveCount == 0) {
            mViability = 0;
            mViableCellDensity = 0;
        } else {
            mViability = (float) mQ1LiveCount / (mQ1LiveCount + mQ1DeadCount) * 100;
            mViableCellDensity = (float) mQ1LiveCount * 10000 * (1 + (mDilution / 100f));
        }
    }

    private QuadrantCount getActiveQuadrant(){
        if (mQ1Button.isChecked()) return mTotalCount.getQ1Count();
        if (mQ2Button.isChecked()) return mTotalCount.getQ2Count();
        if (mQ3Button.isChecked()) return mTotalCount.getQ3Count();
        if (mQ4Button.isChecked()) return mTotalCount.getQ4Count();
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
