package com.computing.millenium.springers.livedeadcounter;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "Title";
    private static final String JSON_DATE ="Date";
    private static final String JSON_COMMENT = "Comment";
    private static final String JSON_VCD = "VCD";
    private static final String JSON_VIABILITY = "Viability";
    private static final String JSON_Q1LIVE = "Q1Live";
    private static final String JSON_Q1DEAD = "Q1Dead";
    private static final String JSON_Q2LIVE = "Q2Live";
    private static final String JSON_Q2DEAD = "Q2Dead";
    private static final String JSON_Q3LIVE = "Q3Live";
    private static final String JSON_Q3DEAD = "Q3Dead";
    private static final String JSON_Q4LIVE = "Q4Live";
    private static final String JSON_Q4DEAD = "Q4Dead";

    private QuadrantCount mQ1Count;
    private QuadrantCount mQ2Count;
    private QuadrantCount mQ3Count;
    private QuadrantCount mQ4Count;

    private double mViability;
    private double mViableCellDensity;

    private int mTrypanConcentration;

    public TotalCount(){
        //Generate unique identifier
        setId(UUID.randomUUID());
        mDate = new Date();
        mQ1Count = new QuadrantCount();
        mQ1Count.setActivated(true);
        mQ2Count = new QuadrantCount();
        mQ3Count = new QuadrantCount();
        mQ4Count = new QuadrantCount();

    }

    public TotalCount(JSONObject json)throws JSONException{
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE))
            mTitle = json.getString(JSON_TITLE);
        if (json.has(JSON_COMMENT))
            mComment = json.getString(JSON_COMMENT);
        try{
            Long date = Long.parseLong(json.getString(JSON_DATE));
            mDate = new Date(date);
            Log.d(TAG, "Loaded Date: " + mDate.toString());}
        catch (Exception e){
            Log.e(TAG, "Failed to load date: ", e);
        }
        //TODO: Extract activated quadrants
        mQ1Count = new QuadrantCount(json.getInt(JSON_Q1LIVE), json.getInt(JSON_Q1DEAD), true);
        mQ2Count = new QuadrantCount(json.getInt(JSON_Q2LIVE), json.getInt(JSON_Q2DEAD), true);
        mQ3Count = new QuadrantCount(json.getInt(JSON_Q3LIVE), json.getInt(JSON_Q3DEAD), true);
        mQ4Count = new QuadrantCount(json.getInt(JSON_Q4LIVE), json.getInt(JSON_Q4DEAD), true);
        mViability = json.getDouble(JSON_VIABILITY);
        mViableCellDensity = json.getDouble(JSON_VCD);

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
        //Check for all zero values and remove from count before calculation
        int activeIncluded = 0;
        for (QuadrantCount q:activeQuads){
            if (!(q.getLiveCount()==0 & q.getDeadCount()==0)){
                totalLiveCounts += q.getLiveCount();
                totalDeadCounts += q.getDeadCount();
                activeIncluded += 1;
            }
        }
        //Calculate viability and VCD using dilution in settings
        double Viability = (double) totalLiveCounts/ (totalLiveCounts + totalDeadCounts) * 100;
        double ViableCellDensity = (double) totalLiveCounts/activeIncluded
                * 10000  * (1 + (mTrypanConcentration/100f));
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

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_COMMENT, mComment);
        json.put(JSON_VCD, mViableCellDensity);
        json.put(JSON_VIABILITY, mViability);
        json.put(JSON_Q1DEAD, mQ1Count.getDeadCount());
        json.put(JSON_Q1LIVE, mQ1Count.getLiveCount());
        json.put(JSON_Q2DEAD, mQ2Count.getDeadCount());
        json.put(JSON_Q2LIVE, mQ2Count.getLiveCount());
        json.put(JSON_Q3DEAD, mQ3Count.getDeadCount());
        json.put(JSON_Q3LIVE, mQ3Count.getLiveCount());
        json.put(JSON_Q4DEAD, mQ4Count.getDeadCount());
        json.put(JSON_Q4LIVE, mQ4Count.getLiveCount());

        return json;
    }

    public int getTrypanConcentration() {
        return mTrypanConcentration;
    }

    public void setTrypanConcentration(int trypanConcentration) {
        mTrypanConcentration = trypanConcentration;
    }

    public String getDateString(Context context){
        String date = android.text.format.DateFormat.getMediumDateFormat(context).format(mDate)
                + ", "
                + android.text.format.DateFormat.getTimeFormat(context).format(mDate);
        return date;
    }
}
