package com.computing.millenium.springers.livedeadcounter;

/**
 * Created by Mike on 6/18/2015.
 */
public class QuadrantCount {
    private boolean mActivated;
    private int mLiveCount;
    private int mDeadCount;

    public QuadrantCount(){
        setActivated(false);
    }

    public boolean isActivated() {
        return mActivated;
    }

    public void setActivated(boolean activated) {
        mActivated = activated;
    }

    public int getLiveCount() {
        return mLiveCount;
    }

    public void setLiveCount(int liveCount) {
        mLiveCount = liveCount;
    }

    public int getDeadCount() {
        return mDeadCount;
    }

    public void setDeadCount(int deadCount) {
        mDeadCount = deadCount;
    }

    public void incrementLiveCount(){
        if (mLiveCount == 999) mLiveCount = 0;
        else mLiveCount++;
    }

    public void incrementDeadCount(){
        mDeadCount++;
    }

    public void reset(){
        setDeadCount(0);
        setLiveCount(0);
    }
}
