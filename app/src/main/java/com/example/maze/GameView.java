package com.example.maze;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.View;

public class GameView extends View {
    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final GameSensorListener gameSensorListener;
    private final GameMaze gameMaze;

    public GameView(Context context) {
        super(context);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        gameMaze = new GameMaze();
        gameSensorListener = new GameSensorListener(this, gameMaze);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        gameMaze.draw(canvas);
        invalidate();
    }

    public void registerSensor() {
        sensorManager.registerListener(gameSensorListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void unregisterSensor() {
        sensorManager.unregisterListener(gameSensorListener);
    }
}
