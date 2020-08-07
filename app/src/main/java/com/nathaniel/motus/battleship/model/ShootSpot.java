package com.nathaniel.motus.battleship.model;

public class ShootSpot {

    public enum ShootSpotStatus{UNSHOT,MISS,HIT,SUNK}

    private ShootSpotStatus mStatus;

//    **********************************************************************************************
//    Constructors
//    **********************************************************************************************

    public ShootSpot() {
        mStatus=ShootSpotStatus.UNSHOT;
    }

//    **********************************************************************************************
//    Getters and setters
//    **********************************************************************************************

    public ShootSpotStatus getStatus() {
        return mStatus;
    }

    public void setStatus(ShootSpotStatus status) {
        mStatus = status;
    }

//    **********************************************************************************************
//    Testers
//    **********************************************************************************************

    private boolean isUnshot() {
        //return true if this is not yet shot

        return mStatus.compareTo(ShootSpotStatus.UNSHOT)==0;
    }
}
