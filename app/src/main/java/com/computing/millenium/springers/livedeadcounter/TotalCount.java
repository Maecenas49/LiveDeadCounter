package com.computing.millenium.springers.livedeadcounter;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mike on 6/18/2015.
 */
public class TotalCount {
    private static final String TAG = "TotalCountClass";
    private UUID mId;
    private String mTitle;
    private String mComment;
    private Date mDate;

    private QuadrantCount mQ1Count;
    private QuadrantCount mQ2Count;
    private QuadrantCount mQ3Count;
    private QuadrantCount mQ4Count;

    private double mViability;
    private double mViableCellDensity;

    public TotalCount(){
        //Generate unique identifier
        setId(UUID.randomUUID());
        mQ1Count = new QuadrantCount();
        mQ1Count.setActivated(true);
        mQ2Count = new QuadrantCount();
        mQ3Count = new QuadrantCount();
        mQ4Count = new QuadrantCount();

    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public QuadrantCount getQ1Count() {
        return mQ1Count;
    }

    public QuadrantCount getQ2Count() {
        return mQ2Count;
    }

    public QuadrantCount getQ3Count() {
        return mQ3Count;
    }

    public QuadrantCount getQ4Count() {
        return mQ4Count;
    }

    public void resetCounts(){
        mQ1Count.setLiveCount(0);
        mQ1Count.setDeadCount(0);
        mQ2Count.setLiveCount(0);
        mQ2Count.setDeadCount(0);
        mQ2Count.setActivated(false);
        mQ3Count.setLiveCount(0);
        mQ3Count.setDeadCount(0);
        mQ3Count.setActivated(false);
        mQ4Count.setLiveCount(0);
        mQ4Count.setDeadCount(0);
        mQ4Count.setActivated(false);
    }

    public List<QuadrantCount> getActiveQuadrants(){
        List <QuadrantCount> activeQauds = new ArrayList<QuadrantCount>();
        if (mQ1Count.isActivated()) {
            activeQauds.add(mQ1Count);
            //Log.d(TAG, "Total Count Q1 is active");
        }
        if (mQ2Count.isActivated()) {
            activeQauds.add(mQ2Count);
            //Log.d(TAG, "Total Count Q2 is active");
        }
        if (mQ3Count.isActivated()) {
            activeQauds.add(mQ3Count);
            //Log.d(TAG, "Total Count Q3 is active");
        }
        if (mQ4Count.isActivated()) {
            activeQauds.add(mQ4Count);
            //Log.d(TAG, "Total Count Q4 is active");
        }

        return activeQauds;
    }

    public double[] calculate() {
        List<QuadrantCount> activeQuads = getActiveQuadrants();
        int numActiveQuads = activeQuads.size();
        int totalLiveCounts = 0;
        int totalDeadCounts = 0;
        //TODO: Check for all zero values before calculation
        for (QuadrantCount q:activeQuads){
            totalLiveCounts += q.getLiveCount();
            totalDeadCounts += q.getDeadCount();
        }
        //TODO: Use dilution in settings
        double Viability = (double) totalLiveCounts/ (totalLiveCounts + totalDeadCounts) * 100;
        double ViableCellDensity = (double) totalLiveCounts/numActiveQuads
                * 10000;
                //* (1 + (10/100f));
        setViability(Viability);
        setViableCellDensity(ViableCellDensity);
        return new double[] {Viability, ViableCellDensity};
    }

    public double getViability() {
        return mViability;
    }

    public void setViability(double viability) {
        mViability = viability;
    }

    public double getViableCellDensity() {
        return mViableCellDensity;
    }

    public void setViableCellDensity(double viableCellDensity) {
        mViableCellDensity = viableCellDensity;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }
}
