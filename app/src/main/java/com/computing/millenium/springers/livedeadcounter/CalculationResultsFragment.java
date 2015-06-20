package com.computing.millenium.springers.livedeadcounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Mike on 6/19/2015.
 */
public class CalculationResultsFragment extends DialogFragment {
    public static final String EXTRA_VIABILITY =
            "com.computing.millenium.springers.livedeadcounter.viability";
    public static final String EXTRA_VCD =
            "com.computing.millenium.springers.livedeadcounter.VCD";

    private TotalCount mTotalCount;

    //TODO:Pass through results instead of object
    public static CalculationResultsFragment newInstance(double[] viabilityCounts){
        Bundle args = new Bundle();
        args.putDouble(EXTRA_VIABILITY, viabilityCounts[0]);
        args.putDouble(EXTRA_VCD, viabilityCounts[1]);
        CalculationResultsFragment fragment = new CalculationResultsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_results, null);
        //TODO: Check is these are null
        double viability = getArguments().getDouble(EXTRA_VIABILITY);
        double VCD = getArguments().getDouble(EXTRA_VCD);

        String calcText = getString(R.string.vcd_text);
        NumberFormat formatter = new DecimalFormat("0.##E0");
        TextView textView = (TextView) v.findViewById(R.id.calculate_dialog_text_view);
        textView.setText(String.format(calcText, formatter.format(VCD), viability));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.results_title)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
