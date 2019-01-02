package com.example.shakthi.magnetometer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import static android.telephony.TelephonyManager.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements LocationListener {

    TextView dValue, latValue, longValue;
    LocationManager locationManager;
    double latitude;
    double longitude;

    TelephonyManager Tel;
    MyPhoneStateListener MyListener;
    CellInfoGsm cellInfoGsm;
    Button dbm;
    TextView tv_dbm;
    String strength;

    @SuppressLint({"MissingPermission", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        MyListener = new MyPhoneStateListener();
        Tel = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

        //Intent intent = getIntent();
       // String text = intent.getStringExtra("text");

        latValue = (TextView) findViewById(R.id.latValue);
        longValue = (TextView) findViewById(R.id.longValue);
       // dValue = (TextView) findViewById(R.id.dValue);
        tv_dbm = (TextView) findViewById(R.id.tv_dbm);
        dbm = (Button) findViewById(R.id.dbm);
        //dValue.setText("Signal Strength =" + text + "dB");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);

        List<CellInfo> cellInfos = Tel.getAllCellInfo();   //This will give info of all sims present inside your mobile
        if(cellInfos!=null){
            for (int i = 0 ; i<cellInfos.size(); i++){
                if (cellInfos.get(i).isRegistered()){
                    if(cellInfos.get(i) instanceof CellInfoWcdma){
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) Tel.getAllCellInfo().get(0);
                        CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthWcdma.getDbm());
                    }else if(cellInfos.get(i) instanceof CellInfoGsm){
                        CellInfoGsm cellInfogsm = (CellInfoGsm) Tel.getAllCellInfo().get(0);
                        CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthGsm.getDbm());
                    }else if(cellInfos.get(i) instanceof CellInfoLte){
                        CellInfoLte cellInfoLte = (CellInfoLte) Tel.getAllCellInfo().get(0);
                        CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthLte.getDbm());
                    }
                }
            }
            //  return strength;
            //  Dbs = new ArrayList<>();
            //    Dbs.add(new DBS(strength));
        }

        dbm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_dbm.setText(strength + " dbm");

                //Intent intent = new Intent(MainActivity.this,ListViewActivity.class);
                //startActivity(intent);

            }
        });
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            /**  Toast.makeText(getApplicationContext(), "GSM Cinr = "
             + String.valueOf(signalStrength.getGsmSignalStrength()), Toast.LENGTH_SHORT).show(); **/

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        latValue.setText("Latitude: "+ latitude);
        longValue.setText("Longitude: "+ longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
