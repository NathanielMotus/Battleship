package com.nathaniel.motus.battleship.model;

import android.util.Log;

import java.util.ArrayList;

public class Ship {

    public enum  ShipStatus{OPERATIONAL,SUNK}
    public enum Orientation{NORTH_SOUTH,SOUTH_NORTH,EAST_WEST,WEST_EAST}
    public enum Rotation{CLOCKWISE,COUNTER_CLOCKWISE}

    private int mSize;
    private ArrayList<ShipBlock> mShipBlocks;
    private ShipStatus mStatus;
    private Orientation mOrientation;
    private int mXPos;
    private int mYPos;

//    **********************************************************************************************
//    Constructors
//    **********************************************************************************************

    public Ship(int size, Orientation orientation, int xPos, int yPos) {
        mSize = size;
        mOrientation = orientation;
        mXPos = xPos;
        mYPos = yPos;

        mStatus=ShipStatus.OPERATIONAL;
        mShipBlocks=new ArrayList<ShipBlock>();

        for (int i=0;i<mSize;i++){
            switch (mOrientation){
                case EAST_WEST:
                    mShipBlocks.add(new ShipBlock(mXPos-i, mYPos));
                    break;
                case WEST_EAST:
                    mShipBlocks.add(new ShipBlock(mXPos+i, mYPos));
                    break;
                case NORTH_SOUTH:
                    mShipBlocks.add(new ShipBlock(mXPos, mYPos +i));
                    break;
                case SOUTH_NORTH:
                    mShipBlocks.add(new ShipBlock(mXPos, mYPos -i));
                    break;
            }
        }
    }

//    **********************************************************************************************
//    Getters and setters
//    **********************************************************************************************

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public ArrayList<ShipBlock> getShipBlocks() {
        return mShipBlocks;
    }

    public void setShipBlocks(ArrayList<ShipBlock> shipBlocks) {
        mShipBlocks = shipBlocks;
    }

    public ShipStatus getStatus() {
        return mStatus;
    }

    public void setStatus(ShipStatus status) {
        mStatus = status;
    }

    public Orientation getOrientation() {
        return mOrientation;
    }

    public void setOrientation(Orientation orientation) {
        mOrientation = orientation;
    }

    public int getXPos() {
        return mXPos;
    }

    public void setXPos(int XPos) {
        mXPos = XPos;
    }

    public int getYPos() {
        return mYPos;
    }

    public void setYPos(int YPos) {
        mYPos = YPos;
    }

    public int getXMin() {
        //return xmin among all bocks

        int xx=this.getXPos();

        for (ShipBlock b:this.getShipBlocks()) if (b.getXPos()<xx) xx=b.getXPos();

        return xx;
    }

    public int getXMax() {
        //return xmax amon all blocks

        int xx=this.getXPos();

        for (ShipBlock b:this.getShipBlocks()) if (b.getXPos()>xx) xx=b.getXPos();

        return xx;
    }

    public int getYMin() {
        //return ymin among all bocks

        int yy=this.getYPos();

        for (ShipBlock b:this.getShipBlocks()) if (b.getYPos()<yy) yy=b.getYPos();

        return yy;
    }

    public int getYMax() {
        //return ymax among all bocks

        int yy=this.getYPos();

        for (ShipBlock b:this.getShipBlocks()) if (b.getYPos()>yy) yy=b.getYPos();

        return yy;
    }



//    **********************************************************************************************
//    Testers
//    **********************************************************************************************

    public Boolean isSunk() {
        return this.mStatus.compareTo(ShipStatus.SUNK) == 0;
    }

    public boolean isOverlapping(Ship ship) {
        //return true if this and ship are overlapping, false otherwise
        //if this and ship are the same, return false

        if (this==ship) return false;

        else {

            boolean b = false;

            for (ShipBlock sb1 : this.mShipBlocks) {
                for (ShipBlock sb2 : ship.mShipBlocks)
                    b = b || (sb1.getXPos() == sb2.getXPos() &&
                            sb1.getYPos() == sb2.getYPos());
            }

            return b;
        }
    }

    public boolean isOverlapping(Fleet fleet) {
        //return true if this and at least one ship of fleet are overlapping

        boolean b=false;

        if (fleet.getShips().size()!=0)
            for (Ship s:fleet.getShips()) b=b||this.isOverlapping(s);

        return b;
    }

    public boolean isTouched(int touchX, int touchY, float shipThickness,int spotSize) {
        //return true if the coordinates touchX and touchY are in the graphical representation of the ship
        //spotSize is in px
        //shipThickness is in spotSize

        return  (touchX<=this.getXMax()*spotSize+spotSize*(1+shipThickness)/2 &&
        touchX>=this.getXMin()*spotSize+spotSize*(1-shipThickness)/2 &&
        touchY<=this.getYMax()*spotSize+spotSize*(1+shipThickness)/2 &&
        touchY>=this.getYMin()*spotSize+spotSize*(1-shipThickness)/2);
    }

//    **********************************************************************************************
//    Transformers
//    **********************************************************************************************

    public void rotate(@org.jetbrains.annotations.NotNull Rotation rotation){
        //rotate a boat 90° around the block closest to its center (by default)

        int cX=this.mShipBlocks.get((mSize-1)/2).getXPos();
        int cY=this.mShipBlocks.get((mSize-1)/2).getYPos();

        switch (rotation){
            case CLOCKWISE:
                for (ShipBlock b:mShipBlocks){
                    int x=b.getXPos();
                    int y=b.getYPos();
                    b.setXPos(cX-(y-cY));
                    b.setYPos(cY+(x-cX));
                }
                switch (mOrientation){
                    case SOUTH_NORTH:
                        mOrientation=Orientation.WEST_EAST;
                        break;
                    case NORTH_SOUTH:
                        mOrientation=Orientation.EAST_WEST;
                        break;
                    case WEST_EAST:
                        mOrientation=Orientation.NORTH_SOUTH;
                        break;
                    case EAST_WEST:
                        mOrientation=Orientation.SOUTH_NORTH;
                        break;
                }
                break;
            case COUNTER_CLOCKWISE:
                for (ShipBlock b:mShipBlocks){
                    int x=b.getXPos();
                    int y=b.getYPos();
                    b.setXPos(cX+(y-cY));
                    b.setYPos(cY-(x-cX));
                }
                switch (mOrientation){
                    case EAST_WEST:
                        mOrientation=Orientation.NORTH_SOUTH;
                        break;
                    case WEST_EAST:
                        mOrientation=Orientation.SOUTH_NORTH;
                        break;
                    case NORTH_SOUTH:
                        mOrientation=Orientation.WEST_EAST;
                        break;
                    case SOUTH_NORTH:
                        mOrientation=Orientation.EAST_WEST;
                        break;
                }
                break;
        }
        //redefine the position of the ship, which is the position of its first block
        this.mXPos=this.mShipBlocks.get(0).getXPos();
        this.mYPos =this.mShipBlocks.get(0).getYPos();
    }

    public void move(int deltaX,int deltaY){
        //move a ship from (X,Y) to (X+deltaX,Y+deltaY)

        //change ship coordinates first
        this.mXPos=mXPos+deltaX;
        this.mYPos = mYPos +deltaY;

        //change blocks coordinates
        for (ShipBlock b : mShipBlocks) {
            b.setXPos(b.getXPos()+deltaX);
            b.setYPos(b.getYPos()+deltaY);
        }
    }

    public void checkBoundaries(int xMin, int xMax, int yMin, int yMax) {
        //check if the ship is within boundaries
        //if not, move it back into them

        int deltaX=0;
        int deltaY=0;

        if (this.getXMin()<xMin) deltaX=xMin-this.getXMin();
        if (this.getXMax()>xMax) deltaX=xMax-this.getXMax();
        if (this.getYMin() <yMin) deltaY=yMin-this.getYMin();
        if (this.getYMax() >yMax) deltaY=yMax-this.getYMax();

        this.move(deltaX,deltaY);
    }

    public void checkStatus(){
        //check if the status needs to be changed an do so if need be

        this.mStatus=ShipStatus.SUNK;

        for (ShipBlock b : mShipBlocks) {
            if (b.getStatus().compareTo(ShipBlock.ShipBlockStatus.SAFE)==0) this.mStatus=ShipStatus.OPERATIONAL;
        }

        //if ship is sunk, change every blocks status to sunk too
        if (this.mStatus.compareTo(ShipStatus.SUNK) == 0) {
            for (ShipBlock b:mShipBlocks) b.setStatus(ShipBlock.ShipBlockStatus.SUNK);
        }

    }

}
