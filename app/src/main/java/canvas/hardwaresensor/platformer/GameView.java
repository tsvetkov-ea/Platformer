package canvas.hardwaresensor.platformer;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;

class GameView extends BackgroundView {

  private class GameObject  {

    //  object types
    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_WALL = 1;
    private static final int TYPE_PLATFORM = 2;
    private static final int TYPE_BALL = 3;
    private static final int TYPE_BLOCK = 4;
    private static final int TYPE_BOTTOM = 5;
    private static final int TYPE_TOP = 6;

    private static final int PLATFORM_LENGTH = 4;

    private final int mGameObjectType;

    private int mPlatform_LengthCounter;
    private int mPlatform_DirectionX;
    private boolean isPlatform;

    private int mBall_DirectionX = 1;
    private int mBall_DirectionY = -1;
    private boolean isBall;

    GameObject(int _type)  {
      mGameObjectType = _type;
    }

    private void computeNextPosition() {
      switch(mGameObjectType) {

        case GameObject.TYPE_PLATFORM:
          mPlatform_LengthCounter = 0;
          isPlatform = false;
          for(int _i = 0; _i < mXTileQuantity; _i++)
          {
            for(int _j = 0; _j < mYTileQuantity; _j++)
            {
              if (mTileGrid[_i][_j] == GameObject.TYPE_PLATFORM)
              {
                if(mPlatform_DirectionX != GameView.PLATFORM_DIRECTION_NONE) {
                  if  (
                      mTileGrid[_i + mPlatform_DirectionX][_j] != GameObject.TYPE_WALL &&
                      mTileGrid[_i + mPlatform_DirectionX][_j] != GameObject.TYPE_BALL &&
                      mTileGrid[_i + PLATFORM_LENGTH - 1 + mPlatform_DirectionX][_j] != GameObject.TYPE_WALL &&
                      mTileGrid[_i + PLATFORM_LENGTH - 1 + mPlatform_DirectionX][_j] != GameObject.TYPE_BALL
                      )
                  {
                    if(mPlatform_DirectionX == GameView.PLATFORM_DIRECTION_RIGHT)
                    {
                      mTileGrid[_i][_j] = GameObject.TYPE_EMPTY;
                    }

                    if(mPlatform_DirectionX == GameView.PLATFORM_DIRECTION_LEFT)
                    {
                      mTileGrid[_i + PLATFORM_LENGTH - 1][_j] = GameObject.TYPE_EMPTY;
                    }

                    for(int _ii = _i; _ii - _i < PLATFORM_LENGTH; _ii++)
                    {
                      mPlatform_LengthCounter = mPlatform_LengthCounter + 1;
                      mTileGrid[_ii + mPlatform_DirectionX][_j] = GameObject.TYPE_PLATFORM;
                    }
                  }
                }
                mPlatform_DirectionX = GameView.PLATFORM_DIRECTION_NONE;

                if(mPlatform_LengthCounter == GameObject.PLATFORM_LENGTH)
                {
                  isPlatform = true;
                  continue;
                }

                break;
              }
              if(isPlatform)  {return;}
            }
            if(isPlatform)  {return;}
          }
          break;

        case GameObject.TYPE_BALL:
          isBall = false;
          for (int _x = 0; _x < mXTileQuantity; _x++)
          {
            for (int _y = 0; _y < mYTileQuantity; _y++)
            {
              if(mTileGrid[_x][_y] == GameObject.TYPE_BALL)
              {
                //TODO  ->> ball behaviour with corner
                isBall = true;

                if(mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] == GameObject.TYPE_EMPTY)
                {
                  mTileGrid[_x][_y] = GameObject.TYPE_EMPTY;
                  mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] = GameObject.TYPE_BALL;
                  break;
                }

                if(mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] == GameObject.TYPE_WALL)
                {
                  mBall_DirectionX = -mBall_DirectionX;
                  mTileGrid[_x][_y] = GameObject.TYPE_EMPTY;

                  if(mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] == GameObject.TYPE_EMPTY)
                  {
                    mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] = GameObject.TYPE_BALL;
                    break;
                  }

                  if(mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] == GameObject.TYPE_BLOCK)
                  {
                    mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] = GameObject.TYPE_EMPTY;
                    mBall_DirectionY = -mBall_DirectionY;
                    mTileGrid[_x][_y] = GameObject.TYPE_EMPTY;
                    mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] = GameObject.TYPE_BALL;
                    break;
                  }

                  if(mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] == GameObject.TYPE_PLATFORM)
                  {
                    mBall_DirectionY = -mBall_DirectionY;
                    mTileGrid[_x][_y] = GameObject.TYPE_EMPTY;
                    mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] = GameObject.TYPE_BALL;
                    break;
                  }

                  break;
                }

                if(mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] == GameObject.TYPE_BLOCK)
                {
                  mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] = GameObject.TYPE_EMPTY;
                  mBall_DirectionY = -mBall_DirectionY;
                  mTileGrid[_x][_y] = GameObject.TYPE_EMPTY;
                  mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] = GameObject.TYPE_BALL;
                  break;
                }

                if(mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] == GameObject.TYPE_PLATFORM)
                {
                  mBall_DirectionY = -mBall_DirectionY;
                  mTileGrid[_x][_y] = GameObject.TYPE_EMPTY;
                  mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] = GameObject.TYPE_BALL;
                  break;
                }

                if(mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] == GameObject.TYPE_TOP)
                {
                  mBall_DirectionY = -mBall_DirectionY;
                  mTileGrid[_x][_y] = GameObject.TYPE_EMPTY;
                  mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] = GameObject.TYPE_BALL;
                  break;
                }

                if(mTileGrid[_x+ mBall_DirectionX][_y+ mBall_DirectionY] == GameObject.TYPE_BOTTOM)
                {
                  for(int _xx = 0; _xx < mXTileQuantity; _xx++)
                  {
                    for(int _yy = 0; _yy < mYTileQuantity; _yy++)
                    {
                      mTileGrid[_xx][_yy] = GameObject.TYPE_WALL;
                    }
                  }
                  break;
                }

              break;
              }
              if(isBall){return;}
            }
            if(isBall){return;}
          }
      }
    }

  }

  //  game states
  private static final int STATE_READY = 0;
  private static final int STATE_RUNNING = 1;
//  private static final int STATE_PAUSE = 2;
//  private static final int STATE_LOSE = 3;

  private int mGameState = STATE_READY;

  public static final int PLATFORM_DIRECTION_LEFT = -1;
  public static final int PLATFORM_DIRECTION_NONE = 0;
  public static final int PLATFORM_DIRECTION_RIGHT = 1;

  //  create game objects:  platform, ball
  private final GameObject mPlatform = new GameObject(GameObject.TYPE_PLATFORM);
  private final GameObject mBall = new GameObject(GameObject.TYPE_BALL);

  //  constructors
  public GameView(Context _Context) {
    super(_Context);
    mSensorManager = (SensorManager) _Context.getSystemService(Context.SENSOR_SERVICE);
    mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }

  public GameView(Context _Context, AttributeSet _AttributeSet) {
    super(_Context, _AttributeSet);
    mSensorManager = (SensorManager) _Context.getSystemService(Context.SENSOR_SERVICE);
    mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }

  public GameView(Context _Context, AttributeSet _AttributeSet, int _DefStyle)  {
    super(_Context, _AttributeSet, _DefStyle);
    mSensorManager = (SensorManager) _Context.getSystemService(Context.SENSOR_SERVICE);
    mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }

  public void setPlatformDirection(int _direction)  {
    mPlatform.mPlatform_DirectionX = _direction;
  }

//  private int[][] generateBlockStructure()  {}

  private void initializeBorders() {

    for (int _x = 0; _x < mXTileQuantity; _x++)
    {
      setTile(GameObject.TYPE_TOP, _x, 0);
      setTile(GameObject.TYPE_BOTTOM, _x, mYTileQuantity - 1);
    }

    for (int _y = 1; _y < mYTileQuantity - 1; _y++)
    {
      setTile(GameObject.TYPE_WALL, 0, _y);
      setTile(GameObject.TYPE_WALL, mXTileQuantity - 1, _y);
    }

  }

  private void initializeBlocks() {

    for (int _x = 0; _x < mXTileQuantity - 4; _x++)
    {
      setTile(GameObject.TYPE_BLOCK, _x + 2, mYTileQuantity - (mYTileQuantity - 10));
      setTile(GameObject.TYPE_BLOCK, _x + 2, mYTileQuantity - (mYTileQuantity - 9));
      setTile(GameObject.TYPE_BLOCK, _x + 2, mYTileQuantity - (mYTileQuantity - 5));
      setTile(GameObject.TYPE_BLOCK, _x + 2, mYTileQuantity - (mYTileQuantity - 4));
    }

    for (int _x = 0; _x < mXTileQuantity - 8; _x++)
    {
      setTile(GameObject.TYPE_BLOCK, _x + 4, mYTileQuantity - (mYTileQuantity - 8));
      setTile(GameObject.TYPE_BLOCK, _x + 4, mYTileQuantity - (mYTileQuantity - 7));
      setTile(GameObject.TYPE_BLOCK, _x + 4, mYTileQuantity - (mYTileQuantity - 6));
    }
  }

  private void initializePlatform() {
    for (int _x = 0; _x < GameObject.PLATFORM_LENGTH; _x++)
    {
      setTile(GameObject.TYPE_PLATFORM, _x - GameObject.PLATFORM_LENGTH / 2 + mXTileQuantity / 2, mYTileQuantity - 3);
    }
  }

  private void initializeRound() {
    setTile(GameObject.TYPE_BALL, mXTileQuantity / 2, mYTileQuantity - 4);
  }


  @Override
  public void onDraw(Canvas _canvas)  {

    if(mGameState == GameView.STATE_RUNNING)
    {
      mPlatform.computeNextPosition();
      mBall.computeNextPosition();
    }

    if(mGameState == GameView.STATE_READY)
    {
      super.loadTileDrawable(GameObject.TYPE_WALL, getContext().getResources().getDrawable(R.drawable.wall));
      super.loadTileDrawable(GameObject.TYPE_PLATFORM, getContext().getResources().getDrawable(R.drawable.platform));
      super.loadTileDrawable(GameObject.TYPE_BALL, getContext().getResources().getDrawable(R.drawable.ball));
      super.loadTileDrawable(GameObject.TYPE_BLOCK, getContext().getResources().getDrawable(R.drawable.block));
      super.loadTileDrawable(GameObject.TYPE_TOP, getContext().getResources().getDrawable(R.drawable.wall));
      super.loadTileDrawable(GameObject.TYPE_BOTTOM, getContext().getResources().getDrawable(R.drawable.wall));

      this.initializeBorders();
      this.initializeBlocks();
      this.initializePlatform();
      this.initializeRound();

      mGameState = GameView.STATE_RUNNING;
    }

    super.onDraw(_canvas);
  }

  //  work with sensor: accelerometer
  //  axes: axis_x, axis_y, axis_z
  private float[] mAxes = new float[3];
  private final Sensor mSensor;
  private final SensorManager mSensorManager;
  private final SensorEventListener mSensorEventListener = new SensorEventListener() {
    @Override
    public void onSensorChanged(SensorEvent _SensorEvent) {
      if(_SensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
      {
        mAxes[0] = _SensorEvent.values[0];
      }
    }

    @Override
    public void onAccuracyChanged(Sensor _Sensor, int _i)  {}
  };

  public void registerListener() {
    mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_UI);
  }

  public void unregisterListener() {
    mSensorManager.unregisterListener(mSensorEventListener);
  }

  public float getAxisX() {
    return mAxes[0];
  }

}
