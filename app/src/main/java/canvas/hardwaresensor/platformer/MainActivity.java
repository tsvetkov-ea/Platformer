package canvas.hardwaresensor.platformer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

  private GameView mGameView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mGameView = (GameView) findViewById(R.id.game_object_view_CC);
//    mGameView.setOnTouchListener(new View.OnTouchListener() {
//      @Override
//      public boolean onTouch(View _view, MotionEvent _event) {
//        float _width = _view.getWidth();
//        float _x = _event.getX();
//
//        int _direction = GameView.PLATFORM_DIRECTION_NONE;
//
//        if(_x < _width / 4)
//        {
//          _direction = GameView.PLATFORM_DIRECTION_RIGHT;
//        }
//
//        if(_x > _width / 2 + _width / 4)
//        {
//          _direction = GameView.PLATFORM_DIRECTION_LEFT;
//        }
//
//        mGameView.setPlatformDirection(_direction);
//        return false;
//      }
//    });

    //  start the task every 0.2 second
    this.scheduleAtFixedRate(200);
  }

  @Override
  public void onResume()  {
    mGameView.registerListener();
    super.onResume();
  }

  @Override
  public void onPause() {
    mGameView.unregisterListener();
    super.onPause();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu _Menu) {
    getMenuInflater().inflate(R.menu.menu_main, _Menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem _MenuItem) {
    switch(_MenuItem.getItemId()) {
      case R.id.new_game:
//        mGameView.initNewGame();
        return true;
      default:
        return super.onOptionsItemSelected(_MenuItem);
    }
  }


  private void updateGUI() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        float _delta = 1.5f;
        int _direction = GameView.PLATFORM_DIRECTION_NONE;
        if(mGameView.getAxisX() > _delta)
        {
          _direction = GameView.PLATFORM_DIRECTION_LEFT;
        }
        else if(mGameView.getAxisX() < - _delta)
        {
          _direction = GameView.PLATFORM_DIRECTION_RIGHT;
        }
        mGameView.setPlatformDirection(_direction);
        mGameView.invalidate();
      }
    });
  }

  private void scheduleAtFixedRate(int _period) {
    Timer _timer = new Timer();
    TimerTask _timerTask = new TimerTask() {
      @Override
      public void run() {
        updateGUI();
      }
    };
    _timer.scheduleAtFixedRate(_timerTask, 0, _period);
  }

  private void MAIN_ACTIVITY_LOG_DEBUG(String _message)  {
    final String MAIN_ACTIVITY_TAG = "MAIN_ACTIVITY_TAG";
    Log.d(MAIN_ACTIVITY_TAG, "// " + _message + " //");
  }
}
