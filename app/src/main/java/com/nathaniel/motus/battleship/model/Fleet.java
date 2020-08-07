package com.nathaniel.motus.battleship.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fleet {

    private int mInitialShipNumber;
    private int mCurrentShipNumber;
    private ArrayList<Ship> mShips;

//    **********************************************************************************************
//    Constructors
//    **********************************************************************************************

    public Fleet(int initialShipNumber) {
        mInitialShipNumber = initialShipNumber;
        mCurrentShipNumber=initialShipNumber;
        mShips=new ArrayList<Ship>();
    }

    public Fleet(int initialShipNumber, ArrayList<Ship> ships) {
        mInitialShipNumber = initialShipNumber;
        mCurrentShipNumber=initialShipNumber;
        mShips = ships;
    }

    public Fleet(List<Integer> list, int dimX, int dimY) {
        //build a random fleet of ships which size is in list

        Ship currentShip;
        mShips=new ArrayList<Ship>();

        for (int i = 0; i < list.size(); i++) {
            currentShip=new Ship(list.get(i), Ship.Orientation.NORTH_SOUTH,Math.round((float)Math.random()*dimX),Math.round((float)Math.random()*dimY));
            for (int j=0;j<Math.round((float)Math.random()*4);j++) currentShip.rotate(Ship.Rotation.CLOCKWISE);
            currentShip.checkBoundaries(0,dimX-1,0,dimY-1);
            if (currentShip.isOverlapping(this)) i--;
            else this.addShip(currentShip);
        }
    }

//    **********************************************************************************************
//    Getters and setters
//    **********************************************************************************************

    public int getInitialShipNumber() {
        return mInitialShipNumber;
    }

    public void setInitialShipNumber(int initialShipNumber) {
        mInitialShipNumber = initialShipNumber;
    }

    public int getCurrentShipNumber() {
        return mCurrentShipNumber;
    }

    public void setCurrentShipNumber(int currentShipNumber) {
        mCurrentShipNumber = currentShipNumber;
    }

    public ArrayList<Ship> getShips() {
        return mShips;
    }

    public void setShips(ArrayList<Ship> ships) {
        mShips = ships;
    }

//    **********************************************************************************************
//    Testers
//    **********************************************************************************************

    public boolean isSunk() {
        //return true if all ships are sunk

        boolean b=true;

        for (Ship s:mShips) b=b&&s.isSunk();

        return b;
    }

    public ArrayList<Ship> overlappingShips() {
        //return the list of overlapping ships

        ArrayList<Ship> ovs=new ArrayList<>();

        for (Ship s1 : mShips) {
            for (Ship s2:mShips)
                if (s1.isOverlapping(s2)) ovs.add(s1);
        }

        return ovs;
    }

//    **********************************************************************************************
//    Modifiers
//    **********************************************************************************************

    public void addShip(Ship ship) {
        //add a ship to this fleet

        this.mShips.add(ship);
    }
}
