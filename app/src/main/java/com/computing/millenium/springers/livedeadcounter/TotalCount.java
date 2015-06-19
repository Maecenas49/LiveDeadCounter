package com.computing.millenium.springers.livedeadcounter;

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

    public TotalCount(){
        //Generate unique identifier
        setId(UUID.randomUUID());
        mQ1Count = new QuadrantCount();
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


}
