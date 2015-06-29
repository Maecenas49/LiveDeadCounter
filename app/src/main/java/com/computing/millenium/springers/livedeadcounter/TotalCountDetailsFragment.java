package com.computing.millenium.springers.livedeadcounter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Mike on 6/21/2015.
 */
public class TotalCountDetailsFragment extends Fragment {
    public static final String EXTRA_ID =
            "com.computing.millenium.springers.livedeadcounter.count_id";
    private static final String TAG = "CountDetailsFragment";
    private static final String CONFIRM_DELETE_DIALOG = "delete";

    private static final int REQUEST_CONFIRM_DELETE = 1;

    private TotalCount mTotalCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_total_count_details, container, false);
        double VCD = mTotalCount.getViableCellDensity();
        double viability = mTotalCount.getViability();

        EditText titleText = (EditText)v.findViewById(R.id.total_count_title);
        String title = mTotalCount.getTitle();
        if (title != null){
            titleText.setText(title);
        }
        titleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTotalCount.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Intentionally left blank
            }
        });

        TextView countDetailsText = (TextView)v.findViewById(R.id.total_count_vcd_text);
        String calcText = getString(R.string.vcd_text);
        NumberFormat formatter = new DecimalFormat("0.##E0");
        countDetailsText.setText(String.format(calcText, formatter.format(VCD), viability));

        String quadText = getString(R.string.quadrant_count_text);
        TextView q1DetailsText = (TextView)v.findViewById(R.id.total_count_q1_text);
        q1DetailsText.setText(String.format(quadText, mTotalCount.getQ1Count().getLiveCount(),
                mTotalCount.getQ1Count().getDeadCount()));

        TextView q2DetailsText = (TextView)v.findViewById(R.id.total_count_q2_text);
        q2DetailsText.setText(String.format(quadText, mTotalCount.getQ2Count().getLiveCount(),
                mTotalCount.getQ2Count().getDeadCount()));

        TextView q3DetailsText = (TextView)v.findViewById(R.id.total_count_q3_text);
        q3DetailsText.setText(String.format(quadText, mTotalCount.getQ3Count().getLiveCount(),
                mTotalCount.getQ3Count().getDeadCount()));

        TextView q4DetailsText = (TextView)v.findViewById(R.id.total_count_q4_text);
        q4DetailsText.setText(String.format(quadText, mTotalCount.getQ4Count().getLiveCount(),
                mTotalCount.getQ4Count().getDeadCount()));

        Button dateButton = (Button)v.findViewById(R.id.total_count_date);
        Date date = mTotalCount.getDate();
        Log.d(TAG, "Retrieve date: " + date.toString());
        dateButton.setText(
                DateFormat.getMediumDateFormat(getActivity()).format(date) + "; "
        + DateFormat.getTimeFormat(getActivity()).format(date));

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID countId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_ID);
        mTotalCount = TotalCountSingleton.get(getActivity()).getTotalCount(countId);
        setRetainInstance(true);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_total_count_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_settings:
                FragmentManager fm = getActivity().getFragmentManager();
                ConfirmDeleteDialog dialog = new ConfirmDeleteDialog();
                dialog.setTargetFragment(TotalCountDetailsFragment.this, REQUEST_CONFIRM_DELETE);
                dialog.show(fm, CONFIRM_DELETE_DIALOG);
                return true;
            default: return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_CONFIRM_DELETE){
            TotalCountSingleton counts = TotalCountSingleton.get(getActivity());
            counts.deleteCount(mTotalCount);
            getActivity().finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        TotalCountSingleton.get(getActivity()).saveCounts();
    }
}
