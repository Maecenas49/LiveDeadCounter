package com.computing.millenium.springers.livedeadcounter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;

/**
 * Created by Mike on 6/21/2015.
 */
public class TotalCountDetailsFragment extends Fragment {
    public static final String EXTRA_ID =
            "com.computing.millenium.springers.livedeadcounter.count_id";

    private TotalCount mTotalCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_total_count_details, container, false);
        double VCD = mTotalCount.getViableCellDensity();
        double viability = mTotalCount.getViability();

        TextView countDetailsText = (TextView)v.findViewById(R.id.total_count_vcd_text);
        String calcText = getString(R.string.vcd_text);
        NumberFormat formatter = new DecimalFormat("0.##E0");
        countDetailsText.setText(String.format(calcText, formatter.format(VCD), viability));

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID countId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_ID);
        mTotalCount = TotalCountSingleton.get(getActivity()).getTotalCount(countId);

    }
}
