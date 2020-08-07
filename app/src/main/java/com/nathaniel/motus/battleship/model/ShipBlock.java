package com.nathaniel.motus.battleship.model;

public class ShipBlock {

    enum ShipBlockStatus {SAFE,DAMAGED,SUNK}

    private ShipBlockStatus mStatus;
    private int mXPos;
    private int mYPos;

    public ShipBlock(int xPos, int yPos) {
        this.mXPos = xPos;
        this.mYPos = yPos;
        this.mStatus= ShipBlockStatus.SAFE;
    }

    public ShipBlockStatus getStatus() {
        return mStatus;
    }

    public void setStatus(ShipBlockStatus status) {
        mStatus = status;
    }

    public int getXPos() {
        return mXPos;
    }

    public void setXPos(int XPos) {
        this.mXPos = XPos;
    }

    public int getYPos() {
        return mYPos;
    }

    public void setYPos(int YPos) {
        this.mYPos = YPos;
    }
}
