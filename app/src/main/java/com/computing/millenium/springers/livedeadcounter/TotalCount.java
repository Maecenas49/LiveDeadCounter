package com.computing.millenium.springers.livedeadcounter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mike on 6/18/2015.
 */
public class TotalCount {
    private UUID mId;
    private String mTitle;

    private QuadrantCount mQ1Count;
    private QuadrantCount mQ2Count;
    private QuadrantCount mQ3Count;
    private QuadrantCount mQ4Count;

    private float mViability;
    private float mViableCellDensity;

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
        if (mQ1Count.isActivated()) activeQauds.add(mQ1Count);
        if (mQ2Count.isActivated()) activeQauds.add(mQ2Count);
        if (mQ3Count.isActivated()) activeQauds.add(mQ3Count);
        if (mQ4Count.isActivated()) activeQauds.add(mQ4Count);

        return activeQauds;
    }

    public float[] calculate() {
        List<QuadrantCount> activeQuads = getActiveQuadrants();
        int numActiveQuads = activeQuads.size();
        int totalLiveCounts = 0;
        int totalDeadCounts = 0;
        for (QuadrantCount q:activeQuads){
            totalLiveCounts += q.getLiveCount();
            totalDeadCounts += q.getDeadCount();
        }
        //TODO: Use dilution in settings
        float Viability = (float) totalLiveCounts/ (totalLiveCounts + totalDeadCounts) * 100;
        float ViableCellDensity = (float) totalLiveCounts/numActiveQuads
                * 10000 * (1 + (10/ 100f));
        setViability(Viability);
        setViableCellDensity(ViableCellDensity);
        return new float[] {Viability, ViableCellDensity};
    }

    public float getViability() {
        return mViability;
    }

    public void setViability(float viability) {
        mViability = viability;
    }

    public float getViableCellDensity() {
        return mViableCellDensity;
    }

    public void setViableCellDensity(float viableCellDensity) {
        mViableCellDensity = viableCellDensity;
    }
}
