package com.m2dl.helloandroid.helloandroid;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class HelloAndroid extends AppCompatActivity implements View.OnTouchListener, SensorEventListener {
    
    private Handler mHandler;
    private int compt = 5;
    private int touchCompt = 0;
    SensorManager sm = null;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        tv = new TextView(this);
        setContentView(tv);
        tv.setText("Application Hello Android");
        // tv.setOnTouchListener(this);

        //mHandler = new Handler();
        //mHandler.postDelayed(mUpdateTimeTask, 1000);
    }
    
    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            compt--;
            if (compt > 0) {
                mHandler.postDelayed(this, 1000);
                tv.setText(String.valueOf(compt));
            } else {
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(1000);
                tv.setText(Build.MODEL);
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(touchCompt < 10){
            touchCompt++;
            float posx = event.getX();
            float posy = event.getY();
            tv.setText("Position X : " + posx + ", Position Y : " + posy + ", Count : " + touchCompt);

        } else {
            System.exit(RESULT_OK);
        }
        return true;
    }

    protected void onResume(){
        super.onResume();
        Sensor mMagneticField = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onStop(){
        sm.unregisterListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensor = event.sensor.getType();
        float[] values = event.values;

        synchronized(this){
            if(sensor == Sensor.TYPE_ACCELEROMETER){
                float magField_x = values[0];
                float magField_y = values[1];
                float magField_z = values[2];

                tv.setText("X : " + magField_x + ", Y : " + magField_y + ", Z : " + magField_z);
            }
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    };
}
