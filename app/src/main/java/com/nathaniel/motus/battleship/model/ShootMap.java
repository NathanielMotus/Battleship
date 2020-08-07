package com.nathaniel.motus.battleship.model;

public class ShootMap {

    private ShootSpot[][] mShootSpots;
    private int mDimX;
    private int mDimY;

//    **********************************************************************************************
//    Constructors
//    **********************************************************************************************

    public ShootMap(int dimX,int dimY) {

        mDimX=dimX;
        mDimY=dimY;
        mShootSpots=new ShootSpot[dimX][dimY];

        for (int i = 0; i < mDimX; i++) {
            for (int j = 0; j < mDimY; j++) {
                mShootSpots[i][j]=new ShootSpot();
            }
        }
    }

//    **********************************************************************************************
//    Getters and setters
//    **********************************************************************************************

    public ShootSpot getShootSpots(int i,int j) {
        return mShootSpots[i][j];
    }


//    **********************************************************************************************
//    Test methods
//    **********************************************************************************************

    public String shootMapToString() {

        String mapString="MAP\n";

        for (int j = 0; j < mDimY;j++) {
            mapString=mapString+"                                                  ";
            for (int i = 0; i < mDimX; i++) {
                switch (mShootSpots[i][j].getStatus()) {
                    case HIT:
                        mapString=mapString+"X";
                        break;
                    case MISS:
                        mapString=mapString+"#";
                        break;
                    case SUNK:
                        mapString=mapString+"+";
                        break;
                    default:
                        mapString=mapString+".";
                }
            }
            mapString=mapString+"\n";
        }
        return mapString;
    }

    public void addShipToMap(Ship ship) {

        for (ShipBlock b : ship.getShipBlocks()) {
            this.mShootSpots[b.getXPos()][b.getYPos()].setStatus(ShootSpot.ShootSpotStatus.HIT);
        }
    }


}
