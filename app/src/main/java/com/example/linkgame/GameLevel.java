package com.example.linkgame;

public class GameLevel {
    /**
     * Game Level
     */
    private int level;

    /**
     * Game card num (The real number is going to be twice that)
     */
    private int cardNum;

    private int viewTime;

    /**
     * interval time
     */
    private int intervalTime;

    /**
     * Total time each level
     */
    private long totalTime;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getCardNum() {
        return cardNum;
    }

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    public int getViewTime() {
        return viewTime;
    }

    public void setViewTime(int viewTime) {
        this.viewTime = viewTime;
    }

    @Override
    public String toString() {
        return "GameLevel{" +
                "level=" + level +
                ", cardNum=" + cardNum +
                ", viewTime=" + viewTime +
                ", intervalTime=" + intervalTime +
                ", totalTime=" + totalTime +
                '}';
    }
}
