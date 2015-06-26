package com.computing.millenium.springers.livedeadcounter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;

public class LiveDeadCounterFragment extends Fragment {

    private static final int REQUEST_CALC = 1;
    //Implement total counts class
    private int mDilution;
    private int mQ1LiveCount;
    private int mQ1DeadCount;

    private RadioButton mQ1Button;
    private RadioButton mQ2Button;
    private RadioButton mQ3Button;
    private RadioButton mQ4Button;

    private Button mLiveCounter;
    private Button mDeadCounter;
    private Button mResetButton;
    private Button mCalculateButton;

    private TextView mDeadCountTextView;
    private TextView mLiveCountTextView;

    private TotalCount mTotalCount;
    private float mViableCellDensity;
    private float mViability;

    private static final String DIALOG_RESULTS = "results";
    private final String TAG = "LiveDeadCounterFragment";

    public LiveDeadCounterFragment() {
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
        mLiveCountTextView = (TextView) v.findViewById(R.id.liveTextView);
        mDeadCountTextView = (TextView) v.findViewById(R.id.deadTextView);
        mLiveCountTextView.setText(Integer.toString(mQ1LiveCount));
        mDeadCountTextView.setText(Integer.toString(mQ1DeadCount));

        //Set live counter button with haptic feedback and calculation update on each click
        mLiveCounter = (Button) v.findViewById(R.id.liveButton);
        mLiveCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                QuadrantCount activeCount = getActiveQuadrant();
                if (activeCount != null) {
                    activeCount.incrementLiveCount();
                }
                updateLiveDeadText();
            }
        });

        //Set dead counter button with haptic feedback and calculation update on each click
        mDeadCounter = (Button) v.findViewById(R.id.deadButton);
        mDeadCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                QuadrantCount activeCount = getActiveQuadrant();
                if (activeCount != null) {
                    activeCount.incrementDeadCount();
                }
                updateLiveDeadText();
            }
        });

        //Onclicklistener for all quadrant buttons that changes the text to activate each quadrant
        //and activates the specified quadrant in total count
        View.OnClickListener quadClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Button b = (Button)v;
                b.setTextColor(getResources().getColor(R.color.quadButtonActiveText));
                switch (b.getId()){
                    case R.id.quadrant_one_button:
                        QuadrantCount Q1Count = mTotalCount.getQ1Count();
                        Q1Count.setActivated(true);
                        //Log.d(TAG, "Q1 Activated");
                        break;
                    case R.id.quadrant_two_button:
                        QuadrantCount Q2Count = mTotalCount.getQ2Count();
                        Q2Count.setActivated(true);
                        //Log.d(TAG, "Q2 Activated");
                        break;
                    case R.id.quadrant_three_button:
                        QuadrantCount Q3Count = mTotalCount.getQ3Count();
                        Q3Count.setActivated(true);
                        //Log.d(TAG, "Q3 Activated");
                        break;
                    case R.id.quadrant_four_button:
                        QuadrantCount Q4Count = mTotalCount.getQ4Count();
                        Q4Count.setActivated(true);
                        //Log.d(TAG, "Q4 Activated");
                        break;
                }
                QuadrantCount activeCount = getActiveQuadrant();
                mDeadCountTextView.setText(Integer.toString(activeCount.getDeadCount()));
                mLiveCountTextView.setText(Integer.toString(activeCount.getLiveCount()));
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

        mResetButton = (Button)v.findViewById(R.id.resetButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                getActiveQuadrant().reset();
                updateLiveDeadText();
            }
        });

        mCalculateButton = (Button)v.findViewById(R.id.calculateButton);
        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                double[] viabilityCalc = mTotalCount.calculate();
                FragmentManager fm = getActivity().getFragmentManager();
                CalculationResultsFragment dialog = CalculationResultsFragment
                        .newInstance(viabilityCalc);
                dialog.setTargetFragment(LiveDeadCounterFragment.this, REQUEST_CALC);
                dialog.show(fm, DIALOG_RESULTS);
            }
        });

        return v;
    }

    /**
    private void calculateAndUpdate(TextView calcView) {
        //calculate();
        String calcText = getString(R.string.vcd_text);
        NumberFormat formatter = new DecimalFormat("0.##E0");

        calcView.setText(String.format(calcText, formatter.format(mViableCellDensity), mViability));
    }**/

    //Return which quadrant is currently active
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_live_dead_counter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.clear_all_settings:
                //Clear all quadrants and reset to quadrant 1
                mQ1Button.setChecked(true);
                mQ2Button.setTextColor(getResources().getColor(R.color.quadButtonInactiveText));
                mQ3Button.setTextColor(getResources().getColor(R.color.quadButtonInactiveText));
                mQ4Button.setTextColor(getResources().getColor(R.color.quadButtonInactiveText));
                mTotalCount.resetCounts();
                updateLiveDeadText();
                return true;
            case R.id.counts_list_settings:
                Intent intent = new Intent(getActivity(), CountsListActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_CALC){
            UUID id = mTotalCount.getId();
            TotalCountSingleton.get(getActivity()).addCount(mTotalCount);
            Intent intent = new Intent(getActivity(), TotalCountDetailsActivity.class);
            intent.putExtra(TotalCountDetailsFragment.EXTRA_ID, id);
            startActivity(intent);
        }
    }

    private void updateLiveDeadText(){
        QuadrantCount activeCount = getActiveQuadrant();
        mDeadCountTextView.setText(Integer.toString(activeCount.getDeadCount()));
        mLiveCountTextView.setText(Integer.toString(activeCount.getLiveCount()));
    }
}
