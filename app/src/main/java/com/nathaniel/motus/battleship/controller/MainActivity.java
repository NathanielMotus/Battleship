package com.nathaniel.motus.battleship.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.nathaniel.motus.battleship.R;
import com.nathaniel.motus.battleship.model.Fleet;
import com.nathaniel.motus.battleship.model.Ship;
import com.nathaniel.motus.battleship.model.ShootMap;
import com.nathaniel.motus.battleship.model.ShootSpot;
import com.nathaniel.motus.battleship.view.MapView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

//    **********************************************************************************************
//    Test objects
//    **********************************************************************************************
    MapView testMapView;
    MapView testAdversaryMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testMapView=findViewById(R.id.testmapview);
        testAdversaryMapView=findViewById(R.id.adversarytestmapview);



        test();
    }



//    **********************************************************************************************
//    Test methods
//    **********************************************************************************************

    private void test() {

        ShootMap map1=new ShootMap(10,10);
        testMapView.setMap(map1);
        map1.getShootSpots(2,2).setStatus(ShootSpot.ShootSpotStatus.MISS);
        map1.getShootSpots(3,5).setStatus(ShootSpot.ShootSpotStatus.HIT);
        map1.getShootSpots(9,9).setStatus(ShootSpot.ShootSpotStatus.SUNK);
        map1.getShootSpots(8,8).setStatus(ShootSpot.ShootSpotStatus.HIT);

        Fleet fleet=new Fleet(Arrays.asList(2,3,3,4,5),10,10);
        testAdversaryMapView.setFleet(fleet);

    }

    private void displayMap(ShootMap map) {
        Log.i("TEST",map.shootMapToString());
    }
}