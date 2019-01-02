package com.example.shakthi.magnetometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.karan.churi.PermissionManager.PermissionManager;

import org.w3c.dom.Text;

import java.lang.*;
import java.text.NumberFormat;
import java.util.*;
import java.lang.Math;

import de.nitri.gauge.Gauge;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor mMagno;
    TextView xValue, yValue, zValue, fValue,disp;
    private Button button;
    String text;
    Gauge gauge;
    int curValue;
    int kk;
    double k;
    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xValue = (TextView) findViewById(R.id.xValue);
        yValue = (TextView) findViewById(R.id.yValue);
        zValue = (TextView) findViewById(R.id.zValue);
        fValue = (TextView) findViewById(R.id.fValue);
        gauge = (Gauge) findViewById(R.id.gauge);

        curValue=-75;
        gauge.setValue(curValue);


        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);




        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });
        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagno != null) {
            sensorManager.registerListener(MainActivity.this, mMagno, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Magnetometer Listener");
        } else {
            xValue.setText("Magno Not Supported");
            yValue.setText("Magno Not Supported");
            zValue.setText("Magno Not Supported");
            fValue.setText("0");
        }
    }
    public void openActivity2(){
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("text",text);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Log.d(TAG, "onSensorChanged: X: " + sensorEvent.values[0] + " Y: " + sensorEvent.values[1] + " Z: " + sensorEvent.values[2]);
            float x= sensorEvent.values[0];
            float y= sensorEvent.values[1];
            float z= sensorEvent.values[2];
            xValue.setText("xValue: " + x);
            yValue.setText("yValue: " + y);
            zValue.setText("zValue: " + z);

            k = Math.sqrt((x*x)+(y*y)+(z*z));
            kk = (int) k;
            text= String.format("%.0f", k);
            kk=kk*-1;
            fValue.setText("Signal Strength =" + kk + "dB"); //use variable text also


            gauge.moveToValue(kk);
        }
    }
}