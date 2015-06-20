package com.computing.millenium.springers.livedeadcounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Mike on 6/19/2015.
 */
public class CalculationResultsFragment extends DialogFragment {
    public static final String EXTRA_COUNT =
            "com.computing.millenium.springers.livedeadcounter.count";

    private TotalCount mTotalCount;

    //TODO:Pass through results instead of object
    public static CalculationResultsFragment newInstance(TotalCount totalCount){
        Bundle args = new Bundle();
        return new CalculationResultsFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_results, null);

        String calcText = getString(R.string.vcd_text);
        NumberFormat formatter = new DecimalFormat("0.##E0");
        //calcView.setText(String.format(calcText, formatter.format(mViableCellDensity), mViability));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.results_title)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
