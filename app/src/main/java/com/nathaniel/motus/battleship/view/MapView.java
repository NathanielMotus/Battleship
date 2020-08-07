package com.nathaniel.motus.battleship.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.nathaniel.motus.battleship.R;
import com.nathaniel.motus.battleship.model.Fleet;
import com.nathaniel.motus.battleship.model.Ship;
import com.nathaniel.motus.battleship.model.ShootMap;
import com.nathaniel.motus.battleship.model.ShootSpot;

public class MapView extends View implements View.OnClickListener, View.OnTouchListener {

    private ShootMap mMap;
    private Fleet mFleet;
    private Ship mSelectedShip;
    private int mDimX;
    private int mDimY;
    private int mSpotSize=-1;
    private Paint mPaintPlain;
    private Paint mPaintLine;
    private int mLastTouchX =0;
    private int mLastTouchY =0;
    private int mDeltaX=0;
    private int mDeltaY=0;
    private boolean shipHasMoved=false;

    public enum GamePhase{SETUP,PLAY,NEUTRAL}
    private GamePhase mGamePhase;


//    thickness of a ship, in spotSize
    private static final float SHIP_THICKNESS=0.75f;

//    thickness of a shot, in spotSize
    private static final float SHOT_THICKNESS=0.5f;

//    **********************************************************************************************
//    Constructors
//    **********************************************************************************************

    public MapView(Context context) {
        super(context);

        init(-1,-1);
    }

    public MapView(Context context, int dimX, int dimY) {
        super(context);

        init(dimX,dimY);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

//    **********************************************************************************************
//    Getters and setters
//    **********************************************************************************************

    public int getSpotSize() {
        return mSpotSize;
    }

    public void setMap(ShootMap map) {
        mMap = map;
    }

    public void setFleet(Fleet fleet) {
        mFleet = fleet;
    }

    public void setGamePhase(GamePhase phase) {
        mGamePhase =phase;
    }

//    **********************************************************************************************
//    Initializers
//    **********************************************************************************************

    private void init(int dimX, int dimY) {

        if (dimX==-1) mDimX=10;
        else mDimX=dimX;

        if (dimY==-1) mDimY=10;
        else mDimY=dimY;

        mMap=new ShootMap(mDimX,mDimY);

        mFleet=new Fleet(5);

        mSelectedShip=null;

        mPaintPlain=new Paint();
        mPaintPlain.setColor(Color.BLUE);

        mPaintLine=new Paint();
        mPaintLine.setColor(Color.WHITE);

        mGamePhase=GamePhase.SETUP;

        setOnClickListener(this);
        setOnTouchListener(this);

    }

    private void init(AttributeSet attributeSet) {

        TypedArray attr=getContext().obtainStyledAttributes(attributeSet, R.styleable.MapView);
        int dimX=attr.getInt(R.styleable.MapView_dimX,-1);
        int dimY=attr.getInt(R.styleable.MapView_dimY,-1);

        init(dimX,dimY);
    }

//    **********************************************************************************************
//    Modifiers
//    **********************************************************************************************

    public void update() {
        this.invalidate();
    }

//    **********************************************************************************************
//    Dimensions calculators
//    **********************************************************************************************

    private int shootSpotSize(int spec, int screenDim,int dim) {
        //calculate the size of a shootSpot in a direction

        int mode=MeasureSpec.getMode(spec);
        int size=MeasureSpec.getSize(spec);

        if (mode == MeasureSpec.UNSPECIFIED)
            return screenDim/dim;
        else return size/dim;
    }

//    **********************************************************************************************
//    Events
//    **********************************************************************************************

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        DisplayMetrics metrics=getContext().getResources().getDisplayMetrics();
        int screenWidth=metrics.widthPixels;
        int screenHeight=metrics.heightPixels;

        int shootSpotSizeX=shootSpotSize(widthMeasureSpec,screenWidth,mDimX);
        int shootSpotSizeY=shootSpotSize(heightMeasureSpec,screenHeight,mDimY);

        mSpotSize=Math.min(shootSpotSizeX,shootSpotSizeY);

        setMeasuredDimension(mSpotSize*mDimX,mSpotSize*mDimY);
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        draw the board
        drawBoard(canvas);

//        draw fleet
        drawShips(canvas);

//        draw the shots
        drawShots(canvas);
    }

//    **********************************************************************************************
//    Click management
//    **********************************************************************************************


    @Override
    public void onClick(View v) {
        //is used only for rotation

        if (mSelectedShip!=null && !shipHasMoved && mGamePhase==GamePhase.SETUP){
            mSelectedShip.rotate(Ship.Rotation.CLOCKWISE);
            mSelectedShip.checkBoundaries(0,mDimX-1,0,mDimY-1);
        }

        update();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

//        ------------------------------------------------------------------------------------------
//        Setup phase
//        ------------------------------------------------------------------------------------------

        if (mGamePhase == GamePhase.SETUP) {

//        First touch
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mLastTouchX = Math.round(event.getX());
                mLastTouchY = Math.round(event.getY());

                for (Ship s : mFleet.getShips())
                    if (s.isTouched(mLastTouchX, mLastTouchY, SHIP_THICKNESS, mSpotSize))
                        mSelectedShip = s;

                shipHasMoved = false;
                update();
            }

//        Drag
            if (event.getAction() == MotionEvent.ACTION_MOVE && mSelectedShip != null && mSelectedShip.isTouched(mLastTouchX, mLastTouchY, SHIP_THICKNESS, mSpotSize)) {
                mDeltaX = Math.round(event.getX()) - mLastTouchX;
                mDeltaY = Math.round(event.getY()) - mLastTouchY;
                update();
            }

//        Release
            if (event.getAction() == MotionEvent.ACTION_UP && mSelectedShip != null && mSelectedShip.isTouched(mLastTouchX, mLastTouchY, SHIP_THICKNESS, mSpotSize)) {

                mSelectedShip.move(Math.round((float) mDeltaX / (float) mSpotSize), Math.round((float) mDeltaY / (float) mSpotSize));
                mSelectedShip.checkBoundaries(0, mDimX - 1, 0, mDimY - 1);

                if (Math.abs(mDeltaX) <= mSpotSize / 4 && Math.abs(mDeltaY) <= mSpotSize / 4)
                    shipHasMoved = false;
                else
                    shipHasMoved = true;

                mDeltaX = 0;
                mDeltaY = 0;
                update();
            }
        }

//        ------------------------------------------------------------------------------------------
//        Play phase
//        ------------------------------------------------------------------------------------------

        if (mGamePhase == GamePhase.PLAY) {

        }

        return false;
    }

//    **********************************************************************************************
//    Drawing methods
//    **********************************************************************************************

    private void drawShots(Canvas canvas) {

        Paint paintMiss=new Paint();
        paintMiss.setStyle(Paint.Style.FILL);
        paintMiss.setColor(Color.CYAN);

        Paint paintHit=new Paint();
        paintHit.setStyle(Paint.Style.FILL);
        paintHit.setColor(Color.RED);

        Paint paintSunk=new Paint();
        paintSunk.setStyle(Paint.Style.FILL);
        paintSunk.setColor(Color.BLACK);

        Paint currentPaint=new Paint();

        for (int i = 0; i < mDimX; i++) {
            for (int j = 0; j < mDimY; j++) {
                if (mMap.getShootSpots(i,j).getStatus()!= ShootSpot.ShootSpotStatus.UNSHOT) {
                    switch (mMap.getShootSpots(i, j).getStatus()) {

                        case MISS:
                            currentPaint = paintMiss;
                            break;
                        case HIT:
                            currentPaint = paintHit;
                            break;
                        case SUNK:
                            currentPaint = paintSunk;
                            break;
                    }
                    canvas.drawCircle(i * mSpotSize + mSpotSize / 2, j * mSpotSize + mSpotSize / 2, mSpotSize*SHOT_THICKNESS/2, currentPaint);
                }
            }
        }
    }

    private void drawBoard(Canvas canvas) {

        int width=getWidth();
        int height=getHeight();

//        draw the background
        canvas.drawRect(0,0,width,height,mPaintPlain);

//        draw the lines
        for (int x = 0; x <= width; x = x + mSpotSize)
            canvas.drawLine(x,0,x,height,mPaintLine);

        for (int y=0;y<=height;y=y+mSpotSize)
            canvas.drawLine(0,y,width,y,mPaintLine);
    }

    private void drawShip(Canvas canvas, Ship ship, int deltaX, int deltaY) {

        Paint paintShip=new Paint();
        paintShip.setStyle(Paint.Style.FILL);
        paintShip.setColor(Color.GRAY);

        Paint paintSelectedShip=new Paint();
        paintSelectedShip.setStyle(Paint.Style.FILL);
        paintSelectedShip.setColor(Color.GREEN);

        Paint paintOverlappingShip=new Paint();
        paintOverlappingShip.setStyle(Paint.Style.FILL);
        paintOverlappingShip.setColor(Color.YELLOW);

        Paint currentPaint=paintShip;

        if (ship.isOverlapping(mFleet)) currentPaint=paintOverlappingShip;

        if (ship==mSelectedShip) currentPaint=paintSelectedShip;

        canvas.drawRect(ship.getXMin()*mSpotSize+mSpotSize*(1-SHIP_THICKNESS)/2+deltaX,
                ship.getYMin()*mSpotSize+mSpotSize*(1-SHIP_THICKNESS)/2+deltaY,
                ship.getXMax()*mSpotSize+mSpotSize*(1+SHIP_THICKNESS)/2+deltaX,
                ship.getYMax()*mSpotSize+mSpotSize*(1+SHIP_THICKNESS)/2+deltaY,
                currentPaint);
    }

    private void drawShips(Canvas canvas) {

        int deltaX=0;
        int deltaY=0;

        for (Ship s:mFleet.getShips()) {
            if (s == mSelectedShip) {
                deltaX=mDeltaX;
                deltaY=mDeltaY;
            } else {
                deltaX = 0;
                deltaY = 0;
            }
            drawShip(canvas, s, deltaX, deltaY);
        }
    }
}
