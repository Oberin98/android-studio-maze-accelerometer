package com.example.maze;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;

public class GameSensorListener implements SensorEventListener {
    View view;
    GameMaze gameMaze;

    public GameSensorListener(View view, GameMaze gameMaze) {
        this.view = view;
        this.gameMaze = gameMaze;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) sensorEvent.values[0];
            int y = (int) sensorEvent.values[1];

            if (x <= -2) {
                gameMaze.move(GameMaze.DIRECTION.R);
            } else if (x >= 2) {
                gameMaze.move(GameMaze.DIRECTION.L);
            }else if (y <= -2) {
                gameMaze.move(GameMaze.DIRECTION.T);
            } else if (y >= 2) {
                gameMaze.move(GameMaze.DIRECTION.B);
            }

            view.invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
