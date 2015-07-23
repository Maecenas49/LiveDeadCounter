package com.computing.millenium.springers.livedeadcounter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.UUID;

public class LiveDeadCounterFragment extends Fragment {

    private static final int REQUEST_CALC = 1;
    private static final int REQUEST_CONFIRM_CLEAR = 2;
    //Implement total counts class
    private int mConcentration;
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

    private boolean mSoundOn;
    private boolean mDeadSoundLoaded;
    private boolean mLiveSoundLoaded;

    private TotalCount mTotalCount;

    private SoundPool mSoundPool;

    private static final String DIALOG_RESULTS = "results";
    private static final String CONFIRM_CLEAR_DIALOG = "clear";
    private final String TAG = "LiveDeadCounterFragment";

    public LiveDeadCounterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferenceManager = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getBaseContext());
        mConcentration = Integer.parseInt(preferenceManager.getString("Concentration", "10"));
        mSoundOn = preferenceManager.getBoolean("Sound Enabled", true);

//        Log.d(TAG, "Retrieved Sound Option: " + String.valueOf(mSoundOn));
//        Log.d(TAG, "Retrieved Concentration as " + Integer.toString(mConcentration));

        if (savedInstanceState == null) {
            mQ1DeadCount = 0;
            mQ1LiveCount = 0;
            mTotalCount = new TotalCount();
//        Log.d(TAG, "onCreateView() called");
            mTotalCount.getQ1Count().setActivated(true);
            mTotalCount.setTrypanConcentration(mConcentration);

        }

        View v = inflater.inflate(R.layout.fragment_live_dead_counter, container, false);

        //Get Textviews associated with counts and set to initial count
        mLiveCountTextView = (TextView) v.findViewById(R.id.liveTextView);
        mDeadCountTextView = (TextView) v.findViewById(R.id.deadTextView);
        mLiveCountTextView.setText(Integer.toString(mQ1LiveCount));
        mDeadCountTextView.setText(Integer.toString(mQ1DeadCount));

        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mSoundPool = getSoundPool();
        final int liveSoundID = mSoundPool
                .load(getActivity(), R.raw.multimedia_button_click_014, 1);
        final int deadSoundID = mSoundPool
                .load(getActivity(), R.raw.multimedia_button_click_028, 1);
        mLiveSoundLoaded = false;
        mDeadSoundLoaded = true;
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (sampleId == deadSoundID){
                    mDeadSoundLoaded = true;
                }
                if (sampleId == liveSoundID){
                    mLiveSoundLoaded = true;
                }
            }
        });

        //Set live counter button with haptic feedback and calculation update on each click
        mLiveCounter = (Button) v.findViewById(R.id.liveButton);
        mLiveCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if (mSoundOn){
                    playSound(mSoundPool, liveSoundID, mLiveSoundLoaded);
                }
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
                if (mSoundOn){
                    playSound(mSoundPool, deadSoundID, mDeadSoundLoaded);
                }
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

        clearAll();

        return v;
    }

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
//        Log.d(TAG, "onCreate() called");
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
//        Log.d(TAG, "OptionsItemSelected ID:" + Integer.toString(item.getItemId()));
        switch (item.getItemId()){
            case R.id.clear_all_settings:
                FragmentManager fm = getActivity().getFragmentManager();
                ConfirmClearDialog dialog = new ConfirmClearDialog();
                dialog.setTargetFragment(LiveDeadCounterFragment.this, REQUEST_CONFIRM_CLEAR);
                dialog.show(fm, CONFIRM_CLEAR_DIALOG);
                return true;
            case R.id.counts_list_settings:
                Intent listIntent = new Intent(getActivity(), CountsListActivity.class);
                startActivity(listIntent);
                return true;
            case R.id.help_settings:
                Intent helpIntent = new Intent(getActivity(), HelpActivity.class);
                startActivity(helpIntent);
                return true;
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
        if(requestCode == REQUEST_CONFIRM_CLEAR){
            //Clear all quadrants and reset to quadrant 1
            clearAll();
            updateLiveDeadText();
        }
    }

    private void updateLiveDeadText(){
        QuadrantCount activeCount = getActiveQuadrant();
        mDeadCountTextView.setText(Integer.toString(activeCount.getDeadCount()));
        mLiveCountTextView.setText(Integer.toString(activeCount.getLiveCount()));
    }

    @Override
    public void onDestroy() {
//        Log.d(TAG, "OnDestroy() called");
        super.onDestroy();

    }

    @Override
    public void onResume() {
//        Log.d(TAG, "OnResume() called");
        mTotalCount = new TotalCount();
        clearAll();
        super.onResume();
    }

    @Override
    public void onPause() {
//        Log.d(TAG, "onPause() called");
        super.onPause();
    }

    public SoundPool getSoundPool(){
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_NOTIFICATION, 0);
        }
        return soundPool;
    }

    private void playSound(SoundPool soundPool, int soundID, boolean loaded){
        AudioManager audioManager = (AudioManager)
                getActivity().getSystemService(getActivity().AUDIO_SERVICE);
        final float volume = ((float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) /
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // Is the sound loaded already?
        if (loaded) {
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
    }
    }

    private void clearAll(){
        mQ1Button.setChecked(true);
        mQ2Button.setTextColor(getResources().getColor(R.color.quadButtonInactiveText));
        mQ3Button.setTextColor(getResources().getColor(R.color.quadButtonInactiveText));
        mQ4Button.setTextColor(getResources().getColor(R.color.quadButtonInactiveText));
        mTotalCount.resetCounts();
        updateLiveDeadText();
    }
}
