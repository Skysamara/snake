package ru.panorobot.snake;

import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;

public class GameActivity extends Activity implements SensorEventListener {

    GameSurface surf;
    Timer t;
    int width, height;

    SensorManager mSensorManager;
    Sensor mAccelerometerSensor;

    float SSX = 0, SSY = 0;
    float SX = 0, SY = 0;
    boolean firstTime;

    // Ну тут обрабатываем создание активити
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surf = new GameSurface(this);
        this.setContentView(surf);
        t = new Timer();
        height = this.getWindowManager().getDefaultDisplay().getHeight();
        width = this.getWindowManager().getDefaultDisplay().getWidth();

        // Инициализируем акселерометр
        mSensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        if (sensors.size() > 0) {
            for (Sensor sensor : sensors) {
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    if (mAccelerometerSensor == null)
                        mAccelerometerSensor = sensor;
                }
            }
        }
    }

    // Запуск активити
    @Override
    public void onStart() {
        super.onStart();
        // Запускаем таймер обновления картинки на экране
        t.scheduleAtFixedRate(new GraphUpdater(surf), 0, 100);
        // Запускаем таймер обновления положения змейки
        t.scheduleAtFixedRate(new StepUpdater(this), 0, 500);
        // регистрируем нашу форму как объект слушающий
        // изменения датчика - акселерометра
        mSensorManager.registerListener(this, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
        this.firstTime = true;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Обрабатываем остановку активити
    @Override
    public void onStop() {
        super.onStop();
        // Останавливаем таймеры
        t.cancel();
        t.purge();
        // Отписываемся от получения сообщений об изменении
        // от датчика
        mSensorManager.unregisterListener(this);
    }

    //  @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing!
    }

    // метод, который определяет по показаниям акселерометра
    // передаваемым ему как параметры (х и у)
    // в каком направлении должна двигаться змея
    private int getDirection(float x, float y) {
        if (Math.abs(x) > Math.abs(y)) {
            if (x > 0) {
                return SnakeGame.DIR_WEST;
            } else {
                return SnakeGame.DIR_EAST;
            }
        } else {
            if (y > 0) {
                return SnakeGame.DIR_SOUTH;
            } else {
                return SnakeGame.DIR_NORTH;
            }
        }
    }

    // А вот так мы обрабатываем изменение ориентации
    // телефона в пространстве
//  @Override
    public void onSensorChanged(SensorEvent event) {
        surf.setSomeText("Your score is: "+SimpleSnakeActivity.GAME_SCORE);

        // получаем показания датчика
        SX = event.values[0];
        SY = event.values[1];

        // Если игра уже идет, то
        if (!this.firstTime) {
            // получаем положение телефона в пространстве
            // с поправкой на его начальное положение
            float dirX = SX - SSX;
            float dirY = SY - SSY;
            // Устанавливаем для змеи новое направление
            surf.mField.setDirection(this.getDirection(dirX, dirY));
            // передаем в нашу повержность координаты телефона в пространстве
            surf.setXY(dirX, dirY);
        } else {
            // Если игра только началась делаем поправку на начальное
            // положение телефона
            this.firstTime = false;
            SSX = SX;
            SSY = SY;
        }
    }

    // Этот метод вызывается из потока одного из таймеров
    // именно в этом методе происходит движение змейки в
    // зависимости от ее направления установленного в предидущем
    // методе
    public void Step() {
        // Если ход не удался то закрываем текущую активити
        if (!surf.mField.nextMove()) {
            SimpleSnakeActivity.GAME_MODE=1;
            this.finish();
        }
        // Если все впорядке то обновляем очки
        // в стартовой активити
        else{
            SimpleSnakeActivity.GAME_SCORE=this.surf.mField.mScore;
        }
    }
}
