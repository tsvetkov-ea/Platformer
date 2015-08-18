package canvas.hardwaresensor.platformer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

class BackgroundView extends View  {

  private int mTileSize;
  private int mXOffset, mYOffset;

  protected int mXTileQuantity, mYTileQuantity;
  protected int[][] mTileGrid;

  private final int mObjectTypeQuantity = 7;
  private final Bitmap[] mBitmapArray = new Bitmap[mObjectTypeQuantity];

  private final int mBackgroundColor = getResources().getColor(R.color.light_green_700);
  private final Paint mPaintTile = new Paint(Paint.ANTI_ALIAS_FLAG);

  //  constructors
  public BackgroundView(Context _Context) {
    super(_Context);
  }

  public BackgroundView(Context _Context, AttributeSet _AttributeSet) {
    super(_Context, _AttributeSet);
    TypedArray _array = _Context.obtainStyledAttributes(_AttributeSet, R.styleable.BackgroundView);
    mTileSize = _array.getDimensionPixelSize(R.styleable.BackgroundView_TileSize, 16);
    _array.recycle();
  }

  public BackgroundView(Context _Context, AttributeSet _AttributeSet, int _DefStyle)  {
    super(_Context, _AttributeSet, _DefStyle);
    TypedArray _array = _Context.obtainStyledAttributes(_AttributeSet, R.styleable.BackgroundView);
    mTileSize = _array.getDimensionPixelSize(R.styleable.BackgroundView_TileSize, 16);
    _array.recycle();
  }


  private void clearTiles()  {
    for (int _x = 0; _x < mXTileQuantity; _x++)
    {
      for (int _y = 0; _y < mYTileQuantity; _y++)
      {
        setTile(0, _x, _y);
      }
    }
  }

  protected void setTile(int _index, int _x, int _y) {
    mTileGrid[_x][_y] = _index;
  }

  protected void loadTileDrawable(int _key, Drawable _drawable)  {
    Bitmap _bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_4444);
    Canvas _canvas = new Canvas(_bitmap);
    _drawable.setBounds(0, 0, mTileSize, mTileSize);
    _drawable.draw(_canvas);

    mBitmapArray[_key] = _bitmap;
  }


  @Override
  public void onDraw(Canvas _canvas)  {
    super.onDraw(_canvas);
    _canvas.drawColor(mBackgroundColor);

    for (int _x = 0; _x < mXTileQuantity; _x++)
    {
      for (int _y = 0; _y < mYTileQuantity; _y++)
      {
        if(mTileGrid[_x][_y] > 0)
        {
          _canvas.drawBitmap(mBitmapArray[mTileGrid[_x][_y]], mXOffset + _x * mTileSize, mYOffset + _y * mTileSize, mPaintTile);
        }
      }
    }
  }

  @Override
  protected void onSizeChanged(int _width, int _height, int _previousWidth, int _previousHeight)  {
    mXTileQuantity = (int) Math.floor(_width / mTileSize);
    mYTileQuantity = (int) Math.floor(_height / mTileSize);

    mXOffset = ((_width - (mTileSize * mXTileQuantity)) / 2);
    mYOffset = ((_height - (mTileSize * mYTileQuantity)) / 2);

    mTileGrid = new int[mXTileQuantity][mYTileQuantity];
    clearTiles();
  }

}
